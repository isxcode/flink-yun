package com.isxcode.acorn.agent.controller;

import com.isxcode.acorn.agent.service.FlinkYunAgentBizService;
import com.isxcode.acorn.api.agent.constants.AgentUrl;
import com.isxcode.acorn.api.agent.pojos.req.GetWorkInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetWorkLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopWorkReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitWorkReq;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopWorkRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitWorkRes;
import com.isxcode.acorn.common.annotations.successResponse.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.immutables.value.Value;
import org.springframework.web.bind.annotation.*;

@Tag(name = "至流云代理模块")
@RestController
@RequiredArgsConstructor
public class FlinkYunAgentController {

    private final FlinkYunAgentBizService flinkYunAgentBizService;

    @Operation(summary = "提交作业接口")
    @PostMapping(AgentUrl.SUBMIT_WORK_URL)
    @SuccessResponse("提交成功")
    public SubmitWorkRes submitWork(@Value @RequestBody SubmitWorkReq submitWorkReq) {

        return flinkYunAgentBizService.submitWork(submitWorkReq);
    }

    @Operation(summary = "获取作业信息接口")
    @PostMapping(AgentUrl.GET_WORK_INFO_URL)
    @SuccessResponse("获取成功")
    public GetWorkInfoRes getWorkInfo(@Value @RequestBody GetWorkInfoReq getWorkInfoReq) {

        return flinkYunAgentBizService.getWorkInfo(getWorkInfoReq);
    }

    @Operation(summary = "获取日志接口")
    @PostMapping(AgentUrl.GET_WORK_LOG_URL)
    @SuccessResponse("获取成功")
    public GetWorkLogRes getWorkLog(@Value @RequestBody GetWorkLogReq getWorkLogReq) {

        return flinkYunAgentBizService.getWorkLog(getWorkLogReq);
    }

    @Operation(summary = "中止作业接口")
    @PostMapping(AgentUrl.STOP_WORK_URL)
    @SuccessResponse("中止成功")
    public StopWorkRes stopWork(@Value @RequestBody StopWorkReq stopWorkReq) {

        return flinkYunAgentBizService.stopWork(stopWorkReq);
    }

    @Operation(summary = "心跳检测接口")
    @PostMapping(AgentUrl.HEART_CHECK_URL)
    @SuccessResponse("正常心跳")
    public void heartCheck() {

    }
}
