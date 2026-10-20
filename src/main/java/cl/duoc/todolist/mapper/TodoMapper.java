package cl.duoc.todolist.mapper;

import cl.duoc.todolist.dto.TodoRequest;
import cl.duoc.todolist.dto.TodoResponse;
import cl.duoc.todolist.model.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

    public Todo toEntity(TodoRequest request) {
        return Todo.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .finalizado(Boolean.TRUE.equals(request.finalizado()))
                .build();
    }

    public void updateEntity(Todo todo, TodoRequest request) {
        todo.setNombre(request.nombre());
        todo.setDescripcion(request.descripcion());
        todo.setFinalizado(Boolean.TRUE.equals(request.finalizado()));
    }

    public TodoResponse toResponse(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getNombre(), todo.getDescripcion(), todo.isFinalizado());
    }
}
