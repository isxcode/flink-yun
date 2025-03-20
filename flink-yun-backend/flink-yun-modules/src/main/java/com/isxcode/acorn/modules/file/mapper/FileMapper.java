package com.isxcode.acorn.modules.file.mapper;

import com.isxcode.acorn.api.file.res.PageFileRes;
import com.isxcode.acorn.modules.file.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    PageFileRes fileEntityToPageFileRes(FileEntity fileEntity);
}
