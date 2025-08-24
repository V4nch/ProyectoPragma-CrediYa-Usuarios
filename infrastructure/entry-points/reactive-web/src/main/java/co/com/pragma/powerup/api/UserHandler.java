package co.com.pragma.powerup.api;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserHandler {
    private final UserUseCase createUserUseCase;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        log.info("➡️ Iniciando proceso de creación de usuario...");

        return request.bodyToMono(User.class)
                .doOnNext(user -> log.debug("Payload recibido: {}", user))
                .flatMap(createUserUseCase::saveUser)
                .doOnSuccess(user -> log.info("✅ Usuario creado con email={}", user.getEmailAddress()))
                .doOnError(error -> log.error("❌ Error en el proceso de creación de usuario: {}", error.getMessage(), error))
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .onErrorResume(error -> {
                    if (error instanceof IllegalArgumentException) {
                        return ServerResponse.badRequest()
                                .bodyValue(new ErrorResponse("BAD_REQUEST", error.getMessage()));
                    }
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(new ErrorResponse("INTERNAL_ERROR", "Ha ocurrido un error inesperado"));
                });
    }
}
