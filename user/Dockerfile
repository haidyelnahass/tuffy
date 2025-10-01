FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY target/*.jar user.jar

ENTRYPOINT ["java", "-jar", "user.jar"]


