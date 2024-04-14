package com.isxcode.acorn.agent.run;

import com.alibaba.fastjson2.JSON;
import com.isxcode.acorn.api.agent.pojos.req.GetJobInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetJobLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopJobReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitJobReq;
import com.isxcode.acorn.api.agent.pojos.res.GetJobInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetJobLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopJobRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitJobRes;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class KubernetesAcorn implements AcornRun {

    @Override
    public SubmitJobRes submitJob(SubmitJobReq submitJobReq) {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptions.TARGET, KubernetesDeploymentTarget.APPLICATION.getName());
        flinkConfig.set(PipelineOptions.NAME, submitJobReq.getAppName());
        flinkConfig.set(ApplicationConfiguration.APPLICATION_ARGS, Collections.singletonList(Base64.getEncoder().encodeToString(JSON.toJSONString(submitJobReq.getAcornPluginReq()).getBytes())));
        flinkConfig.set(ApplicationConfiguration.APPLICATION_MAIN_CLASS, submitJobReq.getEntryClass());
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
        flinkConfig.set(KubernetesConfigOptions.KUBERNETES_POD_TEMPLATE,
            "/Users/ispong/isxcode/flink-yun/resources/pod/pod-template.yaml");
        flinkConfig.set(KubernetesConfigOptions.FLINK_LOG_DIR, "/log");
        flinkConfig.set(DeploymentOptionsInternal.CONF_DIR, submitJobReq.getFlinkHome() + "/conf");
        flinkConfig.set(JobManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("2g"));
        flinkConfig.set(TaskManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("2g"));
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
            return SubmitJobRes.builder()
                .jobId(String.valueOf(clusterClientProvider.getClusterClient().getClusterId())).build();
        } catch (Exception e) {
            throw new IsxAppException("提交任务失败" + e.getMessage());
        }

    }

    @Override
    public GetJobInfoRes getJobInfo(GetJobInfoReq getJobInfoReq) {

        // 直接去日志文件中获取
        return GetJobInfoRes.builder().build();
    }

    @Override
    public GetJobLogRes getJobLog(GetJobLogReq getJobLogReq) {

        // 直接去日志文件中获取
        return GetJobLogRes.builder().build();
    }

    @Override
    public StopJobRes stopJobReq(StopJobReq stopJobReq) {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptions.TARGET, KubernetesDeploymentTarget.APPLICATION.getName());
        flinkConfig.set(KubernetesConfigOptions.NAMESPACE, "zhiliuyun-space");
        flinkConfig.set(KubernetesConfigOptions.KUBERNETES_SERVICE_ACCOUNT, "zhiliuyun");

        KubernetesClusterClientFactory kubernetesClusterClientFactory = new KubernetesClusterClientFactory();
        try (KubernetesClusterDescriptor clusterDescriptor =
                 kubernetesClusterClientFactory.createClusterDescriptor(flinkConfig)) {
            clusterDescriptor.killCluster(stopJobReq.getJobId());
            return StopJobRes.builder().build();
        } catch (Exception e) {
            throw new IsxAppException("提交任务失败" + e.getMessage());
        }
    }
}
