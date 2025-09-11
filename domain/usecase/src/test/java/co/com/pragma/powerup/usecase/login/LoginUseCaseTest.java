package co.com.pragma.powerup.usecase.login;


import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.AuthResponse;
import co.com.pragma.powerup.model.auth.AuthUser;
import co.com.pragma.powerup.model.auth.gateways.AuthManager;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.exceptions.InvalidCredentialsException;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoginUseCaseTest {
    private AuthRepository authRepository;
    private UserRepository userRepository;
    private AuthManager authManager;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        authRepository = Mockito.mock(AuthRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        authManager = Mockito.mock(AuthManager.class);

        loginUseCase = new LoginUseCase(authRepository, userRepository, authManager);
    }

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        Auth request = new Auth("test@mail.com", "password");

        AuthUser authUser = new AuthUser("test@mail.com", List.of("ADMIN"));

        User user = new User();
        user.setEmailAddress("test@mail.com");
        user.setIdCard("12345");

        String fakeToken = "jwt.token";

        when(authManager.authenticate(eq("test@mail.com"), eq("password")))
                .thenReturn(Mono.just(authUser));

        when(userRepository.findByEmail(eq("test@mail.com")))
                .thenReturn(Mono.just(user));

        when(authRepository.generateToken(
                eq("test@mail.com"),
                any(Map.class),
                any(Duration.class)))
                .thenReturn(Mono.just(fakeToken));

        // Act
        Mono<AuthResponse> result = loginUseCase.execute(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getToken().equals(fakeToken))
                .verifyComplete();

        verify(authManager).authenticate("test@mail.com", "password");
        verify(userRepository).findByEmail("test@mail.com");
        verify(authRepository).generateToken(eq("test@mail.com"), any(Map.class), any(Duration.class));
    }

    @Test
    void shouldFailWhenUserNotFound() {
        // Arrange
        Auth request = new Auth("notfound@mail.com", "password");

        AuthUser authUser = new AuthUser("notfound@mail.com", List.of("CLIENT"));

        when(authManager.authenticate(eq("notfound@mail.com"), eq("password")))
                .thenReturn(Mono.just(authUser));

        when(userRepository.findByEmail(eq("notfound@mail.com")))
                .thenReturn(Mono.empty());

        Mono<AuthResponse> result = loginUseCase.execute(request);


        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof InvalidCredentialsException &&
                        ex.getMessage().equals(Constants.USER_NOT_FOUND + "notfound@mail.com"))
                .verify();

        verify(authManager).authenticate("notfound@mail.com", "password");
        verify(userRepository).findByEmail("notfound@mail.com");
        verifyNoInteractions(authRepository);
    }

    @Test
    void shouldFailWhenInvalidCredentials() {

        Auth request = new Auth("wrong@mail.com", "badpass");

        when(authManager.authenticate(eq("wrong@mail.com"), eq("badpass")))
                .thenReturn(Mono.error(new InvalidCredentialsException(Constants.EXC_INVALID_CREDENTIALS)));

        Mono<AuthResponse> result = loginUseCase.execute(request);


        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof InvalidCredentialsException &&
                        ex.getMessage().equals(Constants.EXC_INVALID_CREDENTIALS))
                .verify();

        verify(authManager).authenticate("wrong@mail.com", "badpass");
        verifyNoInteractions(userRepository);
        verifyNoInteractions(authRepository);
    }
}
