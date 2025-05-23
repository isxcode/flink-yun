package com.isxcode.acorn.api.agent.req;

import com.isxcode.acorn.api.func.dto.FuncInfo;
import lombok.*;

import java.util.List;

@Data
public class SubmitWorkReq {

    private String clusterType;

    private String flinkHome;

    private PluginReq pluginReq;

    private FlinkSubmit flinkSubmit;

    private String agentHomePath;

    private String workInstanceId;

    private String workType;

    private String workId;

    private List<String> libConfig;

    private List<FuncInfo> funcConfig;
}
