package com.effcode.clean.me.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

/**
 * Authentication token related specialization of authentication exception
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see AuthException
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, code = HttpStatus.UNAUTHORIZED)
public class TokenException extends AuthException {

    protected TokenException(final @NotNull Error error) {
        super(error);
    }

    protected TokenException(final @NotNull Error error, final String message) {
        super(error, message);
    }
}
