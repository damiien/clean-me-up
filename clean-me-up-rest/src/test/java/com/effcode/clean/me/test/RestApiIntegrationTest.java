package com.effcode.clean.me.test;

import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.rest.error.ErrorResponse;
import com.effcode.clean.me.rest.data.*;
import io.jsonwebtoken.lang.Assert;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * REST API integration tests used for verification of user authentication, API connectivity and data consistency
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see SpringBootTest
 * @see Test
 */
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "spring.main.web-application-type=reactive")
public class RestApiIntegrationTest {

    private static final String ERR_NO_RESPONSE = "Failed to retrieve response";
    private static final String ERR_NO_ERROR = "Failed to parse error from response";
    private static final String ERR_NO_UNAUTHORIZED_STATUS = "Failed to parse error from response";

    private static final Logger LOG = LoggerFactory.getLogger(RestApiIntegrationTest.class);
    /**
     * REST client instance used for issuing testing requests
     */
    private final WebClient client;

    public RestApiIntegrationTest() throws SSLException {

        final SslContext ctx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        final HttpClient http = HttpClient.create().secure(t -> t.sslContext(ctx) );
        client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(http)).baseUrl("https://localhost:8080/api/v1").build();
    }

    /**
     * Performs full integration test on REST API application
     */
    @Test
    public void testRestApi() {

        ClientResponse response;
        ErrorResponse error;
        TokenResponse token;
        TokenRequest credentials;

        // test an endpoint without authentication information to assert unauthorized error response
        response = client.get().uri("/mail/messages").exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        error = response.bodyToMono(ErrorResponse.class).block();
        Assert.notNull(error, ERR_NO_ERROR);
        Assert.isTrue(HttpStatus.UNAUTHORIZED.value() == error.getStatus(), ERR_NO_UNAUTHORIZED_STATUS);

        // perform bad authentication scenario, use invalid credentials to retrieve error
        credentials = new TokenRequest("user1@api.com", "wrongPassword");
        response = client.post().uri("/auth/token").bodyValue(credentials).exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        error = response.bodyToMono(ErrorResponse.class).block();
        Assert.notNull(error, ERR_NO_ERROR);
        Assert.isTrue(Error.AUTH_PASSWORD_INVALID.equals(error.getError()));
        Assert.isTrue(HttpStatus.UNAUTHORIZED.value() == error.getStatus(), ERR_NO_UNAUTHORIZED_STATUS);

        // perform authentication scenario, retrieve auth token for later use
        credentials = new TokenRequest("user1@api.com", "user");
        response = client.post().uri("/auth/token").bodyValue(credentials).exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        token = response.bodyToMono(TokenResponse.class).block();
        Assert.notNull(token, "Token retrieve failed");
        Assert.notNull(token.getToken(), "Token retrieve failed");

        MessageRequest request;
        MessageResponse message;

        // perform message send request
        request = new MessageRequest("user2@api.com", "Test Message 1", "Test Message 1 Text");
        response = client.post().uri("/mail/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                .bodyValue(request).exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        message = response.bodyToMono(MessageResponse.class).block();
        Assert.notNull(message, "Message send failed");
        Assert.notNull(message.getId(), "Message id is missing");
        Assert.isTrue("Test Message 1".equals(message.getSubject()), "Message subject is wrong");

        // perform message send request
        request = new MessageRequest("admin@api.com", "Test Message 2", "Test Message 2 Text");
        response = client.post().uri("/mail/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                .bodyValue(request).exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        message = response.bodyToMono(MessageResponse.class).block();
        Assert.notNull(message, "Message send failed");
        Assert.notNull(message.getId(), "Message id is missing");
        Assert.isTrue("Test Message 2".equals(message.getSubject()), "Message subject is wrong");

        // view list of messages and verify length
        response = client.get().uri("/mail/messages")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()).exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        final List messages = response.bodyToMono(List.class).block();
        Assert.notNull(messages, "Failed to view message list");
        Assert.notEmpty(messages, "Message list is empty, should be full");

        // test security with forbidden endpoint
        response = client.get().uri("/auth/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()).exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        error = response.bodyToMono(ErrorResponse.class).block();
        Assert.notNull(error, ERR_NO_ERROR);
        Assert.isTrue("org.springframework.security.access.AccessDeniedException".equals(error.getError()), "Expecting access denied error");

        // view user auth information
        response = client.get().uri("/auth/info")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()).exchange().block();
        Assert.notNull(response, ERR_NO_RESPONSE);
        final UserResponse user = response.bodyToMono(UserResponse.class).block();
        Assert.notNull(user, "User info is null");
        Assert.notNull(user.getId(), "User identifier is null");
    }

    @Before
    public void init() throws NoSuchAlgorithmException {

    }
}
