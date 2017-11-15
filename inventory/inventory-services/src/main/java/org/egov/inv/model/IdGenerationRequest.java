package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IdGenerationRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private List<IdRequest> idRequests;

}
