package co.com.pragma.powerup.model.user.exceptions;

import co.com.pragma.powerup.model.user.utils.Constants;

public class IdCardUserAlreadyExistsException extends RuntimeException {
    public IdCardUserAlreadyExistsException(String idCard) {

        super(Constants.GIVEN_ID_CARD_ALREADY_EXIST + idCard);
    }
}
