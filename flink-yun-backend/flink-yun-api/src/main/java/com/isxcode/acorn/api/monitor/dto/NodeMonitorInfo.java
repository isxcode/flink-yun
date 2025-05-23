package com.isxcode.acorn.api.monitor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NodeMonitorInfo {

    private String status;

    private String log;

    private Double usedStorageSize;

    private Double usedMemorySize;

    private Long networkIoReadSpeed;

    private String networkIoReadSpeedStr;

    private Long networkIoWriteSpeed;

    private String networkIoWriteSpeedStr;

    private Long diskIoReadSpeed;

    private String diskIoReadSpeedStr;

    private Long diskIoWriteSpeed;

    private String diskIoWriteSpeedStr;

    private Double cpuPercent;

    private String clusterNodeId;

    private String clusterId;

    private String tenantId;

    private LocalDateTime createDateTime;
}
