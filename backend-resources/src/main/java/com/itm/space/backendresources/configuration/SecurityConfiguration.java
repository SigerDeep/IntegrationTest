package com.itm.space.backendresources.configuration;

import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.mapper.UserMapper;
import com.itm.space.backendresources.service.UserService;
import com.itm.space.backendresources.service.UserServiceImpl;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll())
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(SecurityConfiguration::convertJwtToken);
        return http.build();
    }

    private static JwtAuthenticationToken convertJwtToken(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, authorities);
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return new JwtAuthenticationToken(jwt, authorities, authenticationToken.getName());
    }
}
