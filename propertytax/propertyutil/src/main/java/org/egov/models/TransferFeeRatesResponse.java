package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 
 * @author Yosadhara
 *
 */
public class TransferFeeRatesResponse {
	
	private ResponseInfo responseInfo;
	
	private List<TransferFeeRate> transferFeeRates;
}
