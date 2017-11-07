package com.bzb.websocket.chatroom.decoder;

import com.bzb.websocket.chatroom.message.ChatMessage;
import com.bzb.websocket.chatroom.message.JoinMessage;
import com.bzb.websocket.chatroom.message.Message;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decodes/deserializes a JSON string into a {@link JoinMessage} or a {@link ChatMessage}.
 */
public class MessageDecoder implements Decoder.Text<Message> {

  private final static String[] JOIN_MESSAGE_FIELDS = {"type", "name"};
  private final static String[] CHAT_MESSAGE_FIELDS = {"type", "name", "target", "message"};

  @Override
  public void init(EndpointConfig ec) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public Message decode(String string) throws DecodeException {
    if (!willDecode(string)) {
      throw new DecodeException(string, "Message can't be decoded.");
    }

    /* Converting the JSON string in a Map<string, string>. */
    Map<String, String> fields = new HashMap<>();
    JsonParser parser = Json.createParser(new StringReader(string));
    while (parser.hasNext()) {
      if (parser.next() == JsonParser.Event.KEY_NAME) {
        String fieldName = parser.getString();
        parser.next();
        String fieldValue = parser.getString();
        fields.put(fieldName, fieldValue);
      }
    }

    switch (fields.get("type")) {
      case "join":
        return new JoinMessage(fields.get(JOIN_MESSAGE_FIELDS[1]));
      case "chat":
        return new ChatMessage(fields.get(CHAT_MESSAGE_FIELDS[1]), fields.get(CHAT_MESSAGE_FIELDS[2]), fields.get(CHAT_MESSAGE_FIELDS[3]));
      default:
        throw new DecodeException(string, String.format("Unknown message type: %s.", fields.get("type")));
    }
  }

  @Override
  public boolean willDecode(String jsonString) {
    Set<String> fields = new HashSet<>();

    JsonParser parser = Json.createParser(new StringReader(jsonString));
    while (parser.hasNext()) {
      if (parser.next() == JsonParser.Event.KEY_NAME) {
        fields.add(parser.getString());
      }
    }
    return fields.containsAll(Arrays.asList(JOIN_MESSAGE_FIELDS)) || fields.containsAll(Arrays.asList(CHAT_MESSAGE_FIELDS));
  }
}
