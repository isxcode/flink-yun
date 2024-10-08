package com.isxcode.acorn.backend.api.base.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.isxcode.acorn.backend.api.base.exceptions.AbstractIsxAppExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    private String code;

    private String msg;

    private String err;

    private T data;

    public BaseResponse(String code, String message) {

        this.code = code;
        this.msg = message;
    }

    public BaseResponse(String code, String message, String err) {

        this.err = err;
        this.code = code;
        this.msg = message;
    }

    public BaseResponse(AbstractIsxAppExceptionEnum abstractFlinkYunExceptionEnum) {

        this.code = abstractFlinkYunExceptionEnum.getCode();
        this.msg = abstractFlinkYunExceptionEnum.getMsg();
    }
}
