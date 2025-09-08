package co.com.pragma.powerup.security;

import co.com.pragma.powerup.model.user.utils.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class BearerServerAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(Constants.BEARER_SPACE)) {
            String token = authHeader.substring(7);

            Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);
            return Mono.just(authentication);
        }

        return Mono.empty();
    }
}
