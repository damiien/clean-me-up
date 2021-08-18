package com.effcode.clean.me.rest.data;

import com.effcode.clean.me.core.mapper.ObjectMapper;
import com.effcode.clean.me.core.spec.IDataModel;
import com.effcode.clean.me.domain.model.MailMessage;
import com.effcode.clean.me.domain.model.MailModel;
import com.fasterxml.jackson.annotation.*;

import java.util.Date;

/**
 * Mail message response {@link IDataModel data transfer model}. Inflates the internal data state from
 * {@link MailMessage domain message model} to provide response data.
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see MailModel
 * @see IDataModel
 * @see MailMessage
 */
@JsonPropertyOrder({"id", "timestamp", "address", "subject", "content", "origin"})
@JsonClassDescription("Mail message response data transfer model")
public class MessageResponse extends MailModel<MessageResponse, String> implements IDataModel<MessageResponse, MailMessage, String> {

    @JsonPropertyDescription("Mail message identifier")
    @JsonProperty("id")
    private String id;

    @JsonPropertyDescription("Mail message timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ObjectMapper.DATE_FORMAT_PATTERN)
    private Date timestamp;

    @JsonPropertyDescription("Mail message origin")
    private String origin;

    @Override
    public MessageResponse from(final MailMessage target) {
        this.address = target.getAddress();
        this.subject = target.getSubject();
        this.content = target.getContent();
        this.id = target.getId().toString();
        this.timestamp = target.getTimestamp();
        this.origin = target.getOrigin();
        return this;
    }

    @JsonGetter
    @Override
    public String getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getOrigin() {
        return origin;
    }

    @JsonPropertyDescription("Mail message destination address")
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @JsonPropertyDescription("Mail message subject")
    @Override
    public String getSubject() {
        return super.getSubject();
    }

    @JsonPropertyDescription("Mail message text content")
    @Override
    public String getContent() {
        return super.getContent();
    }
}
