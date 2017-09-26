package org.egov.calculator.api;

import org.egov.calculator.exception.InvalidTaxCalculationDataException;
import org.egov.calculator.service.TaxCalculatorService;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.LatePaymentPenaltyResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TransferFeeCalRequest;
import org.egov.models.TransferFeeCalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * TaxCalculator Controller have the api's related to tax calculation
 * 
 * @author Narendra
 */
@Slf4j
@RestController
@RequestMapping("/properties/taxes")
public class TaxCalculatorController {

	@Autowired
	TaxCalculatorService taxCalculatorService;

	@RequestMapping(path = "/_calculate", method = RequestMethod.POST)
	public CalculationResponse calculatePropertyTax(@RequestBody CalculationRequest calculationRequest)
			throws Exception {
		CalculationResponse calculationResponse = null;
		log.info("TaxCalculatorController calculatePropertyTax() ---->> \n calculationRequest -------- "
				+ calculationRequest);
		try {
			calculationResponse = taxCalculatorService.calculatePropertyTax(calculationRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidTaxCalculationDataException(ex.getMessage(), calculationRequest.getRequestInfo(),
					ex.getMessage());
		}
		log.info("TaxCalculatorController calculatePropertyTax() ---->> \n calculationResponse -------- "
				+ calculationResponse);
		return calculationResponse;
	}

	/**
	 * Description : This will calculate the late payment penalty parameter
	 * 
	 * @param tenantId
	 * @param upicNo
	 * @param requestInfo
	 * @return {@link LatePaymentPenaltyResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/latepaymentpenalty/_calculate", method = RequestMethod.POST)
	public LatePaymentPenaltyResponse getLatePaymentPenalty(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = true) String upicNo)
			throws Exception {

		return taxCalculatorService.getLatePaymentPenalty(requestInfo, tenantId, upicNo);

	}

	/**
	 * This will create title transfer fee
	 * 
	 * @param transferFeeCalRequest
	 * @return transferFeeCalResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/transferfee/_calculate", method = RequestMethod.POST)
	public TransferFeeCalResponse calculateTransferFee(@RequestBody TransferFeeCalRequest transferFeeCalRequest)
			throws Exception {
		TransferFeeCalResponse transferFeeCalResponse = null;
		log.info("TaxCalculatorController calculateTransferFee() ---->> \n transferFeeCalRequest -------- "
				+ transferFeeCalRequest);
		try {
			transferFeeCalResponse = taxCalculatorService.calculateTransferFee(transferFeeCalRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidTaxCalculationDataException(ex.getMessage(), transferFeeCalRequest.getRequestInfo(),
					ex.getMessage());
		}
		log.info("TaxCalculatorController calculateTransferFee() ---->> \n TransferFeeCalResponse -------- "
				+ transferFeeCalResponse);
		return transferFeeCalResponse;
	}

}