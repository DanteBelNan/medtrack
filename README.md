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
- **Docker & Docker Compose** (Containerización y orquestación)

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
├── Dockerfile
├── docker-compose.yml
├── .env.example
└── README.md
```

## 🚀 Requisitos Previos

- **Docker** y **Docker Compose** instalados
- **Java 21** (solo para desarrollo local sin Docker)
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
DB_USER=medtrack_user
DB_PASSWORD=medtrack_pass_dev
DB_NAME=medtrack_db
DB_PORT=5432

# Application Configuration (opcional)
# JWT_SECRET=tu_secret_key_aqui
# WHATSAPP_API_KEY=tu_api_key_aqui
```

⚠️ **Importante**: Modifica las credenciales de la base de datos en producción.

## 🐳 Ejecución con Docker (Recomendado)

### Opción 1: Levantar todo el stack (Base de datos + Aplicación)

```bash
docker-compose up --build
```

La aplicación estará disponible en `http://localhost:8080`

Para correr en segundo plano:
```bash
docker-compose up -d --build
```

### Opción 2: Solo la base de datos (para desarrollo local)

Si prefieres correr tu código Java localmente desde el IDE pero usar PostgreSQL en Docker:

```bash
docker-compose up db
```

Luego ejecuta la aplicación desde IntelliJ IDEA normalmente.

### Comandos útiles de Docker

```bash
# Ver logs de la aplicación
docker-compose logs -f app

# Ver logs de la base de datos
docker-compose logs -f db

# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (⚠️ borra los datos de la DB)
docker-compose down -v

# Reconstruir imágenes
docker-compose build --no-cache
```

## 💻 Desarrollo Local (sin Docker para la app)

### 1. Levantar solo PostgreSQL

```bash
docker-compose up db
```

### 2. Configurar `application.properties`

Asegúrate de que tu archivo `application.properties` apunte a localhost:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medtrack_db
spring.datasource.username=medtrack_user
spring.datasource.password=medtrack_pass_dev
```

### 3. Ejecutar desde IntelliJ IDEA

1. Abre IntelliJ IDEA
2. File → Open → Selecciona la carpeta del proyecto
3. Espera a que Maven descargue las dependencias
4. Asegúrate de que el SDK esté configurado en Java 21
5. Busca la clase principal (anotada con `@SpringBootApplication`)
6. Click derecho → Run

### 4. Ejecutar desde terminal con Maven

```bash
./mvnw spring-boot:run
```

### 5. Compilar JAR y ejecutar

```bash
./mvnw clean package
java -jar target/medtrack-backend-0.0.1-SNAPSHOT.jar
```

## 🗄️ Base de Datos

### Acceder a PostgreSQL

Para acceder a la base de datos directamente:

```bash
docker exec -it medtrack-db psql -U medtrack_user -d medtrack_db
```

### Migraciones

La base de datos se genera automáticamente usando **JPA/Hibernate** a partir de las entidades definidas con **Lombok**.

## 🧪 Testing

Ejecutar tests:

```bash
./mvnw test
```

Con Docker:
```bash
docker-compose run --rm app ./mvnw test
```

## 🏗️ Arquitectura de Microservicios

Este proyecto está preparado para escalar a una arquitectura de microservicios. Todos los servicios se comunican a través de la red `medtrack-network` definida en `docker-compose.yml`.

Para agregar nuevos microservicios, simplemente añade un nuevo servicio al `docker-compose.yml`:

```yaml
servicio-notificaciones:
  build:
    context: ./notificaciones-service
    dockerfile: Dockerfile
  ports:
    - "8081:8080"
  networks:
    - medtrack-network
  depends_on:
    - db
```

## 🚀 Deploy a AWS

Este proyecto deployara en AWS usando:
- **Amazon ECS** para orquestación de contenedores
- **Amazon ECR** para el registro de imágenes Docker
- **Amazon RDS for PostgreSQL** para la base de datos administrada

(Instrucciones detalladas de deployment próximamente)

## 📝 Notas de Desarrollo

- Las tablas de la base de datos se crean automáticamente al iniciar la aplicación gracias a Hibernate
- Lombok genera automáticamente getters, setters, constructores y builders
- El Dockerfile usa multi-stage build para optimizar el tamaño de la imagen final
- La aplicación dentro del contenedor se conecta a la DB usando el nombre del servicio (`db:5432`)

---

**Estado del Proyecto**: 🚧 En Desarrollo (POC)


    @Test
    void shouldReturnAllMedicines() throws Exception {
        Medicine m1 = new Medicine();
        m1.setName("Ibuprofeno");
        m1.setDosage("600mg");

        Medicine m2 = new Medicine();
        m2.setName("Aspirina");
        m2.setDosage("100mg");

        medicineRepository.save(m1);
        medicineRepository.save(m2);

        mockMvc.perform(get("/api/medicines/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Ibuprofeno"))
                .andExpect(jsonPath("$[1].name").value("Aspirina"));
    }

    @Test
    void shouldReturnMedicinesByUserIdWithAuth() throws Exception {
        Medicine med = new Medicine();
        med.setName("Loratadina");
        med.setUser(testUser);
        medicineRepository.save(med);

        mockMvc.perform(get("/api/medicines/user/" + testUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Loratadina"));
    }