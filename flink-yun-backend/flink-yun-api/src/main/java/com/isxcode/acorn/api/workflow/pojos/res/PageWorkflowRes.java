package com.isxcode.acorn.api.workflow.pojos.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageWorkflowRes {

    private String id;

    private String name;

    private String remark;

    private String status;

    private String defaultClusterId;

    private String clusterName;
}
