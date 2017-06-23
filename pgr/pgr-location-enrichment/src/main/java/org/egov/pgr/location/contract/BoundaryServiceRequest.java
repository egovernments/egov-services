package org.egov.pgr.location.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoundaryServiceRequest {


    @JsonProperty("request_info")
    private RequestInfo requestInfo;
    @JsonProperty("cross_hierarchy_id")
    private String crossHierarchyId;
    private String latitude;
    private String longitude;

    public BoundaryServiceRequest(RequestInfo requestInfo, String latitude, String longitude) {
        this.requestInfo = requestInfo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BoundaryServiceRequest(RequestInfo requestInfo, String crossHierarchyId) {
        this.requestInfo = requestInfo;
        this.crossHierarchyId = crossHierarchyId;
    }

    public String getCrossHierarchyId() {
        return crossHierarchyId;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
