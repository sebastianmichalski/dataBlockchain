FROM java:8
VOLUME /tmp
COPY target/node-1.0-SNAPSHOT.jar node.jar
ENTRYPOINT ["java","-jar","node.jar"]