FROM openjdk:17-jdk-alpine
COPY target/gourmetguide-0.0.1-SNAPSHOT.jar db_interaction.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "db_interaction.jar"]
