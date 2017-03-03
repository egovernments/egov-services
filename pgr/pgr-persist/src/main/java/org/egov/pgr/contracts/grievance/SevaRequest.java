package org.egov.pgr.contracts.grievance;

import java.util.HashMap;

public class SevaRequest {

    private static final String SERVICE_REQUEST = "ServiceRequest";
    private HashMap<String, Object> sevaRequestMap;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
    }

    @SuppressWarnings("unchecked")
    public ServiceRequest getServiceRequest() {
        return new ServiceRequest((HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST));
    }
}