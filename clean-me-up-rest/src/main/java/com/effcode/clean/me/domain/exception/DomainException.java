package com.effcode.clean.me.domain.exception;

import com.effcode.clean.me.core.spec.IException;

import javax.validation.constraints.NotNull;

/**
 * Base abstraction of a domain exception as runtime exception. Augments an {@link Error} for unique error designation.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see RuntimeException
 * @see IException
 * @see Error
 */
abstract class DomainException extends RuntimeException implements IException<DomainException> {

    private final @NotNull Error error;

    protected DomainException(final @NotNull Error error) {
        super(error.message());
        this.error = error;
    }

    protected DomainException(final @NotNull Error error, final String message) {
        super(message);
        this.error = error;
    }

    @Override
    public final Error getError() {
        return error;
    }
}
