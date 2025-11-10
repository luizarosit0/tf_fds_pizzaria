package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Seguranca;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Seguranca.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private AutenticacaoService autenticacaoService;
    
    @Autowired
    public SecurityConfig (AutenticacaoService autenticacaoService){
        this.autenticacaoService = autenticacaoService; 
    }
    // 1. Define o "cofre" de senhas (BCrypt) 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Ensina o Spring a autenticar usando nosso UserDetailsService e o BCrypt
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService)
            .passwordEncoder(passwordEncoder());
    }

    // 3. Define as regras de acesso (quem pode acessar o quê) [cite: 137]
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API é stateless
            .authorizeHttpRequests(auth -> auth
                // Requisitos do Enunciado:
                .requestMatchers("/cardapio/**").permitAll() // UC1 é público
                .requestMatchers("/usuarios/registrar").permitAll() // Novo endpoint para se registrar
                .requestMatchers("/pedidos/**").hasRole("CLIENTE") // UC2, 3, 4, 5
                .requestMatchers("/admin/**").hasRole("MASTER") // UC de admin
                
                // Rotas do H2 Console (para debug)
                .requestMatchers("/h2/**").permitAll()
                
                // Nega qualquer outra requisição
                .anyRequest().denyAll()
            )
            .headers(headers -> headers.frameOptions().disable()) // Permite o H2 console
            .httpBasic(Customizer.withDefaults()); // Usa Autenticação Básica (como no PDF) [cite: 15]

        return http.build();
    }
}
