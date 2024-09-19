package com.isxcode.acorn.api.instance.pojos.res;

import com.isxcode.acorn.api.instance.pojos.dto.WorkInstanceDto;

import java.util.List;
import lombok.Data;

@Data
public class GetWorkflowInstanceRes {

    private String webConfig;

    private List<WorkInstanceDto> workInstances;
}
