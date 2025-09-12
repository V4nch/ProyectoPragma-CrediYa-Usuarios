package co.com.pragma.powerup.usecase.user;

import co.com.pragma.powerup.model.role.gateways.RoleRepository;
import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.*;
import co.com.pragma.powerup.model.user.gateways.TransactionGateway;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.model.user.response.UserResponse;
import co.com.pragma.powerup.model.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final TransactionGateway transactionGateway;
    private final RoleRepository roleRepository;

    public Mono<UserResponse> saveUser(User user, String roleName) {
        return this.validate(user)
                .flatMap(u -> transactionGateway.doInTransaction(
                        userRepository.findByEmail(u.getEmailAddress())
                                .flatMap(existing -> Mono.<User>error(
                                        new EmailUserAlreadyExistsException(u.getEmailAddress())))
                                .switchIfEmpty(userRepository.findByIdCard(u.getIdCard())
                                        .flatMap(existing -> Mono.<User>error(
                                                new IdCardUserAlreadyExistsException(u.getIdCard()))))
                                .switchIfEmpty(Mono.defer(() -> this.assignRole(u, roleName)))
                                .flatMap(this::saveUser)
                ));
    }

    public Mono<User> getUser(String idCard) {
        return
                transactionGateway.doInTransaction(
                        userRepository.findByIdCard(idCard)
                                .switchIfEmpty(Mono.error(new UserNotFoundException(Constants.USER_NOT_FOUND)))
                );
    }
    private Mono<UserResponse> saveUser(User user) {
        return userRepository.save(user)
                .map(la -> new UserResponse(user.getIdCard(),user.getName(),user.getLastName(),user.getBirthDate(),
                        user.getAddress(),user.getPhoneNumber(),user.getEmailAddress(),user.getBaseSalary())
                );
    }


    private Mono<User> assignRole(User user,String roleName) {
        return roleRepository.findByName(roleName)
                .switchIfEmpty(Mono.error(new RoleNotFoundException(Constants.ROLE_NOT_FOUND)))
                .map(role -> {
                    user.setIdRole(role.getIdRole());
                    return user;
                });
    }

    private Mono<User> validate(User user) {
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
