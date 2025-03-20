package com.isxcode.acorn.agent.run;

import com.isxcode.acorn.api.agent.req.GetWorkInfoReq;
import com.isxcode.acorn.api.agent.req.GetWorkLogReq;
import com.isxcode.acorn.api.agent.req.StopWorkReq;
import com.isxcode.acorn.api.agent.req.SubmitWorkReq;
import com.isxcode.acorn.api.agent.res.GetWorkInfoRes;
import com.isxcode.acorn.api.agent.res.GetWorkLogRes;
import com.isxcode.acorn.api.agent.res.StopWorkRes;
import com.isxcode.acorn.api.agent.res.SubmitWorkRes;

public interface AgentService {

    /**
     * 获取当前代理的类型
     */
    String getAgentType();

    /**
     * 提交flink作业
     */
    SubmitWorkRes submitWork(SubmitWorkReq submitWorkReq) throws Exception;

    /**
     * 获取作业信息
     */
    GetWorkInfoRes getWorkInfo(GetWorkInfoReq getWorkInfoReq) throws Exception;

    /**
     * 获取作业日志
     */
    GetWorkLogRes getWorkLog(GetWorkLogReq getWorkLogReq) throws Exception;

    /**
     * 中止作业
     */
    StopWorkRes stopWork(StopWorkReq stopWorkReq) throws Exception;
}
