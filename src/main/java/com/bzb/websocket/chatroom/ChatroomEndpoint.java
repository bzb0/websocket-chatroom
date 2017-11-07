package com.bzb.websocket.chatroom;

import com.bzb.websocket.chatroom.decoder.MessageDecoder;
import com.bzb.websocket.chatroom.encoder.ChatMessageEncoder;
import com.bzb.websocket.chatroom.encoder.JoinMessageEncoder;
import com.bzb.websocket.chatroom.encoder.UsersMessageEncoder;
import com.bzb.websocket.chatroom.message.ChatMessage;
import com.bzb.websocket.chatroom.message.JoinMessage;
import com.bzb.websocket.chatroom.message.Message;
import com.bzb.websocket.chatroom.message.MessageType;
import com.bzb.websocket.chatroom.message.UsersMessage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Server-side implementation of the '/chatroom' endpoint.
 */
@ServerEndpoint(
    value = "/chatroom",
    encoders = {JoinMessageEncoder.class, ChatMessageEncoder.class, UsersMessageEncoder.class},
    decoders = {MessageDecoder.class})
public class ChatroomEndpoint {

  private static final String ACTIVE_PROPERTY = "active";
  private static final String NAME_PROPERTY = "name";

  /**
   * Processes an input message. The server can only process {@link MessageType#JOIN} and  {@link MessageType#CHAT} messages.
   *
   * @param session The WebSocket session.
   * @param message The received message.
   */
  @OnMessage
  public void processMessage(final Session session, Message message) {
    if (message instanceof JoinMessage) {
      String username = ((JoinMessage) message).getName();

      session.getUserProperties().put(NAME_PROPERTY, username);
      session.getUserProperties().put(ACTIVE_PROPERTY, true);

      broadcastMessage(session, new ChatMessage("Server", username, "Welcome to the chatroom!"));
      broadcastMessage(session, new UsersMessage(getUserList(session)));
    } else if (message instanceof ChatMessage) {
      broadcastMessage(session, message);
    }
  }

  /**
   * Sends an info message that the participant has left the chatroom.
   *
   * @param session The WebSocket session.
   */
  @OnClose
  public void closeConnection(Session session) {
    session.getUserProperties().put(ACTIVE_PROPERTY, false);
    broadcastMessage(session, new UsersMessage(getUserList(session)));
  }

  @OnError
  public void processError(Throwable t) {
    t.printStackTrace();
  }

  /**
   * Broadcasts the message to all connected clients (open WebSocket connections).
   *
   * @param session The WebSocket session.
   * @param message The message.
   */
  private synchronized void broadcastMessage(Session session, Message message) {
    session.getOpenSessions().forEach(openSession -> {
      try {
        openSession.getBasicRemote().sendObject(message);
      } catch (IOException | EncodeException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Returns a list of the participants in the chatroom.
   *
   * @param session The WebSocket session.
   * @return List of all participant names.
   */
  private List<String> getUserList(Session session) {
    return session.getOpenSessions()
        .stream()
        .filter(Session::isOpen)
        .filter(s -> (boolean) s.getUserProperties().getOrDefault(ACTIVE_PROPERTY, false))
        .map(s -> s.getUserProperties().get(NAME_PROPERTY).toString())
        .collect(Collectors.toList());
  }
}