package com.isxcode.acorn.agent.run;

import com.isxcode.acorn.api.agent.pojos.req.GetWorkInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetWorkLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopWorkReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitWorkReq;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopWorkRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitWorkRes;

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
