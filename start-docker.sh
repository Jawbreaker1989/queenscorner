#!/bin/bash

# Script de inicialización rápida para Queens Corner con Docker

set -e

echo "🚀 Queens Corner - Docker Setup"
echo "================================"

# Verificar Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Por favor instala Docker Desktop."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado."
    exit 1
fi

echo "✅ Docker detectado"

# Menú de opciones
echo ""
echo "Elige una opción:"
echo "1) Iniciar todos los servicios (build + up)"
echo "2) Iniciar en segundo plano"
echo "3) Detener servicios"
echo "4) Ver logs en tiempo real"
echo "5) Limpiar y reiniciar todo"
echo "6) Abrir URLs en el navegador"
echo ""

read -p "Opción (1-6): " option

case $option in
    1)
        echo "🏗️  Construyendo y iniciando servicios..."
        docker-compose up --build
        ;;
    2)
        echo "🔄 Iniciando en segundo plano..."
        docker-compose up -d --build
        echo ""
        echo "✅ Servicios iniciados:"
        echo "   Frontend: http://localhost:4200"
        echo "   Backend: http://localhost:8080"
        echo "   Swagger: http://localhost:8080/swagger-ui.html"
        ;;
    3)
        echo "⛔ Deteniendo servicios..."
        docker-compose down
        echo "✅ Servicios detenidos"
        ;;
    4)
        echo "📊 Mostrando logs..."
        docker-compose logs -f
        ;;
    5)
        echo "🧹 Limpiando volúmenes y reconstruyendo..."
        docker-compose down -v
        docker-compose up --build
        ;;
    6)
        echo "🌐 Abriendo URLs..."
        if command -v xdg-open &> /dev/null; then
            xdg-open http://localhost:4200
            xdg-open http://localhost:8080/swagger-ui.html
        elif command -v open &> /dev/null; then
            open http://localhost:4200
            open http://localhost:8080/swagger-ui.html
        else
            echo "Frontend: http://localhost:4200"
            echo "Swagger: http://localhost:8080/swagger-ui.html"
        fi
        ;;
    *)
        echo "❌ Opción inválida"
        exit 1
        ;;
esac
