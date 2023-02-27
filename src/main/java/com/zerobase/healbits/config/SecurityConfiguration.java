package com.zerobase.healbits.config;

import com.zerobase.healbits.jwt.JwtAuthenticationFilter;
import com.zerobase.healbits.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()//rest api이므로 basic auth 및 csrf 보안을 사용하지 않는다는 설정이다.
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정이다.
                .and()
                .authorizeRequests()
                .antMatchers("/member/login", "/member/register").permitAll() //해당 API에 대해서는 모든 요청을 허가한다는 설정이다.
                .anyRequest().hasRole("USER") //이 밖의 모든 요청은 USER 권한이 있어야 요청할 수 있다는 설정이다.
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); //: JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정이다.

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
