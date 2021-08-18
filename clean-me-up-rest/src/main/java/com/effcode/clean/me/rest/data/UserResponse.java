package com.effcode.clean.me.rest.data;

import com.effcode.clean.me.core.spec.IDataModel;
import com.effcode.clean.me.core.spec.IModel;
import com.effcode.clean.me.domain.model.UserPrincipal;
import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User details response data transfer object
 *
 * @author dame.gjorgjievski
 * @see IModel
 * @see IDataModel
 * @see UserPrincipal
 */
@JsonPropertyOrder({"id", "email", "authorities", "timestamp"})
@JsonClassDescription("User details response data transfer model")
public class UserResponse implements IModel<UserResponse, String>, IDataModel<UserResponse, UserPrincipal, String> {

    @JsonProperty("id")
    @JsonPropertyDescription("User identifier")
    private String id;

    @JsonPropertyDescription("User email address")
    private String email;

    @JsonPropertyDescription("User authorities")
    private List<String> authorities;

    @JsonPropertyDescription("User last login timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timestamp;

    @Override
    public UserResponse from(final UserPrincipal target) {
        this.id = target.getId().toString();
        this.email = target.getUsername();
        this.authorities = target.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        this.timestamp = target.getTimestamp();
        return this;
    }

    @JsonGetter
    @Override
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
