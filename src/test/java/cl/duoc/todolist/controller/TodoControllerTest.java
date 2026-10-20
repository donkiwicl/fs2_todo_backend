package cl.duoc.todolist.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.todolist.model.Todo;
import cl.duoc.todolist.repository.TodoRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    private static final String URL = "/api/todos";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    private String bearer;

    @BeforeEach
    void setUp() throws Exception {
        todoRepository.deleteAll();
        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario": "admin", "password": "admin123"}
                                """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        bearer = "Bearer " + JsonPath.read(body, "$.token");
    }

    @Test
    void lecturaEsPublica() throws Exception {
        Todo todo = todoRepository.save(new Todo(null, "Pública", null, false));

        mockMvc.perform(get(URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get(URL + "/" + todo.getId())).andExpect(status().isOk());
    }

    @Test
    void escrituraSinTokenEsRechazada() throws Exception {
        Todo todo = todoRepository.save(new Todo(null, "Protegida", null, false));
        String json = """
                {"nombre": "Intento"}
                """;

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(URL + "/" + todo.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(patch(URL + "/" + todo.getId() + "/finalizar")).andExpect(status().isUnauthorized());
        mockMvc.perform(delete(URL + "/" + todo.getId())).andExpect(status().isUnauthorized());
    }

    @Test
    void escrituraConTokenInvalidoEsRechazada() throws Exception {
        mockMvc.perform(post(URL).header(HttpHeaders.AUTHORIZATION, "Bearer token.invalido.123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "Intento"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cicloCompletoCrudConToken() throws Exception {
        String location = mockMvc.perform(post(URL).header(HttpHeaders.AUTHORIZATION, bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "Estudiar", "descripcion": "Patrón CSR"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nombre").value("Estudiar"))
                .andExpect(jsonPath("$.finalizado").value(false))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Patrón CSR"));

        mockMvc.perform(put(location).header(HttpHeaders.AUTHORIZATION, bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "Estudiar mucho", "descripcion": "Patrón CSR", "finalizado": false}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Estudiar mucho"));

        mockMvc.perform(patch(location + "/finalizar").header(HttpHeaders.AUTHORIZATION, bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalizado").value(true));

        mockMvc.perform(get(URL).param("finalizado", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete(location).header(HttpHeaders.AUTHORIZATION, bearer))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    void validaLongitudDelNombre() throws Exception {
        mockMvc.perform(post(URL).header(HttpHeaders.AUTHORIZATION, bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "Un nombre que supera claramente los treinta caracteres"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombre").exists());
    }

    @Test
    void documentacionOpenApiEsPublica() throws Exception {
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }
}
