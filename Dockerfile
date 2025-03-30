# 1단계: Build Stage (JDK 21)
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build --no-daemon

# 2단계: Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]