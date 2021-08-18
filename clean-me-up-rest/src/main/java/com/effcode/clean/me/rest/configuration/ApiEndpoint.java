package com.effcode.clean.me.rest.configuration;

import com.effcode.clean.me.core.spec.IEnum;

/**
 * REST API endpoint enumeration set
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IEnum
 * @since 1.0
 */
public enum ApiEndpoint implements IEnum<ApiEndpoint> {

    /**
     * Mail API endpoint
     */
    MAIL(Value.MAIL),

    /**
     * Auth API endpoint
     */
    AUTH(Value.AUTH);

    private final String path;

    ApiEndpoint(final String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

    /**
     * REST API endpoint path constant values
     */
    public static final class Value {
        public static final String MAIL = ApplicationConfig.PATH + "/" + ApiVersion.Value.V1 + "/mail";
        public static final String AUTH = ApplicationConfig.PATH + "/" + ApiVersion.Value.V1 + "/auth";
    }
}
