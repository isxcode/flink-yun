package com.isxcode.acorn.modules.workflow.mapper;

import com.isxcode.acorn.api.instance.pojos.ao.WorkflowInstanceAo;
import com.isxcode.acorn.api.instance.pojos.dto.WorkInstanceDto;
import com.isxcode.acorn.api.instance.pojos.res.QueryWorkFlowInstancesRes;
import com.isxcode.acorn.api.monitor.pojos.ao.WorkflowMonitorAo;
import com.isxcode.acorn.api.monitor.pojos.res.PageInstancesRes;
import com.isxcode.acorn.api.workflow.pojos.dto.WorkInstanceInfo;
import com.isxcode.acorn.api.workflow.pojos.req.AddWorkflowReq;
import com.isxcode.acorn.api.workflow.pojos.req.UpdateWorkflowReq;
import com.isxcode.acorn.api.workflow.pojos.res.PageWorkflowRes;
import com.isxcode.acorn.modules.work.entity.WorkInstanceEntity;
import com.isxcode.acorn.modules.workflow.entity.WorkflowEntity;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkflowMapper {

    WorkflowEntity addWorkflowReqToWorkflowEntity(AddWorkflowReq addWorkflowReq);

    @Mapping(source = "wofUpdateWorkflowReq.name", target = "name")
    @Mapping(source = "workflowEntity.id", target = "id")
    @Mapping(source = "wofUpdateWorkflowReq.remark", target = "remark")
    @Mapping(source = "wofUpdateWorkflowReq.defaultClusterId", target = "defaultClusterId")
    WorkflowEntity updateWorkflowReqToWorkflowEntity(UpdateWorkflowReq wofUpdateWorkflowReq,
        WorkflowEntity workflowEntity);

    PageWorkflowRes workflowEntityToQueryWorkflowRes(WorkflowEntity workflowEntity);

    @Mapping(source = "status", target = "runStatus")
    @Mapping(source = "id", target = "workInstanceId")
    WorkInstanceInfo workInstanceEntityToWorkInstanceInfo(WorkInstanceEntity workInstances);

    List<WorkInstanceInfo> workInstanceEntityListToWorkInstanceInfoList(List<WorkInstanceEntity> workInstances);

    PageInstancesRes workflowMonitorAoToPageInstancesRes(WorkflowMonitorAo workflowMonitorAo);

    @Mapping(target = "startDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "endDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "nextPlanDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "planStartDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    QueryWorkFlowInstancesRes wfiWorkflowInstanceAo2WfiQueryWorkFlowInstancesRes(
        WorkflowInstanceAo workflowInstanceAoPage);

    @Mapping(target = "startDateTime", source = "execStartDateTime")
    @Mapping(target = "endDateTime", source = "execEndDateTime")
    @Mapping(target = "type", source = "instanceType")
    WorkInstanceDto workInstanceEntity2WorkInstanceVo(WorkInstanceEntity workInstanceEntity);
}
