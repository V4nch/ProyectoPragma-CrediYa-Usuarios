package co.com.pragma.powerup.model.auth.gateways;

import co.com.pragma.powerup.model.auth.TokenClaims;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

public interface AuthRepository {
    Mono<String> generateToken(String subject, Map<String, Object> claims, Duration ttl);
    Mono<Boolean> validateToken(String token);
    Mono<TokenClaims> getClaims(String token);
}
