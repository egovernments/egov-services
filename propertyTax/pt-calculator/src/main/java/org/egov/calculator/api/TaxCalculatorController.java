package org.egov.calculator.api;

import org.egov.calculator.service.TaxCalculatorService;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaxCalculator Controller have the api's related to tax calculation
 * 
 * @author Pavan Kumar Kamma
 */
@RestController
public class TaxCalculatorController {

	@Autowired
	TaxCalculatorService calculatorService;

	/**
	 * Description : This api for creating new factor(s)
	 * 
	 * @param tenantId
	 * @param calculationFactorRequest
	 * @return calculationFactorResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/factor/_create", method = RequestMethod.POST)
	public CalculationFactorResponse createFactor(@RequestParam String tenantId,
			@RequestBody CalculationFactorRequest calculationFactorRequest)
			throws Exception {

		return calculatorService.createFactor(tenantId,
				calculationFactorRequest);
	}

	/**
	 * Description : This api to Update any of the factor
	 * 
	 * @param tenantId
	 * @param calculationFactorRequest
	 * @return calculationFactorResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/factor/_update", method = RequestMethod.POST)
	public CalculationFactorResponse updateFactor(@RequestParam String tenantId,
			@RequestBody CalculationFactorRequest calculationFactorRequest)
			throws Exception {

		return calculatorService.updateFactor(tenantId,
				calculationFactorRequest);
	}

	/**
	 * Description : This api for getting factor details
	 * 
	 * @param tenantId
	 * @param factorType
	 * @param validDate
	 * @param code
	 * @param requestInfo
	 * @return calculationFactorResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/factor/_search", method = RequestMethod.POST)
	public CalculationFactorResponse getFactor(
			@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId,
			@RequestParam(required = true) String factorType,
			@RequestParam(required = true) String validDate,
			@RequestParam(required = true) String code) throws Exception {

		return calculatorService.getFactor(requestInfo.getRequestInfo(),
				tenantId, factorType, validDate, code);

	}

	/**
	 * Description : This api for creating new guidanceValue(s)
	 * 
	 * @param tenantId
	 * @param guidanceValueRequest
	 * @return GuidanceValueResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/guidancevalue/_create", method = RequestMethod.POST)
	public GuidanceValueResponse createGuidanceValue(
			@RequestParam String tenantId,
			@RequestBody GuidanceValueRequest guidanceValueRequest)
			throws Exception {

		return calculatorService.createGuidanceValue(tenantId,
				guidanceValueRequest);
	}

	/**
	 * Description : This api for updating guidanceValue(s)
	 * 
	 * @param tenantId
	 * @param guidanceValueRequest
	 * @return GuidanceValueResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/guidancevalue/_update", method = RequestMethod.POST)
	public GuidanceValueResponse updateGuidanceValue(
			@RequestParam String tenantId,
			@RequestBody GuidanceValueRequest guidanceValueRequest)
			throws Exception {

		return calculatorService.updateGuidanceValue(tenantId,
				guidanceValueRequest);
	}

	/**
	 * Description : This api for getting guidancevalue details
	 * 
	 * @param tenantId
	 * @param boundary
	 * @param structure
	 * @param usage
	 * @param subUsage
	 * @param occupancy
	 * @param validDate
	 * @param code
	 * @param requestInfo
	 * @return GuidanceValueResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/guidancevalue/_search", method = RequestMethod.POST)
	public GuidanceValueResponse getGuidanceValue(
			@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId,
			@RequestParam(required = true) String boundary,
			@RequestParam(required = false) String structure,
			@RequestParam(required = false) String usage,
			@RequestParam(required = false) String subUsage,
			@RequestParam(required = false) String occupancy,
			@RequestParam(required = true) String validDate,
			@RequestParam(required = true) String code) throws Exception {

		return calculatorService.getGuidanceValue(requestInfo.getRequestInfo(),
				tenantId, boundary, structure, usage, subUsage, occupancy,
				validDate, code);

	}

	/**
	 * Description : This api for creating new taxRate(s)
	 * 
	 * @param tenantId
	 * @param taxRatesRequest
	 * @return TaxRatesResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxrates/_create", method = RequestMethod.POST)
	public TaxRatesResponse createTaxRate(@RequestParam String tenantId,
			@RequestBody TaxRatesRequest taxRatesRequest) throws Exception {

		return calculatorService.createTaxRate(tenantId, taxRatesRequest);
	}

	/**
	 * Description : This api for updating taxRate(s)
	 * 
	 * @param tenantId
	 * @param taxRatesRequest
	 * @return TaxRatesResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxrates/_update", method = RequestMethod.POST)
	public TaxRatesResponse updateTaxRate(@RequestParam String tenantId,
			@RequestBody TaxRatesRequest taxRatesRequest) throws Exception {

		return calculatorService.updateTaxRate(tenantId, taxRatesRequest);
	}

	/**
	 * Description : This api for getting taxRate details
	 * 
	 * @param tenantId
	 * @param taxHead
	 * @param validDate
	 * @param validARVAmount
	 * @param parentTaxHead
	 * @param requestInfo
	 * @return TaxRatesResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/taxrates/_search", method = RequestMethod.POST)
	public TaxRatesResponse getTaxRate(
			@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId,
			@RequestParam(required = true) String taxHead,
			@RequestParam(required = true) String validDate,
			@RequestParam(required = true) Double validARVAmount,
			@RequestParam(required = false) String parentTaxHead)
			throws Exception {

		return calculatorService.getTaxRate(requestInfo.getRequestInfo(),
				tenantId, taxHead, validDate, validARVAmount, parentTaxHead);

	}

	/**
	 * Description : This api for creating new taxperiod(s)
	 * 
	 * @param tenantId
	 * @param TaxPeriodRequest
	 * @return TaxPeriodResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxperiods/_create", method = RequestMethod.POST)
	public TaxPeriodResponse createTaxPeriod(@RequestParam String tenantId,
			@RequestBody TaxPeriodRequest taxPeriodRequest) throws Exception {

		return calculatorService.createTaxPeriod(tenantId, taxPeriodRequest);
	}

	/**
	 * Description : This api for updating taxperiod(s)
	 * 
	 * @param tenantId
	 * @param TaxPeriodRequest
	 * @return TaxPeriodResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxperiods/_update", method = RequestMethod.POST)
	public TaxPeriodResponse updateTaxPeriod(@RequestParam String tenantId,
			@RequestBody TaxPeriodRequest taxPeriodRequest) throws Exception {

		return calculatorService.updateTaxPeriod(tenantId, taxPeriodRequest);
	}

	/**
	 * Description : This api for getting taxperiod details
	 * 
	 * @param tenantId
	 * @param validDate
	 * @param requestInfo
	 * @return code
	 * @throws Exception
	 */

	@RequestMapping(path = "/taxperiods/_search", method = RequestMethod.POST)
	public TaxPeriodResponse getTaxPeriod(
			@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId,
			@RequestParam(required = true) String validDate,
			@RequestParam(required = true) String code) throws Exception {

		return calculatorService.getTaxPeriod(requestInfo.getRequestInfo(),
				tenantId, validDate, code);

	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public CalculationResponse calculatePropertyTax(
			@RequestBody CalculationRequest calculationRequest) {

		return calculatorService.calculatePropertyTax(calculationRequest);
	}

}
