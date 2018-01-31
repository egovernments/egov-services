package org.egov.works.measurementbook.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ErrorRes;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.web.contract.DetailedEstimate;
import org.egov.works.measurementbook.web.contract.DetailedEstimateRequest;
import org.egov.works.measurementbook.web.contract.DetailedEstimateResponse;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EstimateRepository {

	private final RestTemplate restTemplate;

    private String detailedEstimateByDepartmentUrl;
    
    private String detailedEstimateByIdsUrl;
    
    private String detailedEstimateCreateUrl;
    
    private String detailedEstimateUpdateUrl;
    
    @Autowired
    private ObjectMapper objectMapper;

	@Autowired
	public EstimateRepository(final RestTemplate restTemplate,
                              @Value("${egov.services.egov_works_estimate.hostname}") final String worksEstimateHostname,
                              @Value("${egov.services.egov_works_estimate.searchbydepartment}") final String detailedEstimateByDepartmentUrl,
                              @Value("${egov.services.egov_works_estimate.searchbyids}") final String detailedEstimateByIdsUrl,
                              @Value("${egov.services.egov_works_estimate.createpath}") final String detailedEstimateCreateUrl,
                              @Value("${egov.services.egov_works_estimate.updatepath}") final String detailedEstimateUpdateUrl) {
		this.restTemplate = restTemplate;
        this.detailedEstimateByDepartmentUrl = worksEstimateHostname + detailedEstimateByDepartmentUrl;
        this.detailedEstimateByIdsUrl = worksEstimateHostname + detailedEstimateByIdsUrl;
        this.detailedEstimateCreateUrl = worksEstimateHostname + detailedEstimateCreateUrl;
        this.detailedEstimateUpdateUrl = worksEstimateHostname + detailedEstimateUpdateUrl;
	}

    public List<DetailedEstimate> searchDetailedEstimatesByDepartment(final List<String> departmentCodes, final String tenantId,final RequestInfo requestInfo) {

        String status = CommonConstants.STATUS_TECH_SANCTIONED;
        String departments = String.join(",", departmentCodes);
        return restTemplate.postForObject(detailedEstimateByDepartmentUrl, requestInfo, DetailedEstimateResponse.class,tenantId,departments, status).getDetailedEstimates();
    }
    
    public List<DetailedEstimate> searchDetailedEstimatesByIds(final List<String> idList, final String tenantId,final RequestInfo requestInfo) {

        String status = CommonConstants.STATUS_TECH_SANCTIONED;
        String ids = String.join(",", idList);
        return restTemplate.postForObject(detailedEstimateByIdsUrl, requestInfo, DetailedEstimateResponse.class,tenantId,ids, status).getDetailedEstimates();
    }

    public DetailedEstimateResponse createUpdateDetailedEstimate(DetailedEstimateRequest detailedEstimateRequest, Boolean isUpdate) {
    	ErrorHandler errorHandler = new ErrorHandler();
    	Map<String, String> errors = new HashMap<>();
    	restTemplate.setErrorHandler(errorHandler);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> request = new HttpEntity<Object>(detailedEstimateRequest, headers);
    	ResponseEntity<String> response = null;
    	if (isUpdate)
    		response = restTemplate.exchange(detailedEstimateUpdateUrl, HttpMethod.POST, request, String.class);
    	else
    		response = restTemplate.exchange(detailedEstimateCreateUrl, HttpMethod.POST, request, String.class);
        String responseBody = response.getBody();
        try {
            if (errorHandler.hasError(response.getStatusCode())) {
            	ErrorRes errorRes = objectMapper.readValue(responseBody, ErrorRes.class);
            	if (errorRes != null && errorRes.getErrors() != null) {
        			for (org.egov.tracer.model.Error error : errorRes.getErrors())
        				errors.put(error.getCode(), error.getMessage());
            	} else
            		errors.put(Constants.KEY_COMMON_ERROR_CODE, Constants.MESSAGE_RE_COMMON_ERROR_CODE);
            	throw new CustomException(errors);
            } else {
                DetailedEstimateResponse detailedEstimateResponse = objectMapper.readValue(responseBody, DetailedEstimateResponse.class);
                return detailedEstimateResponse;
            }
        } catch (IOException e) {
        	errors.put(Constants.KEY_COMMON_ERROR_CODE, Constants.MESSAGE_RE_COMMON_ERROR_CODE);
        	throw new CustomException(errors);
        }
    }
}
