package com.effcode.clean.me.core.spec;

import javax.validation.constraints.NotNull;

/**
 * Root interface for error enumeration descriptors.
 *
 * @param <E> error enumeration type
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IEnum
 */
public interface IError<E extends Enum<E> & IError<E>> extends IEnum<E> {

    /**
     * Accessor for the unique numeric error code.
     *
     * @return error code
     */
    @NotNull int code();

    /**
     * Accesor for the error message.
     *
     * @return error message
     */
    @NotNull String message();

    /**
     * Type of exception augmenting this error
     *
     * @return type of augmenting exception
     */
    @NotNull Class<? extends Exception> type();

    /**
     * Immediately builds and throws new exception associated with this error.
     *
     * @param errors array of additional error messages
     * @throws RuntimeException exception associated with error
     */
    void throwException(final String... errors) throws Exception;

    /**
     * Builds a new throwable exception instance associated with this error.
     *
     * @param errors array of additional error messages
     * @return error exception instance
     */
    Throwable buildException(final String... errors);
}
