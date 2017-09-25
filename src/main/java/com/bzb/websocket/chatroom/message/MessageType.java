package com.bzb.websocket.chatroom.message;

/**
 * Defines the different message types.
 */
public enum MessageType {

  CHAT("chat"), JOIN("join"), USER_LIST("users");

  private final String type;

  MessageType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
