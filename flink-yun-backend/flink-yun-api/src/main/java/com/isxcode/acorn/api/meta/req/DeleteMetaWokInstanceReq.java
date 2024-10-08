package com.isxcode.acorn.api.meta.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteMetaWokInstanceReq {

    @Schema(title = "采集任务Id", example = "")
    @NotEmpty(message = "采集任务Id不能为空")
    private String id;
}
