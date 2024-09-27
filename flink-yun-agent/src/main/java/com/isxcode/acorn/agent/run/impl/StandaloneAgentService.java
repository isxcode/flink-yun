package com.isxcode.acorn.agent.run.impl;

import com.alibaba.fastjson2.JSON;
import com.isxcode.acorn.agent.run.AgentService;
import com.isxcode.acorn.api.agent.constants.AgentType;
import com.isxcode.acorn.api.agent.pojos.dto.FlinkVerticesDto;
import com.isxcode.acorn.api.agent.pojos.req.*;
import com.isxcode.acorn.api.agent.pojos.res.*;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StandaloneAgentService implements AgentService {

    public String getRestUrl(String flinkHome) {

        // 获取本地flink的配置，并从中获取rest.port、rest.address，如果获取不到默认8081、localhost
        String flinkConfigPath = flinkHome + File.separator + "conf" + File.separator + "flink-conf.yaml";

        try (InputStream inputStream = Files.newInputStream(new File(flinkConfigPath).toPath())) {
            Yaml yaml = new Yaml();
            Map<String, String> flinkYaml = yaml.load(inputStream);

            String restAddress = flinkYaml.getOrDefault("rest.address", "localhost");
            String restPort = flinkYaml.getOrDefault("rest.port", "8081");
            return restAddress + ":" + restPort;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("获取flink配置文件异常", e);
        }
    }

    public String uploadAppResource(SubmitWorkReq submitWorkReq, String restUrl) {

        String uploadUrl = "http://" + restUrl + "/jars/upload";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("jarfile", new FileSystemResource(new File(submitWorkReq.getAgentHomePath() + File.separator
            + "plugins" + File.separator + submitWorkReq.getFlinkSubmit().getAppResource())));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);

        ResponseEntity<FlinkRestUploadRes> result =
            restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, FlinkRestUploadRes.class);
        if (!HttpStatus.OK.equals(result.getStatusCode())) {
            throw new IsxAppException("提交资源文件异常");
        }
        if (result.getBody() == null || result.getBody().getFilename() == null) {
            throw new IsxAppException("提交资源文件异常");
        }
        String[] sub = result.getBody().getFilename().split("/");
        return sub[sub.length - 1];
    }

    @Override
    public String getAgentType() {

        return AgentType.StandAlone;
    }

    @Override
    public SubmitWorkRes submitWork(SubmitWorkReq submitWorkReq) throws Exception {

        String restUrl = getRestUrl(submitWorkReq.getFlinkHome());
        String fileName = uploadAppResource(submitWorkReq, restUrl);

        // 添加额外依赖
        if (submitWorkReq.getLibConfig() != null && !submitWorkReq.getLibConfig().isEmpty()) {
            Map<String, String> flinkConfig = new HashMap<>();
            List<String> pipelineClasspath = new ArrayList<>();
            submitWorkReq.getLibConfig().forEach(e -> pipelineClasspath
                .add(submitWorkReq.getAgentHomePath() + File.separator + "file" + File.separator + e + ".jar"));
            flinkConfig.put("pipeline.jars", Strings.join(pipelineClasspath, ';'));
            flinkConfig.put("jobmanager.memory.jvm-overhead.max", "1gb");
            flinkConfig.put("jobmanager.memory.off-heap.size", "128mb");
            submitWorkReq.getFlinkSubmit().setConf(flinkConfig);
        }

        // 提交作业
        String submitUrl = "http://" + restUrl + "/jars/" + fileName + "/run";
        FlinkRestRunReq flinkRestRunReq = FlinkRestRunReq.builder()
            .entryClass(submitWorkReq.getFlinkSubmit().getEntryClass())
            .programArgs(Base64.getEncoder().encodeToString(JSON.toJSONString(submitWorkReq.getPluginReq()).getBytes()))
            .flinkConfiguration(submitWorkReq.getFlinkSubmit().getConf()).build();

        ResponseEntity<FlinkRestRunRes> flinkRestRunResResult =
            new RestTemplate().postForEntity(submitUrl, flinkRestRunReq, FlinkRestRunRes.class);
        if (!HttpStatus.OK.equals(flinkRestRunResResult.getStatusCode()) || flinkRestRunResResult.getBody() == null
            || flinkRestRunResResult.getBody().getJobid() == null) {
            throw new Exception("提交作业失败");
        }
        return SubmitWorkRes.builder().appId(flinkRestRunResResult.getBody().getJobid()).build();
    }

    @Override
    public GetWorkInfoRes getWorkInfo(GetWorkInfoReq getWorkInfoReq) throws Exception {

        String restUrl = getRestUrl(getWorkInfoReq.getFlinkHome());

        String getStatusUrl = "http://" + restUrl + "/jobs/" + getWorkInfoReq.getAppId();
        ResponseEntity<FlinkRestJobRes> result = new RestTemplate().getForEntity(getStatusUrl, FlinkRestJobRes.class);

        if (!HttpStatus.OK.equals(result.getStatusCode()) || result.getBody() == null) {
            throw new IsxAppException("提交作业失败");
        }

        List<String> vertices =
            result.getBody().getVertices().stream().map(FlinkVerticesDto::getId).collect(Collectors.toList());

        return GetWorkInfoRes.builder().appId(result.getBody().getJid()).vertices(vertices)
            .status(result.getBody().getState()).build();
    }

    @Override
    public GetWorkLogRes getWorkLog(GetWorkLogReq getWorkLogReq) throws Exception {

        String restUrl = getRestUrl(getWorkLogReq.getFlinkHome());

        // 判断作业是否成功
        String getStatusUrl = "http://" + restUrl + "/jobs/" + getWorkLogReq.getAppId() + "/status";
        ResponseEntity<FlinkRestStatusRes> result =
            new RestTemplate().getForEntity(getStatusUrl, FlinkRestStatusRes.class);
        if (!HttpStatus.OK.equals(result.getStatusCode()) || result.getBody() == null
            || result.getBody().getStatus() == null) {
            throw new IsxAppException("获取作业状态失败");
        }

        if ("FAILED".equals(result.getBody().getStatus())) {
            String getExceptionUrl = "http://" + restUrl + "/jobs/" + getWorkLogReq.getAppId() + "/exceptions";
            ResponseEntity<FlinkRestExceptionRes> exceptionResult =
                new RestTemplate().getForEntity(getExceptionUrl, FlinkRestExceptionRes.class);
            if (!HttpStatus.OK.equals(exceptionResult.getStatusCode())) {
                throw new IsxAppException("提交作业失败");
            }
            if (exceptionResult.getBody() == null) {
                throw new IsxAppException("提交作业失败");
            }
            return GetWorkLogRes.builder().log(exceptionResult.getBody().getRootException()).build();
        } else {

            GetWorkInfoRes jobInfo = getWorkInfo(GetWorkInfoReq.builder().appId(getWorkLogReq.getAppId())
                .flinkHome(getWorkLogReq.getFlinkHome()).clusterType(getWorkLogReq.getClusterType()).build());

            String getVerticesUrl =
                "http://" + restUrl + "/jobs/" + getWorkLogReq.getAppId() + "/vertices/" + jobInfo.getVertices().get(0);
            ResponseEntity<FlinkRestVerticesRes> forEntity =
                new RestTemplate().getForEntity(getVerticesUrl, FlinkRestVerticesRes.class);

            // 查询taskmanager的日志
            String taskmanagerId = forEntity.getBody().getSubtasks().get(0).getTaskmanagerId();
            String getLogUrl = "http://" + restUrl + "/taskmanagers/" + taskmanagerId + "/log";
            ResponseEntity<String> log = new RestTemplate().getForEntity(getLogUrl, String.class);

            String logRegex = "job " + getWorkLogReq.getAppId()
                + " from resource manager with leader id.*?Close JobManager connection for job "
                + getWorkLogReq.getAppId();
            Pattern pattern = Pattern.compile(logRegex, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(Objects.requireNonNull(log.getBody()));
            if (matcher.find()) {
                String matchedLog = matcher.group();
                return GetWorkLogRes.builder().log(matchedLog).build();
            }
            return GetWorkLogRes.builder().log("").build();
        }
    }

    @Override
    public StopWorkRes stopWork(StopWorkReq stopWorkReq) throws Exception {

        String restUrl = getRestUrl(stopWorkReq.getFlinkHome());

        // 判断作业是否成功
        String stopJobUrl = "http://" + restUrl + "/jobs/" + stopWorkReq.getAppId() + "/yarn-cancel";
        ResponseEntity<FlinkRestStopRes> result;
        try {
            result = new RestTemplate().getForEntity(stopJobUrl, FlinkRestStopRes.class);
        } catch (HttpClientErrorException exception) {
            if (HttpStatus.NOT_FOUND.equals(exception.getStatusCode())) {
                throw new Exception("作业已停止");
            }
            throw new Exception("停止作业失败");
        }

        return StopWorkRes.builder().requestId(result.getBody().getRequestId()).build();
    }
}
