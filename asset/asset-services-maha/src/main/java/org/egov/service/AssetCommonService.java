package org.egov.service;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.config.ApplicationProperties;
import org.egov.model.AuditDetails;
import org.egov.model.enums.Sequence;
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
    
    @Autowired
    private ApplicationProperties appProps;

    public ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);

       /* final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError errs : errors.getFieldErrors())
                error.getFields().put(errs.getField(), errs.getRejectedValue());
        errRes.setError(error);*/
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

    public String getCode(final Sequence sequence) {
    
            return sequenceGenService.getIds(1, sequence.toString()).get(0).toString();
       
    }

    public Long getNextId(final Sequence sequence) {

        final String query = "SELECT nextval('" + sequence.toString() + "')";
        Integer result = null;
        try {
            result = jdbcTemplate.queryForObject(query, Integer.class);
            log.info("result:" + result);
            return result.longValue();
        } catch (final Exception ex) {
            throw new RuntimeException("Next id is not generated.");
        }
    }
    
    /***
     * method to convert the set of long values to coma separated string for the purpose of appending to url and queries
     * @param idSet
     * @return
     */
	public String getIdQuery(final Set<Long> idSet) {
		StringBuilder query = null;
		Long[] arr = new Long[idSet.size()];
		arr = idSet.toArray(arr);
		String value1 = arr[0].toString();
		if(value1!=null)
		query = new StringBuilder(value1);
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}
	
	public String getIdQueryFromString(final Set<String> idSet) {
		log.info("the recieved id set : "+ idSet);
		StringBuilder query = null;
		String[] arr = new String[idSet.size()];
		arr = idSet.toArray(arr);
		String value1 = arr[0];
		if(value1!=null)
		query = new StringBuilder(value1);
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}
	
	
}