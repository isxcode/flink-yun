package com.isxcode.acorn.modules.alarm.mapper;

import com.isxcode.acorn.api.alarm.req.AddAlarmReq;
import com.isxcode.acorn.api.alarm.req.AddMessageReq;
import com.isxcode.acorn.api.alarm.res.PageAlarmInstanceRes;
import com.isxcode.acorn.api.alarm.res.PageAlarmRes;
import com.isxcode.acorn.api.alarm.res.PageMessageRes;
import com.isxcode.acorn.modules.alarm.entity.AlarmEntity;
import com.isxcode.acorn.modules.alarm.entity.AlarmInstanceEntity;
import com.isxcode.acorn.modules.alarm.entity.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlarmMapper {

    MessageEntity addMessageReqToMessageEntity(AddMessageReq addMessageReq);

    PageMessageRes messageEntityToPageMessageRes(MessageEntity messageEntity);

    @Mapping(target = "receiverList", ignore = true)
    AlarmEntity addAlarmReqToAlarmEntity(AddAlarmReq addAlarmReq);

    PageAlarmRes alarmEntityToPageAlarmRes(AlarmEntity alarmEntity);

    PageAlarmInstanceRes alarmInstanceEntityToPageAlarmInstanceRes(AlarmInstanceEntity alarmInstanceEntity);
}
