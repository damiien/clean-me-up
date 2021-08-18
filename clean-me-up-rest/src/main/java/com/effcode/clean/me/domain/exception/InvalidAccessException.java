package com.effcode.clean.me.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

/**
 * Exception specialized for specifying invalid access due to lack of permissions.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see AuthException
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, code = HttpStatus.FORBIDDEN)
public class InvalidAccessException extends AuthException {

    protected InvalidAccessException(final @NotNull Error error) {
        super(error);
    }

    protected InvalidAccessException(final @NotNull Error error, final String message) {
        super(error, message);
    }

}
