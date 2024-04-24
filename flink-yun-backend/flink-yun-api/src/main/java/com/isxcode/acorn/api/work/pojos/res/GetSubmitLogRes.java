package com.isxcode.acorn.api.work.pojos.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSubmitLogRes {

    private String log;

    private String status;
}
