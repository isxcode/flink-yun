package com.isxcode.acorn.api.work.pojos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClusterConfig {

    private String setMode;

    private String resourceLevel;

    private String clusterId;

    private String clusterNodeId;

    private Map<String, Object> flinkConfig;

    private String flinkConfigJson;

    private Boolean enableHive;
}
