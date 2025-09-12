package co.com.pragma.powerup.jwtauth;

import co.com.pragma.powerup.model.auth.AuthUser;
import co.com.pragma.powerup.model.auth.gateways.AuthManager;
import co.com.pragma.powerup.model.user.exceptions.InvalidCredentialsException;
import co.com.pragma.powerup.model.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SpringAuthManagerAdapter implements AuthManager {
    private final ReactiveAuthenticationManager delegate;

    @Override
    public Mono<AuthUser> authenticate(String email, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(email, password);
        return delegate.authenticate(authToken)
                .map(auth -> new AuthUser(
                        auth.getName(),
                        auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                ))
                .onErrorMap(ex -> new InvalidCredentialsException(Constants.EXC_INVALID_CREDENTIALS));
    }
}
