package com.isxcode.acorn.api.workflow.dto;

import lombok.Data;

@Data
public class WorkInstanceInfo {

    private String workId;

    private String runStatus;

    private String workInstanceId;
}
