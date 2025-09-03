package co.com.pragma.powerup.api;

import co.com.pragma.powerup.api.exception.ErrorResponse;
import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.utils.Constants;
import co.com.pragma.powerup.usecase.user.UserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
        summary =Constants.SUMMARY_REGISTER_USER,
        description =Constants.DESCRIPTION_REGISTER_USER,
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = User.class),
                examples = {
                    @ExampleObject(
                        name =Constants.EXAMPLE_USER_REGISTERED_NAME,
                        value =Constants.EXAMPLE_USER_REGISTERED_VALUE
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = Constants.CODE_200,
                description = Constants.RESPONSE_USER_REGISTERED,
                content = @Content(
                    schema = @Schema(implementation = User.class),
                    examples = {
                        @ExampleObject(
                            name =Constants.EXAMPLE_USER_REGISTERED_NAME,
                            value =Constants.EXAMPLE_USER_REGISTERED_VALUE
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = Constants.CODE_400,
                description =Constants.RESPONSE_BAD_REQUEST,
                content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name =Constants.EXAMPLE_INVALID_SALARY_NAME,
                            value =Constants.EXAMPLE_INVALID_SALARY_VALUE
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = Constants.CODE_409,
                description =Constants.RESPONSE_CONFLICT,
                content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name =Constants.EXAMPLE_DUPLICATE_EMAIL_NAME,
                            value =Constants.EXAMPLE_DUPLICATE_EMAIL_VALUE
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
