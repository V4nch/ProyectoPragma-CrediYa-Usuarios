package co.com.pragma.powerup.model.user.exceptions;


public class InvalidEmailException extends BusinessException {
    public InvalidEmailException(String invalidFormat) {
        super(invalidFormat);
    }
}
