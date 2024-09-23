package com.isxcode.acorn.api.agent.pojos.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWorkLogReq {

    private String appId;

    private String flinkHome;

    private String clusterType;

    private String agentHomePath;

    private String workInstanceId;
}
