package com.isxcode.acorn.agent.run.impl;

import com.alibaba.fastjson2.JSON;
import com.isxcode.acorn.agent.run.AgentService;
import com.isxcode.acorn.api.agent.constants.AgentType;
import com.isxcode.acorn.api.agent.pojos.req.GetWorkInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetWorkLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopWorkReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitWorkReq;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopWorkRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitWorkRes;
import com.isxcode.acorn.api.api.constants.PathConstants;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.*;
import org.apache.flink.yarn.YarnClusterClientFactory;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;

@Service
@Slf4j
@RequiredArgsConstructor
public class YarnAgentService implements AgentService {

    @Override
    public String getAgentType() {

        return AgentType.YARN;
    }

    @Override
    public SubmitWorkRes submitWork(SubmitWorkReq submitWorkReq) throws Exception {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();

        flinkConfig.set(PipelineOptions.NAME, submitWorkReq.getFlinkSubmit().getAppName());
        flinkConfig.set(YarnConfigOptions.APPLICATION_NAME, submitWorkReq.getFlinkSubmit().getAppName());

        // 使用application模式部署
        flinkConfig.set(DeploymentOptions.TARGET, YarnDeploymentTarget.APPLICATION.getName());

        flinkConfig.set(DeploymentOptionsInternal.CONF_DIR, submitWorkReq.getFlinkHome() + "/conf");

        flinkConfig.set(PipelineOptions.JARS,
            singletonList(submitWorkReq.getAgentHomePath() + File.separator + PathConstants.AGENT_PATH_NAME
                + File.separator + "plugins" + File.separator + submitWorkReq.getFlinkSubmit().getAppResource()));

        flinkConfig.set(ApplicationConfiguration.APPLICATION_ARGS, singletonList(
            Base64.getEncoder().encodeToString(JSON.toJSONString(submitWorkReq.getPluginReq()).getBytes())));
        flinkConfig.set(ApplicationConfiguration.APPLICATION_MAIN_CLASS,
            submitWorkReq.getFlinkSubmit().getEntryClass());
        flinkConfig.set(JobManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("1g"));
        flinkConfig.set(TaskManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("1g"));
        flinkConfig.set(TaskManagerOptions.NUM_TASK_SLOTS, 1);

        // 配置yarn文件
        Path path = new Path(System.getenv("HADOOP_CONF_DIR") + "/yarn-site.xml");
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.addResource(path);
        Map<String, String> yarn = conf.getPropsWithPrefix("yarn");
        yarn.forEach((k, v) -> flinkConfig.setString("flink.yarn" + k, v));

        // 添加flink dist
        flinkConfig.set(YarnConfigOptions.FLINK_DIST_JAR, submitWorkReq.getFlinkHome() + "/lib/flink-dist-1.18.1.jar");

        // 添加lib
        List<String> libFile = new ArrayList<>();
        libFile.add(submitWorkReq.getFlinkHome() + "/lib");
        libFile.add(submitWorkReq.getAgentHomePath() + File.separator + PathConstants.AGENT_PATH_NAME + File.separator
            + "/lib");
        flinkConfig.set(YarnConfigOptions.SHIP_FILES, libFile);

        ClusterSpecification clusterSpecification =
            new ClusterSpecification.ClusterSpecificationBuilder().setMasterMemoryMB(1024).setTaskManagerMemoryMB(1024)
                .setSlotsPerTaskManager(1).createClusterSpecification();

        ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.fromConfiguration(flinkConfig);
        YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();
        try (YarnClusterDescriptor clusterDescriptor = yarnClusterClientFactory.createClusterDescriptor(flinkConfig)) {
            ClusterClientProvider<ApplicationId> applicationIdClusterClientProvider =
                clusterDescriptor.deployApplicationCluster(clusterSpecification, applicationConfiguration);
            return SubmitWorkRes.builder()
                .appId(String.valueOf(applicationIdClusterClientProvider.getClusterClient().getClusterId())).build();
        }
    }

    @Override
    public GetWorkInfoRes getWorkInfo(GetWorkInfoReq getWorkInfoReq) throws Exception {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptionsInternal.CONF_DIR, getWorkInfoReq.getFlinkHome() + "/conf");
        Path path = new Path(System.getenv("HADOOP_CONF_DIR") + "/yarn-site.xml");
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.addResource(path);
        Map<String, String> yarn = conf.getPropsWithPrefix("yarn");
        yarn.forEach((k, v) -> {
            flinkConfig.setString("flink.yarn" + k, v);
        });

        YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();
        try (YarnClusterDescriptor clusterDescriptor = yarnClusterClientFactory.createClusterDescriptor(flinkConfig)) {
            ApplicationReport applicationReport = clusterDescriptor.getYarnClient()
                .getApplicationReport(ApplicationId.fromString(getWorkInfoReq.getAppId()));
            return GetWorkInfoRes.builder().appId(getWorkInfoReq.getAppId())
                .status(applicationReport.getYarnApplicationState().toString()).build();
        }
    }

    @Override
    public GetWorkLogRes getWorkLog(GetWorkLogReq getWorkLogReq) throws Exception {

        String getLogCmdFormat = "yarn logs -applicationId %s";
        Process process = Runtime.getRuntime().exec(String.format(getLogCmdFormat, getWorkLogReq.getAppId()));

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        StringBuilder errLog = new StringBuilder();
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null)
                    break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            errLog.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        if (exitCode == 1) {
            throw new IsxAppException(errLog.toString());
        } else {
            Pattern regex =
                Pattern.compile("LogType:taskmanager.log\\s*([\\s\\S]*?)\\s*End of LogType:taskmanager.log");
            Matcher matcher = regex.matcher(errLog);
            String log = "";
            while (matcher.find()) {
                String tmpLog = matcher.group();
                if (tmpLog.contains("ERROR")) {
                    log = tmpLog;
                    break;
                }
                if (tmpLog.length() > log.length()) {
                    log = tmpLog;
                }
            }
            if (Strings.isEmpty(log)) {
                regex = Pattern.compile("LogType:jobmanager.log\\s*([\\s\\S]*?)\\s*End of LogType:jobmanager.log");
                matcher = regex.matcher(errLog);
                while (matcher.find()) {
                    String tmpLog = matcher.group();
                    if (tmpLog.contains("ERROR")) {
                        log = tmpLog;
                        break;
                    }
                    if (tmpLog.length() > log.length()) {
                        log = tmpLog;
                    }
                }
            }
            return GetWorkLogRes.builder().log(log).build();
        }
    }

    @Override
    public StopWorkRes stopWork(StopWorkReq stopWorkReq) throws Exception {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptionsInternal.CONF_DIR, stopWorkReq.getFlinkHome() + "/conf");
        Path path = new Path(System.getenv("HADOOP_CONF_DIR") + "/yarn-site.xml");
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.addResource(path);
        Map<String, String> yarn = conf.getPropsWithPrefix("yarn");
        yarn.forEach((k, v) -> {
            flinkConfig.setString("flink.yarn" + k, v);
        });

        YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();
        try (YarnClusterDescriptor clusterDescriptor = yarnClusterClientFactory.createClusterDescriptor(flinkConfig)) {
            clusterDescriptor.getYarnClient().killApplication(ApplicationId.fromString(stopWorkReq.getAppId()));
            return StopWorkRes.builder().build();
        }
    }
}
