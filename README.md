# TODO List — Spring Boot (patrón CSR)

API REST para gestionar tareas, organizada en capas **Controller → Service → Repository**.

```
src/main/java/cl/duoc/todolist
├── controller/   TodoController, AuthController (endpoints REST)
├── service/      TodoService, AuthService       (lógica de negocio)
├── repository/   TodoRepository        (Spring Data JPA)
├── model/        Todo                  (entidad, tabla TODO)
├── dto/          TodoRequest / TodoResponse
├── mapper/       TodoMapper
├── exception/    manejo global de errores (ProblemDetail)
├── security/     JwtService            (emisión de tokens)
└── config/       Spring Security, JWT, CORS y OpenAPI
```

## Ejecutar

```bash
./mvnw spring-boot:run                                  # H2 en memoria (perfil por defecto)
SPRING_PROFILES_ACTIVE=mysql ./mvnw spring-boot:run     # MySQL (DB_URL, DB_USER, DB_PASSWORD)
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- Consola H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:tododb`, usuario `sa`)
- Usuario: `admin` / `admin123` (configurables con `APP_USER` / `APP_PASSWORD`)

## Seguridad

| Sección  | Acceso                       | Endpoints                                  |
|----------|------------------------------|--------------------------------------------|
| Pública  | Sin autenticación (lectura)  | `GET /api/todos`, `GET /api/todos/{id}`, `POST /api/auth/login` |
| Privada  | JWT `Authorization: Bearer`  | `POST`, `PUT`, `PATCH`, `DELETE` sobre `/api/todos` |

1. `POST /api/auth/login` con `{"usuario": "admin", "password": "admin123"}` devuelve un JWT (HS256).
2. Enviar el token en `Authorization: Bearer <token>` para las operaciones de escritura.

Variables de entorno:

| Variable               | Por defecto              | Descripción                                 |
|------------------------|--------------------------|---------------------------------------------|
| `JWT_SECRET`           | secreto de desarrollo    | Clave HS256, mínimo 32 bytes                |
| `JWT_EXPIRACION`       | `1h`                     | Vigencia del token (`30m`, `2h`, …)         |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:5173`  | Orígenes permitidos, separados por comas    |

## Endpoints

| Método | Ruta                          | Descripción                         |
|--------|-------------------------------|-------------------------------------|
| POST   | `/api/auth/login`             | Obtener JWT                         |
| GET    | `/api/todos?finalizado=`      | Listar (filtro opcional por estado) |
| GET    | `/api/todos/{id}`             | Obtener por id                      |
| POST   | `/api/todos`                  | Crear                               |
| PUT    | `/api/todos/{id}`             | Actualizar                          |
| PATCH  | `/api/todos/{id}/finalizar`   | Marcar como finalizada              |
| PATCH  | `/api/todos/{id}/reabrir`     | Marcar como pendiente               |
| DELETE | `/api/todos/{id}`             | Eliminar                            |

```bash
TOKEN=$(curl -s -H 'Content-Type: application/json' \
     -d '{"usuario":"admin","password":"admin123"}' \
     http://localhost:8080/api/auth/login | jq -r .token)

curl -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
     -d '{"nombre":"Comprar pan","descripcion":"Integral"}' \
     http://localhost:8080/api/todos
```

## Tests

```bash
./mvnw test
```
