package cl.duoc.todolist.service;

import cl.duoc.todolist.dto.LoginRequest;
import cl.duoc.todolist.dto.LoginResponse;
import cl.duoc.todolist.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.usuario(), request.password()));
        Jwt jwt = jwtService.generarToken(authentication);
        return new LoginResponse(jwt.getTokenValue(), "Bearer", jwt.getExpiresAt(), authentication.getName());
    }
}
