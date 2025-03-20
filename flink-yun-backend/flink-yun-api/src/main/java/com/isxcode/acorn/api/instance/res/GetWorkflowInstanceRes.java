package com.isxcode.acorn.api.instance.res;

import com.isxcode.acorn.api.instance.dto.WorkInstanceDto;

import java.util.List;
import lombok.Data;

@Data
public class GetWorkflowInstanceRes {

    private String webConfig;

    private List<WorkInstanceDto> workInstances;
}
