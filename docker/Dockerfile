FROM openjdk:8-jdk-alpine

WORKDIR /com/ratheesh

ADD target/original-pubsubproducer-1.0-SNAPSHOT.jar /com/ratheesh
EXPOSE 8081
CMD ["java", "-jar", " original-pubsubproducer-1.0-SNAPSHOT.jar"]