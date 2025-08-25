package com.org.Movie_Ticket_Booking.config;

import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@PropertySource("classpath:endpointUrl.properties")
public class SecurityConfig {
    @Value("${loginUrl}")
    private String loginUrl;

    @Value("${logoutUrl}")
    private String logoutUrl;

    @Value("${dashboard}")
    private String dashboard;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> userRepository.findByEmail(email)
                .map(user -> new CustomUserDetail(
                        user,
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .toList())
                ).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/bootstrap/**").permitAll()
                        .requestMatchers("/movies/**", "/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/manager/**").hasRole("CINEMA_MANAGER")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .formLogin(
                        form->form.loginPage(loginUrl).usernameParameter("email").passwordParameter("password").loginProcessingUrl("/authenticateTheUser").permitAll().defaultSuccessUrl(dashboard, true)
                ).logout(logout -> logout
                        .logoutUrl(loginUrl)
                        .logoutSuccessUrl(dashboard)
                        .permitAll()
                );
        return http.build();
    }
}
