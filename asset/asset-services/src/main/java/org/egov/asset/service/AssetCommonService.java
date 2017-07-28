package org.egov.asset.service;

import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class AssetCommonService {

    private static final Logger logger = LoggerFactory.getLogger(AssetCommonService.class);

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

    public Double getDepreciationRate(final Double depreciationRate) {
        if (depreciationRate != null) {
            final Double deprRate = Math.round(depreciationRate * 100.0) / 100.0;
            logger.debug("Depreciation Rate ::" + deprRate);
            return deprRate;
        } else
            return null;
    }
}
