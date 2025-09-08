package co.com.pragma.powerup.auth;

import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final LoginUseCase loginUseCase;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(Auth.class)
                .flatMap(loginUseCase::execute)
                .flatMap(res -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res));
    }
}
