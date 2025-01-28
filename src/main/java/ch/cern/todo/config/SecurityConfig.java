package ch.cern.todo.config;

import ch.cern.todo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.stream.Collectors;

/**
 * Security configuration class for the Todo application.
 * Configures authentication, authorization, and security features.
 *
 * Features:
 * - HTTP Basic authentication for API endpoints
 * - Role-based access control (USER and ADMIN roles)
 * - H2 Console security configuration
 * - BCrypt password encryption
 * - Custom error handling for authentication/authorization
 *
 * Security Rules:
 * 1. Public Access:
 *    - /h2-console/** (database management interface)
 *
 * 2. Authentication Required:
 *    - /api/tasks/** (POST) - any authenticated user
 *    - /api/categories/** (GET) - any authenticated user
 *
 * 3. Admin Access Only:
 *    - /api/categories/** (POST) - create categories
 *    - /api/categories/** (PUT) - update categories
 *    - /api/categories/** (DELETE) - delete categories
 *
 * User Roles:
 * - ROLE_USER: Basic access to tasks and viewing categories
 * - ROLE_ADMIN: Full access including category management
 *
 * Authentication:
 * - Uses Basic Authentication
 * - Passwords are encrypted using BCrypt
 * - Users are stored in the H2 database
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * Defines security rules, authentication requirements, and H2 Console access.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF and frameOptions for H2 Console
                .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers.frameOptions().disable()
                )

                // Request authorization rules
                .authorizeHttpRequests(auth -> auth
                        // H2 Console access configuration
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/h2-console/login.do**").permitAll()
                        .requestMatchers("/h2-console/header.jsp**").permitAll()
                        .requestMatchers("/h2-console/query.jsp**").permitAll()
                        .requestMatchers("/h2-console/stylesheet.css**").permitAll()

                        // API endpoint security configuration
                        .requestMatchers(HttpMethod.POST, "/api/tasks/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    }

    /**
     * Configures the user details service.
     * Loads user-specific data from the database and maps it to Spring Security's User model.
     *
     * Process:
     * 1. Searches for user in database by username
     * 2. Converts user roles to Spring Security authorities
     * 3. Creates UserDetails object with username, password, and authorities
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRoles().stream()
                            .map(role -> {
                                System.out.println("Adding authority: " + role);
                                return new SimpleGrantedAuthority(role);
                            })
                            .collect(Collectors.toList()))
                    .build();
        };
    }

    /**
     * Configures the password encoder for secure password storage.
     * Uses BCrypt hashing algorithm with default strength (10 rounds).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
