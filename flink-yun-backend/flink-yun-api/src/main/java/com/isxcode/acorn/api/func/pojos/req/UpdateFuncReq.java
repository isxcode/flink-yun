package com.isxcode.acorn.api.func.pojos.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateFuncReq {

    @Schema(title = "udf的id", example = "fy_48c4304593ea4897b6af999e48685896")
    @NotEmpty(message = "udf的id不能为空")
    private String id;

    @Schema(title = "函数名称", example = "a1")
    @NotEmpty(message = "函数名称不能为空")
    private String funcName;

    @Schema(title = "udf文件类名称", example = "org.example.flink.udf.MyUdf")
    @NotEmpty(message = "类名称不能为空")
    private String className;

    @Schema(title = "udf文件id", example = "fy_123")
    @NotEmpty(message = "文件id不能为空")
    private String fileId;

    @Schema(title = "备至", example = "描述函数的定义")
    private String remark;
}
