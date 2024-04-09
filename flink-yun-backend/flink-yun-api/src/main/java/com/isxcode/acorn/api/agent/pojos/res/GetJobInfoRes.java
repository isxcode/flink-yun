package com.isxcode.acorn.api.agent.pojos.res;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetJobInfoRes {

	private String jobId;

	private String status;

	private List<String> vertices;
}
