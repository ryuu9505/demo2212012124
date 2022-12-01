package com.example.demo5new.security.config;

import com.example.demo5new.security.factory.UrlResourcesMapFactoryBean;
import com.example.demo5new.security.handler.CustomAccessDeniedHandler;
import com.example.demo5new.security.handler.CustomAuthenticationFailureHandler;
import com.example.demo5new.security.handler.CustomAuthenticationSuccessHandler;
import com.example.demo5new.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.example.demo5new.security.provider.CustomAuthenticationProvider;
import com.example.demo5new.security.service.SecurityResourceService;
import com.example.demo5new.security.voter.IpAddressVoter;
import com.example.demo5new.service.impl.CustomUserDetailsService;
import com.example.demo5new.service.user.CustomOAuth2UserService;
import com.example.demo5new.service.user.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider authenticationProvider;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UrlResourcesMapFactoryBean urlResourcesMapFactoryBean;
    private final SecurityResourceService securityResourceService;
    private final RoleHierarchyImpl roleHierarchy;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomOidcUserService oidcUserService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ProviderManager authenticationManager = (ProviderManager)authenticationConfiguration.getAuthenticationManager();
        authenticationManager.getProviders().add(authenticationProvider);
        return authenticationManager;
    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filterSecurityInterceptor;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean.getObject(), securityResourceService);
    }

    private AccessDecisionManager affirmativeBased() {
        AffirmativeBased affirmativeBased = new AffirmativeBased(getAccessDecisionVoters());
        return affirmativeBased;
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
//        todo accessDecisionVoters.add(new IpAddressVoter(securityResourceService));
        accessDecisionVoters.add(roleVoter());
        return accessDecisionVoters;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy);
        return roleHierarchyVoter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests((requests) -> requests
                        .antMatchers("/", "/login*", "/signup").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login_proc")
                        .defaultSuccessUrl("/")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout") // default = "/logout"
                        .permitAll())
                .rememberMe((rememberMe) -> rememberMe
                        .tokenValiditySeconds(3600)
                        .userDetailsService(userDetailsService))
                .exceptionHandling((ex) -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .addFilterBefore(customFilterSecurityInterceptor(authenticationConfiguration), FilterSecurityInterceptor.class)
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(oAuth2UserService)
                                .oidcUserService(oidcUserService)))
                .userDetailsService(userDetailsService);


        return http.build();
    }




    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
