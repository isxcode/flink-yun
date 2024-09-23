package com.isxcode.acorn.modules.alarm.message;

public interface MessageAction {

    String getActionName();

    Object sendMessage(MessageContext messageContext);
}
