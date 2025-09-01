package co.com.pragma.powerup.usecase.user;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.*;
import co.com.pragma.powerup.model.user.gateways.TransactionGateway;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final TransactionGateway transactionGateway;

    public Mono<User> saveUser(User user) {
        return this.validate(user)
            .flatMap(u -> transactionGateway.doInTransaction(
                userRepository.findByEmail(u.getEmailAddress())
                .flatMap(existing -> Mono.error(new EmailUserAlreadyExistsException(u.getEmailAddress())))
                .switchIfEmpty(userRepository.save(u))
                .cast(User.class)
            ))
            .doOnSuccess(savedUser ->
                log.info(Constants.LOG_USER_CREATE_SUCCESSFUL, savedUser.getEmailAddress()))
            .doOnError(error ->
                log.error(Constants.LOG_USER_CREATE_ERROR, user != null ? user.getEmailAddress() : Constants.NULL,
                    error.getMessage()));
    }

    private Mono<User> validate(User user) {
        log.info(Constants.LOG_VALIDATE_USER);
        return Mono.justOrEmpty(user)
                .switchIfEmpty(Mono.error(new InvalidUserException(Constants.USER_NULL)))
                .flatMap(u -> this.requireNonNullOrBlank(u.getName(),Constants.NAME_REQUIRED, u))
                .flatMap(u -> this.requireNonNullOrBlank(u.getLastName(),Constants.LASTNAME_REQUIRED, u))
                .flatMap(u -> this.requireNonNullOrBlank(u.getEmailAddress(),Constants.EMAIL_REQUIRED, u))
                .flatMap(u -> this.requireNonNullOrBlank(u.getBaseSalary(),Constants.BASE_SALARY_REQUIRED, u))
                .flatMap(this::validateEmailFormat)
                .flatMap(this::validateSalaryRange);
    }

    private  Mono<User> requireNonNullOrBlank(String value, String message,User user) {
        return isNullOrBlank(value)
                ? Mono.error(new MissingFieldException(message))
                : Mono.just(user);
    }

    private Mono<User> validateEmailFormat(User user) {
        return (!Constants.EMAIL_PATTERN.matcher(user.getEmailAddress()).matches())
            ? Mono.error(new InvalidEmailException(Constants.INVALID_FORMAT))
            : Mono.just(user);
    }

    private Mono<User> validateSalaryRange(User user) {
        try {
            double salary = Double.parseDouble(user.getBaseSalary());
            if (salary < 0 || salary > 15000000) {
                return Mono.error(new InvalidSalaryException(Constants.SALARY_RANGE));
            }
        } catch (NumberFormatException e) {
            return Mono.error(new InvalidSalaryException(Constants.SALARY_IS_NUMERIC));
        }
        return Mono.just(user);
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }



}
