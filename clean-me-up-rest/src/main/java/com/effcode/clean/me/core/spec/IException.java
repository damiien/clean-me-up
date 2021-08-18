package com.effcode.clean.me.core.spec;

import java.io.Serializable;

/**
 * Root super interface for all domain exceptions.
 *
 * @param <E> exception type boundary
 * @author dame.gjorgjievski
 * @version 1.0
 * @see Serializable
 */
public interface IException<E extends Throwable> extends Serializable {

    /**
     * Convenience accessor method for the exception message
     *
     * @return exception message
     */
    String getMessage();

    /**
     * Accessor for the unique error designator of this exception.
     *
     * @return unique exception error
     */
    IError<?> getError();

    /**
     * Accessor for this exception object in bound type
     *
     * @return exception instance in bound type
     */
    default E getException() {
        return (E) this;
    }

    /**
     * Wraps a throwable instance in exception interface
     *
     * @param throwable specified throwable
     * @param <T>       type of throwable class
     * @return wrapped exception
     */
    static <T extends Throwable> IException<T> wrap(final T throwable) {
        return (IException<T>) throwable;
    }
}
