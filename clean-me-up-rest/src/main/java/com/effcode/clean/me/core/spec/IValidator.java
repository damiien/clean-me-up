package com.effcode.clean.me.core.spec;

import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.domain.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Root validator specification interface
 *
 * @param <T> type of validation target
 * @author dame.gjorgjievski
 * @version 1.0
 * @since 1.0
 */
public interface IValidator<T> {

    /**
     * Provides new instance of internal validator delegate
     *
     * @return delegate validator
     */
    default Validator delegate() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Validates a specified target object by using validation annotations introspection
     *
     * @param target object to validate
     * @return set of constraint violations detected during validation, empty set indicates validation success
     */
    default Set<ConstraintViolation<T>> validate(final T target) {
        return new HashSet<>();
    }

    /**
     * Validates a specified target object by using validation annotations introspection. If validation fails then a
     * validation exception is thrown with specified error message.
     *
     * @param target  object to validate
     * @param error validation error designator
     * @throws ValidationException if target object validation fails
     */
    default void validate(final T target, final Error error) throws ValidationException {
        final List<String> errors = validate(target).stream().
                map(cv -> "Field [" + cv.getPropertyPath() + "] " + cv.getMessage()).collect(Collectors.toList());
        if (!errors.isEmpty()) error.throwException(errors.toArray(new String[0]));
    }
}
