package com.effcode.clean.me.rest.data;

import com.effcode.clean.me.core.spec.IDataModel;
import com.effcode.clean.me.domain.model.MailModel;
import com.effcode.clean.me.domain.model.MailSend;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Email send request {@link IDataModel data transfer model}, convertible to instances of {@link MailSend mail send model}
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see MailModel
 * @see MailSend
 * @since 1.0
 */
@JsonClassDescription("Mail message send request data transfer model")
public class MessageRequest extends MailModel<MessageRequest, Void> implements IDataModel<MessageRequest, MailSend, Void> {

    public MessageRequest() {
    }

    public MessageRequest(final String address, final String subject, final String content) {
        super(address, subject, content, null);
    }

    @Override
    public final MailSend to() {
        return new MailSend(getAddress(), getSubject(), getContent(), getOrigin());
    }

    @JsonPropertyDescription("Mail message receiver destination address")
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @JsonPropertyDescription("Mail message subject line")
    @Override
    public String getSubject() {
        return super.getSubject();
    }

    @JsonPropertyDescription("Mail message body content")
    @Override
    public String getContent() {
        return super.getContent();
    }

    @JsonPropertyDescription("Mail message sender origin address")
    @Override
    public String getOrigin() {
        return super.getOrigin();
    }
}
