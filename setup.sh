#!/bin/bash

# build.sh - Script para build da imagem Docker
BUILD_SCRIPT=$(cat << 'EOF'
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
EOF
)

# run.sh - Script para execução do container
RUN_SCRIPT=$(cat << 'EOF'
#!/bin/bash

echo "Iniciando Async Processing Application..."

# Verifica se o container já existe e o remove
if docker ps -a | grep -q async-processing-app; then
    echo "Removendo container anterior..."
    docker rm -f async-processing-app
fi

# Executa o container
docker run -d \
    --name async-processing-app \
    -p 8080:8080 \
    -e SPRING_PROFILES_ACTIVE=docker \
    async-processing-app:latest

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
EOF
)

# Cria os arquivos de script
echo "$BUILD_SCRIPT" > build.sh
echo "$RUN_SCRIPT" > run.sh

# Torna os scripts executáveis
chmod +x build.sh run.sh