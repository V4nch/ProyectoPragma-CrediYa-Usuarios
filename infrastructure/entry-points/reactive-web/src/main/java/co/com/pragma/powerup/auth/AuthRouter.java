package co.com.pragma.powerup.auth;

import co.com.pragma.powerup.model.user.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {
    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler handler) {
        return route(POST(Constants.PATH_LOGIN), handler::login);
    }
}
