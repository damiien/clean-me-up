package com.effcode.clean.me.rest.configuration;

import com.effcode.clean.me.support.SmtpHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class, declares bean initializer methods and application properties mapping
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see Configuration
 * @see EnableConfigurationProperties
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfig {

    public static final String PATH = "/api";
    public static final String SECURITY_SCHEME = "Bearer";

    /**
     * SMTP mail handler bean initializer method
     *
     * @return singleton instance of SMTP mail handler
     */
    @Bean
    public SmtpHandler smtpHandler() {
        return new SmtpHandler();
    }

    /**
     * Open API descriptor bean initializer method
     *
     * @return open API descriptor
     */
    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI().components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
                        .name(SECURITY_SCHEME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                ));
    }

}
