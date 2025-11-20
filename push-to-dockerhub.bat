@echo off
REM Script para construir y subir a Docker Hub (Windows)

setlocal enabledelayedexpansion

REM Variables
set DOCKER_USERNAME=jawbreaker1989
set PROJECT_NAME=queenscorner
set VERSION=1.0.0
set REGISTRY=docker.io

echo.
echo 🚀 Docker Hub - Build y Push
echo ============================
echo.

REM Verificar si Docker está corriendo
docker info >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker no está corriendo. Por favor inicia Docker Desktop.
    exit /b 1
)

echo ✅ Docker está corriendo
echo.

REM Verificar login en Docker Hub
echo 🔐 Verificando autenticación en Docker Hub...
docker images >nul 2>&1
if errorlevel 1 (
    echo ⚠️  Necesitas hacer login en Docker Hub
    docker login
)

echo.
echo 🏗️  Construyendo imágenes...
echo.

REM Build Backend
echo [1/4] Construyendo backend versión %VERSION%...
docker build -t %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-backend:%VERSION% .
if errorlevel 1 (
    echo ❌ Error construyendo backend
    exit /b 1
)

echo [2/4] Tagging backend como latest...
docker tag %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-backend:%VERSION% %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-backend:latest

REM Build Frontend
echo [3/4] Construyendo frontend versión %VERSION%...
docker build -t %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-frontend:%VERSION% ./queenscorner-frontend
if errorlevel 1 (
    echo ❌ Error construyendo frontend
    exit /b 1
)

echo [4/4] Tagging frontend como latest...
docker tag %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-frontend:%VERSION% %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-frontend:latest

echo.
echo 📤 Subiendo a Docker Hub...
echo.

REM Push Backend
echo Pushing backend:%VERSION%...
docker push %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-backend:%VERSION%
if errorlevel 1 (
    echo ❌ Error subiendo backend versión
    exit /b 1
)

echo Pushing backend:latest...
docker push %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-backend:latest

REM Push Frontend
echo Pushing frontend:%VERSION%...
docker push %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-frontend:%VERSION%
if errorlevel 1 (
    echo ❌ Error subiendo frontend versión
    exit /b 1
)

echo Pushing frontend:latest...
docker push %REGISTRY%/%DOCKER_USERNAME%/%PROJECT_NAME%-frontend:latest

echo.
echo ✅ ¡Completado! Imágenes publicadas en Docker Hub:
echo.
echo 📦 Backend:
echo    %DOCKER_USERNAME%/%PROJECT_NAME%-backend:%VERSION%
echo    %DOCKER_USERNAME%/%PROJECT_NAME%-backend:latest
echo.
echo 📦 Frontend:
echo    %DOCKER_USERNAME%/%PROJECT_NAME%-frontend:%VERSION%
echo    %DOCKER_USERNAME%/%PROJECT_NAME%-frontend:latest
echo.
echo 🔗 Ver en Docker Hub:
echo    https://hub.docker.com/r/%DOCKER_USERNAME%/%PROJECT_NAME%-backend
echo    https://hub.docker.com/r/%DOCKER_USERNAME%/%PROJECT_NAME%-frontend
echo.

pause
