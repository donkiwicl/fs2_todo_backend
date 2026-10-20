package cl.duoc.todolist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tarea registrada")
public record TodoResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Comprar pan") String nombre,
        @Schema(example = "Pan integral en la panadería de la esquina") String descripcion,
        @Schema(example = "false") boolean finalizado) {
}
