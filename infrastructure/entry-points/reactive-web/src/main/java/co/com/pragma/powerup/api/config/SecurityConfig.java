package co.com.pragma.powerup.api.config;

import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import co.com.pragma.powerup.security.BearerServerAuthenticationConverter;
import co.com.pragma.powerup.security.JwtReactiveAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthRepository tokenProvider;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter jwtFilter =
                new AuthenticationWebFilter(new JwtReactiveAuthenticationManager(tokenProvider));
        jwtFilter.setServerAuthenticationConverter(new BearerServerAuthenticationConverter());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(HttpMethod.POST, Constants.PATH_LOGIN).permitAll()
                        .pathMatchers(
                                Constants.SWAGGER_UI_HTML,
                                Constants.SWAGGER_UI_ALL,
                                Constants.V3_API_DOCS,
                                Constants.WEBJARS
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST, Constants.PATH_USER).hasAnyRole(Constants.ROLE_ADMIN,Constants.ROLE_ADVISOR)
                        .pathMatchers(HttpMethod.GET, Constants.PATH_USER+Constants.ID_PARAMS).hasRole(Constants.ROLE_ADMIN)
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
