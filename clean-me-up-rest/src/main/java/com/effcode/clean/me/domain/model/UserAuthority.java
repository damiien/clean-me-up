package com.effcode.clean.me.domain.model;

import com.effcode.clean.me.core.spec.IEnum;
import org.springframework.security.core.GrantedAuthority;

/**
 * Granted user authority enumeration set
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IEnum
 * @see GrantedAuthority
 * @since 1.0
 */
public enum UserAuthority implements IEnum<UserAuthority>, GrantedAuthority {

    /**
     * Regular user authority
     */
    USER(Value.USER),

    /**
     * Admin user authority
     */
    ADMIN(Value.ADMIN),

    /**
     * Anonymous user authority
     */
    ANONYMOUS(Value.ANONYMOUS);

    private final String authority;

    UserAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public final String getAuthority() {
        return authority;
    }

    /**
     * User security authority constant values
     */
    public static final class Value {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
        public static final String ANONYMOUS = "ANONYMOUS";
    }
}
