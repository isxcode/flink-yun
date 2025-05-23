package com.isxcode.acorn.api.real.req;

import com.isxcode.acorn.api.work.dto.SyncWorkConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ConfigRealReq {

    @Schema(title = "实时同步作业id", example = "fy_123")
    @NotEmpty(message = "id不能为空")
    private String realId;

    @Schema(title = "数据同步规则")
    private SyncWorkConfig syncConfig;

    @Schema(title = "自定义函数选择")
    private List<String> funcList;

    @Schema(title = "依赖选择")
    private List<String> LibList;

    @Schema(title = "flink配置字符串")
    private String flinkConfigJson;

    @Schema(title = "集群id")
    private String clusterId;
}
