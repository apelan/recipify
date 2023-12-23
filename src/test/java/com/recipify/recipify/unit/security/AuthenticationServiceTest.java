package com.recipify.recipify.unit.security;

import com.recipify.recipify.api.dto.UserRegisterDto;
import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.UserRepository;
import com.recipify.recipify.security.AuthenticationService;
import com.recipify.recipify.security.JWTManager;
import com.recipify.recipify.services.integrations.clearbit.ClearbitUser;
import com.recipify.recipify.services.integrations.clearbit.ClearbitWebClient;
import com.recipify.recipify.services.integrations.hunter.HunterWebClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


import jakarta.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTManager jwtManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private HunterWebClient hunterWebClient;
    @Mock
    private ClearbitWebClient clearbitWebClient;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthenticationService service;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void login() {
        // GIVEN
        String token = "test-jwt";
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test@test.com", "test"))
        ).thenReturn(authentication);

        when(jwtManager.generateToken(authentication)).thenReturn(token);

        // WHEN
        String result = service.login("test@test.com", "test");

        // THEN
        assertEquals(token, result);

    }

    @Test
    public void register_success() {
        // GIVEN
        String email = "test@test.com";
        String password = "test";
        UserRegisterDto userRegisterDto = new UserRegisterDto(email, password);

        ClearbitUser clearbitUser = new ClearbitUser(
                "test_first_name",
                "test_last_name",
                "test_city",
                "test_country"
        );

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(hunterWebClient.verifyEmail(email)).thenReturn(true);
        when(clearbitWebClient.getUserData(email)).thenReturn(clearbitUser);
        when(passwordEncoder.encode(password)).thenReturn("encoded-test-password");

        // WHEN
        service.register(userRegisterDto);

        // THEN
        verify(userRepository).save(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertEquals(email, user.getEmail());
        assertEquals("encoded-test-password", user.getPassword());
        assertEquals(clearbitUser.givenName(), user.getFirstName());
        assertEquals(clearbitUser.familyName(), user.getLastName());
        assertEquals(clearbitUser.city(), user.getCity());
        assertEquals(clearbitUser.country(), user.getCountry());

    }

    @Test
    public void register_failed_emailTaken() {
        // GIVEN
        String email = "test@test.com";
        String password = "test";
        UserRegisterDto userRegisterDto = new UserRegisterDto(email, password);

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // WHEN THEN
        assertThrows(ValidationException.class, () -> service.register(userRegisterDto));
        verify(userRepository, times(0)).save(userArgumentCaptor.capture());

    }

    @Test
    public void register_failed_hunterValidationFailed() {
        // GIVEN
        String email = "test@test.com";
        String password = "test";
        UserRegisterDto userRegisterDto = new UserRegisterDto(email, password);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(hunterWebClient.verifyEmail(email)).thenReturn(false);

        // WHEN THEN
        assertThrows(ValidationException.class, () -> service.register(userRegisterDto));
        verify(userRepository, times(0)).save(userArgumentCaptor.capture());

    }

    @Test
    public void register_failed_hunterCallFailed() {
        // GIVEN
        String email = "test@test.com";
        String password = "test";
        UserRegisterDto userRegisterDto = new UserRegisterDto(email, password);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(hunterWebClient.verifyEmail(email)).thenThrow(RuntimeException.class);

        // WHEN THEN
        assertThrows(RuntimeException.class, () -> service.register(userRegisterDto));
        verify(userRepository, times(0)).save(userArgumentCaptor.capture());
    }

    @Test
    public void register_failed_clearbitCallFailed() {
        // GIVEN
        String email = "test@test.com";
        String password = "test";
        UserRegisterDto userRegisterDto = new UserRegisterDto(email, password);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(hunterWebClient.verifyEmail(email)).thenReturn(true);
        when(clearbitWebClient.getUserData(email)).thenThrow(RuntimeException.class);

        // WHEN THEN
        assertThrows(RuntimeException.class, () -> service.register(userRegisterDto));
        verify(userRepository, times(0)).save(userArgumentCaptor.capture());
    }

}
