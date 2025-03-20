package com.isxcode.acorn.api.work.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSubmitLogRes {

    private String log;

    private String status;
}
