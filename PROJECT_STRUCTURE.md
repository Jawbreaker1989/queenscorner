# Queens Corner - Estructura del Proyecto

## 📋 Información General

- **Nombre**: Queens Corner
- **Grupo**: com.uptc
- **Artifact**: queenscorner
- **Versión**: 1.0.0
- **Java**: 17
- **Spring Boot**: 3.5.7
- **Base de Datos**: MySQL 8
- **Puerto**: 8080

## 🏗️ Arquitectura del Proyecto

### Patrón de Diseño
- **MVC (Model-View-Controller)** con separación de capas
- **Repository Pattern** para acceso a datos
- **Service Layer** para lógica de negocio
- **DTO Pattern** para transferencia de datos

### Tecnologías Principales
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring Cache (Caffeine)** - Sistema de caché
- **Spring Async** - Procesamiento asíncrono
- **SpringDoc OpenAPI** - Documentación API
- **iText PDF** - Generación de PDFs
- **Lombok** - Reducción de código boilerplate
- **MySQL** - Base de datos

## 📁 Estructura de Directorios

```
d:\queenscorner/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── uptc/
│   │   │           └── queenscorner/
│   │   │               ├── QueenscornerApplication.java    # Clase principal
│   │   │               ├── config/                         # Configuraciones
│   │   │               ├── controllers/                    # Controladores REST
│   │   │               ├── exceptions/                     # Manejo de excepciones
│   │   │               ├── models/                         # Modelos de datos
│   │   │               ├── repositories/                   # Repositorios JPA
│   │   │               ├── services/                       # Servicios de negocio
│   │   │               └── utils/                          # Utilidades
│   │   └── resources/
│   │       └── application.properties                      # Configuración de la aplicación
│   └── test/
│       └── java/
│           └── com/
│               └── uptc/
│                   └── queenscorner/
│                       └── QueenscornerApplicationTests.java
├── target/                                                  # Archivos compilados
├── logs/                                                    # Logs de la aplicación
├── queenscornerarchives/                                    # Archivos generados
├── pom.xml                                                  # Configuración Maven
├── mvnw, mvnw.cmd                                          # Maven Wrapper
└── *.postman_collection.json                              # Colecciones Postman
```

## 📦 Estructura Detallada del Código

### 🔧 **config/** - Configuraciones
```
config/
├── AsyncConfig.java           # Configuración de hilos @Async
├── CacheConfig.java           # Configuración de Caffeine Cache
└── SwaggerConfig.java         # Documentación OpenAPI/Swagger
```

**Funcionalidades:**
- **AsyncConfig**: Pool de hilos para operaciones asíncronas (PDF, notificaciones)
- **CacheConfig**: Caché en memoria para consultas frecuentes
- **SwaggerConfig**: Documentación automática de la API REST

### 🎮 **controllers/** - Controladores REST
```
controllers/
├── ClienteController.java      # CRUD de clientes
├── CotizacionController.java   # Gestión de cotizaciones
├── FacturaController.java      # Manejo de facturas
├── NegocioController.java      # Información del negocio
├── OrdenTrabajoController.java # Órdenes de trabajo
└── PagoController.java         # Registro de pagos
```

**Endpoints Principales:**
- `/api/clientes` - Gestión de clientes
- `/api/cotizaciones` - Cotizaciones y presupuestos
- `/api/facturas` - Facturación
- `/api/negocio` - Configuración del negocio
- `/api/ordenes-trabajo` - Órdenes de trabajo
- `/api/pagos` - Registro de pagos

### ⚠️ **exceptions/** - Manejo de Errores
```
exceptions/
├── BusinessException.java         # Excepciones de negocio
├── GlobalExceptionHandler.java    # Manejador global de excepciones
└── ResourceNotFoundException.java # Recurso no encontrado
```

### 🏗️ **models/** - Modelos de Datos
```
models/
├── dtos/                          # Data Transfer Objects
│   ├── requests/                  # DTOs para requests
│   └── responses/                 # DTOs para responses
├── entities/                      # Entidades JPA
│   ├── ClienteEntity.java
│   ├── CotizacionEntity.java
│   ├── FacturaEntity.java
│   ├── ItemCotizacionEntity.java
│   ├── NegocioEntity.java
│   ├── OrdenTrabajoEntity.java
│   ├── PagoEntity.java
│   └── UsuarioEntity.java
└── mappers/                       # Mappers Entity ↔ DTO
```

**Entidades Principales:**
- **ClienteEntity**: Información de clientes
- **CotizacionEntity**: Cotizaciones y presupuestos
- **FacturaEntity**: Facturas emitidas
- **ItemCotizacionEntity**: Items de cotización
- **NegocioEntity**: Configuración del negocio
- **OrdenTrabajoEntity**: Órdenes de trabajo
- **PagoEntity**: Pagos realizados
- **UsuarioEntity**: Usuarios del sistema

