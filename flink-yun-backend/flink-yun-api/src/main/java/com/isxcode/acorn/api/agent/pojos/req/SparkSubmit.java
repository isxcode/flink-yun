package com.isxcode.acorn.api.agent.pojos.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparkSubmit {

	private Map<String, String> conf;

	private String appResource;

	private String appName;

	private String master;

	private String deployMode;

	private List<String> appArgs;

	private List<String> jars;

	private String javaHome;

	private String mainClass;

	private String sparkHome;

	private boolean verbose;
}