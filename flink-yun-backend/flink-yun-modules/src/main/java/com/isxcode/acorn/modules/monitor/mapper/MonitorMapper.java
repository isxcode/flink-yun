package com.isxcode.acorn.modules.monitor.mapper;

import com.isxcode.acorn.api.monitor.dto.NodeMonitorInfo;
import com.isxcode.acorn.modules.monitor.entity.MonitorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorMapper {

    MonitorEntity nodeMonitorInfoToMonitorEntity(NodeMonitorInfo nodeMonitorInfo);
}
