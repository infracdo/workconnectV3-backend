package com.workconnectV3.workconnectV3_backend.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.SystemPropertiesJsonParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	String jwkSetUri = "https://wcdssi.apolloglobal.net:8443/auth/realms/workconnect-test/protocol/openid-connect/certs";

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authorize) -> authorize 
						.requestMatchers("/api/hi").permitAll()
						.anyRequest().permitAll()
				)
				.oauth2ResourceServer((oauth2) -> oauth2
						.jwt(withDefaults())
				)
				;
		return http.build();
	}

	private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
		PolicyEnforcerConfig config;

		try {
			ObjectMapper mapper = new ObjectMapper(new SystemPropertiesJsonParserFactory());
			mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
			config = mapper.readValue(getClass().getResourceAsStream("/policy-enforcer.json"), PolicyEnforcerConfig.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
			@Override
			public PolicyEnforcerConfig resolve(HttpRequest request) {
				return config;
			}
		});
	}

	@Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() { // Custom converter to map JWT roles to Spring Security authorities
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        
        // Set up the converter to map JWT roles into Spring Security authorities
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			// Extract the "realm_access" claim as a Map
             Map<String, Object> realmAccess = jwt.getClaim("realm_access");

			 // Extract the "resource_access" claim as a Map
			 Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        // Safely extract the roles list from the "realm_access" map
        List<String> roles = Collections.emptyList();

        if (realmAccess != null) {
            // Get the "roles" from "realm_access" and safely cast it
            Object rolesObj = realmAccess.get("roles");

            // If rolesObj is an instance of List, cast safely
            if (rolesObj instanceof List<?>) {
                // Check if the List contains String elements
                roles = ((List<?>) rolesObj).stream()
                        .filter(item -> item instanceof String)
                        .map(item -> (String) item)
                        .collect(Collectors.toList());
            }
        }

		// Process roles from "resource_access"
        if (resourceAccess != null) {
            Object clientAccessObj = resourceAccess.get("test-workconnect-client");
            if (clientAccessObj instanceof Map<?, ?>) {
                // Safe check for Map type before casting
                @SuppressWarnings("unchecked")  // Suppress the unchecked cast warning here
                Map<String, Object> clientAccess = (Map<String, Object>) clientAccessObj;

                if (clientAccess.get("roles") instanceof List) {
                    List<?> clientRoles = (List<?>) clientAccess.get("roles");
                    // Add the roles from resource_access (test-workconnect-client)
                    roles.addAll(clientRoles.stream()
                            .filter(role -> role instanceof String)
                            .map(role -> (String) role)
                            .collect(Collectors.toList()));
                }
            }
        }

            // Map roles to authorities (SimpleGrantedAuthority)
            return roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role)) // Add 'ROLE_' prefix if needed
                        .collect(Collectors.toList());
        });
        
        return converter;
    }

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
	}
}