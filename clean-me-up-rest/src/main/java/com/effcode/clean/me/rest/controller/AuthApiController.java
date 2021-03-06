package com.effcode.clean.me.rest.controller;

import com.effcode.clean.me.core.validation.ModelValidator;
import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.domain.service.UserService;
import com.effcode.clean.me.rest.configuration.ApiEndpoint;
import com.effcode.clean.me.rest.configuration.ApiStatus;
import com.effcode.clean.me.rest.configuration.ApplicationConfig;
import com.effcode.clean.me.rest.data.TokenRequest;
import com.effcode.clean.me.rest.data.TokenResponse;
import com.effcode.clean.me.rest.data.UserResponse;
import com.effcode.clean.me.rest.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authentication REST API controller, exposes endpoints for user authentication operations
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see RestController
 * @since 1.0
 */
@Tag(name = AuthApiController.TAG, description = "Auth API")
@RestController
@RequestMapping(path = ApiEndpoint.Value.AUTH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthApiController {

    protected static final String TAG = "auth";

    @Autowired
    private UserService service;

    /**
     * Handler method exposing an user credential authentication entry point. On successful authentication
     * user details info is returned including a token.
     *
     * @param request authentication request
     * @return authenticated user principal details including auth token
     */
    @Operation(summary = "Retrieve authentication token", description = "Exchange valid username and password credentials " +
            "for authentication token", tags = {TAG}, security = @SecurityRequirement(name = ApplicationConfig.SECURITY_SCHEME))
    @ApiResponses(value = {
            @ApiResponse(responseCode = ApiStatus.Value.OK, description = "Authentication token retrieved",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = ApiStatus.Value.BAD_REQUEST,
                    description = "Authentication token retrieve failure due to request validation or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/token")
    @PreAuthorize("isAnonymous()")
    public Mono<TokenResponse> authenticate(final @RequestBody TokenRequest request) {
        new ModelValidator<TokenRequest>().validate(request, Error.AUTH_CREDENTIALS_INVALID);
        return service.authenticate(request.getUsername(), request.getPassword()).map(TokenResponse::new);
    }

    /**
     * Handler method exposing an endpoint for displaying a collection of registered users,
     * accessible only to admin users
     *
     * @return collection of registered users
     */
    @Operation(summary = "View registered users", description = "Display collection of registered users", tags = {TAG},
            security = @SecurityRequirement(name = ApplicationConfig.SECURITY_SCHEME))
    @ApiResponses(value = {
            @ApiResponse(responseCode = ApiStatus.Value.OK, description = "Registered user collection retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = ApiStatus.Value.UNAUTHORIZED,
                    description = "Registered user collection retrieve failure due to unauthorized access",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = ApiStatus.Value.FORBIDDEN,
                    description = "Registered user collection retrieve failure due to missing authority",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Mono<List<UserResponse>> users() {
        return service.getUsers().map(r -> r.stream()
                .map(u -> new UserResponse().from(u)).collect(Collectors.toList()));
    }

    /**
     * Handler method exposing an endpoint for displaying the currently authenticated user details
     *
     * @return current authenticated user ubfo
     */
    @Operation(summary = "View authenticated user info", description = "Display info of authenticated user",
            tags = {TAG}, security = @SecurityRequirement(name = ApplicationConfig.SECURITY_SCHEME))
    @ApiResponses(value = {
            @ApiResponse(responseCode = ApiStatus.Value.OK, description = "Authenticated user info retrieved",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = ApiStatus.Value.UNAUTHORIZED,
                    description = "Authenticated user info retrieve failure due to unauthorized access",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/info")
    @PreAuthorize("isAuthenticated()")
    public Mono<UserResponse> user() {
        return service.getUser().map(u -> new UserResponse().from(u));
    }
}
