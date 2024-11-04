# Async Processing Service

## Sobre o Projeto
Serviço desenvolvido em Spring Boot para processamento assíncrono de requisições, permitindo o acompanhamento do status de processamento em tempo real.

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3.2.x
- Maven
- Docker
- Lombok
- Spring Actuator

## Pré-requisitos
- Java 17+
- Maven 3.9+
- Docker (opcional)
- Git

## Estrutura do Projeto
```
br.com.globit.async_processing/
├── config/
│   └── AsyncConfig.java         # Configurações de processamento assíncrono
├── controller/
│   └── ProcessController.java   # Endpoints da API
├── service/
│   └── AsyncProcessingService.java  # Lógica de processamento
├── model/
│   ├── ProcessRequest.java      # Modelo de requisição
│   ├── ProcessResult.java       # Modelo de resultado
│   └── ProcessStatus.java       # Enum de status
├── repository/
│   └── ProcessTracker.java      # Rastreamento de processamentos
├── scheduler/
│   └── ProcessCleanupScheduler.java # Limpeza automática
└── exception/
    └── AsyncProcessingException.java # Exceções customizadas
```

## Configuração e Instalação

### Instalação Local
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/async-processing.git
cd async-processing
```

2. Compile o projeto:
```bash
mvn clean package
```

3. Execute a aplicação:
```bash
java -jar target/async-processing-0.0.1-SNAPSHOT.jar
```

### Instalação com Docker
1. Build da imagem:
```bash
./build.sh
# ou
docker build -t async-processing-app:latest .
```

2. Execute o container:
```bash
./run.sh
# ou
docker run -d -p 8080:8080 async-processing-app:latest
```

## Configurações (application.properties)
```properties
# Server
server.port=8080

# Async Processing
spring.task.execution.pool.core-size=5
spring.task.execution.pool.max-size=10
spring.task.execution.pool.queue-capacity=25

# Cleanup Schedule
async.cleanup.retention-days=7
async.cleanup.interval-ms=86400000
async.cleanup.initial-delay-ms=60000

# Logging
logging.level.br.com.globit=DEBUG
```

## Uso da API

### Iniciar Processamento
```bash
curl -X POST http://localhost:8080/api/process \
-H "Content-Type: application/json" \
-d '{
    "id": "123",
    "data": {
        "key": "value"
    }
}'
```

Resposta:
```json
{
    "id": "123",
    "status": "PENDING",
    "result": null,
    "lastUpdated": "2024-11-04T10:30:00"
}
```

### Consultar Status
```bash
curl http://localhost:8080/api/process/status/123
```

Resposta:
```json
{
    "id": "123",
    "status": "COMPLETED",
    "result": {
        "processedAt": "2024-11-04T10:30:05",
        "originalData": {
            "key": "value"
        }
    },
    "lastUpdated": "2024-11-04T10:30:05"
}
```

## Estados do Processamento
- `PENDING`: Processamento registrado
- `PROCESSING`: Em execução
- `COMPLETED`: Finalizado com sucesso
- `FAILED`: Finalizado com erro

## Monitoramento
A aplicação utiliza Spring Actuator para monitoramento. Endpoints disponíveis:

- Health Check: `http://localhost:8080/actuator/health`
- Métricas: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`

## Limpeza Automática
O sistema possui um scheduler que limpa automaticamente processamentos antigos:
- Executa a cada 24 horas
- Remove processamentos mais antigos que 7 dias
- Configurável via `application.properties`

## Desenvolvimento

### Compilação
```bash
mvn clean install
```

### Testes
```bash
mvn test
```

### Execução Local com Perfil de Desenvolvimento
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

## Docker Compose
```bash
docker-compose up -d
```

## Contribuição
1. Fork o projeto
2. Crie sua Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas alterações (`git commit -m 'Add some AmazingFeature'`)
4. Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença
Este projeto está sob a licença [MIT](LICENSE).

Link do Projeto: [https://github.com/seu-usuario/async-processing](https://github.com/seu-usuario/async-processing)

## Agradecimentos
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Docker](https://www.docker.com/)
- [Maven](https://maven.apache.org/)

