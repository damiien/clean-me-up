package com.effcode.clean.me.domain.model;

import com.effcode.clean.me.core.util.DateUtil;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Data model used for representation of chronological mail messages
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see MailModel
 * @since 1.0
 */
public final class MailMessage extends MailModel<MailMessage, UUID> {

    @NotNull
    private UUID id;

    @NotNull
    private Date timestamp;

    public MailMessage() {
    }

    public MailMessage(final MailModel<?, ?> email) {
        super(email);
        this.id = UUID.randomUUID();
        this.timestamp = DateUtil.utc();
    }

    /**
     * Accessor for retrieval of email message identifier
     *
     * @return mail message identifier
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Accessor for retrieval of email send timestamp
     *
     * @return mail message sent timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

}
