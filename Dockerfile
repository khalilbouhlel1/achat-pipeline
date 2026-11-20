FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/achat-*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]