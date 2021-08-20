package com.effcode.clean.me.rest.configuration;

import com.effcode.clean.me.domain.security.AuthManager;
import com.effcode.clean.me.rest.error.AccessDeniedHandler;
import com.effcode.clean.me.rest.error.AuthEntryPoint;
import com.effcode.clean.me.rest.error.AuthErrorHandler;
import com.effcode.clean.me.rest.security.AuthConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Web Flux configuration used for setup of reactive processing environment, primarily the security aspects. Enables the
 * web flux and web flux security auto-configuration.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see Configuration
 * @see EnableWebFlux
 * @see EnableWebFluxSecurity
 * @see EnableReactiveMethodSecurity
 * @see WebFluxConfigurer
 */
@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxConfig implements WebFluxConfigurer {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Web Flux security filter chain bean initializer
     *
     * @param http       server HTTP security
     * @param manager    authentication manager
     * @param converter  authentication converter
     * @param properties application properties
     * @return security web filter chain bean instance
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http, final AuthManager manager,
                                                      final AuthConverter converter, final ApplicationProperties properties) {

        final AuthenticationWebFilter filter = new AuthenticationWebFilter(manager);
        filter.setServerAuthenticationConverter(converter);
        filter.setAuthenticationFailureHandler(new AuthErrorHandler(mapper));
        return http
                .exceptionHandling()
                .authenticationEntryPoint(new AuthEntryPoint(mapper))
                .accessDeniedHandler(new AccessDeniedHandler(mapper)).and()
                .authenticationManager(manager)
                .securityContextRepository(securityContextRepository())
                .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(properties.getPublicEndpoints().toArray(new String[0])).permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    /**
     * Security context repository bean initializer
     *
     * @return security context repository
     */
    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }

    @Override
    public void configureHttpMessageCodecs(final ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
    }

}
