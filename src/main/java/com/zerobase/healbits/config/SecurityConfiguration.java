package com.zerobase.healbits.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Order(0)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable();

        return httpSecurity.build();
    }


    @Bean
    public SecurityFilterChain resources(HttpSecurity http) throws Exception {
        //example
        http.authorizeRequests()
                .antMatchers("/member/register")
                .permitAll()
                .anyRequest()
                .authenticated();

        return http.build();
    }
}
