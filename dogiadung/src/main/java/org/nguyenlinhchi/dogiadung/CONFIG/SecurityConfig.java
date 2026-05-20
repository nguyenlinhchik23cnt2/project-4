package org.nguyenlinhchi.dogiadung.CONFIG;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .requestCache(cache -> cache.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(

                                // PUBLIC PAGES
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",
                                "/admin.html",
                                "/nga-customers.html",

                                // AUTH API
                                "/api/auth/**",

                                // STATIC
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/**/*.png",
                                "/**/*.jpg",

                                // ADMIN
                                "/nga-user.html"

                        ).permitAll()

                        // CUSTOMER API
                        .requestMatchers(HttpMethod.PUT, "/api/customers/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/customers/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/customers/**").permitAll()

                        // PRODUCT API
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/products/**").permitAll()

                        .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").permitAll()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable());

        return http.build();
    }
}