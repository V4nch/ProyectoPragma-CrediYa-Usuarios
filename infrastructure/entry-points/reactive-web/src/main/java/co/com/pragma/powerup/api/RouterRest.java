package co.com.pragma.powerup.api;

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
public class RouterRest {
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = Constants.PATH_USER,
            produces = { Constants.CONTENT_TYPE },
            consumes = { Constants.CONTENT_TYPE },
            beanClass = UserHandler.class,
            beanMethod = Constants.NAME_FUNCTION
        )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST(Constants.PATH_USER), handler::createUser);

    }
}
