package com.isxcode.acorn.api.meta.req;

import com.isxcode.acorn.backend.api.base.pojos.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class PageMetaTableReq extends BasePageRequest {

    @Schema(title = "数据源id", example = "fy_e4d80a6b561d47afa81504e93054e8e8")
    private String datasourceId;
}
