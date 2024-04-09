package com.isxcode.acorn.api.agent.pojos.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetJobInfoReq {

	private String jobId;

	private String flinkHome;

	private String agentType;
}
