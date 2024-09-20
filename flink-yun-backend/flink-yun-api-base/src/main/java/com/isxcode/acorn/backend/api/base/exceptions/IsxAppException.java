package com.isxcode.acorn.backend.api.base.exceptions;

public class IsxAppException extends AbstractIsxAppException {

    public IsxAppException(AbstractIsxAppExceptionEnum abstractFlinkYunExceptionEnum) {
        super(abstractFlinkYunExceptionEnum);
    }

    public IsxAppException(String code, String msg, String err) {
        super(code, msg, err);
    }

    public IsxAppException(String code, String msg) {
        super(code, msg);
    }

    public IsxAppException(String msg) {
        super(msg);
    }
}
