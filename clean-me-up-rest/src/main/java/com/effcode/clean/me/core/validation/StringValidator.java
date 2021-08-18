package com.effcode.clean.me.core.validation;

import java.util.regex.Pattern;

/**
 * String validation handler, encapsulates a target string to provide extended validation functionalities
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @since 1.0
 */
public class StringValidator {

    private final String target;

    /**
     * Creates new validator instance with a specified target string
     *
     * @param target string to validate
     */
    public StringValidator(final String target) {
        this.target = target;
    }

    /**
     * Accessor method for this validator's target string
     *
     * @return validation target string
     */
    public final String getTarget() {
        return target;
    }

    /**
     * Checks if the target string has a value set or is {@code null}
     *
     * @return {@code true} if target string is {@code null}, otherwise {@code false} if target string has a value
     */
    public final boolean isNull() {
        return target == null;
    }

    /**
     * Checks if the target string is blank (is not {@link #isNull() set} or is empty with 0 length)
     *
     * @return {@code true} if target string is empty, otherwise {@code false}
     */
    public final boolean isBlank() {
        return isNull() || target.trim().length() == 0;
    }

    /**
     * Checks if the target string is {@link #isNull() set} and it matches the specified regex pattern
     *
     * @param regex pattern to match
     * @return {@code true} if target string matches the specified pattern, otherwise {@code false}
     */
    public final boolean matches(final String regex) {
        return isNull() && Pattern.matches(regex, target);
    }

}
