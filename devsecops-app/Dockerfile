FROM eclipse-temurin:21-jre-alpine-3.24

WORKDIR /app

COPY target/devsecops-app-0.0.1-SNAPSHOT.jar app.jar

RUN apk upgrade --no-cache

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
