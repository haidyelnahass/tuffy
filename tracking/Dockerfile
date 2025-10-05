FROM openjdk:17-jdk-alpine

EXPOSE 8080
COPY target/*.jar tracking.jar

ENTRYPOINT ["java", "-jar", "tracking.jar"]


