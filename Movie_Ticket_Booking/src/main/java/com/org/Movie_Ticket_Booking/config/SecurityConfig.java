package com.org.Movie_Ticket_Booking.config;

import com.org.Movie_Ticket_Booking.constants.RoleConstants;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import com.org.Movie_Ticket_Booking.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@PropertySource("classpath:endpointUrl.properties")
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${loginUrl}")
    private String loginUrl;

    @Value("${logoutUrl}")
    private String logoutUrl;

    @Value("${dashboard}")
    private String dashboard;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint restAuthenticationEntryPoint;
    private final AccessDeniedHandler restAccessDeniedHandler;

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
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/logout").hasRole(RoleConstants.ROLE_CUSTOMER)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/bootstrap/**").permitAll()
                        .requestMatchers("/movies/**", "/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole(RoleConstants.ROLE_ADMIN)
                        .requestMatchers("/manager/**").hasRole(RoleConstants.ROLE_CINEMA_MANAGER)
                        .requestMatchers("/customer/**").hasRole(RoleConstants.ROLE_CUSTOMER)
                        .anyRequest().authenticated()
                )
                .formLogin(
                        form->form.loginPage(loginUrl).usernameParameter("email").passwordParameter("password").loginProcessingUrl("/authenticateTheUser").permitAll().defaultSuccessUrl(dashboard, true)
                                .failureHandler((request, response, exception) -> {
                                    if (exception.getCause() instanceof AppException appEx &&
                                            appEx.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                                        response.sendRedirect("/login?notfound=true");
                                    } else {
                                        response.sendRedirect("/login?error=true");
                                    }
                                })
                )
                .logout(logout -> logout
                        .logoutUrl(logoutUrl)
                        .logoutSuccessUrl(loginUrl)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }
}
