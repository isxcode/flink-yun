package com.isxcode.acorn.modules.real.mapper;

import com.isxcode.acorn.api.real.req.AddRealReq;
import com.isxcode.acorn.api.real.res.GetRealSubmitLogRes;
import com.isxcode.acorn.api.real.res.PageRealRes;
import com.isxcode.acorn.modules.real.entity.RealEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RealMapper {

    RealEntity addRealReqToRealEntity(AddRealReq addRealReq);

    PageRealRes realEntityToPageRealRes(RealEntity real);

    GetRealSubmitLogRes realEntityToGetRealSubmitLogRes(RealEntity real);
}
