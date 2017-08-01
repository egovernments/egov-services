package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in FeeMatrixResponse
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeMatrixResponse {

	private ResponseInfo responseInfo;

	private List<FeeMatrix> feeMatrices;
}