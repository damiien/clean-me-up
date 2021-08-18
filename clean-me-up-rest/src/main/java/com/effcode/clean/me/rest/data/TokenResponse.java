package com.effcode.clean.me.rest.data;

import com.effcode.clean.me.core.spec.IModel;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * User authentication token data transfer object
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IModel
 */
@JsonClassDescription("User token wrapper data transfer model")
public class TokenResponse implements IModel<TokenResponse, Void> {

    @JsonPropertyDescription("User authentication token")
    private String token;

    public TokenResponse() {
    }

    public TokenResponse(final String token) {
        this.token = token;
    }

    public final String getToken() {
        return token;
    }
}
