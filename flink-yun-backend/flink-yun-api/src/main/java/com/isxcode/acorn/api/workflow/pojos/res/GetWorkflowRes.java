package com.isxcode.acorn.api.workflow.pojos.res;

import com.isxcode.acorn.api.work.pojos.dto.CronConfig;
import lombok.Data;

import java.util.List;

@Data
public class GetWorkflowRes {

    private Object webConfig;

    private CronConfig cronConfig;

    private List<String> alarmList;

    private String invokeStatus;

    private String invokeUrl;
}
