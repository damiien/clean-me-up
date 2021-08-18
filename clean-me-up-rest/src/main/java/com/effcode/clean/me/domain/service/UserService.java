package com.effcode.clean.me.domain.service;

import com.effcode.clean.me.domain.model.UserPrincipal;
import com.effcode.clean.me.domain.security.AuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service for user security operations, an in-memory implementation of security platform's user details service
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see UserDetailsService
 * @since 1.0
 */
@Service
public class UserService implements ReactiveUserDetailsService {

    @Autowired
    private AuthManager manager;

    @Override
    public Mono<UserDetails> findByUsername(final String username) {
        return Mono.just(manager.findByUsername(username));
    }

    /**
     * Authenticates an user with specified username and password credentials. After successful credentials exchange
     * an user token is generated to provide the user with further endpoint authorization and authentication. Stores
     * the authentication details in security context; This method delegates to {@link AuthManager#authenticate(Authentication)}
     *
     * @param username credential for username
     * @param password credential for password
     * @return user authentication token
     * @throws AuthenticationException if user authentication fails
     */
    public Mono<String> authenticate(final String username, final String password) throws AuthenticationException {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        return manager.authenticate(authentication).map(a -> (String) a.getCredentials());
    }

    /**
     * Convenience method delegating to {@link AuthManager#getPrincipal()}
     *
     * @return logged in user details
     */
    public Mono<UserPrincipal> getUser() {
        return manager.getPrincipal();
    }

    /**
     * Convenience method delegating to {@link AuthManager#getUsers()}
     *
     * @return collection of registered users
     */
    public Mono<List<UserPrincipal>> getUsers() {
        return manager.getUsers();
    }

}
