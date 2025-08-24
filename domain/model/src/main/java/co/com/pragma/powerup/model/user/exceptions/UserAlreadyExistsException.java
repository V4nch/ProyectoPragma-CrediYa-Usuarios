package co.com.pragma.powerup.model.user.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Ya existe un usuario registrado con el correo: " + email);
    }
}
