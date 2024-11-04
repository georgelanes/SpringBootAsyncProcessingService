# Dockerfile com build em múltiplos estágios para otimização
FROM maven:3.9-amazoncorretto-17 AS builder

# Definir diretório de trabalho
WORKDIR /build

# Copiar arquivos de dependência primeiro para aproveitar cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fonte e fazer o build
COPY src ./src
RUN mvn clean package -DskipTests

# Imagem final otimizada
FROM amazoncorretto:17-alpine

# Metadados da imagem
LABEL maintainer="Your Name <your.email@example.com>"
LABEL version="1.0"
LABEL description="Spring Boot Async Processing Application"

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR do estágio de build
COPY --from=builder /build/target/*.jar app.jar

# Mudar propriedade do arquivo para o usuário spring
RUN chown spring:spring app.jar

# Mudar para usuário não-root
USER spring

# Expor porta da aplicação
EXPOSE 8080

# Configurar variáveis de ambiente padrão
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]