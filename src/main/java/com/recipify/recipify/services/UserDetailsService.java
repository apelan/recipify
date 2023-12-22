package com.recipify.recipify.services;


import com.recipify.recipify.data.repositories.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Used in order to load user by unique email. <br>
     * This method is overridden from implemented interface provided by Spring Security, that's why naming is different.
     *
     * @param email the email identifying the user whose data is required.
     * @return {@link UserDetails}
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.recipify.recipify.data.entities.User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist."));

        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }

    /**
     * Method used to retrieve current user from SecurityContext.
     *
     * @return {@link com.recipify.recipify.data.entities.User}
     */
    public com.recipify.recipify.data.entities.User currentUser() {
        return userRepository
                .findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist."));
    }

}
