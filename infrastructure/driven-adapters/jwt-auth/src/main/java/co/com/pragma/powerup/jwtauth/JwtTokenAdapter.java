package co.com.pragma.powerup.jwtauth;



import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;


public class JwtTokenAdapter implements AuthRepository {
    private final SecretKey key;
    private final long defaultTtlSeconds;

    public JwtTokenAdapter(@Value(Constants.SECURITY_SECRET) String secret,
                           @Value(Constants.SECURITY_EXPIRATION) long defaultTtlSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.defaultTtlSeconds = defaultTtlSeconds;
    }

    @Override
    public Mono<String> generateToken(String subject, Map<String, Object> claims, Duration ttl) {
        long expMillis = System.currentTimeMillis() + (ttl != null ? ttl.toMillis() : defaultTtlSeconds * 1000);
        String jwt = Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return Mono.just(jwt);
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return Mono.just(true);
        } catch (JwtException | IllegalArgumentException ex) {
            return Mono.just(false);
        }
    }

    @Override
    public Mono<String> getSubject(String token) {
        try {
            var claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            return Mono.just(claims.getSubject());
        } catch (JwtException | IllegalArgumentException ex) {
            return Mono.empty();
        }
    }

    @Override
    public Mono<Claims> getClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Mono.just(claims);
        } catch (JwtException | IllegalArgumentException ex) {
            return Mono.empty();
        }
    }

}
