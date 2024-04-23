package com.isxcode.acorn.backend.api.base.exceptions;

import com.isxcode.acorn.backend.api.base.pojos.BaseResponse;
import lombok.Getter;
import lombok.Setter;

public class AgentResponseException extends RuntimeException {

    @Setter
    @Getter
    private String msg;

    public AgentResponseException(String msg) {

        this.msg = msg;
    }
}
