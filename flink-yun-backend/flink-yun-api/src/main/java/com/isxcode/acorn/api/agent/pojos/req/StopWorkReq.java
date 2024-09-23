package com.isxcode.acorn.api.agent.pojos.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StopWorkReq {

    private String flinkHome;

    private String appId;

    private String clusterType;

    private String agentHomePath;
}
