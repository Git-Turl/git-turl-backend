FROM eclipse-temurin:21-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y git
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]