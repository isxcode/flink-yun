package com.isxcode.acorn.modules.workflow.service;

import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.modules.work.entity.WorkInstanceEntity;
import com.isxcode.acorn.modules.work.repository.WorkInstanceRepository;
import com.isxcode.acorn.modules.workflow.entity.WorkflowConfigEntity;
import com.isxcode.acorn.modules.workflow.entity.WorkflowEntity;
import com.isxcode.acorn.modules.workflow.repository.WorkflowConfigRepository;
import com.isxcode.acorn.modules.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkInstanceRepository workInstanceRepository;

    private final WorkflowRepository workflowRepository;

    private final WorkflowConfigRepository workflowConfigRepository;

    public WorkInstanceEntity getWorkInstance(String workInstanceId) {

        return workInstanceRepository.findById(workInstanceId).orElseThrow(() -> new IsxAppException("实例不存在"));
    }

    public WorkflowEntity getWorkflow(String workflowId) {

        return workflowRepository.findById(workflowId).orElseThrow(() -> new IsxAppException("工作流不存在"));
    }

    public WorkflowConfigEntity getWorkflowConfig(String workflowConfigId) {

        return workflowConfigRepository.findById(workflowConfigId).orElseThrow(() -> new IsxAppException("工作流配置不存在"));
    }
}
