package cl.duoc.todolist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Token de acceso emitido tras un login exitoso")
public record LoginResponse(
        @Schema(description = "JWT a enviar en el header Authorization: Bearer <token>") String token,
        @Schema(example = "Bearer") String tipo,
        @Schema(description = "Fecha de expiración del token") Instant expiraEn,
        @Schema(example = "admin") String usuario) {
}
