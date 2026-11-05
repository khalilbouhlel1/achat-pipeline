FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar achat.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "achat.jar"]