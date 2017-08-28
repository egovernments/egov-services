package org.egov.mr.web.contract;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.model.ReissueCertAppl;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class ReissueCertRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	private ReissueCertAppl reissueApplication;
}
