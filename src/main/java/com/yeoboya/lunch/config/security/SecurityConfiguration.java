package com.yeoboya.lunch.config.security;

import com.yeoboya.lunch.config.security.filter.AuthenticationEntryPointImpl;
import com.yeoboya.lunch.config.security.filter.JwtAuthenticationFilter;
import com.yeoboya.lunch.config.security.filter.JwtExceptionFilter;
import com.yeoboya.lunch.config.security.handler.AccessDeniedHandlerImpl;
import com.yeoboya.lunch.config.security.handler.AuthenticationFailureHandlerImpl;
import com.yeoboya.lunch.config.security.handler.AuthenticationSuccessHandlerImpl;
import com.yeoboya.lunch.config.security.handler.LogoutSuccessHandlerImpl;
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

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;
    private final AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;
    private final LogoutSuccessHandlerImpl logoutSuccessHandlerImpl;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] PERMIT_URL_ARRAY = {

            /* user */
            "/user/sign-up", "/user/sign-in", "/user/reissue", "/user/sign-out",

            /* monitor */
            "/actuator/**",

    };

    private static final String[] USER_URL_ARRAY = {
            "/order/**", "/shop/**", "/item/**",
    };

    private static final String[] ADMIN_URL_ARRAY = {
            "/member/**",
            "/user/authority",

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

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers(PERMIT_URL_ARRAY).permitAll()
                .mvcMatchers(ADMIN_URL_ARRAY).hasRole("ADMIN")
                .mvcMatchers(USER_URL_ARRAY).hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated();

        //fixme formLogin
        http.formLogin().disable();
        http.formLogin()
                .loginPage("/user/sign-in")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandlerImpl)
                .failureHandler(authenticationFailureHandlerImpl)
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandlerImpl)
                .permitAll();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);



        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointImpl)
                .accessDeniedHandler(accessDeniedHandlerImpl);


        return http.build();
    }


}