package com.isxcode.acorn.api.file.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DownloadFileReq {

    @Schema(title = "文件唯一id", example = "fy_48c4304593ea4897b6af999e48685896")
    @NotEmpty(message = "文件id不能为空")
    private String fileId;
}
