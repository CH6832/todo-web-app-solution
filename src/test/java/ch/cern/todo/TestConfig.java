package ch.cern.todo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test configuration class for the Todo application.
 * Provides configuration beans specifically for testing environments.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Provides a password encoder bean for test environments.
     * Uses BCrypt hashing algorithm with default strength (10 rounds).
     *
     * This encoder is used to:
     * - Hash passwords for test users
     * - Verify passwords in authentication tests
     * - Match the production environment's password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}