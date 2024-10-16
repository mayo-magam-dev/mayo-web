package com.example.mayoweb.commons.config;

import com.example.mayoweb.commons.filter.FirebaseAuthFilter;
import com.google.cloud.firestore.Firestore;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final Firestore firestore;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
//                        .requestMatchers("/reservation-new-async", "/sse/reservations-new", "/reservation-proceed-async","/reservations-new","/user", "/stores", "/item-store").permitAll()
                        .anyRequest().access(this::isAllowedIp))
                .addFilterBefore(new FirebaseAuthFilter(firestore), UsernamePasswordAuthenticationFilter.class);
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://www.mayomagam.store", "https://mayomagam.store"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }
            }));

        return http.build();
    }

    private boolean isAllowedIp(HttpServletRequest request) {
        String clientIp = getClientIp(request);

        log.info("Client IP: {}", clientIp);

        List<String> allowedIps = new ArrayList<>();

        allowedIps.add("127.0.0.1");

        return allowedIps.contains(clientIp);
    }

    private String getClientIp(HttpServletRequest request) {

        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            log.info("X-Forwarded-For: {}", xForwardedFor.split(",")[0]);
            return xForwardedFor.split(",")[0];
        }

        log.info(request.getRemoteAddr());
        return request.getRemoteAddr();
    }
}
