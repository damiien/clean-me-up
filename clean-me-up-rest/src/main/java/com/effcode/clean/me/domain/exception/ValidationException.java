package com.effcode.clean.me.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Exception specialized and thrown on object validation failures, encapsulates the target object and list of
 * errors produced by validating the target object.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see DomainException
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST)
public class ValidationException extends DomainException {

    private final List<String> errors = new LinkedList<>();

    protected ValidationException(final @NotNull Error error, final Collection<String> errors) {
        super(error);
        if (errors != null) this.errors.addAll(errors);
    }

    /**
     * Accessor method for the validation constraint errors
     *
     * @return validation constraint errors
     */
    public final List<String> getErrors() {
        return errors;
    }

}
