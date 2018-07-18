FROM java:8
VOLUME /tmp
COPY target/server-1.0-SNAPSHOT.jar server.jar
ENTRYPOINT ["java","-jar","server.jar"]