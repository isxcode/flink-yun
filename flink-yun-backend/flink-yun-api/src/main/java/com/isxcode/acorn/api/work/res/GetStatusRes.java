package com.isxcode.acorn.api.work.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetStatusRes {

    private String yarnApplicationState;

    private String finalApplicationStatus;

    private String trackingUrl;
}
