package co.com.pragma.powerup.api;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.utils.Constants;
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
        log.info(Constants.LOG_USER_CREATE_RECEIVED);

        return request.bodyToMono(User.class)
                .doOnNext(user -> log.debug(Constants.LOG_RECEIVED_DATA, user))
                .flatMap(createUserUseCase::saveUser)
                .doOnSuccess(user -> log.info(Constants.LOG_USER_CREATED, user.getEmailAddress()))
                .doOnError(error -> log.error(Constants.LOG_USER_CREATION_ERROR, error.getMessage()))
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

}
