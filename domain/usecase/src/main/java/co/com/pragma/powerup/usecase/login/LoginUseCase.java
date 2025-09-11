package co.com.pragma.powerup.usecase.login;

import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.AuthResponse;
import co.com.pragma.powerup.model.auth.gateways.AuthManager;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.exceptions.InvalidCredentialsException;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;


@RequiredArgsConstructor
public class LoginUseCase {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final AuthManager authManager;

    public Mono<AuthResponse> execute(Auth request) {

        return authManager.authenticate(request.getEmail(), request.getPassword())

                .flatMap(authUser ->
                        userRepository.findByEmail(authUser.getEmail())
                                .switchIfEmpty(Mono.error(new InvalidCredentialsException(Constants.USER_NOT_FOUND + authUser.getEmail())))
                                .flatMap(user -> {
                                    Map<String, Object> claims = Map.of(
                                            Constants.ROLES, authUser.getRoles(),
                                            "idCard", user.getIdCard()
                                    );

                                    return authRepository.generateToken(
                                            authUser.getEmail(),
                                            claims,
                                            Duration.ofHours(1)
                                    );
                                })
                )
                .map(AuthResponse::new);
    }
}
