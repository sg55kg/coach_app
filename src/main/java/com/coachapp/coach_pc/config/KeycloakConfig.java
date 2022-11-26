package com.coachapp.coach_pc.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class KeycloakConfig {

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    public static Keycloak getInstance(String token, String clientId) {
        return Keycloak
                .getInstance("http://localhost:8080", "dev", clientId, token);
    }

    public static Keycloak getUsernameAndPasswordInstance(String clientId,
                                                          String clientSecret,
                                                          String username,
                                                          String password) {
        return KeycloakBuilder.builder()
                .realm("dev")
                .serverUrl("http://localhost:8080")
                .clientId(clientId)
                .clientSecret(clientSecret)
                //.grantType("password")
                .username(username)
                .password(password)
                .build();
    }

}
