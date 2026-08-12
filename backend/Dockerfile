# Multi-stage build para optimização de tamanho de imagem
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar arquivos Maven para cache de dependências
COPY pom.xml .
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Imagem de runtime (compatível com ARM64)
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar JAR da build stage
COPY --from=build /app/target/patientapi-1.0.0.jar app.jar

# Expor porta
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# Parâmetros de JVM para performance
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Iniciar aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
