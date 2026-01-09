package com.himfg.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // ❌ Desactiva CSRF (no necesario en APIs REST)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // ❌ Desactiva el popup de Basic Auth
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // ❌ Desactiva formulario de login de Spring
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/auth/login",
                                "/auth/refresh",
                                "/v3/api-docs/**",
                                "/swagger-ui/**"
                        ).permitAll() // ✅ Endpoints públicos
                        .anyExchange().permitAll() // 👈 el JWTFilter se encarga de la seguridad
                )
                .build();
    }
}
