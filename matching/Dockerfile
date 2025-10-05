FROM openjdk:17-jdk-alpine

EXPOSE 8080
COPY target/*.jar matching.jar

ENTRYPOINT ["java", "-jar", "matching.jar"]


