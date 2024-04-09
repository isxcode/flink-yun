package com.isxcode.acorn.api.agent.pojos.req;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FlinkRestRunReq {

	private String entryClass;

	private String parallelism;

	private String programArgs;

	private List<String> programArgsList;

	private String savepointPath;

	private boolean allowNonRestoredState;
}