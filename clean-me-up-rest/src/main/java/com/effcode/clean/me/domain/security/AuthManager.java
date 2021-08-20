package com.effcode.clean.me.domain.security;

import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.domain.exception.TokenException;
import com.effcode.clean.me.domain.model.UserAuthority;
import com.effcode.clean.me.domain.model.UserPrincipal;
import com.effcode.clean.me.rest.configuration.ApplicationProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reactive authentication manager component, manages the credentials based authentication with credentials to token exchange.
 * Provides methods for authentication token creation and validation.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see ReactiveAuthenticationManager
 * @see Component
 */
@Component
public class AuthManager implements ReactiveAuthenticationManager {

    private static final Logger LOG = LoggerFactory.getLogger(AuthManager.class);
    private final List<UserPrincipal> registry = Arrays.asList(
            new UserPrincipal("user1@api.com", "user", Collections.singletonList(UserAuthority.USER)),
            new UserPrincipal("user2@api.com", "user", Collections.singletonList(UserAuthority.USER)),
            new UserPrincipal("user3@api.com", "user", Collections.singletonList(UserAuthority.USER)),
            new UserPrincipal("admin@api.com", "admin", Collections.singletonList(UserAuthority.ADMIN))
    );

    @Autowired
    private ApplicationProperties properties;

    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) throws AuthenticationException {

        return Mono.create(sink -> {
            // if anonymous authentication is present it is a public endpoint, skip further authentication
            if (authentication instanceof AnonymousAuthenticationToken) {
                ReactiveSecurityContextHolder.getContext()
                        .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
                sink.success(authentication);
                return;
            }
            // if authentication principal is set, use current authentication
            if (authentication.getPrincipal() instanceof UserPrincipal) {
                ReactiveSecurityContextHolder.getContext()
                        .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
                sink.success(authentication);
                return;
            }

            // extract credentials from request authentication
            final UsernamePasswordAuthenticationToken credentials = (UsernamePasswordAuthenticationToken) authentication;
            final String username = (String) credentials.getPrincipal();
            final String password = (String) credentials.getCredentials();
            LOG.debug("Attempting login with {} : {}", username, password);

            // resolve user by username
            final UserPrincipal user = findByUsername(username);
            if (user == null) {
                sink.error(Error.AUTH_CREDENTIAL_USERNAME_INVALID.buildException());
                return;
            }

            // match passwords or throw exception
            if (!user.getPassword().equals(password)) {
                sink.error(Error.AUTH_CREDENTIAL_PASSWORD_INVALID.buildException());
                return;
            }

            // generate token, authentication and set security context
            final String token = generateToken(user);
            final Authentication result = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
            user.setToken(token);
            user.setTimestamp(new Date());
            LOG.debug("Generated authentication token for user {} : {}", user.getUsername(), token);

            // persist in security context
            ReactiveSecurityContextHolder.getContext()
                    .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(result));
            sink.success(result);
        });
    }

    /**
     * Convenience method for authenticating an user from token
     *
     * @param token authentication token
     * @return user authentication
     * @throws AuthenticationException if token based authentication fails
     */
    public Mono<Authentication> authenticate(final String token) throws AuthenticationException {

        return Mono.create(sink -> {

            // validate token for expiration or interception
            validateToken(token);

            // resolve / lookup user principal by token
            final UserPrincipal user = registry.stream().filter(u -> u.getToken() != null && u.getToken().equals(token))
                    .findFirst().orElse(null);
            if (user == null) {
                sink.error(Error.AUTH_TOKEN_EXPIRED.buildException());
                return;
            }

            // persist in security context
            final Authentication authentication = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
            ReactiveSecurityContextHolder.getContext()
                    .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
            // publish to sink
            sink.success(authentication);
        });
    }

    /**
     * Convenient method for finding a user principal by username
     *
     * @param username the username credential
     * @return user principal matched by username or {@code null} if no match was found
     */
    public UserPrincipal findByUsername(final String username) {
        return registry.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    /**
     * Resolves and returns the user details of the currently logged in principal.
     *
     * @return logged in user details
     */
    public Mono<UserPrincipal> getPrincipal() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(ctx -> ctx.getAuthentication() != null)
                .map(ctx -> (UserPrincipal) ctx.getAuthentication().getPrincipal());
    }

    /**
     * Provides a collection of all registered users
     *
     * @return collection of registered users
     */
    public Mono<List<UserPrincipal>> getUsers() {
        return Mono.just(registry);
    }

    /**
     * Generate a new authentication token for specified authentication principal
     *
     * @param user authentication principal
     * @return newly generated authentication token
     */
    protected String generateToken(final UserPrincipal user) {

        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + properties.getJwtExpiryInterval());
        return Jwts.builder().setSubject(user.getUsername()).
                setIssuedAt(now).setExpiration(expiryDate).setAudience("You").setIssuer("Me").
                setId(user.getId().toString()).compressWith(CompressionCodecs.GZIP).
                signWith(SignatureAlgorithm.HS512, properties.getJwtSecretKey()).
                claim("id", user.getId().toString()).
                claim("username", user.getUsername()).
                claim("authorities",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                compact();
    }

    /**
     * Validate a specified authentication token
     *
     * @param token authentication token
     * @throws TokenException when token validation error occurs
     */
    protected void validateToken(final String token) throws TokenException {
        try {
            final Jws<Claims> result = (Jws<Claims>) Jwts.parser().setSigningKey(properties.getJwtSecretKey()).parse(token);
            if (result == null) throw Error.AUTH_TOKEN_INVALID.buildException();
            if (result.getSignature() == null) throw Error.AUTH_TOKEN_SIGNATURE_INVALID.buildException();
        } catch (final MalformedJwtException e) {
            throw Error.AUTH_TOKEN_MALFORMED.buildException();
        } catch (final SignatureException e) {
            throw Error.AUTH_TOKEN_SIGNATURE_INVALID.buildException();
        } catch (final ExpiredJwtException e) {
            throw Error.AUTH_TOKEN_EXPIRED.buildException();
        } catch (final UnsupportedJwtException e) {
            throw Error.AUTH_TOKEN_UNSUPPORTED.buildException();
        } catch (final IllegalArgumentException e) {
            throw Error.AUTH_TOKEN_INVALID.buildException();
        }
    }
}
