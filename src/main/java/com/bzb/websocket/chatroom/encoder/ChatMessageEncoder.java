package com.bzb.websocket.chatroom.encoder;

import com.bzb.websocket.chatroom.message.ChatMessage;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encodes/serializes a {@link ChatMessage} as JSON.
 */
public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {

  @Override
  public void init(EndpointConfig endpointConfig) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public String encode(ChatMessage chatMessage) {
    StringWriter stringWriter = new StringWriter();
    try (JsonGenerator jsonGen = Json.createGenerator(stringWriter)) {
      jsonGen.writeStartObject()
          .write("type", chatMessage.getType())
          .write("name", chatMessage.getName())
          .write("message", chatMessage.getMessage());
      if (chatMessage.getTarget() == null) {
        jsonGen.writeNull("target").writeEnd();
      } else {
        jsonGen.write("target", chatMessage.getTarget()).writeEnd();
      }
    }
    return stringWriter.toString();
  }
}
