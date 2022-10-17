package com.yeoboya.guinGujik.config.security;

import com.yeoboya.guinGujik.config.security.filter.AuthenticationEntryPointImpl;
import com.yeoboya.guinGujik.config.security.filter.JwtAuthenticationFilter;
import com.yeoboya.guinGujik.config.security.filter.JwtExceptionFilter;
import com.yeoboya.guinGujik.config.security.handler.AccessDeniedHandlerImpl;
import com.yeoboya.guinGujik.config.security.handler.LogoutHandlerImpl;
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
    private final LogoutHandlerImpl logoutHandler;

    //PasswordEncoder 구현 (BCryptPasswordEncoder)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Test Source
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
//    }

    //인증구현
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/member/sign-up",
                "/member/login"
        );
    }

    // Test Source
//    @Bean
//    public UserDetailsService userDetailsService(){
//        var manager = new InMemoryUserDetailsManager();
//        var user1 = User.withUsername("hyunjin@outlook.kr").password("12345").roles("ADMIN").build();
//        manager.createUser(user1);
//        return manager;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers("/member/login").permitAll()
                .mvcMatchers("/user").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                .mvcMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandlerImpl)
                .authenticationEntryPoint(authenticationEntryPointImpl);

        http.logout()
                .logoutUrl("/member/logout")
                .addLogoutHandler(logoutHandler);

        return http.build();
    }


}