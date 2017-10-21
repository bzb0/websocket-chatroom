package com.bzb.websocket.chatroom.encoder;

import com.bzb.websocket.chatroom.message.UsersMessage;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encodes/serializes a {@link UsersMessage} as JSON.
 */
public class UsersMessageEncoder implements Encoder.Text<UsersMessage> {

  @Override
  public void init(EndpointConfig endpointConfig) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public String encode(UsersMessage usersMessage) {
    StringWriter stringWriter = new StringWriter();
    try (JsonGenerator jsonGen = Json.createGenerator(stringWriter)) {
      jsonGen.writeStartObject().write("type", usersMessage.getType()).writeStartArray("userList");
      usersMessage.getUserList().forEach(jsonGen::write);
      jsonGen.writeEnd().writeEnd();
    }
    return stringWriter.toString();
  }
}
