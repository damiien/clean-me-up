package com.effcode.clean.me.domain.service;

import com.effcode.clean.me.core.validation.ModelValidator;
import com.effcode.clean.me.domain.exception.Error;
import com.effcode.clean.me.domain.exception.MailException;
import com.effcode.clean.me.domain.exception.ValidationException;
import com.effcode.clean.me.domain.model.MailMessage;
import com.effcode.clean.me.domain.model.MailSend;
import com.effcode.clean.me.domain.model.UserAuthority;
import com.effcode.clean.me.domain.model.UserPrincipal;
import com.effcode.clean.me.rest.configuration.ApplicationProperties;
import com.effcode.clean.me.support.SmtpEmail;
import com.effcode.clean.me.support.SmtpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for email operations
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see Service
 * @since 1.0
 */
@Service
public class MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);
    private final List<MailMessage> messages = Collections.synchronizedList(new LinkedList<>());
    private final List<String> BLACKLIST = Arrays.asList("microsoft.com", "apple.com", "intel.com");

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private SmtpHandler smtpHandler;

    /**
     * Sends a SMTP mail message and returns sent message data record
     *
     * @param email message to send
     * @return sent mail message record
     * @throws MailException if message sending process fails
     * @throws ValidationException if message send data validation fails
     */
    public Mono<MailMessage> send(final MailSend email) throws MailException, ValidationException {

        return Mono.create(sink -> {
            // validate required data
            new ModelValidator<MailSend>().validate(email, Error.MAIL_REQUEST_INVALID);

            // simulate host blacklist for exception handling demonstration
            if (BLACKLIST.stream().anyMatch(h -> email.getAddress().endsWith(h))) {
                sink.error(Error.MAIL_HOST_INVALID.buildException());
                return;
            }

            // compose support module data object
            final SmtpEmail smtpEmail = new SmtpEmail();
            smtpEmail.adrs = new String[]{email.getAddress()};
            smtpEmail.subject = email.getSubject();
            smtpEmail.content = email.getContent();
            smtpEmail.username = properties.getSmtpUsername();
            smtpEmail.password = properties.getSmtpPassword();
            smtpHandler.post(smtpEmail);

            final MailMessage message = new MailMessage(email);
            messages.add(message);
            LOG.info("Sent email success. Record: {}", message.asString());
            sink.success(message);
        });
    }

    /**
     * Finds all mail messages that are sent to or from specified user. Admin users can see a full list of messages from
     * all users.
     *
     * @param user specified message receiver / sender user
     * @return mail message records
     */
    public Mono<List<MailMessage>> findMessages(final UserPrincipal user) {

        return Mono.create(sink -> {
            final String username = user.getUsername();
            sink.success(user.hasAuthority(UserAuthority.ADMIN) ? messages :
                    messages.stream().filter(m -> m.getOrigin().equals(username) || m.getAddress().equals(username))
                            .collect(Collectors.toList()));
        });
    }

}
