package com.yeoboya.lunch.config.security;

import com.yeoboya.lunch.config.security.factory.UrlResourcesMapFactoryBean;
import com.yeoboya.lunch.config.security.filter.AuthenticationEntryPointImpl;
import com.yeoboya.lunch.config.security.filter.JwtAuthenticationFilter;
import com.yeoboya.lunch.config.security.filter.JwtExceptionFilter;
import com.yeoboya.lunch.config.security.filter.PermitAllFilter;
import com.yeoboya.lunch.config.security.handler.AccessDeniedHandlerImpl;
import com.yeoboya.lunch.config.security.handler.CustomOAuth2AuthenticationSuccessHandler;
import com.yeoboya.lunch.config.security.metaDataSource.UrlSecurityMetadataSource;
import com.yeoboya.lunch.config.security.service.RoleHierarchyService;
import com.yeoboya.lunch.config.security.service.SecurityResourceService;
import com.yeoboya.lunch.config.security.voter.IgnoreUrlVoter;
import com.yeoboya.lunch.config.security.voter.IpAddressVoter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final String[] permitAllPattern = {};

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final RoleHierarchyService roleHierarchyService;
    private final SecurityResourceService securityResourceService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;

    // 1. 프로그램에서 필요한 설정과 관련된 `@Bean`을 정의하는 메소드.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String rolesHierarchy = roleHierarchyService.findAllHierarchy();
        roleHierarchy.setHierarchy(rolesHierarchy);
        return roleHierarchy;
    }

    @Deprecated
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 인코딩 필터 설정: UTF-8 인코딩을 강제합니다.
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        // 보안 설정 시작
        httpSecurity
                .csrf()             // CSRF(Cross-Site Request Forgery) 공격 방어 설정
                .disable()          // CSRF 방어 비활성화

                // 세션 관련 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)      // 세션 생성 정책을 STATELESS로 설정

                .and()                                                     // 다른 보안 설정들로 넘어감
                .cors()                                                     // CORS(Cross-Origin Resource Sharing) 설정 활성화

                // 로그인 설정
                .and()
                .formLogin().disable()                                     // 폼 로그인 비활성
                .httpBasic().disable()                                     // HTTP Basic 로그인 비활성

                .authorizeRequests()
                .antMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/redoc",        // ✅ Redoc 경로 추가
                        "/redoc.html"    // ✅ Redoc 정적 HTML 추가
                ).permitAll()  // 인증 없이 접근 가능하도록 설정

                .anyRequest().authenticated()  // 그 외 요청은 JWT 인증 필요
                .and()

                // 예외 처리 설정
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointImpl)     // 인증 예외 발생 시 처리 설정
                .accessDeniedHandler(accessDeniedHandlerImpl)               // 접근이 거부됐을 때 핸들러 설정

                // 마지막에 필터 설정, 필터는 순서대로 실행됩니다.
                // 먼저 실행하려는 필터를 현재 필터보다 앞에 추가합니다.
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)    // JWT 인증 필터 추가
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)                      // JWT 예외 필터 추가
                .addFilterBefore(filter, CsrfFilter.class)                                              // 인코딩 필터 추가
                .addFilterBefore(this.createPermitAllFilter(), FilterSecurityInterceptor.class)           // 특정 경로의 접근 허용 필터 추가

                // OAuth2 로그인 설정
                .oauth2Login()
                .successHandler(customOAuth2AuthenticationSuccessHandler); // OAuth2 로그인 성공 핸들러 설정

        return httpSecurity.build();
    }

    // 2. HTTP 요청에 대응하는 필터 설정과 관련된 메소드.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:8080",
                        "http://localhost:3000",
                        "https://api.yeoboya-lunch.com",
                        "https://www.yeoboya-lunch.com",
                        "https://yeoboya-lunch.com")
        );
        config.addAllowedMethod("POST");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.setMaxAge(3600L);
        config.setAllowedHeaders(Arrays.asList("x-requested-with", "Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



    @Bean
    public PermitAllFilter createPermitAllFilter() {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllPattern);
        permitAllFilter.setAccessDecisionManager(affirmativeBased(securityResourceService));
        permitAllFilter.setSecurityMetadataSource(filterInvocationSecurityMetadataSource(securityResourceService));
        permitAllFilter.setRejectPublicInvocations(false);
        return permitAllFilter;
    }

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(this.createPermitAllFilter());
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource(SecurityResourceService securityResourceService) {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> object = this.urlResourcesMapFactoryBean(securityResourceService).getObject();
        return new UrlSecurityMetadataSource(object, securityResourceService);
    }

    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(SecurityResourceService securityResourceService) {
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        return urlResourcesMapFactoryBean;
    }

    // 3. 보안과 관련된 `@Bean`을 정의하는 메소드.
    @Bean
    public AccessDecisionManager affirmativeBased(SecurityResourceService securityResourceService) {
        AffirmativeBased accessDecisionManager = new AffirmativeBased(getAccessDecisionVoters(securityResourceService));
        accessDecisionManager.setAllowIfAllAbstainDecisions(false); // 접근 승인 거부 보류시 접근 허용은 true 접근 거부는 false
        return accessDecisionManager;
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters(SecurityResourceService securityResourceService) {
        IpAddressVoter ipAddressVoter = new IpAddressVoter(securityResourceService);
        IgnoreUrlVoter ignoreUrlVoter = new IgnoreUrlVoter(securityResourceService);
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        return Arrays.asList(ipAddressVoter, ignoreUrlVoter, authenticatedVoter, webExpressionVoter, roleVoter());
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }

}
