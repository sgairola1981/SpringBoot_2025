package com.gairola.keylock.config;

import com.gairola.keylock.security.KeycloakRoleConverter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

@Configuration
public class SecurityConfig {

    private final String issuerUri = "http://localhost:8088/realms/myrealm";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/user/**").hasRole("user")
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(resourceServer ->
                        resourceServer.authenticationManagerResolver(authenticationManagerResolver())
                );

        return http.build();
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
        return request -> {
            JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
            jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

            JwtDecoder decoder = jwtDecoder();
            JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
            provider.setJwtAuthenticationConverter(jwtConverter);

            return provider::authenticate;
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }
}

