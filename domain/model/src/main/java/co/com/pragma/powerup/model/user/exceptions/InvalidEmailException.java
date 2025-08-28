package co.com.pragma.powerup.model.user.exceptions;

import co.com.pragma.powerup.model.user.utils.Constants;

public class InvalidEmailException extends BusinessException {
    public InvalidEmailException(String invalidFormat) {
        super(invalidFormat);
    }
}
