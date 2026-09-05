FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/devsecops-app-0.0.1-SNAPSHOT.jar app.jar

RUN useradd -r -s /bin/false appuser

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
