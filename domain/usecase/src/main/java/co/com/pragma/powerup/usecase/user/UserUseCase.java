package co.com.pragma.powerup.usecase.user;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.EmailUserAlreadyExistsException;
import co.com.pragma.powerup.model.user.gateways.TransactionGateway;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import lombok.extern.log4j.Log4j2;


import java.util.regex.Pattern;
@Log4j2
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private final TransactionGateway transactionGateway;


    private Mono<User> validate(User user) {
        log.info("Validacion de los atributos del usuario" );
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
            if (salary < 0 || salary > 15000000) {
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

    public Mono<User> saveUser(User user){

        return transactionGateway.doInTransaction(this.validate(user)
                .flatMap(u ->userRepository.findByEmail(u.getEmailAddress()))
                .flatMap(existing ->Mono.error(new EmailUserAlreadyExistsException(user.getEmailAddress())))
                .switchIfEmpty(userRepository.save(user))
                .cast(User.class)
                .doOnSuccess(savedUser ->
                        log.info("Usuario creado exitosamente con email={}", savedUser.getEmailAddress()))
                .doOnError(error ->
                        log.error("Error al crear usuario con email={}: {}", user.getEmailAddress(),
                                error.getMessage())));

    }

}
