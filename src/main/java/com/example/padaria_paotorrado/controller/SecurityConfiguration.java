package com.example.padaria_paotorrado.controller;

import com.example.padaria_paotorrado.infrastructure.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserRepository repository;

    public SecurityConfiguration(UserRepository repository) {
        this.repository = repository;
    }

    // 🔹 Usuário fixo em memória (admin / 1234)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var user = org.springframework.security.core.userdetails.User.builder()
                .username("admin")
                .password(encoder.encode("1234"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 🔹 Regras de segurança
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 🔒 desabilita CSRF (bom pra API REST)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/register").permitAll() // libera login e cadastro
                        .anyRequest().authenticated() // o resto precisa estar logado
                )
                .httpBasic(withDefaults()); // ✅ habilita login HTTP Basic

        return http.build();
    }
}
