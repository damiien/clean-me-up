package com.effcode.clean.me.domain.model;

import com.effcode.clean.me.core.spec.IModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Common abstraction of email message data model
 *
 * @param <M>  mail model type
 * @param <ID> mail model identifier type
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IModel
 * @since 1.0
 */
public abstract class MailModel<M extends MailModel<M, ID>, ID> implements IModel<M, ID> {

    @NotBlank
    @Email
    protected String address;

    @NotBlank
    protected String subject;

    @NotBlank
    @Size(min = 1, max = 65600)
    protected String content;

    @NotBlank
    @Email
    protected String origin;

    protected MailModel() {
    }

    protected MailModel(final String address, final String subject, final String content, final String origin) {
        this.address = address;
        this.subject = subject;
        this.content = content;
        this.origin = origin;
    }

    protected MailModel(final MailModel<?, ?> model) {
        this.address = model.getAddress();
        this.subject = model.getSubject();
        this.content = model.getContent();
        this.origin = model.getOrigin();
    }

    /**
     * Accessor for the email destination address
     *
     * @return email destination address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Accessor for the email subject
     *
     * @return email subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Accessor for the email body content
     *
     * @return email body content
     */
    public String getContent() {
        return content;
    }


    /**
     * Accessor for retrieval of the mail message origin address
     *
     * @return mail message origin address
     */
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
