package co.com.pragma.powerup.auth;


import co.com.pragma.powerup.model.user.utils.Constants;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = Constants.PATH_LOGIN,
                    produces = { Constants.CONTENT_TYPE },
                    consumes = { Constants.CONTENT_TYPE },
                    beanClass = AuthHandler.class,
                    beanMethod = Constants.NAME_FUNCTION_LOGIN
            )
    })
    public RouterFunction<ServerResponse> authRoutes(AuthHandler handler) {
        return route(POST(Constants.PATH_LOGIN), handler::login);
    }
}
