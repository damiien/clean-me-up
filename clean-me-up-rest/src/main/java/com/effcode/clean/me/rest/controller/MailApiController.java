package com.effcode.clean.me.rest.controller;

import com.effcode.clean.me.core.validation.ModelValidator;
import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.domain.model.UserPrincipal;
import com.effcode.clean.me.domain.service.MailService;
import com.effcode.clean.me.rest.configuration.ApiEndpoint;
import com.effcode.clean.me.rest.configuration.ApiStatus;
import com.effcode.clean.me.rest.configuration.ApplicationConfig;
import com.effcode.clean.me.rest.data.MessageRequest;
import com.effcode.clean.me.rest.data.MessageResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller exposing e-mail API endpoints for various mailing operations
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see RestController
 * @since 1.0
 */
@Tag(name = MailApiController.TAG, description = "Mail API")
@RestController
@RequestMapping(path = ApiEndpoint.Value.MAIL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MailApiController {

    protected static final String TAG = "mail";

    @Autowired
    private MailService service;

    /**
     * Handler method exposing an endpoint for sending a single e-mail message
     *
     * @param request        mail send request
     * @param authentication user authentication
     * @return sent mail message record
     */
    @Operation(summary = "Send Mail Message", description = "Sends an email message with current user", tags = {TAG},
            security = @SecurityRequirement(name = ApplicationConfig.SECURITY_SCHEME))
    @ApiResponses(value = {
            @ApiResponse(responseCode = ApiStatus.Value.CREATED, description = "Mail message send success",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = ApiStatus.Value.BAD_REQUEST, description = "Mail message send failure",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = ApiStatus.Value.UNAUTHORIZED,
                    description = "Mail message send failure due to unauthorized access",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/send")
    @PreAuthorize("isAuthenticated()")
    public Mono<MessageResponse> send(final @RequestBody MessageRequest request, final Authentication authentication) {
        request.setOrigin(((UserPrincipal) authentication.getPrincipal()).getUsername());
        new ModelValidator<MessageRequest>().validate(request, Error.MAIL_REQUEST_INVALID);
        return service.send(request.to()).map(m -> new MessageResponse().from(m));
    }

    /**
     * Handler method exposing an endpoint for listing all sent mail message records for
     * currently authenticated user.
     *
     * @param authentication user authentication
     * @return users' mail message records list
     */
    @Operation(summary = "View Mail Messages", description = "View email messages sent to or from current user, while " +
            "view all email messages as admin user", tags = {TAG},
            security = @SecurityRequirement(name = ApplicationConfig.SECURITY_SCHEME))
    @ApiResponses(value = {
            @ApiResponse(responseCode = ApiStatus.Value.OK, description = "Mail message collection retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class)))),
            @ApiResponse(responseCode = ApiStatus.Value.UNAUTHORIZED,
                    description = "Mail message collection retrieve failure due to unauthorized access",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/messages")
    @PreAuthorize("isAuthenticated()")
    public Mono<List<MessageResponse>> messages(final Authentication authentication) {
        return service.findMessages((UserPrincipal) authentication.getPrincipal())
                .map(r -> r.stream().map(m -> new MessageResponse().from(m)).collect(Collectors.toList()));
    }

}
