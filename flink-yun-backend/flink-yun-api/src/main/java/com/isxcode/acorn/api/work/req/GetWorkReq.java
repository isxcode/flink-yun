package com.isxcode.acorn.api.work.req;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWorkReq {

    @Schema(title = "作业唯一id", example = "fy_123456789")
    @NotEmpty(message = "作业id不能为空")
    private String workId;
}
