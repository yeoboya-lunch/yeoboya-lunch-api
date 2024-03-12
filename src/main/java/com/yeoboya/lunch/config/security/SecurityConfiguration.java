package com.yeoboya.lunch.config.security;

import com.yeoboya.lunch.config.security.factory.UrlResourcesMapFactoryBean;
import com.yeoboya.lunch.config.security.filter.AuthenticationEntryPointImpl;
import com.yeoboya.lunch.config.security.filter.JwtAuthenticationFilter;
import com.yeoboya.lunch.config.security.filter.JwtExceptionFilter;
import com.yeoboya.lunch.config.security.filter.PermitAllFilter;
import com.yeoboya.lunch.config.security.handler.AccessDeniedHandlerImpl;
import com.yeoboya.lunch.config.security.metaDataSource.UrlSecurityMetadataSource;
import com.yeoboya.lunch.config.security.service.RoleHierarchyService;
import com.yeoboya.lunch.config.security.service.SecurityResourceService;
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

    private final String[] permitAllPattern = {"/order/recruits"};

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;
    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final RoleHierarchyService roleHierarchyService;
    private final SecurityResourceService securityResourceService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

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
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        httpSecurity
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointImpl)
                .accessDeniedHandler(accessDeniedHandlerImpl)

                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .addFilterBefore(filter, CsrfFilter.class)
                .addFilterBefore(this.createPermitAllFilter(), FilterSecurityInterceptor.class);

        return httpSecurity.build();
    }

    // 2. HTTP 요청에 대응하는 필터 설정과 관련된 메소드.
    @Bean
    public PermitAllFilter createPermitAllFilter() {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllPattern);
        permitAllFilter.setAccessDecisionManager(affirmativeBased(securityResourceService));
        permitAllFilter.setSecurityMetadataSource(urlSecurityMetadataSource(securityResourceService));
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
    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource(SecurityResourceService securityResourceService) {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> object = this.urlResourcesMapFactoryBean(securityResourceService).getObject();
        return new UrlSecurityMetadataSource(object, securityResourceService);
    }

    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(SecurityResourceService securityResourceService){
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
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        return Arrays.asList(ipAddressVoter, authenticatedVoter, webExpressionVoter, roleVoter());
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }

}
