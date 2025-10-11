FROM eclipse-temurin:17-jdk-alpine


WORKDIR /app


COPY target/*.jar user.jar

ENV PORT=8080

EXPOSE ${PORT}

CMD sh -c 'if [ ! -z "$ORACLE_WALLET_B64" ]; then echo "$ORACLE_WALLET_B64" | base64 -d > /tmp/wallet.zip && unzip -o /tmp/wallet.zip -d /tmp/wallet; fi && java -Doracle.net.tns_admin=/tmp/wallet -jar app.jar'
