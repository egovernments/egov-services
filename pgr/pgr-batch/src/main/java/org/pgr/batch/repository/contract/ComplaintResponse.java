package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ComplaintResponse {
	@JsonProperty("response_info")
	private ResponseInfo responseInfo;

	@JsonProperty("service_requests")
	private List<ServiceRequest> serviceRequests;
}
