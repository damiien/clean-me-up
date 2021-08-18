package com.effcode.clean.me.rest.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

/**
 * Abstraction of a generic error handler, augments an object mapper instance to compose a local error response encoder
 *
 * @author dame.gjorgjievski
 * @see Jackson2JsonEncoder
 */
abstract class ErrorHandler {

    private final Jackson2JsonEncoder encoder;

    /**
     * Construct an error handler with specified JSON encoder used for error output
     *
     * @param mapper object mapper
     */
    protected ErrorHandler(final ObjectMapper mapper) {
        this.encoder = new Jackson2JsonEncoder(mapper);
    }

    /**
     * Handle exception thrown in current web exchange context
     *
     * @param exchange web exchange
     * @param ex       thrown exception
     * @return handled response
     */
    protected Mono<Void> handle(final ServerWebExchange exchange, final Exception ex) {

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // resolve response status
        if (ex instanceof AccessDeniedException) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        } else exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // compose error response
        final ErrorResponse error = new ErrorResponse(ex, exchange.getRequest().getURI().getPath(),
                exchange.getRequest().getMethod(), HttpStatus.UNAUTHORIZED);
        exchange.getResponse()
                .writeWith(getEncoder().encode(Mono.just(error), exchange.getResponse().bufferFactory(),
                        ResolvableType.forInstance(error), MediaType.APPLICATION_JSON,
                        Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())));
        return Mono.error(ex);
    }

    /**
     * Provides access to JSON encoder
     *
     * @return json encoder instance
     */
    protected Jackson2JsonEncoder getEncoder() {
        return encoder;
    }
}
