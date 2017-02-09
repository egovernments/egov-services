package org.egov.pgr.persistence.queue.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.Complaint;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SevaRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("ServiceRequest")
    private ServiceRequest serviceRequest;

    public Complaint toDomain(AuthenticatedUser authenticatedUser, String jurisdictionId) {
        return serviceRequest.toDomain(authenticatedUser, jurisdictionId);
    }
}