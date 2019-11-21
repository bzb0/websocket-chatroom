# Simple WebSocket Chatroom

### Description

A simple chatroom implemented using the WebSocket protocol. The project contains a simple HTML frontend and a Java backend. The frontend is written in
plain HTML and JavaScript, while the backend is written Java 8 and uses the Java EE 7 framework. The WebSocket chatroom uses the following
dependencies:

```
  JavaSE    8
  JavaEE    7
  Lombok    1.16.18
  WildFly   18.0.1.Final
  Docker    19.03.5
```

### Building & Running the WebSocket Chatroom

The following command compiles the project and generates the WAR file `websocket-chatroom.war` in the `target` directory:

```
./mvnw clean install
```

Since we are generating a WAR file we need a target platform (application server), that will run/host our web application. This project uses WildFly
as application server. We use the WildFly Docker image (more infos: https://registry.hub.docker.com/r/jboss/wildfly) as a base for our Docker image.
The project Docker image can be built with the following command:

```
./mvnw dockerfile:build
```

The previous command will generate the following Docker image:

```
bzb0/websocket-chatroom:1.0-SNAPSHOT
```

Finally, we can run the previously built Docker image with the following command:

```
docker run -p 8080:8080 --name websocket-chatroom bzb0/websocket-chatroom:1.0-SNAPSHOT
```

When the Docker container is started, we can access the HTML frontend under the following URL:

```
http://localhost:8080/websocket-chatroom/index.html
```

In order to stop the running Docker container we have to execute the following command:

```
docker stop websocket-chatroom
```
