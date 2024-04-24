package com.isxcode.acorn.modules.work.mapper;

import com.isxcode.acorn.api.work.pojos.dto.SyncWorkConfig;
import com.isxcode.acorn.api.work.pojos.res.GetSyncWorkConfigRes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkConfigMapper {

    GetSyncWorkConfigRes syncWorkConfigToGetSyncWorkConfigRes(SyncWorkConfig syncWorkConfig);
}
