# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/queenscorner-1.0.0.jar queenscorner.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "queenscorner.jar"]
