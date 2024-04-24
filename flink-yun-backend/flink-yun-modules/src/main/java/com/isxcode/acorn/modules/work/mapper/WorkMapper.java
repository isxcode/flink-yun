package com.isxcode.acorn.modules.work.mapper;

import com.isxcode.acorn.api.work.pojos.req.AddWorkReq;
import com.isxcode.acorn.api.work.pojos.req.UpdateWorkReq;
import com.isxcode.acorn.api.work.pojos.res.PageWorkRes;
import com.isxcode.acorn.modules.work.entity.WorkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * mapstruct映射.
 */
@Mapper(componentModel = "spring")
public interface WorkMapper {

    WorkEntity addWorkReqToWorkEntity(AddWorkReq addWorkReq);

    @Mapping(source = "wokUpdateWorkReq.remark", target = "remark")
    @Mapping(source = "wokUpdateWorkReq.name", target = "name")
    @Mapping(source = "workEntity.id", target = "id")
    @Mapping(source = "workEntity.workType", target = "workType")
    WorkEntity updateWorkReqToWorkEntity(UpdateWorkReq wokUpdateWorkReq, WorkEntity workEntity);

    @Mapping(target = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    PageWorkRes workEntityToPageWorkRes(WorkEntity workEntity);
}
