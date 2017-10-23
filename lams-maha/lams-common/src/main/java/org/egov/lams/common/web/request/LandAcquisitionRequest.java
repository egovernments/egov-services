package org.egov.lams.common.web.request;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.LandAcquisition;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class LandAcquisitionRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@JsonProperty("LandAcquisition")
	private List<LandAcquisition> landAcquisition;

}
