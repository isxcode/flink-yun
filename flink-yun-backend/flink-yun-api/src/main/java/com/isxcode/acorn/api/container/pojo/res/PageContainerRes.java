package com.isxcode.acorn.api.container.pojo.res;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isxcode.acorn.backend.api.base.serializer.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageContainerRes {

    private String id;

    private String name;

    private String status;

    private String clusterId;

    private String clusterName;

    private String resourceLevel;

    private String datasourceId;

    private String datasourceName;

    private String remark;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDateTime;
}
