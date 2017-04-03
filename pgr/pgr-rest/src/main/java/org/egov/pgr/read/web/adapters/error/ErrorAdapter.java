package org.egov.pgr.read.web.adapters.error;

import org.egov.pgr.read.web.contract.ErrorResponse;

/**
 * This is to transform the model errors to web channel specific errors
 *
 * @param <T>
 */
public interface ErrorAdapter<T> {
    ErrorResponse adapt(T model);
}
