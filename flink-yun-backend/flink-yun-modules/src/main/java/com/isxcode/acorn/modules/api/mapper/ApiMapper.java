package com.isxcode.acorn.modules.api.mapper;

import com.isxcode.acorn.api.api.pojos.req.AddApiReq;
import com.isxcode.acorn.api.api.pojos.req.UpdateApiReq;
import com.isxcode.acorn.api.api.pojos.res.GetApiRes;
import com.isxcode.acorn.api.api.pojos.res.PageApiRes;
import com.isxcode.acorn.modules.api.entity.ApiEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiMapper {

    @Mapping(target = "reqHeader", ignore = true)
    ApiEntity apiAddApiReqToApiEntity(AddApiReq addApiReq);

    @Mapping(target = "name", source = "updateApiReq.name")
    @Mapping(target = "path", source = "updateApiReq.path")
    @Mapping(target = "apiType", source = "updateApiReq.apiType")
    @Mapping(target = "remark", source = "updateApiReq.remark")
    @Mapping(target = "reqBody", source = "updateApiReq.reqBody")
    @Mapping(target = "apiSql", source = "updateApiReq.apiSql")
    @Mapping(target = "resBody", source = "updateApiReq.resBody")
    @Mapping(target = "datasourceId", source = "updateApiReq.datasourceId")
    @Mapping(target = "tokenType", source = "updateApiReq.tokenType")
    @Mapping(target = "pageType", source = "updateApiReq.pageType")
    @Mapping(target = "id", source = "apiEntity.id")
    @Mapping(target = "reqHeader", ignore = true)
    ApiEntity updateApiReqToApiEntity(UpdateApiReq updateApiReq, ApiEntity apiEntity);

    PageApiRes apiEntityToPageApiRes(ApiEntity apiEntity);

    @Mapping(target = "reqHeader", ignore = true)
    GetApiRes apiEntityToGetApiRes(ApiEntity apiEntity);
}
