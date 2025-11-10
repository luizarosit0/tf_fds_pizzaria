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
    
    // "cofre" de senhas = BCrypt 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService)
            .passwordEncoder(passwordEncoder());
    }

    // regras de acesso
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API é stateless
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/cardapio/**").permitAll() // UC1 = público
                .requestMatchers("/usuarios/registrar").permitAll() // endpoint para registrar
                .requestMatchers("/pedidos/**").hasRole("CLIENTE") // UC2, UC3, UC4, UC5
                .requestMatchers("/admin/**").hasRole("MASTER") // UC de admin
                
                .requestMatchers("/h2/**").permitAll()
                
                .anyRequest().denyAll()
            )
            .headers(headers -> headers.frameOptions().disable()) 
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
