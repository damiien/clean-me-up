package com.effcode.clean.me.rest.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

/**
 * Authentication failure handler
 *
 * @author dame.gjorgjievski
 * @see ServerAuthenticationFailureHandler
 * @see ErrorHandler
 * @version 1.0
 */
public class AuthErrorHandler extends ErrorHandler implements ServerAuthenticationFailureHandler {

    public AuthErrorHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> onAuthenticationFailure(final WebFilterExchange exchange, final AuthenticationException e) {
        return super.handle(exchange.getExchange(), e);
    }
}
