package cl.duoc.todolist.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secret, Duration expiracion, String issuer) {

    private static final int LARGO_MINIMO_BYTES = 32;

    public JwtProperties {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < LARGO_MINIMO_BYTES) {
            throw new IllegalStateException("app.jwt.secret debe tener al menos 32 bytes para HS256");
        }
    }
}
