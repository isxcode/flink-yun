package com.isxcode.acorn.api.view.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GetViewCardDataByIdReq {

    @Schema(title = "卡片id", example = "fy_123")
    @NotEmpty(message = "id不能为空")
    private String id;
}
