# Docker Hub Setup para Queens Corner

## 🐳 Configuración Docker Hub

### 1. Crear Cuenta en Docker Hub

1. Ve a https://hub.docker.com
2. Click en "Sign Up"
3. Completa el registro (username: **jawbreaker1989** recomendado)
4. Verifica tu email

### 2. Configurar Secretos en GitHub

Ve a tu repositorio → **Settings → Secrets and variables → Actions**

Agrega estos secretos:
- `DOCKER_USERNAME`: Tu username de Docker Hub
- `DOCKER_PASSWORD`: Tu contraseña o Personal Access Token

**Recomendado: Usar Personal Access Token**

1. En Docker Hub: Account Settings → Security → Personal Access Tokens
2. Click "Generate New Token"
3. Dale nombre: `github-actions`
4. Permisos: Read, Write, Delete
5. Copia el token
6. En GitHub: Pega como `DOCKER_PASSWORD`

### 3. Opción Manual: Desde tu computadora

```bash
# Login en Docker Hub
docker login

# Construir y subir
.\push-to-dockerhub.bat  # Windows
./push-to-dockerhub.sh   # Linux/Mac
```

## 🚀 Automatización con GitHub Actions

El workflow `docker-publish.yml` se ejecuta automáticamente cuando:

- ✅ Haces push a `main`
- ✅ Haces push a `feature/factura-creation`
- ✅ Creas un tag (`git tag v1.0.1`)
- ✅ Ejecutas manualmente desde Actions

### Tagging automático

Las imágenes se publican con:
- **Versión**: Tag del commit (ej: `v1.0.1` → `1.0.1`)
- **Latest**: Siempre como `latest`

```bash
# Crear y subir una versión
git tag v1.0.1
git push origin v1.0.1
# ✅ GitHub Actions automáticamente construye y publica
```

## 📦 Usar Imágenes desde Docker Hub

### Opción 1: docker-compose con imágenes del Hub

Crear `docker-compose.prod.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    # ... configuración igual

  backend:
    image: jawbreaker1989/queenscorner-backend:latest
    # remove: build context
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/queens_corner_prod_v2
      # ...

  frontend:
    image: jawbreaker1989/queenscorner-frontend:latest
    # remove: build context
    ports:
      - "4200:80"
```

Ejecutar:
```bash
docker-compose -f docker-compose.prod.yml up
```

### Opción 2: docker run directo

```bash
# Backend
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/queens_corner_prod_v2 \
  -e SPRING_DATASOURCE_PASSWORD=3856074 \
  jawbreaker1989/queenscorner-backend:latest

# Frontend
docker run -d \
  -p 4200:80 \
  jawbreaker1989/queenscorner-frontend:latest
```

## 🔄 CI/CD Pipeline

El workflow proporciona:

1. **Build Multi-stage**: Optimización de tamaño
2. **Caché GHA**: Acelera builds subsecuentes
3. **Push a Hub**: Automático en cada push/tag
4. **Releases GitHub**: Crea release con instrucciones
5. **Notificaciones**: Logs de éxito/error

## 📊 Monitorear Publicaciones

### Ver logs en GitHub

Settings → Actions → docker-publish → (último run)

### Ver imágenes en Docker Hub

https://hub.docker.com/r/jawbreaker1989/queenscorner-backend
https://hub.docker.com/r/jawbreaker1989/queenscorner-frontend

### Comandos útiles

```bash
# Ver imágenes locales
docker images | grep queenscorner

# Ver histórico de tags
docker search jawbreaker1989/queenscorner-backend

# Descargar versión específica
docker pull jawbreaker1989/queenscorner-backend:1.0.0
docker pull jawbreaker1989/queenscorner-backend:latest

# Verificar layers
docker inspect jawbreaker1989/queenscorner-backend:latest
```

## 🔐 Seguridad

- ✅ Tokens de acceso en lugar de contraseñas
- ✅ Secretos en GitHub (nunca en código)
- ✅ Imágenes con usuarios no-root (Dockerfile)
- ✅ Escaneo de vulnerabilidades en Docker Hub (Pro)

## 🐛 Troubleshooting

### Error: "unauthorized: authentication required"

```bash
# Solución
docker logout
docker login
# Ingresa credenciales
```

### Error: "denied: requested access to the resource is denied"

- Verifica que los secretos están configurados
- El username debe ser lowercase
- El repo debe ser público o el user debe tener permisos

### Las imágenes son muy grandes

Optimizar Dockerfile:
- Usa `.dockerignore` (ya está)
- Multi-stage builds (ya implementado)
- Alpine en lugar de full images
- Limpiar caché de apt/npm

## 📈 Escalado

Después de publicar en Docker Hub:

1. **Kubernetes**: Deploy con helm
2. **AWS ECS**: Registrar tareas
3. **Docker Swarm**: Orquestar servicios
4. **CI/CD**: Deploy automático a producción

---

**Próximas lecturas:**
- [Docker Hub Documentation](https://docs.docker.com/docker-hub/)
- [GitHub Actions - Docker](https://github.com/docker/build-push-action)
- [Multi-stage builds](https://docs.docker.com/build/building/multi-stage/)
