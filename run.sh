#!/bin/bash

echo "Iniciando Async Processing Application..."

# Verifica se o container já existe e o remove
if docker ps -a | grep -q async-processing-app; then
    echo "Removendo container anterior..."
    docker rm -f async-processing-app
fi

# Executa o container
docker run -d     --name async-processing-app     -p 8080:8080     -e SPRING_PROFILES_ACTIVE=docker     async-processing-app:latest

# Verifica se o container iniciou
if [ $? -eq 0 ]; then
    echo "Container iniciado com sucesso!"
    echo "Aguardando aplicação iniciar..."
    
    # Aguarda a aplicação estar pronta
    attempt=1
    max_attempts=30
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:8080/actuator/health > /dev/null; then
            echo "Aplicação está pronta!"
            echo "Acesse: http://localhost:8080"
            exit 0
        fi
        echo "Tentativa $attempt de $max_attempts..."
        sleep 2
        ((attempt++))
    done
    
    echo "Timeout aguardando aplicação iniciar."
    exit 1
else
    echo "Erro ao iniciar o container."
    exit 1
fi
