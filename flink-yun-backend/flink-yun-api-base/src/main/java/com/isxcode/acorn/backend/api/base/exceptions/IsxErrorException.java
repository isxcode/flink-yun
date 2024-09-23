package com.isxcode.acorn.backend.api.base.exceptions;

public class IsxErrorException extends AbstractIsxAppException {

    public IsxErrorException(AbstractIsxAppExceptionEnum abstractFlinkYunExceptionEnum) {
        super(abstractFlinkYunExceptionEnum);
    }

    public IsxErrorException(String code, String msg, String err) {
        super(code, msg, err);
    }

    public IsxErrorException(String code, String msg) {
        super(code, msg);
    }

    public IsxErrorException(String msg) {
        super(msg);
    }
}
