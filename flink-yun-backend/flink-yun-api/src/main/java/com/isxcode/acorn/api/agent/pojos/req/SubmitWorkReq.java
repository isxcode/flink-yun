package com.isxcode.acorn.api.agent.pojos.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitWorkReq {

    private String clusterType;

    private String flinkHome;

    private PluginReq pluginReq;

    private FlinkSubmit flinkSubmit;

    private String agentHomePath;

    private String workInstanceId;

    private String workType;
}
