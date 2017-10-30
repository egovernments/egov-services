package org.egov.lams.common.web.response;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.lams.common.web.contract.LandAcquisition;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LandAcquisitionResponse {
	private ResponseInfo responseInfo;
	@JsonProperty("LandAcquisition")
	private List<LandAcquisition> landAcquisition;
}
