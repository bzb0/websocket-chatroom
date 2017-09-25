package com.bzb.websocket.chatroom.message;

import java.util.List;
import lombok.Value;

/**
 * Will be sent whenever a new participant enters the chatroom. This message contains a list of the chatroom participants.
 */
@Value
public class UsersMessage implements Message {

  List<String> userList;

  @Override
  public String getType() {
    return MessageType.USER_LIST.getType();
  }
}
