package org.egov.asset.contract;

import org.egov.asset.model.Depreciation;
import org.egov.common.contract.response.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DepreciationResponse {
	
	private ResponseInfo responseInfo;
	
	private Depreciation depreciation;
}
