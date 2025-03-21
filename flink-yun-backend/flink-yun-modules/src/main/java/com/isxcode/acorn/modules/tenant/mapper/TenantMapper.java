package com.isxcode.acorn.modules.tenant.mapper;

import com.isxcode.acorn.api.tenant.req.AddTenantReq;
import com.isxcode.acorn.api.tenant.req.UpdateTenantForSystemAdminReq;
import com.isxcode.acorn.api.tenant.req.UpdateTenantForTenantAdminReq;
import com.isxcode.acorn.api.tenant.res.PageTenantRes;
import com.isxcode.acorn.api.tenant.res.QueryUserTenantRes;
import com.isxcode.acorn.api.user.constants.UserStatus;
import com.isxcode.acorn.modules.tenant.entity.TenantEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(target = "checkDateTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = UserStatus.ENABLE)
    @Mapping(target = "usedMemberNum", constant = "1L")
    @Mapping(target = "usedWorkflowNum", constant = "0L")
    TenantEntity tetAddTenantReqToTenantEntity(AddTenantReq tetAddTenantReq);

    QueryUserTenantRes tenantEntityToTetQueryUserTenantRes(TenantEntity tenantEntity);

    List<QueryUserTenantRes> tenantEntityToTetQueryUserTenantResList(List<TenantEntity> tenantEntities);

    @Mapping(target = "remark", source = "tetUpdateTenantBySystemAdminReq.remark")
    @Mapping(target = "maxWorkflowNum", source = "tetUpdateTenantBySystemAdminReq.maxWorkflowNum")
    @Mapping(target = "maxMemberNum", source = "tetUpdateTenantBySystemAdminReq.maxMemberNum")
    @Mapping(target = "name", source = "tetUpdateTenantBySystemAdminReq.name")
    @Mapping(target = "id", source = "tenantEntity.id")
    TenantEntity tetUpdateTenantBySystemAdminReqToTenantEntity(
        UpdateTenantForSystemAdminReq tetUpdateTenantBySystemAdminReq, TenantEntity tenantEntity);

    @Mapping(target = "introduce", source = "tetUpdateTenantByTenantAdminReq.introduce")
    @Mapping(target = "id", source = "tenantEntity.id")
    TenantEntity tetUpdateTenantByTenantAdminReqToTenantEntity(
        UpdateTenantForTenantAdminReq tetUpdateTenantByTenantAdminReq, TenantEntity tenantEntity);

    @Mapping(target = "checkDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    PageTenantRes tenantEntityToTetQueryTenantRes(TenantEntity tenantEntity);
}
