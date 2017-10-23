package org.egov.lams.common.web.request;

import java.util.List;

import org.egov.lams.common.web.contract.LandAcquisition;
import org.egov.lams.common.web.contract.ResponseInfo;

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
	private List<LandAcquisition> landAcquisitions;
}
