package com.isxcode.acorn.api.workflow.pojos.res;

import com.isxcode.acorn.api.work.pojos.dto.CronConfig;
import lombok.Data;

@Data
public class GetWorkflowRes {

	private Object webConfig;

	private CronConfig cronConfig;
}
