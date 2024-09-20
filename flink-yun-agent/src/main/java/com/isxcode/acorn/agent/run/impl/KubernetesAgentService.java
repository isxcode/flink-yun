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
import com.isxcode.acorn.api.work.constants.WorkType;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.KubernetesClusterClientFactory;
import org.apache.flink.kubernetes.KubernetesClusterDescriptor;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.kubernetes.configuration.KubernetesDeploymentTarget;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class KubernetesAgentService implements AgentService {

    @Override
    public String getAgentType() {
        return AgentType.K8S;
    }

    @Override
    public SubmitWorkRes submitWork(SubmitWorkReq submitWorkReq) throws Exception {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptions.TARGET, KubernetesDeploymentTarget.APPLICATION.getName());
        flinkConfig.set(PipelineOptions.NAME, submitWorkReq.getFlinkSubmit().getAppName());

        if (WorkType.FLINK_JAR.equals(submitWorkReq.getWorkType())) {
            flinkConfig.set(ApplicationConfiguration.APPLICATION_ARGS, submitWorkReq.getFlinkSubmit().getProgramArgs());
        } else {
            flinkConfig.set(ApplicationConfiguration.APPLICATION_ARGS, Collections.singletonList(
                Base64.getEncoder().encodeToString(JSON.toJSONString(submitWorkReq.getPluginReq()).getBytes())));
        }
        flinkConfig.set(ApplicationConfiguration.APPLICATION_MAIN_CLASS,
            submitWorkReq.getFlinkSubmit().getEntryClass());
        flinkConfig.set(PipelineOptions.JARS, Collections.singletonList("local:///opt/flink/examples/app.jar"));
        flinkConfig.set(KubernetesConfigOptions.CLUSTER_ID, "zhiliuyun-cluster-" + System.currentTimeMillis());
        flinkConfig.set(KubernetesConfigOptions.REST_SERVICE_EXPOSED_TYPE,
            KubernetesConfigOptions.ServiceExposedType.NodePort);
        flinkConfig.set(KubernetesConfigOptions.CONTAINER_IMAGE_PULL_POLICY,
            KubernetesConfigOptions.ImagePullPolicy.IfNotPresent);
        flinkConfig.set(KubernetesConfigOptions.NAMESPACE, "zhiliuyun-space");
        flinkConfig.set(KubernetesConfigOptions.KUBERNETES_SERVICE_ACCOUNT, "zhiliuyun");
        flinkConfig.set(KubernetesConfigOptions.CONTAINER_IMAGE, "flink:1.18.1-scala_2.12");
        flinkConfig.set(KubernetesConfigOptions.TASK_MANAGER_CPU, 2.0);

        String podTemplate = "apiVersion: v1\n" + "kind: Pod\n" + "metadata:\n" + "  name: pod-template\n" + "spec:\n"
            + "  containers:\n" + "    - name: flink-main-container\n" + "      volumeMounts:\n" + "%s" + "  volumes:\n"
            + "%s";

        List<String> volumeMounts = new ArrayList<>();
        volumeMounts.add("        - name: app\n" + "          mountPath: /opt/flink/examples/app.jar\n");
        volumeMounts.add("       - name: flink-log\n" + "          mountPath: /log\n");

        List<String> volumes = new ArrayList<>();

        if (WorkType.FLINK_JAR.equals(submitWorkReq.getWorkType())) {
            volumes.add("    - name: app\n" + "      hostPath:\n" + "        path: " + submitWorkReq.getAgentHomePath()
                + File.separator + "file" + File.separator + submitWorkReq.getFlinkSubmit().getAppResource() + "\n");
        } else {
            volumes.add("    - name: app\n" + "      hostPath:\n" + "        path: " + submitWorkReq.getAgentHomePath()
                + File.separator + "plugins" + File.separator + submitWorkReq.getFlinkSubmit().getAppResource() + "\n");
        }

        volumes.add("   - name: flink-log\n" + "      hostPath:\n" + "        path: " + submitWorkReq.getAgentHomePath()
            + File.separator + "k8s-logs" + File.separator + submitWorkReq.getWorkInstanceId() + "\n");

        File[] jarFiles = new File(submitWorkReq.getAgentHomePath() + File.separator + "lib").listFiles();
        if (jarFiles != null) {
            for (File jarFile : jarFiles) {
                if (jarFile.getName().contains("fastjson") || jarFile.getName().contains("flink")
                    || jarFile.getName().contains("connector")) {
                    volumeMounts.add("       - name: " + jarFile.getName().replace(".", "-") + "\n"
                        + "          mountPath: /opt/flink/lib/" + jarFile.getName() + "\n");
                    volumes.add("   - name: " + jarFile.getName().replace(".", "-") + "\n" + "      hostPath:\n"
                        + "        path: " + submitWorkReq.getAgentHomePath() + File.separator + "lib" + File.separator
                        + jarFile.getName() + "\n");
                }
            }
        }

        String podTemplateContent =
            String.format(podTemplate, Strings.join(volumeMounts, ' '), Strings.join(volumes, ' '));

        // 判断pod文件夹是否存在
        if (!new File(submitWorkReq.getAgentHomePath() + File.separator + "pod").exists()) {
            Files.createDirectories(Paths.get(submitWorkReq.getAgentHomePath() + File.separator + "pod"));
        }

        // 判断k8s-logs文件夹是否存在
        if (!new File(submitWorkReq.getAgentHomePath() + File.separator + "k8s-logs").exists()) {
            Files.createDirectories(Paths.get(submitWorkReq.getAgentHomePath() + File.separator + "k8s-logs"));
        }

        try (InputStream inputStream = new ByteArrayInputStream(podTemplateContent.getBytes())) {
            Files.copy(inputStream, Paths.get(submitWorkReq.getAgentHomePath() + File.separator + "pod")
                .resolve(submitWorkReq.getWorkInstanceId() + ".yaml"), StandardCopyOption.REPLACE_EXISTING);
        }

        flinkConfig.set(KubernetesConfigOptions.KUBERNETES_POD_TEMPLATE, submitWorkReq.getAgentHomePath()
            + File.separator + "pod" + File.separator + submitWorkReq.getWorkInstanceId() + ".yaml");

        flinkConfig.set(KubernetesConfigOptions.FLINK_LOG_DIR, "/log");
        flinkConfig.set(DeploymentOptionsInternal.CONF_DIR, submitWorkReq.getFlinkHome() + "/conf");
        flinkConfig.set(JobManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("1g"));
        flinkConfig.set(TaskManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("1g"));
        flinkConfig.set(TaskManagerOptions.NUM_TASK_SLOTS, 1);
        flinkConfig.set(KubernetesConfigOptions.KUBERNETES_HOSTNETWORK_ENABLED, true);

        ClusterSpecification clusterSpecification =
            new ClusterSpecification.ClusterSpecificationBuilder().setMasterMemoryMB(1024).setTaskManagerMemoryMB(1024)
                .setSlotsPerTaskManager(2).createClusterSpecification();

        ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.fromConfiguration(flinkConfig);
        applicationConfiguration.applyToConfiguration(flinkConfig);
        KubernetesClusterClientFactory kubernetesClusterClientFactory = new KubernetesClusterClientFactory();
        try (KubernetesClusterDescriptor clusterDescriptor =
            kubernetesClusterClientFactory.createClusterDescriptor(flinkConfig)) {
            ClusterClientProvider<String> clusterClientProvider =
                clusterDescriptor.deployApplicationCluster(clusterSpecification, applicationConfiguration);
            return SubmitWorkRes.builder().webUrl(clusterClientProvider.getClusterClient().getWebInterfaceURL())
                .appId(String.valueOf(clusterClientProvider.getClusterClient().getClusterId())).build();
        }
    }

    @Override
    public GetWorkInfoRes getWorkInfo(GetWorkInfoReq getWorkInfoReq) throws Exception {

        String getStatusJobManagerFormat = "kubectl get pods -l app=%s -n zhiliuyun-space";
        String line;
        StringBuilder errLog = new StringBuilder();

        String command = String.format(getStatusJobManagerFormat, getWorkInfoReq.getAppId());
        Process process = Runtime.getRuntime().exec(command);
        try (InputStream inputStream = process.getInputStream();
            InputStream errStream = process.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(errStream, StandardCharsets.UTF_8))) {

            while ((line = reader.readLine()) != null) {
                errLog.append(line).append("\n");
                String pattern = "\\s+\\d/\\d\\s+(\\w+)";
                Pattern regex = Pattern.compile(pattern);
                Matcher matcher = regex.matcher(line);
                if (matcher.find()) {
                    return GetWorkInfoRes.builder().status(matcher.group(1)).appId(getWorkInfoReq.getAppId()).build();
                }
            }

            if (errLog.toString().isEmpty()) {
                while ((line = errReader.readLine()) != null) {
                    errLog.append(line).append("\n");
                }
                if (errLog.toString().contains("No resources found in zhiliuyun-space namespace")) {
                    return GetWorkInfoRes.builder().status("FINISHED").appId(getWorkInfoReq.getAppId()).build();
                }
            }
            int exitCode = process.waitFor();
            if (exitCode == 1) {
                throw new Exception("Command execution failed:\n" + errLog);
            }
        }

        throw new Exception("获取状态异常");
    }

    @Override
    public GetWorkLogRes getWorkLog(GetWorkLogReq getWorkLogReq) throws Exception {
        File[] logFiles = new File(getWorkLogReq.getAgentHomePath() + File.separator + "k8s-logs" + File.separator
            + getWorkLogReq.getWorkInstanceId()).listFiles();

        StringBuilder logBuilder = new StringBuilder();
        if (logFiles != null) {
            for (File logFile : logFiles) {
                if (logFile.getName().contains("taskmanager")) {
                    FileReader fileReader = new FileReader(logFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        logBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    fileReader.close();
                    break;
                }
                if (logFile.getName().contains("application")) {
                    FileReader fileReader = new FileReader(logFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        logBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    fileReader.close();
                    break;
                }
            }
        }

        return GetWorkLogRes.builder().log(logBuilder.toString()).build();
    }

    @Override
    public StopWorkRes stopWork(StopWorkReq stopWorkReq) throws Exception {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptions.TARGET, KubernetesDeploymentTarget.APPLICATION.getName());
        flinkConfig.set(KubernetesConfigOptions.NAMESPACE, "zhiliuyun-space");
        flinkConfig.set(KubernetesConfigOptions.KUBERNETES_SERVICE_ACCOUNT, "zhiliuyun");

        KubernetesClusterClientFactory kubernetesClusterClientFactory = new KubernetesClusterClientFactory();
        try (KubernetesClusterDescriptor clusterDescriptor =
            kubernetesClusterClientFactory.createClusterDescriptor(flinkConfig)) {
            clusterDescriptor.killCluster(stopWorkReq.getAppId());
            return StopWorkRes.builder().build();
        }
    }
}
