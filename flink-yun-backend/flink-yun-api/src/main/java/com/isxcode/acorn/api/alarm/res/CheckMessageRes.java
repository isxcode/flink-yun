package com.isxcode.acorn.api.alarm.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckMessageRes {

    private String checkStatus;

    private String log;
}
