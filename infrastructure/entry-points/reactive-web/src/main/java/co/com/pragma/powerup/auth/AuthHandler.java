package co.com.pragma.powerup.auth;

import co.com.pragma.powerup.api.exception.ErrorResponse;
import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.AuthResponse;
import co.com.pragma.powerup.model.user.utils.Constants;
import co.com.pragma.powerup.usecase.login.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final LoginUseCase loginUseCase;
    private static final Logger log = LoggerFactory.getLogger(AuthHandler.class);
    @Operation(
            summary = Constants.SUMMARY_LOGIN_USER,
            description =Constants.DESCRIPTION_LOGIN_USER,
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Auth.class),
                            examples = {
                                    @ExampleObject(
                                            name =Constants.EXAMPLE_LOGIN_NAME,
                                            value =Constants.EXAMPLE_LOGIN_VALUE
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = Constants.CODE_200,
                            description = Constants.RESPONSE_USER_LOGGED,
                            content = @Content(
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name =Constants.EXAMPLE_LOGIN_RESPONSE_NAME,
                                                    value =Constants.EXAMPLE_LOGIN_RESPONSE_VALUE
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = Constants.CODE_401,
                            description =Constants.RESPONSE_INVALID_CREDENTIALS,
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name =Constants.EXAMPLE_INVALID_CREDENTIALS_NAME,
                                                    value =Constants.EXAMPLE_INVALID_CREDENTIALS
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = Constants.CODE_500,
                            description =Constants.RESPONSE_INTERNAL_ERROR,
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name =Constants.EXAMPLE_SERVER_ERROR_NAME,
                                                    value =Constants.EXAMPLE_SERVER_ERROR_VALUE
                                            )
                                    }
                            )
                    )
            }
    )
    public Mono<ServerResponse> login(ServerRequest request) {
        log.info(Constants.LOG_LOGIN_REQUEST);

        return request.bodyToMono(Auth.class)
                .flatMap(auth -> {
                    log.info(Constants.LOG_LOGIN_PROCESSING, auth.getEmail());
                    return loginUseCase.execute(auth);
                })
                .flatMap(res -> {
                    log.info(Constants.LOG_LOGIN_SUCCESS);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(res);
                })
                .doOnError(error -> log.error(Constants.LOG_LOGIN_ERROR, error.getMessage(), error));
    }
}
