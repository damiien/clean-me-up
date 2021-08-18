package com.effcode.clean.me.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Specialized type of authentication exception, pointing to bad authentication credentials.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see AuthException
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, code = HttpStatus.UNAUTHORIZED)
public class CredentialsException extends AuthException {

    protected CredentialsException(Error error) {
        super(error);
    }

    protected CredentialsException(Error error, String message) {
        super(error, message);
    }
}
