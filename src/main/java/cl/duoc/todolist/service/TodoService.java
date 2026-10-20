package cl.duoc.todolist.service;

import cl.duoc.todolist.dto.TodoRequest;
import cl.duoc.todolist.dto.TodoResponse;
import java.util.List;

public interface TodoService {

    List<TodoResponse> listar(Boolean finalizado);

    TodoResponse obtenerPorId(Long id);

    TodoResponse crear(TodoRequest request);

    TodoResponse actualizar(Long id, TodoRequest request);

    TodoResponse cambiarEstado(Long id, boolean finalizado);

    void eliminar(Long id);
}
