package co.com.pragma.powerup.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthResponse {
    private String token;
}
