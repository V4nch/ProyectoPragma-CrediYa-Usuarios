package co.com.pragma.powerup.model.user.exceptions;

import co.com.pragma.powerup.model.user.utils.Constants;

public class EmailUserAlreadyExistsException extends RuntimeException {
    public EmailUserAlreadyExistsException(String email) {
        super(Constants.GIVEN_EMAIL_ALREADY_EXIST + email);
    }
}
