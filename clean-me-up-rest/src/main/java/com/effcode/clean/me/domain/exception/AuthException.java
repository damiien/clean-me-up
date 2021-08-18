package com.effcode.clean.me.domain.exception;

import com.effcode.clean.me.core.spec.IException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

/**
 * Application domain related specialization of security platform authentication exception.
 * Augments an {@link Error} for unique error designation.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see AuthenticationException
 * @see IException
 * @see Error
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, code = HttpStatus.UNAUTHORIZED)
public class AuthException extends AuthenticationException implements IException<AuthException> {

    private final @NotNull Error error;

    protected AuthException(final @NotNull Error error) {
        super(error.message());
        this.error = error;
    }

    protected AuthException(final @NotNull Error error, final String message) {
        super(message);
        this.error = error;
    }

    @Override
    public Error getError() {
        return error;
    }
}
