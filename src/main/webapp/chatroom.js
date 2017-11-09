let websocket;
let messagesElement;
let userListElement;
let inputElement;
let nameElement;
let joinButton;

/**
 * Connects to the WebSocket endpoint & sets the callback function for incoming messages.
 */
function connect() {
  messagesElement = document.getElementById("messages");
  userListElement = document.getElementById("userList");
  inputElement = document.getElementById("textInput");
  joinButton = document.getElementById("joinBtn");
  nameElement = document.getElementById("name");
  nameElement.focus();

  websocket = new WebSocket("ws://localhost:8080/websocket-chatroom/chatroom");
  websocket.onmessage = onMessage;
}

/**
 * Callback function for handling incoming messages.
 *
 * @param evt The WebSocket message.
 */
function onMessage(evt) {
  /* Parse the message into a JavaScript object */
  let msg = JSON.parse(evt.data);
  if (msg.type === "chat") {
    let line = msg.name + ":";
    line += !msg.target ? " " : " @" + msg.target + " ";
    line += msg.message + "\n";
    messagesElement.value += line;
  } else if (msg.type === "info") {
    messagesElement.value += "<" + msg.info + ">\n";
  } else if (msg.type === "users") {
    userListElement.value = msg.userList.map(user => "@" + user).reduce(
        (userList, user) => userList + user + "\n", "");
  }
  messagesElement.scrollTop = 999999;
}

/**
 * Sends a join message to the WebSocket server.
 */
function sendJoin() {
  if (!nameElement.value) {
    return;
  }

  /* Creating the join message and converting it to JSON. */
  let joinMsg = JSON.stringify({
    type: "join",
    name: nameElement.value
  });

  websocket.send(joinMsg);

  /* Disabling UI components. */
  nameElement.disabled = true;
  joinButton.disabled = true;
  inputElement.disabled = false;
}

/**
 * Sends a chat message to all participants in the chatroom.
 */
function sendMessage(evt) {
  if (evt.keyCode === 13 && inputElement.value.length > 0) {
    /* Creating the chat message and converting it to JSON. */
    let chatMsg = JSON.stringify({
      type: "chat",
      name: nameElement.value,
      target: getTarget(inputElement.value.replace(/,/g, "")),
      message: cleanTarget(inputElement.value).replace(/(\r\n|\n|\r)/gm, "")
    });

    websocket.send(chatMsg);
    inputElement.value = "";
  }
}

/**
 * Checks if the user can enter the chatroom.
 */
function checkJoin(evt) {
  if (evt.keyCode === 13 && nameElement.value.length > 0) {
    sendJoin();
    inputElement.focus();
  }
}

/**
 * Fetches the message recipient (target) from the input text.
 *
 * @param text The entered message.
 * @returns {string} The recipient name.
 */
function getTarget(message) {
  return message.split(" ")
  .filter(substr => substr.startsWith("@"))
  .map(substr => substr.substring(1))
  .map(substr => substr.replace(/(\r\n|\n|\r)/gm, ""))[0];
}

/**
 * Removes the recipient name from the message.
 *
 * @param text The entered message.
 * @returns {string} The entered message without the recipient (@Bob).
 */
function cleanTarget(message) {
  return message.split(" ")
  .filter(substr => !substr.startsWith("@"))
  .reduce((result, substr) => result + " " + substr, "")
  .trim()
}
