package co.com.pragma.powerup.usecase.user;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.*;
import co.com.pragma.powerup.model.user.gateways.TransactionGateway;
import co.com.pragma.powerup.model.user.gateways.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UserUseCaseTest {

    private UserRepository userRepository;
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        TransactionGateway transactionGateway =  Mockito.mock(TransactionGateway.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.empty());

        when(transactionGateway.doInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        userUseCase = new UserUseCase(userRepository, transactionGateway);
    }

    private User buildValidUser() {
        User user = new User();
        user.setName("Ivan");
        user.setLastName("Moreno");
        user.setEmailAddress("ivan@example.com");
        user.setBaseSalary("5000000");
        return user;
    }

    @Test
    void saveUser_successful() {
        User user = buildValidUser();

        when(userRepository.findByEmail(user.getEmailAddress())).thenReturn(Mono.empty());
        when(userRepository.save(user)).thenReturn(Mono.just(user));


        StepVerifier.create(userUseCase.saveUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository).save(user);
    }

    @Test
    void saveUser_alreadyExists() {
        User user = new User();
        user.setName("Ivan");
        user.setLastName("Moreno");
        user.setEmailAddress("ivan@example.com");
        user.setBaseSalary("5000000");
        when(userRepository.save(any(User.class))).thenAnswer(inv ->
                Mono.just(inv.getArgument(0))
        );
        when(userRepository.findByEmail(user.getEmailAddress())).thenReturn(Mono.just(user));



        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(EmailUserAlreadyExistsException.class)
                .verify();
    }

    @Test
    void validate_emptyUser() {

        StepVerifier.create(userUseCase.saveUser(null))
                .expectError(InvalidUserException.class)
                .verify();

    }

    @Test
    void validate_missingName() {
        User user = buildValidUser();
        user.setName("");

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(MissingFieldException.class)
                .verify();

    }

    @Test
    void validate_missingLastName() {
        User user = buildValidUser();
        user.setLastName("");

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(MissingFieldException.class)
                .verify();

    }

    @Test
    void validate_invalidEmailFormat() {
        User user = buildValidUser();
        user.setEmailAddress("invalid-emailll");

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(InvalidEmailException.class)
                .verify();

    }

    @Test
    void validate_salaryOutOfRange() {
        User user = buildValidUser();
        user.setBaseSalary("20000000");

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(InvalidSalaryException.class)
                .verify();
    }

    @Test
    void validate_salaryNotNumeric() {
        User user = buildValidUser();
        user.setBaseSalary("notANumber");

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(InvalidSalaryException.class)
                .verify();

    }
}

