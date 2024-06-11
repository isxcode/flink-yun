package com.isxcode.acorn.agent.service;

import com.isxcode.acorn.agent.run.AcornRun;
import com.isxcode.acorn.api.agent.pojos.req.GetJobInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetJobLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopJobReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitJobReq;
import com.isxcode.acorn.api.agent.pojos.res.GetJobInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetJobLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopJobRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitJobRes;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 代理服务层.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AcornAgentBizService {

    private final ApplicationContext applicationContext;

    public AcornRun getAgentRun(String agentType) {

        Optional<AcornRun> agentServiceOptional = applicationContext.getBeansOfType(AcornRun.class).values().stream()
            .filter(agent -> agent.getAgentName().equals(agentType)).findFirst();

        if (!agentServiceOptional.isPresent()) {
            throw new IsxAppException("agent类型不支持");
        }

        return agentServiceOptional.get();
    }

    public SubmitJobRes submitJob(SubmitJobReq submitJobReq) {

        AcornRun agentRun = getAgentRun(submitJobReq.getAgentType());
        return agentRun.submitJob(submitJobReq);
    }

    public GetJobInfoRes getJobInfo(GetJobInfoReq getJobInfoReq) {

        AcornRun agentRun = getAgentRun(getJobInfoReq.getAgentType());
        return agentRun.getJobInfo(getJobInfoReq);
    }

    public GetJobLogRes getJobLog(GetJobLogReq getJobLogReq) {

        AcornRun agentRun = getAgentRun(getJobLogReq.getAgentType());
        return agentRun.getJobLog(getJobLogReq);
    }

    public StopJobRes stopJob(StopJobReq stopJobReq) {

        AcornRun agentRun = getAgentRun(stopJobReq.getAgentType());
        return agentRun.stopJobReq(stopJobReq);
    }
}
