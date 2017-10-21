package com.bzb.websocket.chatroom.encoder;

import com.bzb.websocket.chatroom.message.JoinMessage;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encodes/serializes a {@link JoinMessage} as JSON.
 */
public class JoinMessageEncoder implements Encoder.Text<JoinMessage> {

  @Override
  public void init(EndpointConfig endpointConfig) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public String encode(JoinMessage joinMessage) {
    StringWriter stringWriter = new StringWriter();
    try (JsonGenerator jsonGen = Json.createGenerator(stringWriter)) {
      jsonGen.writeStartObject()
          .write("type", joinMessage.getType())
          .write("name", joinMessage.getName())
          .writeEnd();
    }
    return stringWriter.toString();
  }
}