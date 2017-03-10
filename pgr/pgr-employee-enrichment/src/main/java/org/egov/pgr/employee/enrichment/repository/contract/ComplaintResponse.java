package org.egov.pgr.employee.enrichment.repository.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ComplaintResponse {
	@JsonProperty("service_requests")
	private List<ServiceRequest> serviceRequests;
}
