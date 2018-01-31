package org.egov.works.measurementbook.domain.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ErrorRes;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.web.contract.BillRegisterRequest;
import org.egov.works.measurementbook.web.contract.BillRegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class BillRegisterRepository {

    private static final String HEADER_ACCEPT = "Accept";

    private final RestTemplate restTemplate;

    private String billRegisterCreateUrl;

    private String billRegisterUpdateUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public BillRegisterRepository(final RestTemplate restTemplate,
            @Value("${egov.services.egf.bill.hostname}") final String worksEstimateHostname,
            @Value("${egov.services.egf.billregister.createpath}") final String billRegisterCreateUrl,
            @Value("${egov.services.egf.billregister.updatepath}") final String billRegisterUpdateUrl) {
        this.restTemplate = restTemplate;
        this.billRegisterCreateUrl = worksEstimateHostname + billRegisterCreateUrl;
        this.billRegisterUpdateUrl = worksEstimateHostname + billRegisterUpdateUrl;
    }

    public BillRegisterResponse createUpdateBillRegister(BillRegisterRequest billRegisterRequest,
            Boolean isUpdate) {
        ErrorHandler errorHandler = new ErrorHandler();
        Map<String, String> errors = new HashMap<>();
        restTemplate.setErrorHandler(errorHandler);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> request = new HttpEntity<Object>(billRegisterRequest, headers);
        ResponseEntity<String> response = null;
        if (isUpdate)
            response = restTemplate.exchange(billRegisterUpdateUrl, HttpMethod.POST, request, String.class);
        else
            response = restTemplate.exchange(billRegisterCreateUrl, HttpMethod.POST, request, String.class);
        String responseBody = response.getBody();
        try {
            if (errorHandler.hasError(response.getStatusCode())) {
                ErrorRes errorRes = objectMapper.readValue(responseBody, ErrorRes.class);
                if (errorRes != null && errorRes.getErrors() != null) {
                    for (org.egov.tracer.model.Error error : errorRes.getErrors())
                        errors.put(error.getCode(), error.getMessage());
                } else
                    errors.put(Constants.KEY_COMMON_ERROR_CODE, Constants.MESSAGE_BILLREGISTER_COMMON_ERROR_CODE);
                throw new CustomException(errors);
            } else {
                BillRegisterResponse billRegisterResponse = objectMapper.readValue(responseBody,
                        BillRegisterResponse.class);
                return billRegisterResponse;
            }
        } catch (IOException e) {
            errors.put(Constants.KEY_COMMON_ERROR_CODE, Constants.MESSAGE_BILLREGISTER_COMMON_ERROR_CODE);
            throw new CustomException(errors);
        }
    }
}
