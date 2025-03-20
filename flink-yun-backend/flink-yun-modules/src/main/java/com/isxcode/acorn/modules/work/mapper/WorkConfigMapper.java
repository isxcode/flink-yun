package com.isxcode.acorn.modules.work.mapper;

import com.isxcode.acorn.api.work.dto.SyncWorkConfig;
import com.isxcode.acorn.api.work.res.GetSyncWorkConfigRes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkConfigMapper {

    GetSyncWorkConfigRes syncWorkConfigToGetSyncWorkConfigRes(SyncWorkConfig syncWorkConfig);
}
