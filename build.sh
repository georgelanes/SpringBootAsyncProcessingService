#!/bin/bash

echo "Building Docker image for Async Processing Application..."

# Verifica se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Build da imagem
docker build -t async-processing-app:latest .

if [ $? -eq 0 ]; then
    echo "Build completado com sucesso!"
    echo "Para executar a aplicação, use: ./run.sh"
else
    echo "Erro durante o build da imagem."
    exit 1
fi
