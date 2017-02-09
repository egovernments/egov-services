package org.egov.pgr.web.adapters.error;

import org.egov.pgr.web.contract.ErrorResponse;

public interface ErrorAdapter<T> {
    ErrorResponse validate(T model);
}
