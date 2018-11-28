FROM openjdk:8-jdk-alpine

WORKDIR /

ADD target/pubsubproducer-1.0-SNAPSHOT.jar /
EXPOSE 8081
CMD ["java", "-jar", "pubsubproducer-1.0-SNAPSHOT.jar"]