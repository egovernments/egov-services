package org.egov.pgr.model;

import org.egov.pgr.contract.BoundaryResponse;
import org.egov.pgr.contract.CrossHierarchyResponse;
import org.egov.pgr.contract.RequestInfo;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.json.ObjectMapperFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class SevaRequest {

    private static final String LOCATION_ID = "location_id";
    private static final String LOCATION_NAME = "location_name";
    private static final String CHILD_LOCATION_ID = "child_location_id";
    private final static String SERVICE_REQUEST = "ServiceRequest";
    private final static String REQUEST_INFO = "RequestInfo";
    private static final String VALUES = "values";
    private final ServiceRequest serviceRequestObject;

    private HashMap<String, Object> sevaRequestMap;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
        this.serviceRequestObject = ObjectMapperFactory.create()
                .convertValue(sevaRequestMap.get(SERVICE_REQUEST), ServiceRequest.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getValues() {
        HashMap<String, Object> serviceRequest = getServiceRequest();
        HashMap<String, String> values = (HashMap<String, String>) serviceRequest.get(VALUES);
        if(values == null) {
            serviceRequest.put(VALUES, new HashMap<String, String>());
        }
        return (HashMap<String, String>) serviceRequest.get(VALUES);
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> getServiceRequest() {
        return (HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST);
    }

    public void update(BoundaryResponse response) {
        getValues().put(LOCATION_ID, String.valueOf(response.getId()));
        getValues().put(LOCATION_NAME, response.getName());
    }

    public void update(CrossHierarchyResponse hierarchyResponse) {
        getValues().put(LOCATION_ID, hierarchyResponse.getLocationId());
        getValues().put(LOCATION_NAME, hierarchyResponse.getLocationName());
        getValues().put(CHILD_LOCATION_ID, hierarchyResponse.getChildLocationId());
    }

    public String getLocationId() {
        return getValues().get(LOCATION_ID);
    }

    public String getLocationName() {
        return getValues().get(LOCATION_NAME);
    }

    public String getChildLocationId() {
        return getValues().get(CHILD_LOCATION_ID);
    }

    public String getLatitude() {
        return serviceRequestObject.getLatitude();
    }

    public String getLongitude() {
        return serviceRequestObject.getLongitude();
    }

    public boolean isCrossHierarchyIdPresent() {
        return serviceRequestObject.isCrossHierarchyIdPresent();
    }

    public String getCrossHierarchyId() {
        return serviceRequestObject.getCrossHierarchyId();
    }

    public boolean isLocationCoordinatesPresent() {
        return serviceRequestObject.isLocationCoordinatesPresent();
    }

    public boolean isLocationPresent() {
        return getValues() != null && isNotEmpty(getValues().get(LOCATION_ID));
    }

    public String getCorrelationId() {
        final RequestInfo requestInfo = (RequestInfo) sevaRequestMap.get(REQUEST_INFO);
        return requestInfo.getMsgId();
    }
}