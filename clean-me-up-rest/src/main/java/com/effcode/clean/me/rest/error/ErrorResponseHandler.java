package com.effcode.clean.me.rest.error;

import com.effcode.clean.me.core.mapper.ObjectMapper;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.Hints;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Error response exception handler component used for transforming thrown exceptions into readable
 * {@link ErrorResponse error responses}
 *
 * @author dame.gjorgjievski
 * @see ErrorResponse
 * @see WebExceptionHandler
 */
@Component
@Order(-2)
public class ErrorResponseHandler implements WebExceptionHandler {

    private final Jackson2JsonEncoder encoder = new Jackson2JsonEncoder(ObjectMapper.instance());

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        final ErrorResponse error = new ErrorResponse(ex, exchange.getRequest().getURI().getPath(),
                exchange.getRequest().getMethod(), resolveStatus(ex));
        exchange.getResponse().setStatusCode(HttpStatus.resolve(error.getStatus()));
        return exchange.getResponse()
                .writeWith(encoder.encode(Mono.just(error), exchange.getResponse().bufferFactory(),
                        ResolvableType.forInstance(error), MediaType.APPLICATION_JSON,
                        Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())));
    }

    /**
     * Resolves a HTTP status code from a specified throwable
     *
     * @param ex throwable
     * @return resolved HTTP status, default is {@link HttpStatus#INTERNAL_SERVER_ERROR}
     */
    private HttpStatus resolveStatus(final Throwable ex) {
        final ResponseStatus status = ex.getClass().getAnnotation(ResponseStatus.class);
        if (status != null) return status.code();
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
