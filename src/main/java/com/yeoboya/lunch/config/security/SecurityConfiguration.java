package com.yeoboya.lunch.config.security;

import com.yeoboya.lunch.config.security.filter.AuthenticationEntryPointImpl;
import com.yeoboya.lunch.config.security.filter.JwtAuthenticationFilter;
import com.yeoboya.lunch.config.security.filter.JwtExceptionFilter;
import com.yeoboya.lunch.config.security.handler.AccessDeniedHandlerImpl;
import com.yeoboya.lunch.config.security.handler.AuthenticationFailureHandlerImpl;
import com.yeoboya.lunch.config.security.handler.AuthenticationSuccessHandlerImpl;
import com.yeoboya.lunch.config.security.handler.LogoutSuccessHandlerImpl;
import com.yeoboya.lunch.config.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

//    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;
//    private final AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;
//    private final LogoutSuccessHandlerImpl logoutSuccessHandlerImpl;
//    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] PERMIT_URL_ARRAY = {
            "/order/recruits",
            "/user/sign-in",
            "/user/sign-up",
            "/user/sign-out",
            "/user/reissue",
            "/user/token-reissue",
            "/user/sendResetPasswordMail/**",
            "/user/resetPassword",
            "/docs/index.html",
            "/actuator/**",
            "/memory"
    };

    private static final String[] USER_URL_ARRAY = {
            "/order/**",
            "/item/**",
            "/member/**",
            "/shop/**",
            "/board/**"
    };

    private static final String[] ADMIN_URL_ARRAY = {
            "/user/authority",
    };

    private static final String[] TESTER_URL_ARRAY = {};

    // BCryptPasswordEncoder bean for password encoding
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager bean for handling authentication
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
    public SecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.httpBasic().disable();
        httpSecurity.formLogin().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers(PERMIT_URL_ARRAY).permitAll()
                .mvcMatchers(ADMIN_URL_ARRAY).access("hasRole('ADMIN') and @securityConfiguration.whitelist(request)")
                .mvcMatchers(TESTER_URL_ARRAY).hasRole("TESTER")
                .mvcMatchers(USER_URL_ARRAY).hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated();


        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointImpl)
                .accessDeniedHandler(accessDeniedHandlerImpl);

        return httpSecurity.build();
    }


    private static final String[] WHITELIST = {
            "121.179.12.72",
            "localhost",
            "127.0.0.1",
            "0:0:0:0:0:0:0:1"
    };

    private static final String[] BLACKLIST = {
            "10.0.0.1",
            "10.0.0.2"
    };

    public boolean whitelist(HttpServletRequest request) {
        log.warn("white client ip : {}", request.getRemoteAddr());

        IpAddressMatcher ipAddressMatcher;
        for (String ip: WHITELIST) {
            ipAddressMatcher = new IpAddressMatcher(ip);
            if (ipAddressMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public boolean blacklist(HttpServletRequest request) {
        log.warn("black client ip : {}", request.getRemoteAddr());

        IpAddressMatcher ipAddressMatcher;
        for (String ip: BLACKLIST) {
            ipAddressMatcher = new IpAddressMatcher(ip);
            if (ipAddressMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }


//    @Bean
//    public SecurityFilterChain formLoginFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable();
//        httpSecurity.httpBasic().disable();
//        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        httpSecurity.authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .mvcMatchers(PERMIT_URL_ARRAY).permitAll()
//                .mvcMatchers(ADMIN_URL_ARRAY).hasRole("ADMIN")
//                .mvcMatchers(USER_URL_ARRAY).hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated();
//
//
//        httpSecurity.rememberMe()
//                .rememberMeParameter("remember-me")
//                .tokenValiditySeconds(3600)
//                .alwaysRemember(true)
//                .userDetailsService(userDetailsService);
//
//        httpSecurity.formLogin()
//                .loginPage("/user/sign-in")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .successHandler(authenticationSuccessHandlerImpl)
//                .failureHandler(authenticationFailureHandlerImpl)
//                .and()
//                .logout()
//                .logoutSuccessHandler(logoutSuccessHandlerImpl)
//                .permitAll();
//
//        httpSecurity.exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPointImpl)
//                .accessDeniedHandler(accessDeniedHandlerImpl);
//
//        return httpSecurity.build();
//    }
}
