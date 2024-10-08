package com.isxcode.acorn.api.work.exceptions;

import com.isxcode.acorn.backend.api.base.exceptions.AbstractIsxAppException;
import com.isxcode.acorn.backend.api.base.exceptions.AbstractIsxAppExceptionEnum;

public class WorkRunException extends AbstractIsxAppException {

    public WorkRunException(AbstractIsxAppExceptionEnum abstractFlinkYunExceptionEnum) {
        super(abstractFlinkYunExceptionEnum);
    }

    public WorkRunException(String code, String msg, String err) {
        super(code, msg, err);
    }

    public WorkRunException(String code, String msg) {
        super(code, msg);
    }

    public WorkRunException(String msg) {
        super(msg);
    }
}
