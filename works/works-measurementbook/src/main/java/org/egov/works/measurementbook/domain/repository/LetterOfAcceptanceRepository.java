package org.egov.works.measurementbook.domain.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ErrorRes;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LetterOfAcceptanceRepository {

    private RestTemplate restTemplate;

    private String contractorSearchUrl;
    
    private String loaCreateUrl;
    
    private String loaUpdateUrl;
    
    private String loaSearchUrl;

    private String loaSearchByLoaEstimateIdUrl;
    
    @Autowired
    private ObjectMapper objectMapper;


    public LetterOfAcceptanceRepository(final RestTemplate restTemplate,@Value("${egov.services.workorder.hostname}") final String workOrderHostname,
                               @Value("${egov.services.workorder.contractorsearchpath}") final String contractorSearchUrl,
                               @Value("${egov.services.egov_works_loa.createpath}") final String loaCreateUrl,
                               @Value("${egov.services.egov_works_loa.updatepath}") final String loaUpdateUrl,
                               @Value("${egov.services.egov_works_loa.searchpath}") final String loaSearchUrl,
                               @Value("${egov.services.egov_works_loa.searchbyloaestimateidpath}") final String loaSearchByLoaEstimateIdUrl) {
        this.restTemplate = restTemplate;
        this.contractorSearchUrl = workOrderHostname + contractorSearchUrl;
        this.loaCreateUrl = workOrderHostname + loaCreateUrl;
        this.loaUpdateUrl = workOrderHostname + loaUpdateUrl;
        this.loaSearchUrl = workOrderHostname + loaSearchUrl;
        this.loaSearchByLoaEstimateIdUrl = workOrderHostname + loaSearchByLoaEstimateIdUrl;
    }

    public List<LetterOfAcceptance> searchLetterOfAcceptance(List<String> codes,List<String> names, String tenantId,
                                                             RequestInfo requestInfo) {
        String status = CommonConstants.STATUS_APPROVED;
        String contractorCodes = codes != null ? String.join(",", codes) : "";
        String contractorNames = names != null ? String.join(",", names) : "";
        return restTemplate.postForObject(contractorSearchUrl,requestInfo, LetterOfAcceptanceResponse.class,tenantId, contractorCodes,contractorNames,status).getLetterOfAcceptances();

    }
    
	public LetterOfAcceptanceResponse searchLOAEstimateById(String loaEstimateId, String tenantId,
			RequestInfo requestInfo) {
		String status = CommonConstants.STATUS_APPROVED;
		return restTemplate.postForObject(loaSearchUrl, requestInfo, LetterOfAcceptanceResponse.class, tenantId,
                loaEstimateId, status);
	}

    public LetterOfAcceptanceResponse createUpdateLOA(LetterOfAcceptanceRequest letterOfAcceptanceRequest, Boolean isUpdate) {
    	ErrorHandler errorHandler = new ErrorHandler();
    	Map<String, String> errors = new HashMap<>();
    	restTemplate.setErrorHandler(errorHandler);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> request = new HttpEntity<Object>(letterOfAcceptanceRequest, headers);
    	ResponseEntity<String> response = null;
    	if (isUpdate)
    		response = restTemplate.exchange(loaUpdateUrl, HttpMethod.POST, request, String.class);
    	else
    		response = restTemplate.exchange(loaCreateUrl, HttpMethod.POST, request, String.class);
        String responseBody = response.getBody();
        try {
            if (errorHandler.hasError(response.getStatusCode())) {
            	ErrorRes errorRes = objectMapper.readValue(responseBody, ErrorRes.class);
            	if (errorRes != null && errorRes.getErrors() != null) {
        			for (org.egov.tracer.model.Error error : errorRes.getErrors())
        				errors.put(error.getCode(), error.getMessage());
            	} else
            		errors.put(Constants.KEY_COMMON_ERROR_CODE, Constants.MESSAGE_LOA_COMMON_ERROR_CODE);
            	throw new CustomException(errors);
            } else {
            	LetterOfAcceptanceResponse letterOfAcceptanceResponse = objectMapper.readValue(responseBody, LetterOfAcceptanceResponse.class);
                return letterOfAcceptanceResponse;
            }
        } catch (IOException e) {
        	errors.put(Constants.KEY_COMMON_ERROR_CODE, Constants.MESSAGE_LOA_COMMON_ERROR_CODE);
        	throw new CustomException(errors);
        }
    }

    public List<LetterOfAcceptance> searchLoaByLoaEstimateId(final String tenantId, final LetterOfAcceptanceEstimate letterOfAcceptanceEstimate,
                                                               final RequestInfo requestInfo) {
        String loaEstimateId = letterOfAcceptanceEstimate.getId();
        return restTemplate.postForObject(loaSearchByLoaEstimateIdUrl, requestInfo, LetterOfAcceptanceResponse.class, tenantId,
                loaEstimateId).getLetterOfAcceptances();

    }
}


