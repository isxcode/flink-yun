package com.isxcode.acorn.agent.controller;

import com.isxcode.acorn.agent.service.AcornAgentBizService;
import com.isxcode.acorn.api.agent.pojos.req.GetJobInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetJobLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopJobReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitJobReq;
import com.isxcode.acorn.api.agent.pojos.res.GetJobInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetJobLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopJobRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitJobRes;
import com.isxcode.acorn.common.annotations.successResponse.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.immutables.value.Value;
import org.springframework.web.bind.annotation.*;

@Tag(name = "代理模块")
@RequestMapping("acorn-agent")
@RestController
@RequiredArgsConstructor
public class YunAgentController {

	private final AcornAgentBizService acornAgentBizService;

	@Operation(summary = "提交作业接口")
	@PostMapping("/submitJob")
	@SuccessResponse("提交成功")
	public SubmitJobRes submitJob(@Value @RequestBody SubmitJobReq submitJobReq) {

		return acornAgentBizService.submitJob(submitJobReq);
	}

	@Operation(summary = "获取作业信息接口")
	@PostMapping("/getJobInfo")
	@SuccessResponse("获取成功")
	public GetJobInfoRes getJobInfo(@Value @RequestBody GetJobInfoReq getJobInfoReq) {

		return acornAgentBizService.getJobInfo(getJobInfoReq);
	}

	@Operation(summary = "获取日志接口")
	@PostMapping("/getJobLog")
	@SuccessResponse("获取成功")
	public GetJobLogRes getJobLog(@Value @RequestBody GetJobLogReq getJobLogReq) {

		return acornAgentBizService.getJobLog(getJobLogReq);
	}

	@Operation(summary = "停止作业接口")
	@PostMapping("/stopJob")
	@SuccessResponse("获取成功")
	public StopJobRes stopJob(@Value @RequestBody StopJobReq stopJobReq) {

		return acornAgentBizService.stopJob(stopJobReq);
	}

	@Operation(summary = "心跳检测接口")
	@GetMapping("/heartCheck")
	@SuccessResponse("正常心跳")
	public void heartCheck() {

	}
}
