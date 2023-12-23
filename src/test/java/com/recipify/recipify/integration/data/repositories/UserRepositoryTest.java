package com.recipify.recipify.integration.data.repositories;

import com.recipify.recipify.data.entities.User;
import com.recipify.recipify.data.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.yml")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void findByEmail_userFound() {
        Optional<User> result = repository.findByEmail("user@recipify.com");

        assertTrue(result.isPresent());
    }

    @Test
    public void findByEmail_userNotFound() {
        Optional<User> result = repository.findByEmail("not-existent@recipify.com");

        assertFalse(result.isPresent());
    }

    @Test
    public void existsByEmail_exist() {
        Boolean result = repository.existsByEmail("user@recipify.com");

        assertTrue(result);
    }

    @Test
    public void existsByEmail_notExist() {
        Boolean result = repository.existsByEmail("not-existent@recipify.com");

        assertFalse(result);
    }

}
