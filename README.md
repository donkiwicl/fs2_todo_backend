# TODO List — Spring Boot (patrón CSR)

API REST para gestionar tareas, organizada en capas **Controller → Service → Repository**.

```
src/main/java/cl/duoc/todolist
├── controller/   TodoController        (endpoints REST)
├── service/      TodoService / Impl    (lógica de negocio)
├── repository/   TodoRepository        (Spring Data JPA)
├── model/        Todo                  (entidad, tabla TODO)
├── dto/          TodoRequest / TodoResponse
├── mapper/       TodoMapper
├── exception/    manejo global de errores (ProblemDetail)
└── config/       Spring Security + OpenAPI
```

## Ejecutar

```bash
./mvnw spring-boot:run                                  # H2 en memoria (perfil por defecto)
SPRING_PROFILES_ACTIVE=mysql ./mvnw spring-boot:run     # MySQL (DB_URL, DB_USER, DB_PASSWORD)
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- Consola H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:tododb`, usuario `sa`)
- Credenciales HTTP Basic: `admin` / `admin123` (configurables con `APP_USER` / `APP_PASSWORD`)

## Endpoints

| Método | Ruta                          | Descripción                         |
|--------|-------------------------------|-------------------------------------|
| GET    | `/api/todos?finalizado=`      | Listar (filtro opcional por estado) |
| GET    | `/api/todos/{id}`             | Obtener por id                      |
| POST   | `/api/todos`                  | Crear                               |
| PUT    | `/api/todos/{id}`             | Actualizar                          |
| PATCH  | `/api/todos/{id}/finalizar`   | Marcar como finalizada              |
| PATCH  | `/api/todos/{id}/reabrir`     | Marcar como pendiente               |
| DELETE | `/api/todos/{id}`             | Eliminar                            |

```bash
curl -u admin:admin123 -H 'Content-Type: application/json' \
     -d '{"nombre":"Comprar pan","descripcion":"Integral"}' \
     http://localhost:8080/api/todos
```

## Tests

```bash
./mvnw test
```
