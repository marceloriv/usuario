# 👤 Microservicio de Gestión de Usuarios

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-4.0.0-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-Connector-blue.svg)](https://dev.mysql.com/downloads/connector/j/)

## 📋 Descripción

Microservicio REST desarrollado en Spring Boot para la gestión completa de usuarios. Forma parte del ecosistema **Skak** y proporciona funcionalidades de registro, consulta, actualización y eliminación de usuarios con manejo avanzado de excepciones y documentación automática de API.

## ✨ Características

- 🔐 **Registro y gestión de usuarios**
- 📧 **Búsqueda por email y teléfono**
- 🛡️ **Manejo robusto de excepciones**
- 📚 **Documentación automática con Swagger/OpenAPI**
- 🔄 **Arquitectura RESTful**
- ✅ **Validación de datos con Bean Validation**
- 🧪 **Cobertura de pruebas unitarias e integración**
- 🌱 **Configuración con variables de entorno**

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas:

```
├── 🎮 Controller     # Capa de presentación (API REST)
├── 🔧 Service        # Lógica de negocio
├── 📊 Repository     # Acceso a datos
├── 🏷️ Model          # Entidades JPA
├── 📋 DTO            # Objetos de transferencia de datos
└── ⚠️ Exception      # Manejo de excepciones personalizadas
```

## 🛠️ Tecnologías

- **Framework**: Spring Boot 3.5.3
- **Lenguaje**: Java 17
- **Base de Datos**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Documentación**: SpringDoc OpenAPI (Swagger)
- **Gestión de Dependencias**: Maven
- **Validación**: Jakarta Bean Validation
- **Testing**: JUnit, Spring Boot Test
- **Otras**: Lombok, Spring DevTools

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/skar/usuario/
│   │   ├── UsuarioApplication.java          # Clase principal
│   │   ├── config/
│   │   │   └── SwaggerConfig.java           # Configuración de Swagger
│   │   ├── controller/
│   │   │   └── UsuarioController.java       # Controlador REST
│   │   ├── dto/
│   │   │   ├── ApiRespuestaDto.java         # DTO para respuestas
│   │   │   ├── ApiRespuestaEstados.java     # Estados de respuesta
│   │   │   └── RegistracionUsuarioDto.java  # DTO para registro
│   │   ├── exception/
│   │   │   ├── ErrorLogicaServicioUsuarioException.java
│   │   │   ├── UsuarioNoEncontradoException.java
│   │   │   └── UsuarioYaExisteException.java
│   │   ├── handler/
│   │   │   ├── GlobalExceptionHandler.java  # Manejo global de excepciones
│   │   │   └── UsuarioExceptionHandler.java # Manejo específico
│   │   ├── model/
│   │   │   ├── Rol.java                     # Enumerado de roles
│   │   │   └── Usuario.java                 # Entidad Usuario
│   │   ├── repository/
│   │   │   └── RepositorioUsuario.java      # Repositorio JPA
│   │   └── service/
│   │       ├── UsuarioService.java          # Interfaz del servicio
│   │       └── UsuarioServiceImp.java       # Implementación del servicio
│   └── resources/
│       └── application.properties           # Configuración de la aplicación
└── test/
    └── java/com/skar/usuario/
        ├── UsuarioApplicationTests.java
        ├── controller/
        │   └── UsuarioControllerTest.java
        └── service/
            └── UsuarioServiceTest.java
```

## 🚀 Inicio Rápido

### Prerrequisitos

- ☕ Java 21 o superior (recomendado: Eclipse Temurin)
- 🗃️ MySQL 8.0 o superior (local o AWS RDS)
- 📦 Maven 3.9 o superior

### Configuración

1. **Clona el repositorio**

   ```bash
   git clone <url-del-repositorio>
   cd usuario
   ```

2. **Configura las variables de entorno**

   Crea un archivo `.env` en la raíz del proyecto:

   ```env
   # Configuración de la base de datos (AWS RDS o local)
   SPRING_DATASOURCE_URL=jdbc:mysql://database-1.c4efjw97jtlo.us-east-1.rds.amazonaws.com:3306/powerfit_usuario
   SPRING_DATASOURCE_USERNAME=admin
   SPRING_DATASOURCE_PASSWORD=tu_password_aqui
   SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

   # Configuración de JPA/Hibernate
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_JPA_SHOW_SQL=true
   SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
   ```

   **Para MySQL local (Laragon/XAMPP)**:

   ```env
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/powerfit_usuario
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=
   ```

3. **Crea la base de datos**

   ```sql
   CREATE DATABASE powerfit_usuario;
   ```

   *(Hibernate creará las tablas automáticamente con `ddl-auto=update`)*

### 🏃 Ejecución Manual (Paso a Paso)

#### Opción 1: Con Maven Wrapper (Recomendado)

```powershell
# Windows PowerShell
cd usuario
./mvnw clean install
./mvnw spring-boot:run
```

```bash
# Linux/Mac
cd usuario
./mvnw clean install
./mvnw spring-boot:run
```

#### Opción 2: Con Maven Global

```powershell
cd usuario
mvn clean install
mvn spring-boot:run
```

#### Opción 3: Ejecutar JAR directamente

```powershell
# 1. Compilar y empaquetar
./mvnw clean package -DskipTests

# 2. Ejecutar JAR
java -jar target/usuario-0.0.1-SNAPSHOT.jar
```

#### Opción 4: Con perfil específico

```powershell
# Perfil de producción
$env:SPRING_PROFILES_ACTIVE='prod'; ./mvnw spring-boot:run

# Perfil local
$env:SPRING_PROFILES_ACTIVE='local'; ./mvnw spring-boot:run
```

### ✅ Verificar que está corriendo

Una vez iniciado, verifica:

- **Health Check**: `http://localhost:8082/actuator/health`
- **Swagger UI**: `http://localhost:8082/swagger-ui/index.html`
- **API Docs JSON**: `http://localhost:8082/v3/api-docs`

**Respuesta esperada** de health:

```json
{
  "status": "UP"
}
```

### 🔄 Reiniciar/Detener

- **Ctrl + C** en la terminal para detener
- Ejecutar `./mvnw spring-boot:run` nuevamente para reiniciar

## 📖 Documentación de la API

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de Swagger en:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### 🔍 Endpoints Principales

| Método   | Endpoint                               | Descripción                      |
| -------- | -------------------------------------- | -------------------------------- |
| `POST`   | `/api/v1/usuarios`                     | Registrar nuevo usuario          |
| `GET`    | `/api/v1/usuarios/{email}`             | Obtener usuario por email        |
| `GET`    | `/api/v1/usuarios/telefono/{telefono}` | Obtener usuario por teléfono     |
| `PUT`    | `/api/v1/usuarios/{id}`                | Actualizar usuario completo      |
| `PATCH`  | `/api/v1/usuarios/{id}`                | Actualización parcial de usuario |
| `DELETE` | `/api/v1/usuarios/{id}`                | Eliminar usuario                 |

### 📝 Ejemplo de Registro de Usuario

```json
POST /api/v1/usuarios
Content-Type: application/json

{
  "nombre": "Juan",
  "apellidos": "Pérez García",
  "email": "juan.perez@example.com",
  "contrasena": "password123",
  "telefono": "+34123456789",
  "direccion": "Calle Mayor 123, Madrid",
  "rol": "USER"
}
```

### 📤 Respuesta Exitosa

```json
{
  "mensaje": "Usuario registrado exitosamente",
  "estado": "EXITO",
  "datos": {
    "id": 1,
    "nombre": "Juan",
    "apellidos": "Pérez García",
    "email": "juan.perez@example.com",
    "telefono": "+34123456789",
    "direccion": "Calle Mayor 123, Madrid",
    "rol": "USER",
    "estado": true
  }
}
```

## 🔒 Seguridad

### Mecanismos Implementados

- **Encriptación de Contraseñas**: BCrypt con `PasswordEncoder` de Spring Security
  - Salt automático por usuario
  - Factor de trabajo: 10 rounds (por defecto)
  - Validación con `passwordEncoder.matches()`

- **Validación de Entrada**: Bean Validation (Jakarta)
  - `@NotBlank`, `@Email`, `@Size` en DTOs
  - Validación automática con `@Valid` en controllers

- **Manejo de Excepciones**:
  - `GlobalExceptionHandler` y `UsuarioExceptionHandler`
  - Respuestas estandarizadas sin exponer detalles internos
  - Logging de errores para auditoría

- **Variables de Entorno**:
  - Credenciales nunca en código fuente
  - Archivos `.env` en `.gitignore`
  - Integración con `spring-dotenv`

- **Endpoints Protegidos**:
  - Login devuelve 401 Unauthorized para credenciales inválidas
  - No expone información sobre existencia de usuarios

### Recomendaciones para Producción

- Implementar JWT o Spring Security con roles
- HTTPS obligatorio (ya implementado en Vercel)
- Rate limiting para prevenir fuerza bruta
- Logs de auditoría para accesos y cambios

## 📊 Cobertura de Tests

### Estadísticas Actuales

```bash
./mvnw test
```

**Resultados**:

- ✅ **33 tests** en total
- ✅ **100% tests pasados**
- 📦 **Cobertura estimada**: ~80%

### Tests Incluidos

**Tests Unitarios** (`UsuarioServiceTest`):

- Registro de usuario exitoso
- Validación de email duplicado
- Búsqueda por email, teléfono, ID
- Actualización de usuario
- Eliminación de usuario
- Manejo de excepciones

**Tests de Integración** (`UsuarioControllerTest`):

- Endpoints REST completos
- Validación de request/response
- Códigos HTTP correctos
- Serialización JSON

**Tests de Contexto** (`UsuarioApplicationTests`):

- Carga correcta del contexto Spring
- Beans configurados correctamente

### Ejecutar con Cobertura

```bash
# Generar reporte Jacoco
./mvnw test jacoco:report

# Ver reporte en: target/site/jacoco/index.html
```

## 🧪 Pruebas

### Ejecutar todas las pruebas

```bash
./mvnw test
```

### Ejecutar pruebas específicas

```bash
# Pruebas del controlador
./mvnw test -Dtest=UsuarioControllerTest

# Pruebas del servicio
./mvnw test -Dtest=UsuarioServiceTest
```

### Generar reporte de cobertura

```bash
./mvnw jacoco:report
```

## 🏗️ Build y Deployment

### Compilar el proyecto

```bash
./mvnw clean compile
```

### Generar JAR ejecutable

```bash
./mvnw clean package
```

### Ejecutar JAR

```bash
java -jar target/usuario-0.0.1-SNAPSHOT.jar
```

## 🔧 Configuración Avanzada

### Perfiles de Spring

- **Por defecto (H2)**: `application.properties` (H2 en memoria, útil para desarrollo rápido)
- **Local (Laragon/MySQL)**: `application-local.properties`
- **Producción (RDS u otro MySQL)**: `application-prod.properties`
- **Testing**: `application-test.properties`

#### Ejecutar con perfiles (PowerShell en Windows)

```powershell
# Limpiar variables de entorno que puedan forzar MySQL remoto (opcional)
Remove-Item Env:SPRING_DATASOURCE_URL,Env:SPRING_DATASOURCE_USERNAME,Env:SPRING_DATASOURCE_PASSWORD,Env:SPRING_DATASOURCE_DRIVER_CLASS_NAME,Env:SPRING_JPA_HIBERNATE_DDL_AUTO,Env:SPRING_JPA_SHOW_SQL,Env:SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL -ErrorAction SilentlyContinue

# H2 por defecto (sin perfil activo)
./mvnw spring-boot:run

# MySQL local con Laragon (perfil local)
$env:SPRING_PROFILES_ACTIVE='local'; ./mvnw spring-boot:run

# Producción (perfil prod, credenciales por variables de entorno)
$env:SPRING_PROFILES_ACTIVE='prod'; ./mvnw spring-boot:run
```

En `application-local.properties` se asume MySQL de Laragon en `127.0.0.1:3306` con usuario `root` y contraseña vacía. Ajusta `spring.datasource.username/password` si tu Laragon tiene credenciales distintas.

### Logging

La aplicación utiliza Logback para el manejo de logs. Los niveles se pueden configurar en `application.properties`:

```properties
logging.level.com.skar.usuario=DEBUG
logging.level.org.springframework.web=INFO
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📋 Convenciones de Código

- Usar **Lombok** para reducir código boilerplate
- Seguir las convenciones de nomenclatura de Java
- Documentar métodos públicos con JavaDoc
- Usar `@Valid` para validación de entrada
- Manejar excepciones específicas del dominio

## 🐛 Troubleshooting

### Problemas Comunes

1. **Error de conexión a la base de datos**

   - Verificar que MySQL esté ejecutándose
   - Comprobar las credenciales del perfil activo (p. ej., `application-local.properties`)
   - Asegurar que la base de datos existe
   - Si usas RDS: la instancia debe ser accesible públicamente o via túnel, y el Security Group debe permitir tu IP

2. **Puerto ya en uso**

   - Cambiar el puerto en `SERVER_PORT` en el `.env`
   - Verificar procesos que usan el puerto: `netstat -tlnp | grep :8080`

3. **Problemas con variables de entorno**
   - Verificar que el archivo `.env` esté en la raíz del proyecto
   - Comprobar la sintaxis de las variables

---

<div align="center">
  <sub>Desarrollado con ❤️ usando Spring Boot</sub>
</div>
