package com.isxcode.acorn.api.agent.pojos.req;

import lombok.Data;

@Data
public class StopJobReq {

    private String flinkHome;

    private String jobId;

    private String agentType;
}
