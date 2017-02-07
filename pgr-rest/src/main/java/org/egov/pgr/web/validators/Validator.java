package org.egov.pgr.web.validators;

import org.egov.pgr.web.contract.ErrorResponse;

public interface Validator<T> {
    ErrorResponse validate(T model);
}
