FROM openjdk:17-jdk-alpine
COPY target/gourmetguide-0.0.1-SNAPSHOT.jar businesslogic.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "businesslogic.jar"]
