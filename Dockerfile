FROM jboss/wildfly:18.0.1.Final

LABEL maintainer="bzb"

ARG WAR_FILE

ADD ${WAR_FILE} /opt/jboss/wildfly/standalone/deployments/