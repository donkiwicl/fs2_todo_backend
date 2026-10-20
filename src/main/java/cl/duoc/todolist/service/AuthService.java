package cl.duoc.todolist.service;

import cl.duoc.todolist.dto.LoginRequest;
import cl.duoc.todolist.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
