package com.isxcode.acorn.api.alarm.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isxcode.acorn.api.user.pojos.dto.UserInfo;
import com.isxcode.acorn.backend.api.base.serializer.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageAlarmRes {

    private String id;

    private String name;

    private String remark;

    private String msgId;

    private String msgName;

    private String alarmType;

    private String alarmEvent;

    private String status;

    private String alarmTemplate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDateTime;

    private String createBy;

    private String createByUsername;

    private String receiverList;

    private List<UserInfo> receiverUsers;
}
