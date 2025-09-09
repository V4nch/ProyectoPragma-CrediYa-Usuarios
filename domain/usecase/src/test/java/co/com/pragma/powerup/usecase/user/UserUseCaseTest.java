package co.com.pragma.powerup.usecase.user;

import co.com.pragma.powerup.model.role.Role;
import co.com.pragma.powerup.model.role.gateways.RoleRepository;
import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.*;
import co.com.pragma.powerup.model.user.gateways.TransactionGateway;
import co.com.pragma.powerup.model.user.gateways.UserRepository;

import co.com.pragma.powerup.model.user.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UserUseCaseTest {

    private UserRepository userRepository;
    private UserUseCase userUseCase;
    private RoleRepository roleRepository;
    private TransactionGateway transactionGateway;
    private User mockUser;

    private static final String ROLE_NAME = "ADMIN";

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        transactionGateway = mock(TransactionGateway.class);
        roleRepository = mock(RoleRepository.class);

        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setIdCard("1094123321");
        mockUser.setPassword("1234");

        when(transactionGateway.doInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userUseCase = new UserUseCase(userRepository, transactionGateway, roleRepository);
    }

    private User buildValidUser() {
        User user = new User();
        user.setIdCard("1094123321");
        user.setName("Ivan");
        user.setLastName("Moreno");
        user.setEmailAddress("ivan@example.com");
        user.setBaseSalary("5000000");
        user.setPassword("1234");
        return user;
    }

    @Test
    void saveUser_successful() {
        User user = buildValidUser();
        Role role = new Role();
        role.setIdRole(1L);

        when(userRepository.findByEmail(user.getEmailAddress())).thenReturn(Mono.empty());
        when(userRepository.findByIdCard(user.getIdCard())).thenReturn(Mono.empty());
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Mono.just(role));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectNextMatches(resp -> resp.getEmailAddress().equals(user.getEmailAddress())
                        && resp.getIdCard().equals(user.getIdCard()))
                .verifyComplete();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_alreadyExists() {
        User user = buildValidUser();
        user.setIdCard("1094123321");
        user.setPassword("1234");

        User existingUser = new User();
        existingUser.setEmailAddress(user.getEmailAddress());
        existingUser.setIdCard(user.getIdCard());

        when(userRepository.findByEmail(user.getEmailAddress())).thenReturn(Mono.just(existingUser));
        when(userRepository.findByIdCard(user.getIdCard()))
                .thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(EmailUserAlreadyExistsException.class)
                .verify();
    }

    @Test
    void validate_emptyUser() {

        StepVerifier.create(userUseCase.saveUser(null, ROLE_NAME))
                .expectError(InvalidUserException.class)
                .verify();

    }

    @Test
    void validate_missingName() {
        User user = buildValidUser();
        user.setName("");

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(MissingFieldException.class)
                .verify();

    }

    @Test
    void validate_missingLastName() {
        User user = buildValidUser();
        user.setLastName("");

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(MissingFieldException.class)
                .verify();

    }

    @Test
    void validate_invalidEmailFormat() {
        User user = buildValidUser();
        user.setEmailAddress("invalid-emailll");

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(InvalidEmailException.class)
                .verify();

    }

    @Test
    void validate_salaryOutOfRange() {
        User user = buildValidUser();
        user.setBaseSalary("20000000");

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(InvalidSalaryException.class)
                .verify();
    }

    @Test
    void validate_salaryNotNumeric() {
        User user = buildValidUser();
        user.setBaseSalary("notANumber");

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(InvalidSalaryException.class)
                .verify();

    }
    @Test
    void saveUser_nullUserTriggersDoOnError() {
        StepVerifier.create(userUseCase.saveUser(null, ROLE_NAME))
                .expectError(InvalidUserException.class)
                .verify();
    }
    @Test
    void validate_salaryNegative() {
        User user = buildValidUser();
        user.setBaseSalary("-1000");

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(InvalidSalaryException.class)
                .verify();
    }
    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {

        when(userRepository.findByIdCard("1094123321"))
                .thenReturn(Mono.just(mockUser));


        StepVerifier.create(userUseCase.getUser("1094123321"))
                .expectNextMatches(user -> user.getIdCard().equals("1094123321"))
                .verifyComplete();

        verify(userRepository, times(1)).findByIdCard("1094123321");
        verify(transactionGateway, times(1)).doInTransaction(any(Mono.class));
    }

    @Test
    void getUser_ShouldReturnError_WhenUserDoesNotExist() {

        when(userRepository.findByIdCard("0000"))
                .thenReturn(Mono.empty());


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

        when(userRepository.findByIdCard("9999"))
                .thenReturn(Mono.error(new RuntimeException("DB error")));


        StepVerifier.create(userUseCase.getUser("9999"))
                .expectErrorMatches(error ->
                        error instanceof RuntimeException &&
                                error.getMessage().equals("DB error"))
                .verify();

        verify(userRepository, times(1)).findByIdCard("9999");
        verify(transactionGateway, times(1)).doInTransaction(any(Mono.class));
    }

    @Test
    void saveUser_idCardAlreadyExists_shouldThrowIdCardUserAlreadyExistsException() {
        User user = buildValidUser();
        User existing = buildValidUser();

        when(userRepository.findByEmail(user.getEmailAddress())).thenReturn(Mono.empty());
        when(userRepository.findByIdCard(user.getIdCard())).thenReturn(Mono.just(existing));

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(IdCardUserAlreadyExistsException.class)
                .verify();
    }


    @Test
    void saveUser_roleNotFound_shouldThrowRoleNotFoundException() {
        User user = buildValidUser();

        when(userRepository.findByEmail(user.getEmailAddress())).thenReturn(Mono.empty());
        when(userRepository.findByIdCard(user.getIdCard())).thenReturn(Mono.empty());
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectError(RoleNotFoundException.class)
                .verify();
    }


    @Test
    void saveUser_shouldEncodePasswordAndAssignRole() {
        User user = buildValidUser();
        user.setIdCard("1094123321");
        user.setPassword("1234");

        Role role = new Role();
        role.setIdRole(5L);

        when(userRepository.findByEmail(user.getEmailAddress())).thenReturn(Mono.empty());
        when(userRepository.findByIdCard(user.getIdCard())).thenReturn(Mono.empty());
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Mono.just(role));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(userUseCase.saveUser(user, ROLE_NAME))
                .expectNextMatches(resp ->
                        resp.getEmailAddress().equals(user.getEmailAddress()) &&
                                resp.getIdCard().equals(user.getIdCard())
                )
                .verifyComplete();

        verify(userRepository).save(any(User.class));


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assert encoder.matches("1234", user.getPassword());
    }
}

