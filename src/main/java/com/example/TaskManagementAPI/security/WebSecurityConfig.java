package com.example.TaskManagementAPI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;


    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public WebSecurityConfig(UserDetailsService userDetailsService, JwtUtil jwtUtil, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .requestMatchers("/authenticate").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/error/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(
                jwtAuthorizationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

}
