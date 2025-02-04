FROM docker.io/library/eclipse-temurin:17-jre-alpine

ENV SERVICE_PORT=8080
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=hola123
ENV DATABASE_URL=jdbc:postgresql://postgresql-container:5432/agencia_db

COPY build/libs/*.jar /app.jar

EXPOSE $SERVICE_PORT

CMD java -Xmx2048m -Djava.security.egd=file:/dev/urandom -jar /app.jar
