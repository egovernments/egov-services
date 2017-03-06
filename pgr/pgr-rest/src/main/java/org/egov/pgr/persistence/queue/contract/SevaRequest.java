package org.egov.pgr.persistence.queue.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.Complaint;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SevaRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("ServiceRequest")
    private ServiceRequest serviceRequest;

    @JsonIgnore
    public Complaint toDomainForUpdateRequest(AuthenticatedUser authenticatedUser) {
        return serviceRequest.toDomainForUpdateRequest(authenticatedUser);
    }

    @JsonIgnore
    public Complaint toDomainForCreateRequest(AuthenticatedUser authenticatedUser) {
        return serviceRequest.toDomainForCreateRequest(authenticatedUser);
    }

    @JsonIgnore
    public String getAuthToken() {
        return requestInfo.getAuthToken();
    }

    public void update(Complaint complaint) {
        //RequesterId should be populated in case of complaint update or close
        if(StringUtils.isNotEmpty(requestInfo.getAuthToken()))
            requestInfo.setRequesterId(complaint.getAuthenticatedUser().getId().toString());
        serviceRequest.setCrn(complaint.getCrn());
    }
}