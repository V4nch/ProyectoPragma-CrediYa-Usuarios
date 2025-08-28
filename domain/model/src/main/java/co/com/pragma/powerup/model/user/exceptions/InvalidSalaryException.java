package co.com.pragma.powerup.model.user.exceptions;

public class InvalidSalaryException extends BusinessException {
    public InvalidSalaryException(String salaryIsNumeric) {
        super(salaryIsNumeric);
    }
}
