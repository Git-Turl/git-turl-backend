FROM eclipse-temurin:21-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y git
ENV TZ=Asia/Seoul
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]