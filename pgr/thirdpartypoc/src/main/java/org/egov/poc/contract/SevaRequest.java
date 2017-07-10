package org.egov.poc.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;

import java.util.HashMap;

public class SevaRequest {

    private static final String SERVICE_REQUEST = "serviceRequest";
    private static final String REQUEST_INFO = "RequestInfo";
    public static final String STATUS = "status";

    private HashMap<String, Object> sevaRequestMap;
    private ObjectMapper objectMapper;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
        this.objectMapper = new ObjectMapper();
    }

    @SuppressWarnings("unchecked")
    private ServiceRequest getServiceRequest() {
        return new ServiceRequest((HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST));
    }

    private RequestInfo getRequestInfo() {
        return objectMapper.convertValue(sevaRequestMap.get(REQUEST_INFO), RequestInfo.class);
    }

    public String getTenantId(){
        return getServiceRequest().getTenantId();
    }

    public String getCrn(){
        return getServiceRequest().getCrn();
    }

    public String getStatus(){
        return getServiceRequest().getDynamicSingleValue(STATUS);
    }

    public String getServiceCode(){
        return getServiceRequest().getComplaintTypeCode();
    }

}