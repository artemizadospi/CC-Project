FROM openjdk:17-jdk-alpine
COPY target/gourmetguide-0.0.1-SNAPSHOT.jar auth.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "auth.jar"]
