package com.isxcode.acorn.agent.service;

import com.isxcode.acorn.agent.run.AgentFactory;
import com.isxcode.acorn.agent.run.AgentService;
import com.isxcode.acorn.api.agent.pojos.req.GetWorkInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetWorkLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopWorkReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitWorkReq;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetWorkLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopWorkRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitWorkRes;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlinkYunAgentBizService {

    private final AgentFactory agentFactory;

    public SubmitWorkRes submitWork(SubmitWorkReq submitWorkReq) {

        AgentService agentService = agentFactory.getAgentService(submitWorkReq.getClusterType());
        try {
            return agentService.submitWork(submitWorkReq);
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error(httpClientErrorException.getMessage(), httpClientErrorException);
            if (HttpStatus.BAD_REQUEST.equals(httpClientErrorException.getStatusCode())) {
                String errStr = httpClientErrorException.getMessage().replace("400 Bad Request: \"{\"errors\":[\"", "");
                String substring = errStr.substring(0, errStr.length() - 4);
                throw new IsxAppException(substring);
            }
            if (HttpStatus.METHOD_NOT_ALLOWED.equals(httpClientErrorException.getStatusCode())) {
                String errStr =
                    httpClientErrorException.getMessage().replace("405 Method Not Allowed: \"{\"errors\":[\"", "");
                String substring = errStr.substring(0, errStr.length() - 4);
                throw new IsxAppException(substring);
            }
            throw new IsxAppException(httpClientErrorException.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }

    public GetWorkInfoRes getWorkInfo(GetWorkInfoReq getWorkInfoReq) {

        AgentService agentService = agentFactory.getAgentService(getWorkInfoReq.getClusterType());
        try {
            return agentService.getWorkInfo(getWorkInfoReq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }

    public GetWorkLogRes getWorkLog(GetWorkLogReq getWorkLogReq) {

        AgentService agentService = agentFactory.getAgentService(getWorkLogReq.getClusterType());
        try {
            return agentService.getWorkLog(getWorkLogReq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }

    public StopWorkRes stopWork(StopWorkReq stopWorkReq) {

        AgentService agentService = agentFactory.getAgentService(stopWorkReq.getClusterType());
        try {
            return agentService.stopWork(stopWorkReq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }
}
