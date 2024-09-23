package com.isxcode.acorn.modules.work.run;

import com.isxcode.acorn.api.alarm.constants.AlarmEventType;
import com.isxcode.acorn.api.instance.constants.InstanceStatus;
import com.isxcode.acorn.api.instance.constants.InstanceType;
import com.isxcode.acorn.api.work.constants.WorkLog;
import com.isxcode.acorn.api.work.exceptions.WorkRunException;
import com.isxcode.acorn.modules.alarm.service.AlarmService;
import com.isxcode.acorn.modules.work.entity.WorkInstanceEntity;
import com.isxcode.acorn.modules.work.repository.WorkInstanceRepository;
import com.isxcode.acorn.modules.workflow.entity.WorkflowInstanceEntity;
import com.isxcode.acorn.modules.workflow.repository.WorkflowInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.isxcode.acorn.common.config.CommonConfig.TENANT_ID;
import static com.isxcode.acorn.common.config.CommonConfig.USER_ID;

@Slf4j
@RequiredArgsConstructor
public abstract class WorkExecutor {

    public static final Map<String, Thread> WORK_THREAD = new HashMap<>();

    private final WorkInstanceRepository workInstanceRepository;

    private final WorkflowInstanceRepository workflowInstanceRepository;

    private final AlarmService alarmService;

    public abstract String getWorkType();

    protected abstract void execute(WorkRunContext workRunContext, WorkInstanceEntity workInstance);

    protected abstract void abort(WorkInstanceEntity workInstance);

    public WorkInstanceEntity updateInstance(WorkInstanceEntity workInstance, StringBuilder logBuilder) {

        workInstance.setSubmitLog(logBuilder.toString());
        return workInstanceRepository.saveAndFlush(workInstance);
    }

    @Async("flinkYunWorkThreadPool")
    public void asyncExecute(WorkRunContext workRunContext) {

        // 初始化异步线程中的上下文
        USER_ID.set(workRunContext.getUserId());
        TENANT_ID.set(workRunContext.getTenantId());

        syncExecute(workRunContext);
    }

    public void syncExecute(WorkRunContext workRunContext) {

        // 获取作业实例
        WorkInstanceEntity workInstance = workInstanceRepository.findById(workRunContext.getInstanceId()).get();

        // 初始化日志
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("开始提交作业 \n");

        // 更新作业实例开始运行参数
        workInstance.setStatus(InstanceStatus.RUNNING);
        workInstance.setSubmitLog(logBuilder.toString());
        workInstance.setExecStartDateTime(new Date());
        workInstance = workInstanceRepository.saveAndFlush(workInstance);

        try {

            // 日志需要贯穿上下文
            workRunContext.setLogBuilder(logBuilder);

            // 任务开始运行，异步发送消息
            if (InstanceType.AUTO.equals(workInstance.getInstanceType())) {
                alarmService.sendWorkMessage(workInstance, AlarmEventType.START_RUN);
            }

            // 开始执行作业
            execute(workRunContext, workInstance);

            // 重新获取当前最新实例
            workInstance = workInstanceRepository.findById(workRunContext.getInstanceId()).get();

            // 更新作业实例成功状态
            workInstance.setStatus(InstanceStatus.SUCCESS);
            workInstance.setExecEndDateTime(new Date());
            workInstance
                .setDuration((System.currentTimeMillis() - workInstance.getExecStartDateTime().getTime()) / 1000);
            logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("执行成功 \n");
            workInstance.setSubmitLog(logBuilder.toString());
            workInstanceRepository.save(workInstance);

            // 修改工作流日志
            if (!Strings.isEmpty(workInstance.getWorkflowInstanceId())) {
                WorkflowInstanceEntity workflowInstance =
                    workflowInstanceRepository.findById(workInstance.getWorkflowInstanceId()).get();
                String runLog = workflowInstanceRepository.getWorkflowLog(workflowInstance.getId()) + "\n"
                    + LocalDateTime.now() + WorkLog.SUCCESS_INFO + "作业: 【" + workRunContext.getWorkName() + "】运行成功";
                workflowInstance.setRunLog(runLog);
                workflowInstanceRepository.setWorkflowLog(workflowInstance.getId(), runLog);
            }

            // 任务运行成功，异步发送消息
            if (InstanceType.AUTO.equals(workInstance.getInstanceType())) {
                alarmService.sendWorkMessage(workInstance, AlarmEventType.RUN_SUCCESS);
            }

            // 执行完请求线程
            WORK_THREAD.remove(workInstance.getId());

        } catch (WorkRunException e) {
            log.debug(e.getMsg(), e);

            // 重新获取当前最新实例
            workInstance = workInstanceRepository.findById(workRunContext.getInstanceId()).get();

            // 如果是已中止，直接不处理
            if (InstanceStatus.ABORT.equals(workInstance.getStatus())) {
                return;
            }

            // 更新作业实例失败状态
            workInstance.setStatus(InstanceStatus.FAIL);
            workInstance.setExecEndDateTime(new Date());
            workInstance
                .setDuration((System.currentTimeMillis() - workInstance.getExecStartDateTime().getTime()) / 1000);
            logBuilder.append(e.getMsg());
            logBuilder.append(LocalDateTime.now()).append(WorkLog.ERROR_INFO).append("执行失败 \n");
            workInstance.setSubmitLog(logBuilder.toString());
            workInstanceRepository.save(workInstance);

            // 修改工作流日志
            if (!Strings.isEmpty(workInstance.getWorkflowInstanceId())) {
                WorkflowInstanceEntity workflowInstance =
                    workflowInstanceRepository.findById(workInstance.getWorkflowInstanceId()).get();
                String runLog = workflowInstanceRepository.getWorkflowLog(workflowInstance.getId()) + "\n"
                    + LocalDateTime.now() + WorkLog.SUCCESS_INFO + "作业: 【" + workRunContext.getWorkName() + "】运行失败";
                workflowInstance.setRunLog(runLog);
                workflowInstanceRepository.setWorkflowLog(workflowInstance.getId(), runLog);
            }

            // 任务运行失败，异步发送消息
            if (InstanceType.AUTO.equals(workInstance.getInstanceType())) {
                alarmService.sendWorkMessage(workInstance, AlarmEventType.RUN_FAIL);
            }
        }

        // 任务开始运行，异步发送消息
        if (InstanceType.AUTO.equals(workInstance.getInstanceType())) {
            alarmService.sendWorkMessage(workInstance, AlarmEventType.RUN_END);
        }

        // 执行完请求线程
        WORK_THREAD.remove(workInstance.getId());
    }

    public void syncAbort(WorkInstanceEntity workInstance) {

        this.abort(workInstance);
    }
}
