package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // desabilita CSRF (necessário para H2)
                .csrf(csrf -> csrf.disable())

                // libera acesso a tudo
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                // libera uso de frames (H2 Console usa iframe)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}