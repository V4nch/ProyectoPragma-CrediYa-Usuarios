package co.com.pragma.powerup.usecase.login;


import co.com.pragma.powerup.model.auth.Auth;
import co.com.pragma.powerup.model.auth.gateways.AuthRepository;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;


import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoginUseCaseTest {
    private ReactiveAuthenticationManager authManager;
    private AuthRepository authRepository;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        authManager = mock(ReactiveAuthenticationManager.class);
        authRepository = mock(AuthRepository.class);
        loginUseCase = new LoginUseCase(authManager, authRepository);
    }

    //Nuevo
    @Test
    void execute_successfulLogin_shouldReturnAuthResponse() {
        Auth request = new Auth();
        request.setEmail("ivan@example.com");
        request.setPassword("1234");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("ivan@example.com");
        GrantedAuthority role = mock(GrantedAuthority.class);
        when(role.getAuthority()).thenReturn(Constants.ROLE_1 + "ADMIN");


// Usamos thenAnswer para evitar problemas de genéricos
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of(role));

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(authentication));

        when(authRepository.generateToken(eq("ivan@example.com"), anyMap(), eq(Duration.ofHours(1))))
                .thenReturn(Mono.just("fake-jwt-token"));

        StepVerifier.create(loginUseCase.execute(request))
                .expectNextMatches(resp -> resp.getToken().equals("fake-jwt-token"))
                .verifyComplete();

        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authRepository, times(1))
                .generateToken(eq("ivan@example.com"), anyMap(), eq(Duration.ofHours(1)));
    }

    //Nuevo
    @Test
    void execute_failedLogin_shouldReturnError() {
        Auth request = new Auth();
        request.setEmail("ivan@example.com");
        request.setPassword("wrong-password");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.error(new RuntimeException("Invalid credentials")));

        StepVerifier.create(loginUseCase.execute(request))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Invalid credentials"))
                .verify();

        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authRepository, never()).generateToken(anyString(), anyMap(), any());
    }

    //Nuevo
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

        when(authRepository.generateToken(eq("ivan@example.com"), anyMap(), eq(Duration.ofHours(1))))
                .thenAnswer(invocation -> {
                    Map<String, List<String>> rolesMap = invocation.getArgument(1);
                    // Verificar que se eliminó el prefijo ROLE_1
                    assert rolesMap.get(Constants.ROLES).contains("ADMIN");
                    assert rolesMap.get(Constants.ROLES).contains("USER");
                    return Mono.just("fake-jwt-token");
                });

        StepVerifier.create(loginUseCase.execute(request))
                .expectNextMatches(resp -> resp.getToken().equals("fake-jwt-token"))
                .verifyComplete();
    }
}
