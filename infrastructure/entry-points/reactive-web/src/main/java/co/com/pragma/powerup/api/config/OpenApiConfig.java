package co.com.pragma.powerup.api.config;

import co.com.pragma.powerup.model.user.utils.Constants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(Constants.API_CREDIYA)
                        .version(Constants.VERSION_1)
                        .description(Constants.USER_DESCRIPTION));
    }
}

