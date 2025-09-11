package co.com.pragma.powerup.model.auth;

import java.util.Map;

public record TokenClaims(String subject, Map<String, Object> claims) {


}
