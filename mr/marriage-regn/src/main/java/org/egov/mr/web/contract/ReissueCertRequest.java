package org.egov.mr.web.contract;

import org.egov.mr.model.ReissueCertAppl;
import org.springframework.stereotype.Component;

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
	private RequestInfo requestInfo;

	private ReissueCertAppl reissueApplication;
}
