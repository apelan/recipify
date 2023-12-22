package com.recipify.recipify.security;

import com.recipify.recipify.api.dto.UserRegisterDto;
import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.UserRepository;
import com.recipify.recipify.services.integrations.clearbit.ClearbitUser;
import com.recipify.recipify.services.integrations.clearbit.ClearbitWebClient;
import com.recipify.recipify.services.integrations.hunter.HunterWebClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final JWTManager jwtManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final HunterWebClient hunterWebClient;
    private final ClearbitWebClient clearbitWebClient;

    /**
     * Used to log in user into the app, returning JWT.
     *
     * @param email String
     * @param password String
     * @return JWT
     */
    public String login(String email, String password) {
        log.info("Attempting login for user {}", email);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtManager.generateToken(authentication);
        log.info("User {} logged in ", email);
        return token;
    }

    @Transactional
    public void register(UserRegisterDto userRegisterDto) {
        log.info("Attempting user registration with email {}", userRegisterDto.email());

        if (userRepository.existsByEmail(userRegisterDto.email())) {
            String message = String.format("Email address %s already taken", userRegisterDto.email());
            log.error(message);
            throw new ValidationException(message);
        }

        // verify email with Hunter
        if (!hunterWebClient.verifyEmail(userRegisterDto.email())) {
            String message = String.format("Email address %s is not valid!", userRegisterDto.email());
            log.error(message);
            throw new ValidationException(message);
        }

        // fetch Clearbit user info
        ClearbitUser clearbitUser = clearbitWebClient.getUserData(userRegisterDto.email());

        User user = new User();
        user.setEmail(userRegisterDto.email());
        user.setPassword(passwordEncoder.encode(userRegisterDto.password()));
        user.setFirstName(clearbitUser.givenName());
        user.setLastName(clearbitUser.familyName());
        user.setCity(clearbitUser.city());
        user.setCountry(clearbitUser.country());

        userRepository.save(user);
        log.info("User successfully registered with email {}", user.getEmail());

    }

}
