package co.com.pragma.powerup.model.auth.gateways;

import co.com.pragma.powerup.model.auth.AuthUser;
import co.com.pragma.powerup.model.user.exceptions.InvalidCredentialsException;
import reactor.core.publisher.Mono;

public interface AuthManager {
    Mono<AuthUser> authenticate(String email, String password) throws InvalidCredentialsException;
}
