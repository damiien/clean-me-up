package com.effcode.clean.me.core.validation;

import com.effcode.clean.me.core.spec.IModel;
import com.effcode.clean.me.core.spec.IValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Generic validator for annotation based validation of annotated data models. Delegates the validation process to
 * the platform registered {@link Validator} instance.
 *
 * @param <M> type of validation model
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IValidator
 * @see Validator
 * @since 1.0
 */
public final class ModelValidator<M extends IModel<M, ?>> implements IValidator<M> {

    @Override
    public Set<ConstraintViolation<M>> validate(final M target) {
        return delegate().validate(target);
    }

}
