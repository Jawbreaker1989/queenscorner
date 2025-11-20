# Build y push a Docker Hub
# Requiere: docker login

# Variables
DOCKER_USERNAME=jawbreaker1989
PROJECT_NAME=queenscorner
VERSION=1.0.0
REGISTRY=docker.io

# Construir imágenes
echo "🏗️  Construyendo imágenes..."
docker build -t ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-backend:${VERSION} .
docker build -t ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-backend:latest .
docker build -t ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-frontend:${VERSION} ./queenscorner-frontend
docker build -t ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-frontend:latest ./queenscorner-frontend

# Push a Docker Hub
echo "📤 Subiendo a Docker Hub..."
docker push ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-backend:${VERSION}
docker push ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-backend:latest
docker push ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-frontend:${VERSION}
docker push ${REGISTRY}/${DOCKER_USERNAME}/${PROJECT_NAME}-frontend:latest

echo "✅ Imágenes publicadas:"
echo "   Backend: ${DOCKER_USERNAME}/${PROJECT_NAME}-backend:${VERSION}"
echo "   Frontend: ${DOCKER_USERNAME}/${PROJECT_NAME}-frontend:${VERSION}"
