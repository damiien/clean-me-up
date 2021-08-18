package com.effcode.clean.me.domain.model;

import com.effcode.clean.me.core.spec.IModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Implementation of user security details data model
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see UserDetails
 * @see IModel
 * @since 1.0
 */
public class UserPrincipal implements UserDetails, IModel<UserPrincipal, UUID> {

    private final UUID id;

    private String username;

    private String password;

    private Collection<UserAuthority> authorities;

    private String token;

    private Date timestamp = new Date();

    private UserPrincipal() {
        this.id = UUID.randomUUID();
    }

    public UserPrincipal(final String username, final String password, final Collection<UserAuthority> authorities) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public final UUID getId() {
        return id;
    }

    /**
     * Accessor for the user principal authentication token
     *
     * @return authentication token
     */
    public final String getToken() {
        return token;
    }

    /**
     * Sets the authentication token for current principal
     *
     * @param token authentication token
     */
    public final void setToken(String token) {
        this.token = token;
    }

    /**
     * Accessor for the last login timestamp of this user principal.
     *
     * @return last login timestamp
     */
    public final Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the last login timestamp of this user principal
     *
     * @param timestamp last login timestamp
     */
    public final void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Checks if this user principal has a specified authority
     *
     * @param authority matching authority
     * @return {@code true} if user principal has authority, otherwise {@code false}
     */
    public final boolean hasAuthority(final UserAuthority authority) {
        return getAuthorities().stream().anyMatch(a -> authority.getAuthority().equals(a.getAuthority()));
    }

}
