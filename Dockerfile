FROM openjdk:16-alpine3.13
ADD target/springGPTDemo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "app.jar"]

