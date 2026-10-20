package cl.duoc.todolist.service;

import cl.duoc.todolist.dto.TodoRequest;
import cl.duoc.todolist.dto.TodoResponse;
import cl.duoc.todolist.exception.ResourceNotFoundException;
import cl.duoc.todolist.mapper.TodoMapper;
import cl.duoc.todolist.model.Todo;
import cl.duoc.todolist.repository.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> listar(Boolean finalizado) {
        List<Todo> todos = finalizado == null
                ? todoRepository.findAll()
                : todoRepository.findByFinalizado(finalizado);
        return todos.stream().map(todoMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TodoResponse obtenerPorId(Long id) {
        return todoMapper.toResponse(buscar(id));
    }

    @Override
    public TodoResponse crear(TodoRequest request) {
        Todo guardado = todoRepository.save(todoMapper.toEntity(request));
        return todoMapper.toResponse(guardado);
    }

    @Override
    public TodoResponse actualizar(Long id, TodoRequest request) {
        Todo todo = buscar(id);
        todoMapper.updateEntity(todo, request);
        return todoMapper.toResponse(todoRepository.save(todo));
    }

    @Override
    public TodoResponse cambiarEstado(Long id, boolean finalizado) {
        Todo todo = buscar(id);
        todo.setFinalizado(finalizado);
        return todoMapper.toResponse(todoRepository.save(todo));
    }

    @Override
    public void eliminar(Long id) {
        todoRepository.delete(buscar(id));
    }

    private Todo buscar(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la tarea con id " + id));
    }
}
