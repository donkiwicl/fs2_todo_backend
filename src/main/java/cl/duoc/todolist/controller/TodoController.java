package cl.duoc.todolist.controller;

import cl.duoc.todolist.dto.TodoRequest;
import cl.duoc.todolist.dto.TodoResponse;
import cl.duoc.todolist.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "TODO", description = "Gestión de tareas")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    @Operation(summary = "Listar tareas", description = "Permite filtrar opcionalmente por estado de finalización")
    public List<TodoResponse> listar(
            @Parameter(description = "Filtrar por estado finalizado") @RequestParam(required = false) Boolean finalizado) {
        return todoService.listar(finalizado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tarea por id")
    @ApiResponse(responseCode = "200", description = "Tarea encontrada")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public TodoResponse obtener(@PathVariable Long id) {
        return todoService.obtenerPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crear una tarea")
    @ApiResponse(responseCode = "201", description = "Tarea creada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<TodoResponse> crear(@Valid @RequestBody TodoRequest request) {
        TodoResponse creado = todoService.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea")
    @ApiResponse(responseCode = "200", description = "Tarea actualizada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public TodoResponse actualizar(@PathVariable Long id, @Valid @RequestBody TodoRequest request) {
        return todoService.actualizar(id, request);
    }

    @PatchMapping("/{id}/finalizar")
    @Operation(summary = "Marcar una tarea como finalizada")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public TodoResponse finalizar(@PathVariable Long id) {
        return todoService.cambiarEstado(id, true);
    }

    @PatchMapping("/{id}/reabrir")
    @Operation(summary = "Marcar una tarea como pendiente")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public TodoResponse reabrir(@PathVariable Long id) {
        return todoService.cambiarEstado(id, false);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea")
    @ApiResponse(responseCode = "204", description = "Tarea eliminada")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        todoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
