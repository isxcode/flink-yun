package com.isxcode.acorn.api.instance.pojos.res;

import com.isxcode.acorn.api.instance.pojos.vo.WorkInstanceVo;
import java.util.List;
import lombok.Data;

@Data
public class WfiGetWorkflowInstanceRes {

    private String webConfig;

    private List<WorkInstanceVo> workInstances;
}
