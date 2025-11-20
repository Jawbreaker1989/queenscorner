# Queens Corner - Docker Setup Guide

## 📋 Requisitos Previos

- Docker Desktop (versión 20.10+)
- Docker Compose (versión 1.29+)
- 4GB RAM mínimo disponible
- Puerto 8080, 4200 y 3306 disponibles

## 🚀 Inicio Rápido

### 1. Construir e iniciar todos los servicios

```bash
# Desde la raíz del proyecto
docker-compose up --build

# O en segundo plano
docker-compose up -d --build
```

### 2. Verificar que todo está funcionando

```bash
# Verificar servicios
docker-compose ps

# Ver logs del backend
docker-compose logs backend

# Ver logs del frontend
docker-compose logs frontend

# Ver logs de la base de datos
docker-compose logs mysql
```

## 🌐 URLs de Acceso

- **Frontend (Angular)**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger/OpenAPI**: http://localhost:8080/swagger-ui.html
- **Base de Datos (MySQL)**: localhost:3306

### Credenciales Base de Datos

- **Usuario**: root
- **Contraseña**: 3856074
- **Base de datos**: queens_corner_prod_v2

## 📦 Servicios Disponibles

### MySQL (queenscorner-db)
- Imagen: mysql:8.0
- Puerto: 3306
- Volumen: `mysql_data`
- Health Check: Automático

### Backend (queenscorner-api)
- Build: Dockerfile multi-stage
- Puerto: 8080
- Memoria: 512MB máximo / 256MB mínimo
- Depende de: MySQL
- Health Check: Actuator endpoint

### Frontend (queenscorner-web)
- Build: Node 20 + Nginx Alpine
- Puerto: 4200
- Reverse proxy: Configurado para /api/

## 🛠️ Comandos Útiles

### Detener servicios
```bash
docker-compose down
```

### Detener y eliminar volúmenes (limpia BD)
```bash
docker-compose down -v
```

### Reconstruir un servicio específico
```bash
docker-compose up --build backend
```

### Ejecutar comando en un contenedor
```bash
# Acceso a MySQL
docker-compose exec mysql mysql -u root -p

# Logs en tiempo real
docker-compose logs -f backend
```

### Ver recursos utilizados
```bash
docker stats
```

## 🔧 Configuración Personalizada

### Variables de Entorno

Crear archivo `.env` en la raíz para sobrescribir valores:

```env
MYSQL_ROOT_PASSWORD=tu_contraseña
MYSQL_PASSWORD=tu_pass_user
SPRING_DATASOURCE_PASSWORD=tu_contraseña
```

### Ajustar Memoria JVM

En `docker-compose.yml`, modificar:
```yaml
environment:
  JAVA_TOOL_OPTIONS: "-Xmx1024m -Xms512m"
```

## 📊 Base de Datos

Las migraciones Flyway se ejecutan automáticamente:
- Archivos en: `src/main/resources/db/migration/`
- Versiones: V001 a V012

## 🐛 Troubleshooting

### Puerto 3306 en uso
```bash
docker-compose down
# O cambiar puerto en docker-compose.yml
```

### Backend no se conecta a BD
```bash
# Esperar a que MySQL esté listo
docker-compose logs mysql | grep "ready for connections"
```

### Frontend muestra error de API
- Verificar que backend está corriendo: `docker-compose ps`
- Ver logs: `docker-compose logs backend`
- Comprobar conexión: `docker-compose exec backend curl http://localhost:8080/actuator/health`

### Limpiar todo y empezar de nuevo
```bash
docker-compose down -v
docker system prune -a
docker-compose up --build
```

## 📝 Notas Importantes

- El frontend se genera con `npm run build` automáticamente en Docker
- Las migraciones de BD son idempotentes
- Los logs se guardan en `logs/queenscorner-app.log`
- Cache Caffeine: 10 minutos de expiración
- Zona horaria: America/Bogota

## 🔐 Seguridad

- JWT habilitado en el backend
- CORS configurado para frontend local
- Contraseñas en variables de entorno (cambiar en producción)
- SSL/TLS: Configurar con nginx en producción

---

**Para producción**: Ajustar `docker-compose.yml`, usar secretos en lugar de variables de entorno y configurar certificados SSL.
