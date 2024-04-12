package com.isxcode.acorn.agent.run;

import com.isxcode.acorn.api.agent.pojos.dto.FlinkVerticesDto;
import com.isxcode.acorn.api.agent.pojos.req.*;
import com.isxcode.acorn.api.agent.pojos.res.*;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlinkClusterAcorn implements AcornRun {

	@Override
	public SubmitJobRes submitJob(SubmitJobReq submitJobReq) {

		String restUrl = getRestUrl(submitJobReq.getFlinkHome());
		String fileName = uploadAppResource(submitJobReq, restUrl);

		// 提交作业
		String submitUrl = "http://" + restUrl + "/jars/" + fileName + "/run";
		FlinkRestRunReq flinkRestRunReq = FlinkRestRunReq.builder().entryClass(submitJobReq.getEntryClass())
				.programArgs(submitJobReq.getProgramArgs()).build();
		ResponseEntity<FlinkRestRunRes> flinkRestRunResResult = new RestTemplate().postForEntity(submitUrl,
				flinkRestRunReq, FlinkRestRunRes.class);
		if (!HttpStatus.OK.equals(flinkRestRunResResult.getStatusCode()) || flinkRestRunResResult.getBody() == null
				|| flinkRestRunResResult.getBody().getJobid() == null) {
			throw new IsxAppException("提交作业失败");
		}
		return SubmitJobRes.builder().jobId(flinkRestRunResResult.getBody().getJobid()).build();
	}

	@Override
	public GetJobInfoRes getJobInfo(GetJobInfoReq getJobInfoReq) {

		String restUrl = getRestUrl(getJobInfoReq.getFlinkHome());

		String getStatusUrl = "http://" + restUrl + "/jobs/" + getJobInfoReq.getJobId();
		ResponseEntity<FlinkRestJobRes> result = new RestTemplate().getForEntity(getStatusUrl, FlinkRestJobRes.class);

		if (!HttpStatus.OK.equals(result.getStatusCode()) || result.getBody() == null) {
			throw new IsxAppException("提交作业失败");
		}

		List<String> vertices = result.getBody().getVertices().stream().map(FlinkVerticesDto::getId)
				.collect(Collectors.toList());

		return GetJobInfoRes.builder().jobId(result.getBody().getJid()).vertices(vertices)
				.status(result.getBody().getState()).build();
	}

	@Override
	public GetJobLogRes getJobLog(GetJobLogReq getJobLogReq) {

		String restUrl = getRestUrl(getJobLogReq.getFlinkHome());

		// 判断作业是否成功
		String getStatusUrl = "http://" + restUrl + "/jobs/" + getJobLogReq.getJobId() + "/status";
		ResponseEntity<FlinkRestStatusRes> result = new RestTemplate().getForEntity(getStatusUrl,
				FlinkRestStatusRes.class);
		if (!HttpStatus.OK.equals(result.getStatusCode()) || result.getBody() == null
				|| result.getBody().getStatus() == null) {
			throw new IsxAppException("获取作业状态失败");
		}

		if ("FAILED".equals(result.getBody().getStatus())) {
			String getExceptionUrl = "http://" + restUrl + "/jobs/" + getJobLogReq.getJobId() + "/exceptions";
			ResponseEntity<FlinkRestExceptionRes> exceptionResult = new RestTemplate().getForEntity(getExceptionUrl,
					FlinkRestExceptionRes.class);
			if (!HttpStatus.OK.equals(exceptionResult.getStatusCode())) {
				throw new IsxAppException("提交作业失败");
			}
			if (exceptionResult.getBody() == null) {
				throw new IsxAppException("提交作业失败");
			}
			return GetJobLogRes.builder().log(exceptionResult.getBody().getRootException()).build();
		} else {

			GetJobInfoRes jobInfo = getJobInfo(GetJobInfoReq.builder().jobId(getJobLogReq.getJobId())
					.flinkHome(getJobLogReq.getFlinkHome()).agentType(getJobLogReq.getAgentType()).build());

			String getVerticesUrl = "http://" + restUrl + "/jobs/" + getJobLogReq.getJobId() + "/vertices/"
					+ jobInfo.getVertices().get(0);
			ResponseEntity<FlinkRestVerticesRes> forEntity = new RestTemplate().getForEntity(getVerticesUrl,
					FlinkRestVerticesRes.class);

			// 查询taskmanager的日志
			String taskmanagerId = forEntity.getBody().getSubtasks().get(0).getTaskmanagerId();
			String getLogUrl = "http://" + restUrl + "/taskmanagers/" + taskmanagerId + "/log";
			ResponseEntity<String> log = new RestTemplate().getForEntity(getLogUrl, String.class);

			String logRegex = "job " + getJobLogReq.getJobId()
					+ " from resource manager with leader id.*?Close JobManager connection for job "
					+ getJobLogReq.getJobId();
			Pattern pattern = Pattern.compile(logRegex, Pattern.DOTALL);
			Matcher matcher = pattern.matcher(Objects.requireNonNull(log.getBody()));
			if (matcher.find()) {
				String matchedLog = matcher.group();
				return GetJobLogRes.builder().log(matchedLog).build();
			}
			return GetJobLogRes.builder().log("").build();
		}
	}

	@Override
	public StopJobRes stopJobReq(StopJobReq stopJobReq) {

		String restUrl = getRestUrl(stopJobReq.getFlinkHome());

		// 判断作业是否成功
		String stopJobUrl = "http://" + restUrl + "/jobs/" + stopJobReq.getJobId() + "/stop";
		ResponseEntity<FlinkRestStopRes> result;
		try {
			result = new RestTemplate().getForEntity(stopJobUrl, FlinkRestStopRes.class);
		} catch (HttpClientErrorException exception) {
			if (HttpStatus.NOT_FOUND.equals(exception.getStatusCode())) {
				throw new IsxAppException("作业已停止");
			}
			throw new IsxAppException("停止作业失败");
		}
		if (!HttpStatus.OK.equals(result.getStatusCode()) || result.getBody() == null) {
			throw new IsxAppException("停止作业失败");
		}

		return StopJobRes.builder().requestId(result.getBody().getRequestId()).build();
	}

	/**
	 * 从配置文件中获取连接信息.
	 */
	public String getRestUrl(String flinkHome) {

		return "localhost:8081";
	}

	/**
	 * 提交jar到flink集群.
	 */
	public String uploadAppResource(SubmitJobReq submitJobReq, String restUrl) {

		String uploadUrl = "http://" + restUrl + "/jars/upload";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("jarfile", new FileSystemResource(new File(submitJobReq.getAppResource())));
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);

		ResponseEntity<FlinkRestUploadRes> result = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity,
				FlinkRestUploadRes.class);
		if (!HttpStatus.OK.equals(result.getStatusCode())) {
			throw new IsxAppException("提交资源文件异常");
		}
		if (result.getBody() == null || result.getBody().getFilename() == null) {
			throw new IsxAppException("提交资源文件异常");
		}
		String[] sub = result.getBody().getFilename().split("/");
		return sub[sub.length - 1];
	}
}