package com.effcode.clean.me.core.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * Object mapper decorator component, provides singleton instance with application specific configuration
 *
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see Component
 */
@Component
public class ObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectMapper.class);
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd hh:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    private static ObjectMapper instance;

    public ObjectMapper() {
        super();
        instance = this;
        registerModule(new JavaTimeModule());
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        setDateFormat(DATE_FORMAT);
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        configure(SerializationFeature.CLOSE_CLOSEABLE, true);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(DeserializationFeature.WRAP_EXCEPTIONS, false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    }

    /**
     * Provides singleton instance of object mapper
     *
     * @return object mapper instance
     */
    public static ObjectMapper instance() {
        if (instance == null) instance = new ObjectMapper();
        return instance;
    }

    /**
     * Serializes a specified object to JSON formatted string. Internally ignores thrown instances of
     * {@link JsonProcessingException} and logs the stacktrace, in which case {@code null} is returned.
     *
     * @param object target object to convert
     * @return serialized JSON string, if error occurred then {@code null}
     */
    public final String toString(final Object object) {
        String result = null;
        try {
            result = writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Deserializes a specified JSON formatted content string to an object of required type. Internally ignores thrown instances of
     *      * {@link JsonProcessingException} and logs the stacktrace, in which case {@code null} is returned.
     * @param content JSON formatted content string
     * @param clazz output object class
     * @param <T> output object type
     * @return deserialized object instance, or {@code null} if deserialization fails.
     */
    public final <T> T toObject(final String content, final Class<T> clazz) {
        T result = null;
        try {
            result = readValue(content, clazz);
        } catch (final JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
