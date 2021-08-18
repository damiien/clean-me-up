package com.effcode.clean.me.rest.configuration;

import com.effcode.clean.me.core.spec.IEnum;

/**
 * REST API response status enumeration set
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IEnum
 */
public enum ApiStatus implements IEnum<ApiStatus> {

    OK(Value.CODE_OK),
    CREATED(Value.CODE_CREATED),
    BAD_REQUEST(Value.CODE_BAD_REQUEST),
    UNAUTHORIZED(Value.CODE_UNAUTHORIZED),
    FORBIDDEN(Value.CODE_FORBIDDEN);

    private final int code;

    ApiStatus(int code) {
        this.code = code;
    }

    public final int code() {
        return code;
    }

    /**
     * Response status code values
     */
    public static final class Value {
        public static final int CODE_OK = 200;
        public static final String OK = CODE_OK + "";
        public static final int CODE_CREATED = 201;
        public static final String CREATED = CODE_CREATED + "";
        public static final int CODE_BAD_REQUEST = 400;
        public static final String BAD_REQUEST = CODE_BAD_REQUEST + "";
        public static final int CODE_UNAUTHORIZED = 401;
        public static final String UNAUTHORIZED = CODE_UNAUTHORIZED + "";
        public static final int CODE_FORBIDDEN = 403;
        public static final String FORBIDDEN = CODE_FORBIDDEN + "";

    }
}
