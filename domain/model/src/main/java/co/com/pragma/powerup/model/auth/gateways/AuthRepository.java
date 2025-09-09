package co.com.pragma.powerup.model.auth.gateways;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

public interface AuthRepository {
    Mono<String> generateToken(String subject, Map<String, Object> claims, Duration ttl);
    Mono<Boolean> validateToken(String token);
    Mono<String> getSubject(String token);
    Mono<Claims> getClaims(String token);
}
