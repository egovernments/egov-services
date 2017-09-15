package org.egov.tl.commons.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeMatrixSearchResponse {

	private ResponseInfo responseInfo;
	
	private List<FeeMatrixSearchContract> feeMatrices;
}
