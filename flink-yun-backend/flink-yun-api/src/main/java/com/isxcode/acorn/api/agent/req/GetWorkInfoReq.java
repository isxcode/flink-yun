package com.isxcode.acorn.api.agent.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetWorkInfoReq {

    private String appId;

    private String agentHome;

    private String clusterType;

    private String flinkHome;
}
