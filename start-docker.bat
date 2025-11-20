@echo off
REM Script de inicialización rápida para Queens Corner con Docker (Windows)

echo.
echo 🚀 Queens Corner - Docker Setup
echo ================================

where docker >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker no está instalado. Por favor instala Docker Desktop.
    exit /b 1
)

echo ✅ Docker detectado
echo.
echo Elige una opción:
echo 1) Iniciar todos los servicios (build + up)
echo 2) Iniciar en segundo plano
echo 3) Detener servicios
echo 4) Ver logs en tiempo real
echo 5) Limpiar y reiniciar todo
echo 6) Ver estado de servicios
echo.

set /p option="Opción (1-6): "

if "%option%"=="1" (
    echo 🏗️  Construyendo e iniciando servicios...
    docker-compose up --build
) else if "%option%"=="2" (
    echo 🔄 Iniciando en segundo plano...
    docker-compose up -d --build
    echo.
    echo ✅ Servicios iniciados:
    echo    Frontend: http://localhost:4200
    echo    Backend: http://localhost:8080
    echo    Swagger: http://localhost:8080/swagger-ui.html
    echo.
    echo Para ver logs: docker-compose logs -f
) else if "%option%"=="3" (
    echo ⛔ Deteniendo servicios...
    docker-compose down
    echo ✅ Servicios detenidos
) else if "%option%"=="4" (
    echo 📊 Mostrando logs (presiona Ctrl+C para salir)...
    docker-compose logs -f
) else if "%option%"=="5" (
    echo 🧹 Limpiando volúmenes y reconstruyendo...
    docker-compose down -v
    docker-compose up --build
) else if "%option%"=="6" (
    echo 📋 Estado de servicios:
    docker-compose ps
) else (
    echo ❌ Opción inválida
    exit /b 1
)

pause
