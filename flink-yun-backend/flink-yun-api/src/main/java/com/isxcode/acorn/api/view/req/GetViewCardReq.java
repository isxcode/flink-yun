package com.isxcode.acorn.api.view.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetViewCardReq {

    @Schema(title = "卡片id", example = "fy_123")
    @NotEmpty(message = "id不能为空")
    private String id;
}
