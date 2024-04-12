package com.isxcode.acorn.api.agent.pojos.req;

import lombok.Data;

import java.util.List;

@Data
public class SubmitJobReq {

    private String agentType;

    private String flinkHome;

    private PluginReq pluginReq;

    private String programArgs;

    private List<String> programArgsList;

    private String entryClass;

    private String appResource;

    private String appName;
}
