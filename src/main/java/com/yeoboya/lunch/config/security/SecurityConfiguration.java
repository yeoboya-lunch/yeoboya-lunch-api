package com.yeoboya.lunch.config.security;

import com.yeoboya.lunch.config.security.filter.AuthenticationEntryPointImpl;
import com.yeoboya.lunch.config.security.filter.JwtAuthenticationFilter;
import com.yeoboya.lunch.config.security.filter.JwtExceptionFilter;
import com.yeoboya.lunch.config.security.handler.AccessDeniedHandlerImpl;
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

//    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;
//    private final AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;
//    private final LogoutSuccessHandlerImpl logoutSuccessHandlerImpl;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] PERMIT_URL_ARRAY = {

            /* no login access*/
            "/order/recruits",
            "/board",

            /* user */
            "/user/sign-in", "/user/sign-up", "/user/sign-out",
            "/user/reissue", "/user/token-reissue", "/user/sendResetPasswordMail/**",
            "/user/resetPassword",

            /* spring docs */
            "/docs/index.html",

            /* monitor */
            "/actuator/**",
    };

    private static final String[] USER_URL_ARRAY = {
            "/order/**", "/item/**",
            "/member/**", "/shop/**",
//            "/board/**"
    };

    private static final String[] ADMIN_URL_ARRAY = {
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
        http.formLogin().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers(PERMIT_URL_ARRAY).permitAll()
                .mvcMatchers(ADMIN_URL_ARRAY).hasRole("ADMIN")
                .mvcMatchers(USER_URL_ARRAY).hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated();

        // formLogin 방식으로 변경 할 경우 (성공/실패/로그아웃)
        // formLogin 방식이 아닌 경우 AuthenticationSuccessHandler 에서 처리하는게 맞는지?
//        http.formLogin()
//                .loginPage("/user/sign-in")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .successHandler(authenticationSuccessHandlerImpl)
//                .failureHandler(authenticationFailureHandlerImpl)
//                .and()
//                .logout()
//                .logoutSuccessHandler(logoutSuccessHandlerImpl)
//                .permitAll();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);



        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointImpl)
                .accessDeniedHandler(accessDeniedHandlerImpl);


        return http.build();
    }


}