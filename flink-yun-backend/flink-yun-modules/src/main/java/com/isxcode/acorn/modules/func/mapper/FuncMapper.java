package com.isxcode.acorn.modules.func.mapper;

import com.isxcode.acorn.api.func.pojos.dto.FuncInfo;
import com.isxcode.acorn.api.func.pojos.req.AddFuncReq;
import com.isxcode.acorn.api.func.pojos.req.UpdateFuncReq;
import com.isxcode.acorn.api.func.pojos.res.PageFuncRes;
import com.isxcode.acorn.modules.func.entity.FuncEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FuncMapper {

    FuncEntity addFuncReqToFuncEntity(AddFuncReq addFuncReq);

    @Mapping(source = "updateFuncReq.funcName", target = "funcName")
    @Mapping(source = "updateFuncReq.className", target = "className")
    @Mapping(source = "updateFuncReq.id", target = "id")
    @Mapping(source = "updateFuncReq.remark", target = "remark")
    @Mapping(source = "updateFuncReq.fileId", target = "fileId")
    FuncEntity updateFuncReqToFuncEntity(UpdateFuncReq updateFuncReq, FuncEntity udfEntity);

    PageFuncRes funcEntityToPageFuncRes(FuncEntity funcEntity);

    List<FuncInfo> funcEntityListToFuncInfoList(List<FuncEntity> funcEntities);
}
