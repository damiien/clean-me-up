package com.effcode.clean.me.rest.error;

import com.effcode.clean.me.domain.exception.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Access denied handler component used for output of error response information on access denied exception
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see ServerAccessDeniedHandler
 * @see ErrorHandler
 */
public class AccessDeniedHandler extends ErrorHandler implements ServerAccessDeniedHandler {

    public AccessDeniedHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final AccessDeniedException denied) {
        return super.handle(exchange, Error.AUTH_INVALID_ACCESS.buildException());
    }
}
