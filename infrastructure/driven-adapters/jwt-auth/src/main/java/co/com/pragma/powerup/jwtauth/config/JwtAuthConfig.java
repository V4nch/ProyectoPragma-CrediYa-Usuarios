package co.com.pragma.powerup.jwtauth.config;

import co.com.pragma.powerup.jwtauth.JwtTokenAdapter;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtAuthConfig {
    @Bean
    public AuthRepository authRepository(JwtTokenAdapter provider) {
        return provider;
    }
}
