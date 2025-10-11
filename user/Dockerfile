# ---- BUILD STAGE ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline


COPY src ./src
RUN mvn clean package -DskipTests

# ---- RELEASE STAGE ----
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app


COPY --from=build /app/target/*.jar app.jar

# Environment variables (if needed)
# ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENV PORT=8080

EXPOSE ${PORT}

CMD sh -c 'if [ ! -z "$ORACLE_WALLET_B64" ]; then echo "$ORACLE_WALLET_B64" | base64 -d > /tmp/wallet.zip && unzip -o /tmp/wallet.zip -d /tmp/wallet; fi && java -Doracle.net.tns_admin=/tmp/wallet -jar app.jar'
