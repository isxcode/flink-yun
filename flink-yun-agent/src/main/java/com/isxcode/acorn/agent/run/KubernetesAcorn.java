package com.isxcode.acorn.agent.run;

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
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class KubernetesAcorn implements AcornRun {

    @Override
    public SubmitJobRes submitJob(SubmitJobReq submitJobReq) {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptions.TARGET, KubernetesDeploymentTarget.APPLICATION.getName());
        flinkConfig.set(PipelineOptions.NAME, submitJobReq.getAppName());
        flinkConfig.set(ApplicationConfiguration.APPLICATION_ARGS, Collections.singletonList(""));
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
            "/Users/ispong/isxcode/flink-yun/flink-yun-dist/pod/pod-template.yaml");
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
            System.out.println(clusterClientProvider.getClusterClient().getClusterId());
            System.out.println(clusterClientProvider.getClusterClient().getWebInterfaceURL());
            return null;
        } catch (Exception e) {
            throw new IsxAppException("提交任务失败" + e.getMessage());
        }

    }

    @Override
    public GetJobInfoRes getJobInfo(GetJobInfoReq getJobInfoReq) {

        Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
        flinkConfig.set(DeploymentOptions.TARGET, KubernetesDeploymentTarget.APPLICATION.getName());
        flinkConfig.set(KubernetesConfigOptions.NAMESPACE, "zhiliuyun-space");
        flinkConfig.set(KubernetesConfigOptions.KUBERNETES_SERVICE_ACCOUNT, "zhiliuyun");

        KubernetesClusterClientFactory kubernetesClusterClientFactory = new KubernetesClusterClientFactory();
        try (KubernetesClusterDescriptor clusterDescriptor =
            kubernetesClusterClientFactory.createClusterDescriptor(flinkConfig)) {
            ClusterClientProvider<String> clusterClientProvider =
                clusterDescriptor.retrieve(getJobInfoReq.getJobId());
            System.out.println(clusterClientProvider.getClusterClient().getWebInterfaceURL());
            return null;
        } catch (Exception e) {
            throw new IsxAppException("提交任务失败" + e.getMessage());
        }
    }

    @Override
    public GetJobLogRes getJobLog(GetJobLogReq getJobLogReq) {

        return null;
    }

    @Override
    public StopJobRes stopJobReq(StopJobReq stopJobReq) {

        return null;
    }

    public String genPodTemplate() {
        return "";
    }

}
