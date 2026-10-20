package cl.duoc.todolist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar una tarea")
public record TodoRequest(

        @Schema(description = "Nombre de la tarea", example = "Comprar pan", maxLength = 30)
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 30, message = "El nombre no puede superar los 30 caracteres")
        String nombre,

        @Schema(description = "Descripción de la tarea", example = "Pan integral en la panadería de la esquina", maxLength = 256)
        @Size(max = 256, message = "La descripción no puede superar los 256 caracteres")
        String descripcion,

        @Schema(description = "Indica si la tarea está finalizada", example = "false", defaultValue = "false")
        Boolean finalizado) {
}
