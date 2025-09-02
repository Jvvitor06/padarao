# ------------------------------
# Etapa 1: Build da aplicação com Maven
# ------------------------------
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app

# Copia pom.xml e o código-fonte
COPY pom.xml .
COPY src ./src

# Build do projeto sem executar testes
RUN mvn clean package -DskipTests

# ------------------------------
# Etapa 2: Imagem final para rodar o JAR
# ------------------------------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o JAR gerado na etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Render define a variável PORT em tempo de execução
EXPOSE 8080

# Configuração de ambiente
ENV LANG=C.UTF-8
ENV SPRING_PROFILES_ACTIVE=default

# Inicia a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
