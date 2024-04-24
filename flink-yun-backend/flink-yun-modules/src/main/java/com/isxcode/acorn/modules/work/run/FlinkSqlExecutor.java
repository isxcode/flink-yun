package com.isxcode.acorn.modules.work.run;

import com.alibaba.fastjson.JSON;
import com.isxcode.acorn.api.agent.constants.AgentApi;
import com.isxcode.acorn.api.agent.pojos.req.AcornPluginReq;
import com.isxcode.acorn.api.agent.pojos.req.GetJobInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetJobLogReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitJobReq;
import com.isxcode.acorn.api.agent.pojos.res.GetJobInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetJobLogRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitJobRes;
import com.isxcode.acorn.api.cluster.constants.ClusterNodeStatus;
import com.isxcode.acorn.api.work.constants.WorkLog;
import com.isxcode.acorn.api.work.exceptions.WorkRunException;
import com.isxcode.acorn.api.work.pojos.res.RunWorkRes;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.backend.api.base.pojos.BaseResponse;
import com.isxcode.acorn.backend.api.base.properties.IsxAppProperties;
import com.isxcode.acorn.common.locker.Locker;
import com.isxcode.acorn.common.utils.AesUtils;
import com.isxcode.acorn.common.utils.http.HttpUrlUtils;
import com.isxcode.acorn.common.utils.http.HttpUtils;
import com.isxcode.acorn.modules.cluster.entity.ClusterEntity;
import com.isxcode.acorn.modules.cluster.entity.ClusterNodeEntity;
import com.isxcode.acorn.modules.cluster.mapper.ClusterNodeMapper;
import com.isxcode.acorn.modules.cluster.repository.ClusterNodeRepository;
import com.isxcode.acorn.modules.cluster.repository.ClusterRepository;
import com.isxcode.acorn.modules.datasource.service.DatasourceService;
import com.isxcode.acorn.modules.file.repository.FileRepository;
import com.isxcode.acorn.modules.func.mapper.FuncMapper;
import com.isxcode.acorn.modules.func.repository.FuncRepository;
import com.isxcode.acorn.modules.work.entity.WorkConfigEntity;
import com.isxcode.acorn.modules.work.entity.WorkEntity;
import com.isxcode.acorn.modules.work.entity.WorkInstanceEntity;
import com.isxcode.acorn.modules.work.repository.WorkConfigRepository;
import com.isxcode.acorn.modules.work.repository.WorkInstanceRepository;
import com.isxcode.acorn.modules.work.repository.WorkRepository;
import com.isxcode.acorn.modules.workflow.repository.WorkflowInstanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class FlinkSqlExecutor extends WorkExecutor {

    private final WorkInstanceRepository workInstanceRepository;

    private final ClusterRepository clusterRepository;

    private final ClusterNodeRepository clusterNodeRepository;

    private final WorkRepository workRepository;

    private final WorkConfigRepository workConfigRepository;

    private final Locker locker;

    private final HttpUrlUtils httpUrlUtils;

    private final FuncRepository funcRepository;

    private final FuncMapper funcMapper;

    private final ClusterNodeMapper clusterNodeMapper;

    private final AesUtils aesUtils;

    private final IsxAppProperties isxAppProperties;

    private final FileRepository fileRepository;

    private final DatasourceService datasourceService;

    public FlinkSqlExecutor(WorkInstanceRepository workInstanceRepository, ClusterRepository clusterRepository,
        ClusterNodeRepository clusterNodeRepository, WorkflowInstanceRepository workflowInstanceRepository,
        WorkRepository workRepository, WorkConfigRepository workConfigRepository, Locker locker,
        HttpUrlUtils httpUrlUtils, FuncRepository funcRepository, FuncMapper funcMapper,
        ClusterNodeMapper clusterNodeMapper, AesUtils aesUtils, IsxAppProperties isxAppProperties,
        FileRepository fileRepository, DatasourceService datasourceService) {

        super(workInstanceRepository, workflowInstanceRepository);
        this.workInstanceRepository = workInstanceRepository;
        this.clusterRepository = clusterRepository;
        this.clusterNodeRepository = clusterNodeRepository;
        this.workRepository = workRepository;
        this.workConfigRepository = workConfigRepository;
        this.locker = locker;
        this.httpUrlUtils = httpUrlUtils;
        this.funcRepository = funcRepository;
        this.funcMapper = funcMapper;
        this.clusterNodeMapper = clusterNodeMapper;
        this.aesUtils = aesUtils;
        this.isxAppProperties = isxAppProperties;
        this.fileRepository = fileRepository;
        this.datasourceService = datasourceService;
    }

    @Override
    protected void execute(WorkRunContext workRunContext, WorkInstanceEntity workInstance) {

        // 获取日志构造器
        StringBuilder logBuilder = workRunContext.getLogBuilder();

        // 判断执行脚本是否为空
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("检测脚本内容 \n");
        if (Strings.isEmpty(workRunContext.getScript())) {
            throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "检测脚本失败 : SQL内容为空不能执行  \n");
        }

        // 检测计算集群是否存在
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("开始申请资源 \n");
        if (Strings.isEmpty(workRunContext.getClusterConfig().getClusterId())) {
            throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "申请资源失败 : 计算引擎未配置  \n");
        }

        // 检查计算集群是否存在
        Optional<ClusterEntity> calculateEngineEntityOptional =
            clusterRepository.findById(workRunContext.getClusterConfig().getClusterId());
        if (!calculateEngineEntityOptional.isPresent()) {
            throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "申请资源失败 : 计算引擎不存在  \n");
        }

        // 检测集群中是否有合法节点
        List<ClusterNodeEntity> allEngineNodes = clusterNodeRepository
            .findAllByClusterIdAndStatus(calculateEngineEntityOptional.get().getId(), ClusterNodeStatus.RUNNING);
        if (allEngineNodes.isEmpty()) {
            throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "申请资源失败 : 集群不存在可用节点，请切换一个集群  \n");
        }

        // 节点选择随机数
        ClusterNodeEntity engineNode = allEngineNodes.get(new Random().nextInt(allEngineNodes.size()));
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("申请资源完成，激活节点:【")
            .append(engineNode.getName()).append("】\n");
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("检测运行环境完成  \n");
        workInstance = updateInstance(workInstance, logBuilder);

        // 开始构建作业
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("开始构建作业  \n");

        // 开始构造flinkSubmit
        SubmitJobReq submitJobReq = SubmitJobReq.builder().entryClass("com.isxcode.acorn.plugin.sql.execute.Job")
            .agentHomePath(engineNode.getAgentHomePath()).appResource("flink-sql-execute-plugin.jar")
            .appName("zhiliuyun-job").workInstanceId(workInstance.getId())
            .acornPluginReq(AcornPluginReq.builder().flinkSql(workRunContext.getScript()).build())
            .agentType(calculateEngineEntityOptional.get().getClusterType()).build();

        // 构建作业完成，并打印作业配置信息
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("构建作业完成 \n");
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("开始提交作业  \n");
        workInstance = updateInstance(workInstance, logBuilder);

        // 加锁，必须等待作业提交成功后才能中止
        Integer lock = locker.lock("REQUEST_" + workInstance.getId());
        SubmitJobRes submitJobRes;
        try {
            submitJobRes = new RestTemplate().postForObject(
                httpUrlUtils.genHttpUrl(engineNode.getHost(), engineNode.getAgentPort(), AgentApi.submitJob),
                submitJobReq, SubmitJobRes.class);
            logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("提交作业成功 : ")
                .append(submitJobRes.getJobId()).append("\n");
            workInstance.setSparkStarRes(JSON.toJSONString(submitJobRes));
            workInstance = updateInstance(workInstance, logBuilder);
        } catch (HttpServerErrorException | ResourceAccessException e) {
            throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "提交作业失败 : " + e.getMessage() + "\n");
        } finally {
            locker.unlock(lock);
        }

        // 提交作业成功后，开始循环判断状态
        while (true) {

            // 获取作业状态并保存
            GetJobInfoReq jobInfoReq = GetJobInfoReq.builder().agentHome(engineNode.getAgentHomePath())
                .jobId(submitJobRes.getJobId()).agentType(calculateEngineEntityOptional.get().getClusterType()).build();
            GetJobInfoRes getJobInfoRes;
            try {
                getJobInfoRes = HttpUtils.doPost(
                    httpUrlUtils.genHttpUrl(engineNode.getHost(), engineNode.getAgentPort(), AgentApi.getJobInfo),
                    jobInfoReq, GetJobInfoRes.class);
                log.debug("获取远程获取状态日志:{}", getJobInfoRes);
            } catch (Exception e) {
                throw new WorkRunException(
                    LocalDateTime.now() + WorkLog.ERROR_INFO + "获取作业状态异常 : " + e.getMessage() + "\n");
            }

            // 解析返回状态，并保存
            workInstance.setSparkStarRes(JSON.toJSONString(getJobInfoRes));
            logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("运行状态:")
                .append(getJobInfoRes.getStatus()).append("\n");
            workInstance = updateInstance(workInstance, logBuilder);

            // 如果状态是运行中，更新日志，继续执行
            List<String> runningStatus = Arrays.asList("RUNNING", "UNDEFINED", "SUBMITTED", "CONTAINERCREATING");
            if (runningStatus.contains(getJobInfoRes.getStatus().toUpperCase())) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new WorkRunException(
                        LocalDateTime.now() + WorkLog.ERROR_INFO + "睡眠线程异常 : " + e.getMessage() + "\n");
                }
            } else {
                // 运行结束逻辑

                // 如果是中止，直接退出
                if ("KILLED".equalsIgnoreCase(getJobInfoRes.getStatus())
                    || "TERMINATING".equalsIgnoreCase(getJobInfoRes.getStatus())) {
                    throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "作业运行中止" + "\n");
                }

                // 获取日志并保存
                GetJobLogReq getJobLogReq = GetJobLogReq.builder().agentHomePath(engineNode.getAgentHomePath())
                    .jobId(submitJobRes.getJobId()).workInstanceId(workInstance.getId())
                    .agentType(calculateEngineEntityOptional.get().getClusterType()).build();
                GetJobLogRes getJobLogRes;
                try {
                    getJobLogRes = HttpUtils.doPost(
                        httpUrlUtils.genHttpUrl(engineNode.getHost(), engineNode.getAgentPort(), AgentApi.getJobLog),
                        getJobLogReq, GetJobLogRes.class);
                } catch (IOException e) {
                    throw new WorkRunException(
                        LocalDateTime.now() + WorkLog.ERROR_INFO + "获取作业日志异常 : " + e.getMessage() + "\n");
                }

                logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("日志保存成功 \n");
                if (getJobLogRes != null) {
                    workInstance.setTaskManagerLog(getJobLogRes.getLog());
                }
                updateInstance(workInstance, logBuilder);

                // 如果运行成功，则保存返回数据
                List<String> successStatus = Arrays.asList("FINISHED", "SUCCEEDED", "COMPLETED");
                if (successStatus.contains(getJobInfoRes.getStatus().toUpperCase())) {
                    // 没有数据保存
                } else {
                    // 任务运行错误
                    throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "任务运行异常" + "\n");
                }

                // 运行结束，则退出死循环
                break;
            }
        }
    }

    @Override
    protected void abort(WorkInstanceEntity workInstance) {

        // 判断作业有没有提交成功
        locker.lock("REQUEST_" + workInstance.getId());
        try {
            workInstance = workInstanceRepository.findById(workInstance.getId()).get();
            if (!Strings.isEmpty(workInstance.getSparkStarRes())) {
                RunWorkRes wokRunWorkRes = JSON.parseObject(workInstance.getSparkStarRes(), RunWorkRes.class);
                if (!Strings.isEmpty(wokRunWorkRes.getAppId())) {
                    // 关闭远程线程
                    WorkEntity work = workRepository.findById(workInstance.getWorkId()).get();
                    WorkConfigEntity workConfig = workConfigRepository.findById(work.getConfigId()).get();
                    List<ClusterNodeEntity> allEngineNodes = clusterNodeRepository
                        .findAllByClusterIdAndStatus(workConfig.getClusterConfig(), ClusterNodeStatus.RUNNING);
                    if (allEngineNodes.isEmpty()) {
                        throw new WorkRunException(
                            LocalDateTime.now() + WorkLog.ERROR_INFO + "申请资源失败 : 集群不存在可用节点，请切换一个集群  \n");
                    }
                    ClusterEntity cluster = clusterRepository.findById(workConfig.getClusterConfig()).get();

                    // 节点选择随机数
                    ClusterNodeEntity engineNode = allEngineNodes.get(new Random().nextInt(allEngineNodes.size()));

                    Map<String, String> paramsMap = new HashMap<>();
                    paramsMap.put("appId", wokRunWorkRes.getAppId());
                    paramsMap.put("agentType", cluster.getClusterType());
                    paramsMap.put("sparkHomePath", engineNode.getFlinkHomePath());
                    BaseResponse<?> baseResponse = HttpUtils.doGet(
                        httpUrlUtils.genHttpUrl(engineNode.getHost(), engineNode.getAgentPort(), "/yag/stopJob"),
                        paramsMap, null, BaseResponse.class);

                    if (!String.valueOf(HttpStatus.OK.value()).equals(baseResponse.getCode())) {
                        throw new IsxAppException(baseResponse.getCode(), baseResponse.getMsg(), baseResponse.getErr());
                    }
                } else {
                    // 先杀死进程
                    WORK_THREAD.get(workInstance.getId()).interrupt();
                }
            }
        } finally {
            locker.clearLock("REQUEST_" + workInstance.getId());
        }
    }

    /**
     * 初始化spark作业提交配置.
     */
    public Map<String, String> genSparkSubmitConfig(Map<String, String> sparkConfig) {

        // 过滤掉，前缀不包含spark.xxx的配置，spark
        // submit中必须都是spark.xxx
        Map<String, String> sparkSubmitConfig = new HashMap<>();
        sparkConfig.forEach((k, v) -> {
            if (k.startsWith("spark")) {
                sparkSubmitConfig.put(k, v);
            }
        });
        return sparkSubmitConfig;
    }

    /**
     * sparkConfig不能包含k8s的配置
     */
    public Map<String, String> genSparkConfig(Map<String, String> sparkConfig) {

        // k8s的配置不能提交到作业中
        sparkConfig.remove("spark.kubernetes.driver.podTemplateFile");
        sparkConfig.remove("spark.kubernetes.executor.podTemplateFile");

        return sparkConfig;
    }
}
