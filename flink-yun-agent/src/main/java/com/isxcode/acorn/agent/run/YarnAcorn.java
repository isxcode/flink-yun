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
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

@Service
@Slf4j
@RequiredArgsConstructor
public class YarnAcorn implements AcornRun {

	@Override
	public SubmitJobRes submitJob(SubmitJobReq submitJobReq) {

		Configuration flinkConfig = GlobalConfiguration.loadConfiguration();
		flinkConfig.set(DeploymentOptions.TARGET, YarnDeploymentTarget.APPLICATION.getName());
		flinkConfig.set(PipelineOptions.NAME, submitJobReq.getAppName());
		flinkConfig.set(ApplicationConfiguration.APPLICATION_ARGS, singletonList(submitJobReq.getProgramArgs()));
		flinkConfig.set(ApplicationConfiguration.APPLICATION_MAIN_CLASS, submitJobReq.getEntryClass());
		flinkConfig.set(PipelineOptions.JARS, singletonList(
				"/Users/ispong/isxcode/flink-yun/flink-yun-plugins/flink-sql-execute-plugin/build/libs/flink-sql-execute-plugin.jar"));
		flinkConfig.set(DeploymentOptionsInternal.CONF_DIR, submitJobReq.getFlinkHome() + "/conf");
		flinkConfig.set(JobManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("2g"));
		flinkConfig.set(TaskManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("2g"));
		flinkConfig.set(TaskManagerOptions.NUM_TASK_SLOTS, 1);
		flinkConfig.set(YarnConfigOptions.APPLICATION_NAME, submitJobReq.getAppName());
		flinkConfig.set(YarnConfigOptions.FLINK_DIST_JAR,
				"/Users/ispong/isxcode/flink-yun/flink-yun-dist/flink-min/lib/flink-dist-1.18.1.jar");
		List<String> libFile = new ArrayList<>();
		libFile.add("/Users/ispong/isxcode/flink-yun/flink-yun-dist/flink-min/lib");
		libFile.add("/Users/ispong/isxcode/flink-yun/resources/jdbc/system");
		libFile.add("/Users/ispong/isxcode/flink-yun/resources/cdc");
		flinkConfig.set(YarnConfigOptions.SHIP_FILES, libFile);

		ClusterSpecification clusterSpecification = new ClusterSpecification.ClusterSpecificationBuilder()
				.setMasterMemoryMB(1024).setTaskManagerMemoryMB(1024).setSlotsPerTaskManager(2)
				.createClusterSpecification();

		ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.fromConfiguration(flinkConfig);
		applicationConfiguration.applyToConfiguration(flinkConfig);
		YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();
		try (YarnClusterDescriptor clusterDescriptor = yarnClusterClientFactory.createClusterDescriptor(flinkConfig)) {
			ClusterClientProvider<ApplicationId> applicationIdClusterClientProvider = clusterDescriptor
					.deployApplicationCluster(clusterSpecification, applicationConfiguration);
			System.out.println(applicationIdClusterClientProvider.getClusterClient().getClusterId());
			System.out.println(applicationIdClusterClientProvider.getClusterClient().getWebInterfaceURL());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IsxAppException("提交任务失败" + e.getMessage());
		}
	}

	@Override
	public GetJobInfoRes getJobInfo(GetJobInfoReq getJobInfoReq) {
		return null;
	}

	@Override
	public GetJobLogRes getJobLog(GetJobLogReq getJobLogReq) {
		return null;
	}

	@Override
	public StopJobRes stopJobReq(StopJobReq stopJobReq) {
		return null;
	}

	// public static void main(String[] args) {
	// String flinkSql = "CREATE TABLE from_table(\n" +
	// " username STRING,\n" +
	// " age INT\n" +
	// ") WITH (\n" +
	// " 'connector'='jdbc',\n" +
	// " 'url'='jdbc:mysql://localhost:30306/ispong_db',\n" +
	// " 'table-name'='users',\n" +
	// " 'driver'='com.mysql.cj.jdbc.Driver',\n" +
	// " 'username'='root',\n" +
	// " 'password'='ispong123');" +
	// "CREATE TABLE to_table(\n" +
	// " username STRING,\n" +
	// " age INT\n" +
	// ") WITH (\n" +
	// " 'connector'='jdbc',\n" +
	// " 'url'='jdbc:mysql://localhost:30306/ispong_db',\n" +
	// " 'table-name'='users2',\n" +
	// " 'driver'='com.mysql.cj.jdbc.Driver',\n" +
	// " 'username'='root',\n" +
	// " 'password'='ispong123');" +
	// "insert into to_table select * from from_table";
	// System.out.println(Base64.getEncoder().encodeToString(flinkSql.getBytes()));
	// }
}
