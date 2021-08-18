package com.effcode.clean.me.rest.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Authentication entry point implementation used for commencing authentication exceptions and writing
 * of error response information
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see ServerAuthenticationEntryPoint
 * @see ErrorHandler
 */
public class AuthEntryPoint extends ErrorHandler implements ServerAuthenticationEntryPoint {

    public AuthEntryPoint(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> commence(final ServerWebExchange exchange, final AuthenticationException e) {
        return super.handle(exchange, e);
    }

}
