package com.core.Authentification.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtAuthentificationFilter jwtAuthFilter;
	private final AuthenticationProvider authentificationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    corsConfig.setAllowedMethods(Collections.singletonList("*"));
                    corsConfig.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfig.setExposedHeaders(Collections.singletonList("Authorization"));
                    return corsConfig;
                }))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authentificationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build()
;	}
}
