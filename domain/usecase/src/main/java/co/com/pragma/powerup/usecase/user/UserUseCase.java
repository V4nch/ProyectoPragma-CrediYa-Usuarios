package co.com.pragma.powerup.usecase.user;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.UserAlreadyExistsException;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserUseCase {
    private final TransactionalOperator txOperator;
    private final UserRepository userRepository;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private Mono<User> validate(User user) {
        return Mono.justOrEmpty(user)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El usuario no puede ser null")))
                .flatMap(this::validateRequiredFields)
                .flatMap(this::validateEmailFormat)
                .flatMap(this::validateSalaryRange);
    }

    private Mono<User> validateRequiredFields(User user) {
        if (isNullOrBlank(user.getName())) {
            return Mono.error(new IllegalArgumentException("El nombre es obligatorio"));
        }
        if (isNullOrBlank(user.getLastName())) {
            return Mono.error(new IllegalArgumentException("El apellido es obligatorio"));
        }
        if (isNullOrBlank(user.getEmailAddress())) {
            return Mono.error(new IllegalArgumentException("El email es obligatorio"));
        }
        if (user.getBaseSalary() == null) {
            return Mono.error(new IllegalArgumentException("El salario base es obligatorio"));
        }
        return Mono.just(user);
    }

    private Mono<User> validateEmailFormat(User user) {
        if (!EMAIL_PATTERN.matcher(user.getEmailAddress()).matches()) {
            return Mono.error(new IllegalArgumentException("El email no tiene un formato válido"));
        }
        return Mono.just(user);
    }

    private Mono<User> validateSalaryRange(User user) {
        try {
            double salary = Double.parseDouble(user.getBaseSalary());
            if (salary < 0 || salary > 15_000_000) {
                return Mono.error(new IllegalArgumentException(
                        "El salario base debe estar entre 0 y 15 millones"));
            }
        } catch (NumberFormatException e) {
            return Mono.error(new IllegalArgumentException("El salario base debe ser un valor numerico"));
        }
        return Mono.just(user);
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Mono<User> saveUser(User user){
    return userRepository.findByEmail(user.getEmailAddress())
            .flatMap(existing ->Mono.error(new UserAlreadyExistsException(user.getEmailAddress())))
            .switchIfEmpty(userRepository.save(user))
            .cast(User.class)
            .as(txOperator::transactional);
    }


}
