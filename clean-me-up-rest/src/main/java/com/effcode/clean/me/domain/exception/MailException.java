package com.effcode.clean.me.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

/**
 * Exception specialized and thrown on mail operation failures
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see DomainException
 * @since 1.0
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, value = HttpStatus.BAD_REQUEST)
public class MailException extends DomainException {

    protected MailException(final @NotNull Error error) {
        super(error);
    }

    protected MailException(final @NotNull Error error, final String message) {
        super(error, message);
    }

}
