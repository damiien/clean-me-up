package com.effcode.clean.me.rest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties data mapping class, contains application configuration property values mapped at
 * application bootstrap phase
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @see ConfigurationProperties
 * @since 1.0
 */
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private String smtpUsername;
    private String smtpPassword;
    private String jwtSecretKey;
    private Long jwtExpiryInterval;
    private List<String> publicEndpoints;

    /**
     * Accessor for SMTP username configuration property
     *
     * @return SMTP username
     */
    public String getSmtpUsername() {
        return smtpUsername;
    }

    /**
     * Setter for SMTP username configuration property
     *
     * @param smtpUsername SMTP username
     */
    public void setSmtpUsername(final String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    /**
     * Accessor for SMTP password configuration property
     *
     * @return SMTP password
     */
    public String getSmtpPassword() {
        return smtpPassword;
    }

    /**
     * Setter for SMTP password configuration property
     *
     * @param smtpPassword SMTP password
     */
    public void setSmtpPassword(final String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    /**
     * Accessor for JWT secret key property
     *
     * @return JWT secret key
     */
    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    /**
     * Setter for SMTP password configuration property
     *
     * @param jwtSecretKey JWT secret key
     */
    public void setJwtSecretKey(final String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    /**
     * Accessor for JWT expiration interval property
     *
     * @return JWT secret key
     */
    public Long getJwtExpiryInterval() {
        return jwtExpiryInterval;
    }

    /**
     * Setter for SMTP password configuration property
     *
     * @param jwtExpiryInterval JWT expiry interval
     */
    public void setJwtExpiryInterval(final Long jwtExpiryInterval) {
        this.jwtExpiryInterval = jwtExpiryInterval;
    }

    /**
     * Accessor for collection of whitelisted (public) endpoint paths for which no authentication is required
     * @return whitelisted (public) endpoints
     */
    public List<String> getPublicEndpoints() {
        return publicEndpoints;
    }

    /**
     * Sets a collection of whitelisted (public) endpoints
     * @param publicEndpoints public endpoints
     */
    public void setPublicEndpoints(List<String> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
    }
}
