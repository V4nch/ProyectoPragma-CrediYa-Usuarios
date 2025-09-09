package co.com.pragma.powerup.security;

import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final AuthRepository tokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        return tokenProvider.validateToken(token)
                .filter(Boolean::booleanValue)
                .flatMap(valid -> tokenProvider.getClaims(token))
                .map(claims -> {
                    String subject = claims.getSubject();


                    List<String> roles = claims.get(Constants.ROLES, List.class);

                    Collection<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(Constants.ROLE_1 + role.toUpperCase()))
                            .toList();

                    AbstractAuthenticationToken auth = new AbstractAuthenticationToken(authorities) {
                        @Override
                        public Object getCredentials() {
                            return token;
                        }

                        @Override
                        public Object getPrincipal() {
                            return subject;
                        }
                    };

                    auth.setAuthenticated(true);
                    return auth;
                });
    }
}
