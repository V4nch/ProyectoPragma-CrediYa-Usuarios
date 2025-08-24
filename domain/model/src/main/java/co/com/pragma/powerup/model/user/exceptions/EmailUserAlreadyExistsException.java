package co.com.pragma.powerup.model.user.exceptions;

public class EmailUserAlreadyExistsException extends RuntimeException {
    public EmailUserAlreadyExistsException(String email) {
        super("Ya existe un usuario registrado con el correo: " + email);
    }
}
