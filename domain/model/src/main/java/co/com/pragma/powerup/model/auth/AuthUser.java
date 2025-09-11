package co.com.pragma.powerup.model.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AuthUser {
    private final String email;
    private final List<String> roles;
}
