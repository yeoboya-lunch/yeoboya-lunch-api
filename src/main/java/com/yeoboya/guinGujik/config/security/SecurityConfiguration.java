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

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v3 */
            "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**",

            /* member */
            "/member/sign-up", "/member/login", "/member/reissue", "/member/logout",

            /* monitor */
            "/actuator/**"
    };

    private static final String[] TEMP_URL_ARRAY = {
            "/v1/**", "/item/**"
    };


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
                .mvcMatchers(PERMIT_URL_ARRAY).permitAll()
                .mvcMatchers(TEMP_URL_ARRAY).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandlerImpl)
                .authenticationEntryPoint(authenticationEntryPointImpl);

        return http.build();
    }


}