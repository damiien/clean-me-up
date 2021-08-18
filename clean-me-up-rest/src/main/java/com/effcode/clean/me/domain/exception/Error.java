package com.effcode.clean.me.domain.exception;

import com.effcode.clean.me.core.spec.IError;

import java.util.Arrays;

/**
 * Error type constant enumeration set, maps each of application domain errors to enumeration constant. To be used when
 * throwing exceptions in domain layer.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IError
 */
public enum Error implements IError<Error> {

    /**
     * Invalid username credential detected during authentication
     */
    AUTH_USERNAME_INVALID(410, "Invalid username credential", CredentialsException.class),
    /**
     * Invalid password credential detected during authentication
     */
    AUTH_PASSWORD_INVALID(411, "Invalid password credential", CredentialsException.class),
    /**
     * Invalid authentication credentials format due to bean validation
     */
    AUTH_CREDENTIALS_INVALID(412, "Invalid authentication credentials format", CredentialsException.class),
    /**
     * Invalid authentication token was found that could not be parsed
     */
    AUTH_TOKEN_INVALID(413, "Invalid authentication token", TokenException.class),
    /**
     * Invalid authentication token request was found due to request data validation errors
     */
    AUTH_TOKEN_REQUEST_INVALID(414, "Invalid authentication token request", ValidationException.class),
    /**
     * Invalid authentication token signature found, points to security issue as request forging
     */
    AUTH_TOKEN_SIGNATURE_INVALID(415, "Invalid authentication token signature", TokenException.class),
    /**
     * Expired authentication token was found
     */
    AUTH_TOKEN_EXPIRED(416, "Expired authentication token", TokenException.class),
    /**
     * Malformed authentication token content detected
     */
    AUTH_TOKEN_MALFORMED(417, "Malformed authentication token", TokenException.class),
    /**
     * Unsupported authentication token was found
     */
    AUTH_TOKEN_UNSUPPORTED(418, "Unsupported authentication token", TokenException.class),
    /**
     * Illegal arguments in authentication token
     */
    AUTH_TOKEN_ILLEGAL(419, "Illegal authentication token", TokenException.class),
    /**
     * Authentication token header was not found
     */
    AUTH_TOKEN_HEADER_NOT_FOUND(420, "Authentication token header not found", TokenException.class),
    /**
     * Resource path access is denied
     */
    AUTH_INVALID_ACCESS(421, "Not authorized to view resource, access denied", InvalidAccessException.class),
    /**
     * Mail send request validation failure error
     */
    MAIL_REQUEST_INVALID(510, "Mail send request invalid", ValidationException.class),
    /**
     * Mail destination address host invalid
     */
    MAIL_HOST_INVALID(511, "Invalid email address host", MailException.class);

    private final int code;
    private final String message;
    private final Class<? extends Exception> type;

    Error(final int code, final String message, final Class<? extends Exception> type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    @Override
    public final int code() {
        return code;
    }

    @Override
    public final String message() {
        return message;
    }

    @Override
    public final Class<? extends Exception> type() {
        return type;
    }

    @Override
    public void throwException(final String... errors) throws RuntimeException {
        final RuntimeException ex = buildException(errors);
        if (ex != null) throw ex;
        else throw new UnsupportedOperationException("Error exception not defined");
    }

    @Override
    public RuntimeException buildException(final String... errors) {
        RuntimeException ex = null;
        if (MailException.class.equals(type())) {
            ex = errors.length > 0 ? new MailException(this, errors[0]) : new MailException(this);
        } else if (ValidationException.class.equals(type())) {
            ex = new ValidationException(this, Arrays.asList(errors));
        } else if (AuthException.class.equals(type())) {
            ex = errors.length > 0 ? new AuthException(this, errors[0]) : new AuthException(this);
        } else if (TokenException.class.equals(type())) {
            ex = errors.length > 0 ? new TokenException(this, errors[0]) : new TokenException(this);
        } else if (CredentialsException.class.equals(type())) {
            ex = errors.length > 0 ? new CredentialsException(this, errors[0]) : new CredentialsException(this);
        } else if (InvalidAccessException.class.equals(type())) {
            ex = errors.length > 0 ? new InvalidAccessException(this, errors[0]) : new InvalidAccessException(this);
        }
        return ex;
    }


}
