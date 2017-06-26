package org.egov.calculator.service;

import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;

/**
 * 
 * @author Pavan Kumar Kamma
 * 
 *         This Class will have all the service methods used in api's
 *
 */
public interface TaxCalculatorService {

	public CalculationResponse calculatePropertyTax(CalculationRequest calculationRequest) throws Exception;

}
