package co.com.pragma.powerup.usecase.login;

import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.AuthResponse;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
public class LoginUseCase {
    private final ReactiveAuthenticationManager authManager;
    private final AuthRepository authRepository;

    public Mono<AuthResponse> execute(Auth request) {
        var authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        return authManager.authenticate(authToken)
                .flatMap(auth -> {
                    String username = auth.getName();
                    var roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.replace(Constants.ROLE_1, ""))
                    .toList();


                    return authRepository.generateToken(
                            username,
                            Map.of(Constants.ROLES, roles),
                            Duration.ofHours(1)
                    );
                })
                .map(AuthResponse::new);
    }
}
