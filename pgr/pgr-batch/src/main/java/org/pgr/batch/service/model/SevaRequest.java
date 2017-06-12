package org.pgr.batch.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.contract.ServiceRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SevaRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private ServiceRequest serviceRequest;

	public void markEscalated() {
		serviceRequest.setEscalatedFlag();
	}
}