### 🗃️ **repositories/** - Acceso a Datos
```
repositories/
├── IClienteRepository.java
├── ICotizacionRepository.java
├── IFacturaRepository.java
├── IItemCotizacionRepository.java
├── INegocioRepository.java
├── IOrdenTrabajoRepository.java
└── IPagoRepository.java
```

**Características:**
- Extienden `JpaRepository`
- Consultas personalizadas con `@Query`
- Soporte para paginación y ordenamiento

### 🔄 **services/** - Lógica de Negocio
```
services/
├── async/                         # Servicios asíncronos
│   ├── NotificacionAsyncService.java  # Notificaciones asíncronas
│   └── PdfAsyncService.java           # Generación de PDFs
├── impl/                          # Implementaciones de servicios
│   ├── ClienteServiceImpl.java
│   ├── CotizacionServiceImpl.java
│   ├── FacturaServiceImpl.java
│   ├── NegocioServiceImpl.java
│   ├── OrdenTrabajoServiceImpl.java
│   └── PagoServiceImpl.java
├── IClienteService.java           # Interfaces de servicios
├── ICotizacionService.java
├── IFacturaService.java
├── INegocioService.java
├── IOrdenTrabajoService.java
└── IPagoService.java
```

**Servicios Asíncronos:**
- **NotificacionAsyncService**: Envío de notificaciones en background
- **PdfAsyncService**: Generación de PDFs de forma asíncrona

### 🛠️ **utils/** - Utilidades
```
utils/
└── FileUtils.java                 # Utilidades para manejo de archivos
```

## ⚙️ Configuración de la Aplicación

### 🗄️ Base de Datos
- **URL**: `jdbc:mysql://localhost:3306/queens_corner_prod_v2`
- **Usuario**: `root`
- **Contraseña**: `3856074`
- **Dialecto**: MySQL8Dialect
- **DDL**: update (auto-actualización de esquema)

### 💾 Caché (Caffeine)
- **Tipo**: Caffeine
- **Cachés**: clientes, cotizaciones, negocios, catalogos
- **Configuración**: 500 entradas máximo, expiración 10 minutos

### 🔄 Configuración Asíncrona
- **Core Pool Size**: 5 hilos
- **Max Pool Size**: 10 hilos
- **Queue Capacity**: 100 tareas
- **Thread Prefix**: queens-async-

### 📝 Logging
- **Archivo**: `logs/queenscorner-app.log`
- **Nivel Root**: INFO
- **Nivel App**: DEBUG
- **Formato**: Timestamp, nivel, hilo, clase, mensaje

### 📚 Documentación API
- **Swagger UI**: `/swagger-ui.html`
- **API Docs**: `/api-docs`
- **Ordenamiento**: Alfabético por tags y operaciones

## 📋 Archivos Importantes

### 🔧 Configuración
- `pom.xml` - Dependencias y configuración Maven
- `application.properties` - Configuración de la aplicación
- `mvnw`, `mvnw.cmd` - Maven Wrapper

### 📁 Directorios de Archivos
- `logs/` - Logs de la aplicación
- `queenscornerarchives/` - Archivos generados (PDFs, comprobantes)
- `target/` - Archivos compilados y generados

### 🧪 Testing
- `QueenscornerApplicationTests.java` - Tests de integración

### 📮 API Testing
- `queens-corner-postman-collection.json` - Colección Postman
- `queens-corner-flow-test.postman_collection.json` - Tests de flujo

## 🚀 Comandos Útiles

### Compilar y Ejecutar
```bash
# Compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Ejecutar aplicación
./mvnw spring-boot:run

# Generar JAR
./mvnw clean package
```

### Acceso a la Aplicación
- **Aplicación**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **Actuator**: http://localhost:8080/actuator

## 🔧 Funcionalidades Principales

### 👥 Gestión de Clientes
- CRUD completo de clientes
- Búsqueda y filtrado
- Caché para consultas frecuentes

### 📋 Sistema de Cotizaciones
- Creación y edición de cotizaciones
- Cálculo automático de totales
- Generación de PDFs asíncronos
- Conversión a facturas

### 🧾 Facturación
- Emisión de facturas
- Tracking de pagos
- Generación de reportes

### 🏢 Configuración del Negocio
- Información de la empresa
- Configuraciones generales
- Parámetros del sistema

### ⚡ Características Avanzadas
- **Caché**: Mejora el rendimiento de consultas
- **Async**: Procesamiento en background
- **Exception Handling**: Manejo centralizado de errores
- **API Documentation**: Documentación automática
- **Logging**: Registro detallado de operaciones

---

**Desarrollado con ❤️ usando Spring Boot 3.5.7 y Java 17**