package com.effcode.clean.me.core.spec;

/**
 * Root interface specifying a <b></B>data transfer model</b> and alternate model type for properties inflation
 *
 * @param <M>  type of model
 * @param <T>  target type of model
 * @param <ID> type of model identifier
 * @author dame.gjorgjievski
 * @version 1.0
 * @see IModel
 */
public interface IDataModel<M extends IModel<M, ID>, T extends IModel<?, ?>, ID> extends IModel<M, ID> {

    /**
     * Inflate this model from specified target instance
     *
     * @param target object to inflate from
     */
    default M from(final T target) {
        return (M) this;
    }

    /**
     * Convert this model into instance of specified target type
     *
     * @return converted target instance
     * @throws UnsupportedOperationException until its implemented
     */
    default T to() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
