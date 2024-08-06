# Usar a imagem oficial do Maven para compilar a aplicação
FROM maven:3.9.8-amazoncorretto-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Usar a imagem oficial do OpenJDK 17 para rodar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/api_desafio_backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]