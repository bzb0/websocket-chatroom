package com.bzb.websocket.chatroom.message;

import lombok.Value;

/**
 * Represents a message, that will be sent when a new user enters the chatroom.
 */
@Value
public class JoinMessage implements Message {

  String name;

  @Override
  public String getType() {
    return MessageType.JOIN.getType();
  }
}
