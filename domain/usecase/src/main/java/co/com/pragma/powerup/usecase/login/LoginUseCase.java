package co.com.pragma.powerup.usecase.login;

import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.AuthResponse;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.exceptions.InvalidCredentialsException;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class LoginUseCase {
    private final ReactiveAuthenticationManager authManager;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    public Mono<AuthResponse> execute(Auth request) {
        log.info(Constants.LOG_LOGIN_ATTEMPT, request.getEmail());

        var authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        return authManager.authenticate(authToken)
                .doOnSubscribe(sub -> log.debug(Constants.LOG_AUTHENTICATING, request.getEmail()))
                .doOnError(error -> log.error(Constants.LOG_AUTH_FAILED, request.getEmail(), error.getMessage()))
                .onErrorMap(ex -> new InvalidCredentialsException(Constants.EXC_INVALID_CREDENTIALS))
                .flatMap(auth -> {
                    String email = auth.getName();
                    var roles = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(role -> role.replace(Constants.ROLE_1, ""))
                            .toList();

                    log.info(Constants.LOG_AUTH_SUCCESS, email, roles);

                    return userRepository.findByEmail(email)
                            .switchIfEmpty(Mono.error(new InvalidCredentialsException(Constants.EXC_USER_NOT_FOUND + email)))
                            .flatMap(user -> {
                                log.debug(Constants.LOG_BUILDING_CLAIMS, user.getIdCard());

                                Map<String, Object> claims = Map.of(
                                        Constants.ROLES, roles,
                                        "idCard", user.getIdCard()
                                );

                                return authRepository.generateToken(
                                        email,
                                        claims,
                                        Duration.ofHours(1)
                                ).doOnSuccess(token -> log.info(Constants.LOG_TOKEN_GENERATED, email));
                            });
                })
                .map(AuthResponse::new)
                .doOnSuccess(authResponse -> log.info(Constants.LOG_LOGIN_COMPLETED, request.getEmail()));
    }
}
