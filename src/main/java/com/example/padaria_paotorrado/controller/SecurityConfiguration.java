package com.example.padaria_paotorrado.controller;

import com.example.padaria_paotorrado.infrastructure.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 Carrega usuários do banco
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 🔹 Regras de segurança
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // H2 console
                .authorizeHttpRequests(auth -> auth
                        // Arquivos estáticos e páginas públicas
                        .requestMatchers("/", "/index.html", "/login.html", "/usuario.html",
                                "/padaria.html", "/compra.html", "/css/**", "/pasta-para-js/**",
                                "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif").permitAll()

                        // H2 console
                        .requestMatchers("/h2-console/**").permitAll()

                        // API de login (se usar endpoint próprio)
                        .requestMatchers("/auth/login").permitAll()

                        // Só ADMIN pode cadastrar usuário via API
                        .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")

                        // Todo o resto precisa de autenticação
                        .anyRequest().authenticated()
                )
                // Login via formulário customizado
                .formLogin(form -> form
                        .loginPage("/login.html")           // página de login customizada
                        .loginProcessingUrl("/login")       // endpoint que processa POST
                        .usernameParameter("username")      // campo esperado no form
                        .passwordParameter("password")      // campo esperado no form
                        .defaultSuccessUrl("/index.html", true) // redireciona após login
                        .permitAll()
                )
                // Logout
                .logout(l -> l
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                );

        return http.build();
    }
}