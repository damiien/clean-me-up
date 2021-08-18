package com.effcode.clean.me.rest.error;

import com.effcode.clean.me.core.mapper.ObjectMapper;
import com.effcode.clean.me.core.spec.IException;
import com.effcode.clean.me.core.spec.IModel;
import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.domain.exception.ValidationException;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Response error data transfer object.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IModel
 */
@JsonClassDescription("Response error data transfer object")
public class ErrorResponse implements IModel<ErrorResponse, Void> {

    @JsonPropertyDescription("An URL where the error has occurred")
    private String url;

    @JsonPropertyDescription("The URL request method")
    private HttpMethod method;

    @JsonPropertyDescription("The error response status code")
    private int status;

    @JsonPropertyDescription("Descriptive message of occurred error")
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ObjectMapper.DATE_FORMAT_PATTERN)
    @JsonPropertyDescription("Date & time when error has occurred")
    private Date timestamp;

    @JsonPropertyDescription("Unique error designator")
    private Error error;

    @JsonPropertyDescription("Type of exception kid that produced the error")
    private String kind;

    @JsonPropertyDescription("Collection of error messages")
    private List<String> errors;

    @JsonPropertyDescription("Underlying exception of this error response")
    @JsonIgnore
    private Throwable exception;

    /**
     * Default constructor
     */
    public ErrorResponse() {
    }

    /**
     * Construct response error with required properties
     *
     * @param exception throwable cause of this error
     * @param url       request url that resulted in error
     * @param status    request output status
     * @param method    request method
     */
    protected ErrorResponse(final Throwable exception, final String url, final HttpMethod method, final HttpStatus status) {
        this.exception = exception;
        this.kind = exception.getClass().getCanonicalName();
        this.url = url;
        this.method = method;
        this.status = status.value();
        this.timestamp = new Date();
        this.message = Optional.ofNullable(exception.getCause()).map(Throwable::getMessage).orElse(exception.getMessage());
        if (exception instanceof IException) {
            this.error = (Error) ((IException<?>) exception).getError();
        }
        if (exception instanceof ValidationException) {
            this.errors = ((ValidationException) exception).getErrors();
        }
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Error getError() {
        return error;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getKind() {
        return kind;
    }

    public Throwable getException() {
        return exception;
    }


}
