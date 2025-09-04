package co.com.pragma.powerup.usecase.login;

import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.AuthResponse;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
public class LoginUseCase {
    private final ReactiveAuthenticationManager authManager;
    private final AuthRepository authRepository;

    public Mono<AuthResponse> execute(Auth request) {
        var authToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());

        return authManager.authenticate(authToken)
                .flatMap(auth -> authRepository.generateToken(
                        auth.getName(), Map.of("roles", auth.getAuthorities()), Duration.ofHours(1)))
                .map(AuthResponse::new);
    }
}
