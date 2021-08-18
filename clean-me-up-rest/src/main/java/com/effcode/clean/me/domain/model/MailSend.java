package com.effcode.clean.me.domain.model;

/**
 * Mail message send parameters data model. Contains required parameters for composing and sending a new SMTP mail message.
 *
 * @author dame.gjorgjievski
 * @see MailModel
 */
public final class MailSend extends MailModel<MailSend, Void> {

    public MailSend() {
    }

    public MailSend(final String address, final String subject, final String content, final String origin) {
        super(address, subject, content, origin);
    }
}
