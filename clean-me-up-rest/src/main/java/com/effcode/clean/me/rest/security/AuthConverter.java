package com.effcode.clean.me.rest.security;

import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.domain.security.AuthManager;
import com.effcode.clean.me.rest.configuration.ApplicationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Converter component used for transforming server web exchange info into authentication info
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see ServerAuthenticationConverter
 * @since 1.0
 */
@Component
public class AuthConverter implements ServerAuthenticationConverter {

    private static final String BEARER = "Bearer ";
    private final AuthManager manager;
    private final ApplicationProperties properties;

    public AuthConverter(final AuthManager manager, final ApplicationProperties properties) {
        this.manager = manager;
        this.properties = properties;
    }

    @Override
    public Mono<Authentication> convert(final ServerWebExchange exchange) {

        final List<String> headers = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        final String header = headers != null && !headers.isEmpty() ? headers.get(0) : null;
        if (isAllowed(exchange.getRequest().getURI().getPath())) return Mono.empty();
        // extract token and apply token based authentication
        final String token = Optional.ofNullable(header)
                .map(h -> !h.trim().isEmpty() ? h.replace(BEARER, "").trim() : null)
                .map(h -> !h.trim().isEmpty() ? h : null)
                .orElseThrow(Error.AUTH_TOKEN_HEADER_NOT_FOUND::buildException);
        return manager.authenticate(token);
    }

    /**
     * Checks if a specified API URI is whitelisted
     *
     * @param uri specified URI to check
     * @return {@code true} if URI is allowed i.e. whitelisted, otherwise {@code false}
     */
    private boolean isAllowed(final String uri) {
        return properties.getPublicEndpoints().stream().anyMatch(uri::contains);
    }
}
