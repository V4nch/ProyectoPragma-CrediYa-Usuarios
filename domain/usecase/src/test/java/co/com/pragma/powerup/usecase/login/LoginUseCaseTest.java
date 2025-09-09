package co.com.pragma.powerup.usecase.login;


import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
import co.com.pragma.powerup.model.user.exceptions.InvalidCredentialsException;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.model.user.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.ReactiveAuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Map;


import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoginUseCaseTest {
    private ReactiveAuthenticationManager authManager;
    private AuthRepository authRepository;
    private UserRepository userRepository;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        authManager = mock(ReactiveAuthenticationManager.class);
        authRepository = mock(AuthRepository.class);
        userRepository = mock(UserRepository.class);
        loginUseCase = new LoginUseCase(authManager, authRepository, userRepository);
    }

    @Test
    void execute_successfulLogin_shouldReturnAuthResponse() {
        Auth request = new Auth();
        request.setEmail("ivan@example.com");
        request.setPassword("1234");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("ivan@example.com");
        GrantedAuthority role = mock(GrantedAuthority.class);
        when(role.getAuthority()).thenReturn(Constants.ROLE_1 + "ADMIN");
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of(role));

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authentication));


        var user = new co.com.pragma.powerup.model.user.User();
        user.setEmailAddress("ivan@example.com");
        user.setIdCard("1234567890");
        when(userRepository.findByEmail("ivan@example.com"))
                .thenReturn(Mono.just(user));


        when(authRepository.generateToken(eq("ivan@example.com"), anyMap(), eq(Duration.ofHours(1))))
                .thenAnswer(invocation -> {
                    Map<String, Object> claims = invocation.getArgument(1);
                    assert claims.get(Constants.ROLES) instanceof List;
                    assert ((List<?>) claims.get(Constants.ROLES)).contains("ADMIN");
                    assert claims.get("idCard").equals("1234567890");
                    return Mono.just("fake-jwt-token");
                });

        StepVerifier.create(loginUseCase.execute(request))
                .expectNextMatches(resp -> resp.getToken().equals("fake-jwt-token"))
                .verifyComplete();

        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authRepository, times(1))
                .generateToken(eq("ivan@example.com"), anyMap(), eq(Duration.ofHours(1)));
        verify(userRepository, times(1)).findByEmail("ivan@example.com");
    }

    @Test
    void execute_failedLogin_shouldReturnError() {
        Auth request = new Auth();
        request.setEmail("ivan@example.com");
        request.setPassword("wrong-password");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.error(new InvalidCredentialsException(Constants.EXC_INVALID_CREDENTIALS)));

        StepVerifier.create(loginUseCase.execute(request))
                .expectErrorMatches(throwable -> throwable instanceof InvalidCredentialsException &&
                        throwable.getMessage().equals(Constants.EXC_INVALID_CREDENTIALS))
                .verify();

        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authRepository, never()).generateToken(anyString(), anyMap(), any());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void execute_shouldMapRolesCorrectlyRemovingPrefix() {
        Auth request = new Auth();
        request.setEmail("ivan@example.com");
        request.setPassword("1234");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("ivan@example.com");

        GrantedAuthority role1 = mock(GrantedAuthority.class);
        GrantedAuthority role2 = mock(GrantedAuthority.class);
        when(role1.getAuthority()).thenReturn(Constants.ROLE_1 + "ADMIN");
        when(role2.getAuthority()).thenReturn(Constants.ROLE_1 + "USER");

        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of(role1, role2));
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authentication));


        var user = new co.com.pragma.powerup.model.user.User();
        user.setEmailAddress("ivan@example.com");
        user.setIdCard("1234567890");
        when(userRepository.findByEmail("ivan@example.com"))
                .thenReturn(Mono.just(user));

        when(authRepository.generateToken(eq("ivan@example.com"), anyMap(), eq(Duration.ofHours(1))))
                .thenAnswer(invocation -> {
                    Map<String, Object> claims = invocation.getArgument(1);
                    List<?> rolesList = (List<?>) claims.get(Constants.ROLES);
                    assert rolesList.contains("ADMIN");
                    assert rolesList.contains("USER");
                    assert claims.get("idCard").equals("1234567890");
                    return Mono.just("fake-jwt-token");
                });

        StepVerifier.create(loginUseCase.execute(request))
                .expectNextMatches(resp -> resp.getToken().equals("fake-jwt-token"))
                .verifyComplete();
    }
}
