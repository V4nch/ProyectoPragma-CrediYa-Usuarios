package co.com.pragma.powerup.jwtauth.config;
import co.com.pragma.powerup.jwtauth.JwtTokenAdapter;
import co.com.pragma.powerup.jwtauth.SpringAuthManagerAdapter;
import co.com.pragma.powerup.model.auth.gateways.AuthManager;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;

@Configuration
public class JwtAuthConfig {
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    @Bean
    public AuthRepository jwtTokenAdapter(
            @Value(Constants.SECURITY_SECRET) String secret,
            @Value(Constants.SECURITY_EXPIRATION) long expiration) {

        return new JwtTokenAdapter(secret, expiration);
    }

    public JwtAuthConfig(ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }
    @Bean
    public AuthManager authManager() {
        return new SpringAuthManagerAdapter(reactiveAuthenticationManager);
    }
}
