package org.egov.calculator.api;

import org.egov.calculator.service.TaxCalculatorService;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaxCalculator Controller have the api's related to tax calculation
 * 
 * @author Pavan Kumar Kamma
 */
@RestController
@RequestMapping("/properties/taxes")
public class TaxCalculatorController {

	@Autowired
	TaxCalculatorService taxCalculatorService;

	@RequestMapping(path = "_calculate", method = RequestMethod.POST)
	public CalculationResponse calculatePropertyTax(
			@RequestBody CalculationRequest calculationRequest)
			throws Exception {

		return taxCalculatorService.calculatePropertyTax(calculationRequest);
	}

}