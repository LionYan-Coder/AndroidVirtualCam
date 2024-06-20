FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/virtualcamserver-0.0.1-SNAPSHOT.jar /app/virtualcamserver-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/virtualcamserver-0.0.1-SNAPSHOT.jar"]