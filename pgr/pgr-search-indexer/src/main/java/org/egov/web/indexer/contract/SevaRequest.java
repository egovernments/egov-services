package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@AllArgsConstructor
public class SevaRequest {
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String CITIZEN = "CITIZEN";

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private ServiceRequest serviceRequest;

    @JsonIgnore
    public boolean isNewRequest() {
        return POST.equals(requestInfo.getAction());
    }

    @JsonIgnore
    public boolean isUpdateRequest() {
        return PUT.equals(requestInfo.getAction());
    }

}