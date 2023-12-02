package com.example.tomatoAccounting.config;

import com.example.tomatoAccounting.entity.UserEntity;
import com.example.tomatoAccounting.entity.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(UserRepository userRepository, JwtTokenFilter jwtTokenFilter) {
        this.userRepository = userRepository;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email not found: " + username));                            // Szukanie użytkownika po emailu
    }

    @Bean
    public PasswordEncoder getBcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }                                                                                                                                   // Szyfrowanie hasła

    @Bean
    public AuthenticationManager authorizationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {       // AuthenticationManager jest potrzebny do autoryzacji
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();                                                                              // Wyłączenie CORSA
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());     // Dodanie CORSA mozemy to wyłączyć i ustawić CORS nad metodami, teraz jest dostęp do wszystkich metod z każdego adresu
        http.authorizeRequests()                                                                            // Konfiguracja dostępu
                .antMatchers("/auth/login").permitAll()                                         // Wszyscy mają dostęp
                .antMatchers("/auth/register").permitAll()
                .anyRequest().authenticated();                                                              // Wszyscy muszą być zalogowani
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);                   // Dodanie filtra
        return http.build();
    }
}


