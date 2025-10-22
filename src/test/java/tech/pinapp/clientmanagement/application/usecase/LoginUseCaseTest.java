package tech.pinapp.clientmanagement.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.pinapp.clientmanagement.application.dto.LoginRequest;
import tech.pinapp.clientmanagement.application.dto.LoginResponse;
import tech.pinapp.clientmanagement.application.security.JwtService;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
            .username("user")
            .password("password")
            .build();
    }

    @Test
    @DisplayName("Should return LoginResponse with token on successful authentication")
    void execute_whenAuthenticationSucceeds_shouldReturnLoginResponse() {
        // Arrange
        UserDetails userDetails = new User("user", "password", Collections.emptyList());
        String expectedToken = "sample.jwt.token";

        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        // Act
        LoginResponse result = loginUseCase.execute(loginRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo(expectedToken);

        // Verify
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("user");
        verify(jwtService, times(1)).generateToken(userDetails);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException on failed authentication")
    void execute_whenAuthenticationFails_shouldThrowBadCredentialsException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThatThrownBy(() -> loginUseCase.execute(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid credentials");

        // Verify
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any(UserDetails.class));
    }
}