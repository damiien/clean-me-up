package com.effcode.clean.me.rest.configuration;

import com.effcode.clean.me.core.spec.IEnum;

/**
 * REST API general version enumeration set
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IEnum
 * @since 1.0
 */
public enum ApiVersion implements IEnum<ApiVersion> {

    /**
     * API version 1
     */
    V1(Value.V1),

    /**
     * API version 2
     */
    V2(Value.V2),

    /**
     * API version 3
     */
    V3(Value.V3);

    private final String value;

    ApiVersion(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    /**
     * REST API version constant values
     */
    public static final class Value {
        public static final String V1 = "v1";
        public static final String V2 = "v2";
        public static final String V3 = "v2";
    }
}
