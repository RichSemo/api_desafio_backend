# Usar a imagem oficial do Maven para compilar a aplicação
FROM maven:4.0.0-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Usar a imagem oficial do OpenJDK 17 para rodar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/api_desafio_backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]