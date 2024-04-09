package com.isxcode.acorn.api.agent.pojos.req;

import lombok.Data;

@Data
public class DeployContainerReq {

	private PluginReq pluginReq;

	private String agentHomePath;

	private String agentType;

	private String sparkHomePath;

	private String args;
}
