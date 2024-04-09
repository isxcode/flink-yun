package com.isxcode.acorn.agent.service;

import com.isxcode.acorn.agent.run.FlinkClusterAcorn;
import com.isxcode.acorn.api.agent.constants.AgentType;
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
import org.springframework.stereotype.Service;

/**
 * 代理服务层.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AcornAgentBizService {

	private final FlinkClusterAcorn flinkClusterAcorn;

	public SubmitJobRes submitJob(SubmitJobReq submitJobReq) {

		switch (submitJobReq.getAgentType()) {
			case AgentType.YARN :
				break;
			case AgentType.K8S :

				break;
			case AgentType.FlinkCluster :

				return flinkClusterAcorn.submitJob(submitJobReq);
			default :
				throw new IsxAppException("agent类型不支持");
		}
		return null;
	}

	public GetJobInfoRes getJobInfo(GetJobInfoReq getJobInfoReq) {

		switch (getJobInfoReq.getAgentType()) {
			case AgentType.YARN :

				break;
			case AgentType.K8S :

				break;
			case AgentType.FlinkCluster :

				return flinkClusterAcorn.getJobInfo(getJobInfoReq);
			default :
				throw new IsxAppException("agent类型不支持");
		}

		return null;
	}

	public GetJobLogRes getJobLog(GetJobLogReq getJobLogReq) {

		switch (getJobLogReq.getAgentType()) {
			case AgentType.YARN :
				break;
			case AgentType.K8S :
				break;
			case AgentType.FlinkCluster :
				return flinkClusterAcorn.getJobLog(getJobLogReq);
			default :
				throw new IsxAppException("agent类型不支持");
		}

		return null;
	}

	public StopJobRes stopJob(StopJobReq stopJobReq) {

		switch (stopJobReq.getAgentType()) {
			case AgentType.YARN :

				break;
			case AgentType.K8S :

				break;
			case AgentType.FlinkCluster :
				return flinkClusterAcorn.stopJobReq(stopJobReq);
			default :
				throw new IsxAppException("agent类型不支持");
		}

		return null;
	}
}
