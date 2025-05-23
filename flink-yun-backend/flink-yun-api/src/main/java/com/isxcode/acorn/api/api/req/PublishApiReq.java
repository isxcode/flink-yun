package com.isxcode.acorn.api.api.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PublishApiReq {

    @Schema(title = "API id", example = "fy_123")
    @NotEmpty(message = "id不能为空")
    private String id;
}
