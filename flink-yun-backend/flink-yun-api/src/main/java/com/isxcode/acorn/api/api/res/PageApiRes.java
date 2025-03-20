package com.isxcode.acorn.api.api.res;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isxcode.acorn.backend.api.base.serializer.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PageApiRes {

    private String id;

    private String name;

    private String path;

    private String status;

    private String remark;

    private String apiType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDateTime;

    private String createBy;

    private String createUsername;
}
