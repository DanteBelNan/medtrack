# MedTrack Backend 💊

Backend del sistema MedTrack - Una aplicación de recordatorio de medicamentos que ayuda a los usuarios a gestionar sus tratamientos médicos mediante notificaciones programadas por WhatsApp y push notifications.

## 📋 Descripción

MedTrack permite a los usuarios:
- Autenticarse de forma segura
- Registrar medicamentos con horarios específicos en su zona horaria local
- Configurar recordatorios recurrentes (diarios, semanales) o con fecha de finalización
- Recibir notificaciones automáticas por WhatsApp y notificaciones push cuando es hora de tomar sus medicamentos

Este repositorio contiene el backend desarrollado en **Java 21** con **Spring Boot** y **PostgreSQL**.

## 🛠️ Stack Tecnológico

- **Java 21**
- **Spring Boot** (Framework principal)
- **Spring Data JPA** (Persistencia)
- **PostgreSQL** (Base de datos)
- **Lombok** (Reducción de boilerplate)
- **Docker Compose** (Orquestación de base de datos)

## 📁 Estructura del Proyecto

```
medtrack-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/medtrack/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       └── config/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── docker-compose.yml
├── .env.example
└── README.md
```

## 🚀 Requisitos Previos

- **Java 21** instalado
- **Docker** y **Docker Compose** instalados
- **IntelliJ IDEA** (recomendado) u otro IDE compatible
- **Maven** (generalmente incluido en el IDE)

## ⚙️ Configuración Inicial

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/medtrack-backend.git
cd medtrack-backend
```

### 2. Configurar variables de entorno

Crea un archivo `.env` en la raíz del proyecto basándote en `.env.example`:

```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/medtrack_db
DB_NAME=medtrack_db
DB_USER=user_medtrack
DB_PASSWORD=password_medtrack
DB_PORT=5432

# Application Configuration
# JWT_SECRET=tu_secret_key_aqui
# WHATSAPP_API_KEY=tu_api_key_aqui
```

⚠️ **Importante**: Modifica las credenciales de la base de datos en producción.

### 3. Levantar la base de datos

Ejecuta Docker Compose para iniciar PostgreSQL:

```bash
docker-compose up -d
```

Esto iniciará un contenedor con PostgreSQL configurado según las variables del `.env`.

Para verificar que está corriendo:

```bash
docker-compose ps
```

### 4. Abrir el proyecto en IntelliJ IDEA

1. Abre IntelliJ IDEA
2. File → Open → Selecciona la carpeta del proyecto
3. Espera a que Maven descargue las dependencias
4. Asegúrate de que el SDK esté configurado en Java 21

### 5. Compilar y ejecutar

**Opción A: Desde IntelliJ IDEA**
- Busca la clase principal (anotada con `@SpringBootApplication`)
- Click derecho → Run

**Opción B: Desde terminal con Maven**
```bash
./mvnw spring-boot:run
```

**Opción C: Compilar JAR y ejecutar**
```bash
./mvnw clean package
java -jar target/medtrack-backend-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en `http://localhost:8080`

## 🗄️ Base de Datos

La base de datos se genera automáticamente usando **JPA/Hibernate** a partir de las entidades definidas con **Lombok**.

Para acceder a la base de datos directamente:

```bash
docker exec -it medtrack_db psql -U user_medtrack -d medtrack_db
```

## 🧪 Testing

Ejecutar tests:

```bash
./mvnw test
```

## 📝 Notas de Desarrollo

- Las tablas de la base de datos se crean automáticamente al iniciar la aplicación gracias a Hibernate
- Lombok genera automáticamente getters, setters, constructores y builders

**Estado del Proyecto**: 🚧 En Desarrollo (POC)