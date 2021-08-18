package com.effcode.clean.me.core.spec;

import com.effcode.clean.me.core.mapper.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Root serializable model specification interface
 *
 * @param <M>  model type boundary
 * @param <ID> model identifier type
 * @author dame.gjorgjievski
 * @version 1.0
 * @see Serializable
 * @since 1.0
 */
public interface IModel<M extends IModel<M, ID>, ID> extends Serializable {

    /**
     * Accessor for this data model identifier, default implementation returns {@code null}
     *
     * @return data model identifier or {@code null}
     */
    default ID getId() {
        return null;
    }

    /**
     * Accessor for the type of this model
     *
     * @return the type of this model instance
     */
    @JsonIgnore
    default Class<M> getType() {
        return (Class<M>)
                ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Converts and returns this data object represented as property <> value map
     *
     * @return object data as property <> values map
     */
    default Map<?, ?> asMap() {
        return ObjectMapper.instance().toObject(ObjectMapper.instance().toString(this), LinkedHashMap.class);
    }

    /**
     * Serializes and returns this data object as JSON formatted object string
     *
     * @return JSON formatted object string
     */
    default String asString() {
        return ObjectMapper.instance().toString(this);
    }
}
