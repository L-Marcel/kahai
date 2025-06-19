package org.kahai.framework.config;

import java.util.Arrays;
import java.util.List;

import org.kahai.framework.KahaiProperties;
import org.kahai.framework.auth.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    private final List<String> allowedOrigins;

    public SecurityConfig(KahaiProperties properties) {
        this.allowedOrigins = List.of(properties.getCors().getAllowedOrigins());
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    };

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http
    ) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                (auth) -> auth
                    .anyRequest()
                    .permitAll()
            ).sessionManagement(
                (session) -> session
                    .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                    )
            ).addFilterBefore(
                jwtAuthFilter, 
                UsernamePasswordAuthenticationFilter.class
            ).build();
    };

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(allowedOrigins);
        
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        
        configuration.setAllowedHeaders(
            Arrays.asList(
                "Authorization", 
                "Content-Type"
            )
        );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
            "/**", 
            configuration
        );

        return source;
    };
};