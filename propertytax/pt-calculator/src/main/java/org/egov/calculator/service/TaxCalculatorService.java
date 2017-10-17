package org.egov.calculator.service;

import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.LatePaymentPenaltyResponse;
import org.egov.models.LatePaymentPenaltySearchCriteria;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TransferFeeCalRequest;
import org.egov.models.TransferFeeCalResponse;

/**
 * 
 * @author Pavan Kumar Kamma
 * 
 *         This Class will have all the service methods used in api's
 *
 */
public interface TaxCalculatorService {
	/**
	 * Description: This method will calculate property tax
	 * 
	 * @param calculationRequest
	 * @return calculationResponse
	 * @throws Exception
	 */

	public CalculationResponse calculatePropertyTax(CalculationRequest calculationRequest) throws Exception;

	/**
	 * This will calculate the tax penalty based on the given parameter
	 * 
	 * @param requestInfo
	 * @param LatePaymentPenaltySearchCriteria
	 * @return {@link LatePaymentPenaltyResponse}
	 * @throws Exception
	 */
	public LatePaymentPenaltyResponse getLatePaymentPenalty(RequestInfoWrapper requestInfo,
			LatePaymentPenaltySearchCriteria latePaymentPenaltySearchCriteria) throws Exception;

	/**
	 * /** Description: This method will calculate transfer fee
	 * 
	 * @param TransferFeeCalRequest
	 * @return TransferFeeCalResponse
	 * @throws Exception
	 */

	public TransferFeeCalResponse calculateTransferFee(TransferFeeCalRequest transferFeeCalRequest) throws Exception;
}
