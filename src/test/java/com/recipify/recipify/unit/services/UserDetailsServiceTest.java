package com.recipify.recipify.unit.services;

import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.UserRepository;
import com.recipify.recipify.services.UserDetailsService;
import com.recipify.recipify.unit.CommonMockObjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserDetailsService service;

    @Test
    public void loadUserByUsername_userFound() {
        // GIVEN
        String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(CommonMockObjects.getMockUser()));

        // WHEN
        UserDetails userDetails = service.loadUserByUsername(email);

        // THEN
        assertEquals(email, userDetails.getUsername());
        assertEquals("test", userDetails.getPassword());

    }

    @Test
    public void loadUserByUsername_userNotFound() {
        // GIVEN
        String email = "non-existent@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // WHEN THEN
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(email));

    }

    @Test
    public void currentUser_userFound() {
        // GIVEN
        User user = CommonMockObjects.getMockUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(user));

        // WHEN
        User result = service.currentUser();

        // THEN
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());

    }

    @Test
    public void currentUser_userNotFound() {
        // GIVEN
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.empty());

        // WHEN THEN
        assertThrows(UsernameNotFoundException.class, () -> service.currentUser());

    }

}
