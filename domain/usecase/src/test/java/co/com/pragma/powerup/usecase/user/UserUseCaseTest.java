package co.com.pragma.powerup.usecase.user;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.*;
import co.com.pragma.powerup.model.user.gateways.TransactionGateway;
import co.com.pragma.powerup.model.user.gateways.UserRepository;

import co.com.pragma.powerup.model.user.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UserUseCaseTest {

    private UserRepository userRepository;
    private UserUseCase userUseCase;
    private TransactionGateway transactionGateway;
    private User mockUser;

    @BeforeEach
    void setUp() {

        userRepository = mock(UserRepository.class);
        transactionGateway =  Mockito.mock(TransactionGateway.class);
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setIdCard("1094123321");
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
    @Test
    void saveUser_nullUserTriggersDoOnError() {
        StepVerifier.create(userUseCase.saveUser(null))
                .expectError(InvalidUserException.class)
                .verify();
    }
    @Test
    void validate_salaryNegative() {
        User user = buildValidUser();
        user.setBaseSalary("-1000");

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(InvalidSalaryException.class)
                .verify();
    }
    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByIdCard("1094123321"))
                .thenReturn(Mono.just(mockUser));

        // Act & Assert
        StepVerifier.create(userUseCase.getUser("1094123321"))
                .expectNextMatches(user -> user.getIdCard().equals("1094123321"))
                .verifyComplete();

        verify(userRepository, times(1)).findByIdCard("1094123321");
        verify(transactionGateway, times(1)).doInTransaction(any(Mono.class));
    }

    @Test
    void getUser_ShouldReturnError_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByIdCard("0000"))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(userUseCase.getUser("0000"))
                .expectErrorMatches(throwable ->
                        throwable instanceof UserNotFoundException &&
                                throwable.getMessage().equals(Constants.USER_NOT_FOUND))
                .verify();

        verify(userRepository, times(1)).findByIdCard("0000");
        verify(transactionGateway, times(1)).doInTransaction(any(Mono.class));
    }

    @Test
    void getUser_ShouldPropagateRepositoryError() {
        // Arrange
        when(userRepository.findByIdCard("9999"))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        // Act & Assert
        StepVerifier.create(userUseCase.getUser("9999"))
                .expectErrorMatches(error ->
                        error instanceof RuntimeException &&
                                error.getMessage().equals("DB error"))
                .verify();

        verify(userRepository, times(1)).findByIdCard("9999");
        verify(transactionGateway, times(1)).doInTransaction(any(Mono.class));
    }
}

