package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
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
