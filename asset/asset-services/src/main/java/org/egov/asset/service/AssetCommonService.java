package org.egov.asset.service;

import java.util.Date;
import java.util.List;

import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.Sequence;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetCommonService {

    @Autowired
    private SequenceGenService sequenceGenService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            log.debug("Depreciation Rate ::" + deprRate);
            return deprRate;
        } else
            return null;
    }

    public void validateDepreciationRateValue(final Double depreciationRate) {
        if (depreciationRate != null && Double.compare(depreciationRate, Double.valueOf("0.0")) < 0)
            throw new RuntimeException("Depreciation rate can not be negative.");
    }

    public AuditDetails getAuditDetails(final RequestInfo requestInfo) {
        final String id = requestInfo.getUserInfo().getId().toString();
        final Long time = new Date().getTime();
        return AuditDetails.builder().createdBy(id).lastModifiedBy(id).createdDate(time).lastModifiedDate(time).build();
    }

    public String getCode(final String codeFormat, final Sequence sequence) {
        final List<Long> codeSequence = sequenceGenService.getIds(1, sequence.toString());
        log.debug("code sequence result :: " + codeSequence);
        if (codeSequence != null && !codeSequence.isEmpty()) {
            final StringBuilder code = new StringBuilder(String.format(codeFormat, codeSequence.get(0)));
            log.debug("Generated code :: " + code);
            return code.toString();
        } else
            throw new RuntimeException("Code is not generated.");
    }

    public Long getNextId(final Sequence sequence) {

        final String query = "SELECT nextval('" + sequence.toString() + "')";
        Integer result = null;
        try {
            result = jdbcTemplate.queryForObject(query, Integer.class);
            log.debug("result:" + result);
            return result.longValue();
        } catch (final Exception ex) {
            throw new RuntimeException("Next id is not generated.");
        }
    }
}
