package com.bzb.websocket.chatroom.message;

import lombok.Value;

/**
 * Represents a message that one participant can send in the chatroom. A chat message contains the sender (name), recipient (target) and the text of
 * the message (message).
 */
@Value
public class ChatMessage implements Message {

  String name;
  String target;
  String message;

  @Override
  public String getType() {
    return MessageType.CHAT.getType();
  }
}