package com.isxcode.acorn.api.agent.pojos.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopWorkReq {

    private String flinkHome;

    private String appId;

    private String clusterType;

    private String agentHomePath;
}
