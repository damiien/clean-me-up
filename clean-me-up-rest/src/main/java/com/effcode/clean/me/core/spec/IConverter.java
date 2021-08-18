package com.effcode.clean.me.core.spec;

/**
 * Type converter specification interface, declares conversion of one type instance to another type instance.
 *
 * @param <F> convert from type
 * @param <T> convert to type
 */
public interface IConverter<F, T> {

    /**
     * Converts from an instance to another type instance
     *
     * @param from instance from
     * @return converted to instance
     */
    public T convert(final F from);
}
