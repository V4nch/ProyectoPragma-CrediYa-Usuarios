package co.com.pragma.powerup.model.user.exceptions;

public class InvalidUserException extends BusinessException{
    public InvalidUserException(String userNull) {
        super(userNull);
    }
}
