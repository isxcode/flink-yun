package com.isxcode.acorn.api.workflow.dto;

import lombok.Data;

@Data
public class NodeInfo {

    private String id;

    private String shape;

    private LineInfo source;

    private LineInfo target;
}
