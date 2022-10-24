package com.yeoboya.guinGujik.config.security;

import com.yeoboya.guinGujik.config.security.filter.AuthenticationEntryPointImpl;
import com.yeoboya.guinGujik.config.security.filter.JwtAuthenticationFilter;
import com.yeoboya.guinGujik.config.security.filter.JwtExceptionFilter;
import com.yeoboya.guinGujik.config.security.handler.AccessDeniedHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;
    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    //PasswordEncoder 구현 (BCryptPasswordEncoder)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //인증구현
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Deprecated
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.formLogin().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers("/member/sign-up", "/member/login", "/member/reissue", "/member/logout").permitAll()
                .mvcMatchers("/user").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                .mvcMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                //test pass
                .mvcMatchers("/sample/**").permitAll()
                .mvcMatchers("/items/**").permitAll()

                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandlerImpl)
                .authenticationEntryPoint(authenticationEntryPointImpl);

        return http.build();
    }


}