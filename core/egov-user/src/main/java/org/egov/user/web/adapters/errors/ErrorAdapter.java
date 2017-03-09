package org.egov.user.web.adapters.errors;

import org.egov.user.web.contract.ErrorRes;

public interface ErrorAdapter<T> {
    ErrorRes adapt(T model);
}
