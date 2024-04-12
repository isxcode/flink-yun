package com.isxcode.acorn.api.agent.pojos.req;

import lombok.Data;

@Data
public class GetJobLogReq {

    private String jobId;

    private String flinkHome;

    private String agentType;
}
