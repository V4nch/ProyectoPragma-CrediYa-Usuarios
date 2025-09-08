package co.com.pragma.powerup.api.exception;

import co.com.pragma.powerup.model.user.exceptions.*;
import co.com.pragma.powerup.model.user.utils.Constants;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.warn(Constants.LOG_USER_NOT_FOUND, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(Constants.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(EmailUserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailUserAlreadyExistsException ex) {
        log.warn(Constants.LOG_EMAIL_ALREADY_EXIST, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(Constants.EMAIL_ALREADY_EXISTS, ex.getMessage()));
    }
    @ExceptionHandler(IdCardUserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleIdCardAlreadyExists(IdCardUserAlreadyExistsException ex) {
        log.warn(Constants.LOG_ID_CARD_ALREADY_EXIST, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(Constants.ID_CARD_ALREADY_EXISTS, ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn(Constants.LOG_BUSINESS_ERROR, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(Constants.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(R2dbcDataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleR2dbcIntegrity(R2dbcDataIntegrityViolationException ex) {
        log.error(Constants.LOG_DB_INTEGRITY_ERROR, ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(Constants.DATABASE_ERROR, Constants.DB_VIOLATION_MESSAGE));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex) {
        log.error(Constants.LOG_DATA_ACCESS_ERROR, ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(Constants.DATABASE_ERROR, Constants.DB_ACCESS_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error(Constants.LOG_UNEXPECTED_ERROR, ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(Constants.INTERNAL_ERROR, Constants.UNEXPECTED_ERROR));
    }
}
