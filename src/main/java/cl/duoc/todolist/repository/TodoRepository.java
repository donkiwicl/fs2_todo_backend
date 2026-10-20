package cl.duoc.todolist.repository;

import cl.duoc.todolist.model.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByFinalizado(boolean finalizado);
}
