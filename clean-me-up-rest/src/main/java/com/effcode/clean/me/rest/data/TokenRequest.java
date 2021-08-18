package com.effcode.clean.me.rest.data;

import com.effcode.clean.me.core.spec.IModel;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Authentication request data transfer object, contains user authentication credentials.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IModel
 * @since 1.0
 */
@JsonClassDescription("Data transfer model for authentication request")
public class TokenRequest implements IModel<TokenRequest, Void> {

    @JsonPropertyDescription("Authentication username credential")
    @NotBlank
    @Email
    private String username;

    @JsonPropertyDescription("Authentication password credential")
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    public TokenRequest() {}

    public TokenRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
