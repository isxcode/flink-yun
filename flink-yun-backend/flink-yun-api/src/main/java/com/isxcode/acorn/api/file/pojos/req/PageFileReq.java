package com.isxcode.acorn.api.file.pojos.req;

import com.isxcode.acorn.backend.api.base.pojos.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageFileReq extends BasePageRequest {

    @Schema(title = "资源文件类型", example = "JOB/LIB/FUNC")
    private String type;
}
