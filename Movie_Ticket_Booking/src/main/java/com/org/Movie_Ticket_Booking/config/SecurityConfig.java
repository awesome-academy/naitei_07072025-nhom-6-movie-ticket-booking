package com.org.Movie_Ticket_Booking.config;

import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> User.withUsername(user.getName())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList())
                        .build())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/movies/**", "/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/manager/**").hasRole("CINEMA_MANAGER")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .formLogin(
                        form->form.loginPage("/login").loginProcessingUrl("/authenticateTheUser").permitAll().defaultSuccessUrl("/dashboard", true)
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/dashboard")
                        .permitAll()
                );
        return http.build();
    }

}
