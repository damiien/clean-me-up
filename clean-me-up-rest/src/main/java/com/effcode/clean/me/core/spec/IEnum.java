package com.effcode.clean.me.core.spec;

import java.util.EnumSet;

/**
 * Generic enumeration type interface, an internal specification for enumeration set implementations
 *
 * @param <E> interface implementor type boundary
 * @author dame.gjorgjievski
 * @since 1.0
 */
public interface IEnum<E extends Enum<E> & IEnum<E>> {

    /**
     * Convenience equality comparison with another enumerated instance of same type.
     *
     * @param enumerated instance to compare to
     * @return {@code true} if instances are equal, {@code false} otherwise
     */
    default boolean is(final E enumerated) {
        return this.equals(enumerated);
    }

    /**
     * Checks if this enumeration instance equals any of the provided enumerated arguments
     *
     * @param first  first instance to match
     * @param others other instances to match
     * @return {@code true} if this instance matches any of provided enumerated arguments, {@code false}
     * otherwise
     */
    default boolean isAny(final E first, final E... others) {
        return EnumSet.of(first, others).contains((E) this);
    }

}
