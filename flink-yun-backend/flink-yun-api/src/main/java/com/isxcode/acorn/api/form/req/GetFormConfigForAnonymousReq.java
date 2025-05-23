package com.isxcode.acorn.api.form.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GetFormConfigForAnonymousReq {

    @Schema(title = "表单唯一id", example = "fy_fd34e4a53db640f5943a4352c4d549b9")
    @NotEmpty(message = "formId不能为空")
    private String formId;
}
