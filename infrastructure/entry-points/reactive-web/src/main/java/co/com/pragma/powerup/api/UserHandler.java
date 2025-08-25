package co.com.pragma.powerup.api;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserHandler {
    private final UserUseCase createUserUseCase;


    public Mono<ServerResponse> createUser(ServerRequest request) {
        log.info("Petición recibida para crear usuario");

        return request.bodyToMono(User.class)
                .doOnNext(user -> log.debug("Datos recibidos: {}", user))
                .flatMap(createUserUseCase::saveUser)
                .doOnSuccess(user -> log.info("Usuario creado: {}", user.getEmailAddress()))
                .doOnError(error -> log.error("Error al crear usuario: {}", error.getMessage()))
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

}
