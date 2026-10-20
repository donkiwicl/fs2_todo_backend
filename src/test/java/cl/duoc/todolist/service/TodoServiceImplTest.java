package cl.duoc.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.todolist.dto.TodoRequest;
import cl.duoc.todolist.dto.TodoResponse;
import cl.duoc.todolist.exception.ResourceNotFoundException;
import cl.duoc.todolist.mapper.TodoMapper;
import cl.duoc.todolist.model.Todo;
import cl.duoc.todolist.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoServiceImpl(todoRepository, new TodoMapper());
    }

    @Test
    void crearGuardaLaTareaComoPendientePorDefecto() {
        when(todoRepository.save(any(Todo.class))).thenAnswer(inv -> {
            Todo todo = inv.getArgument(0);
            todo.setId(1L);
            return todo;
        });

        TodoResponse response = todoService.crear(new TodoRequest("Estudiar", "Spring Boot", null));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nombre()).isEqualTo("Estudiar");
        assertThat(response.finalizado()).isFalse();
    }

    @Test
    void listarFiltraPorEstadoCuandoSeIndica() {
        when(todoRepository.findByFinalizado(true))
                .thenReturn(List.of(new Todo(2L, "Hecha", null, true)));

        List<TodoResponse> resultado = todoService.listar(true);

        assertThat(resultado).extracting(TodoResponse::id).containsExactly(2L);
        verify(todoRepository, never()).findAll();
    }

    @Test
    void cambiarEstadoMarcaLaTareaComoFinalizada() {
        Todo todo = new Todo(1L, "Estudiar", null, false);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);

        TodoResponse response = todoService.cambiarEstado(1L, true);

        assertThat(response.finalizado()).isTrue();
    }

    @Test
    void obtenerPorIdLanzaExcepcionSiNoExiste() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void eliminarLanzaExcepcionSiNoExiste() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.eliminar(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(todoRepository, never()).delete(any());
    }
}
