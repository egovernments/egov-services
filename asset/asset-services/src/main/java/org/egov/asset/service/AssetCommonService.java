package org.egov.asset.service;

import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class AssetCommonService {
    
    public ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
                for (final FieldError errs : errors.getFieldErrors())
                        error.getFields().put(errs.getField(), errs.getRejectedValue());
        errRes.setError(error);
        return errRes;
}
}
