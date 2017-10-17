package org.egov.calculator.api;

import javax.validation.Valid;

import org.egov.calculator.exception.InvalidSearchParameterException;
import org.egov.calculator.service.TaxCalculatorMasterService;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationFactorSearchCriteria;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.GuidanceValueSearchCriteria;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxPeriodSearchCriteria;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.egov.models.TaxRatesSearchCriteria;
import org.egov.models.TransferFeeRateSearchCriteria;
import org.egov.models.TransferFeeRatesRequest;
import org.egov.models.TransferFeeRatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaxCalculatorMasterController have the api's related to taxes master
 * 
 * @author Pavan Kumar Kamma
 */
@RestController
@RequestMapping("/properties/taxes")
public class TaxCalculatorMasterController {

	@Autowired
	TaxCalculatorMasterService taxCalculationMasterService;

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
			@Valid @RequestBody CalculationFactorRequest calculationFactorRequest) throws Exception {

		return taxCalculationMasterService.createFactor(tenantId, calculationFactorRequest);
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
			@Valid @RequestBody CalculationFactorRequest calculationFactorRequest) throws Exception {

		return taxCalculationMasterService.updateFactor(tenantId, calculationFactorRequest);
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
	public CalculationFactorResponse getFactor(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid CalculationFactorSearchCriteria calculationFactorSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}

		return taxCalculationMasterService.getFactor(requestInfo.getRequestInfo(), calculationFactorSearchCriteria);
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
	public GuidanceValueResponse createGuidanceValue(@RequestParam String tenantId,
			@Valid @RequestBody GuidanceValueRequest guidanceValueRequest) throws Exception {

		return taxCalculationMasterService.createGuidanceValue(tenantId, guidanceValueRequest);
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
	public GuidanceValueResponse updateGuidanceValue(@RequestParam String tenantId,
			@Valid @RequestBody GuidanceValueRequest guidanceValueRequest) throws Exception {

		return taxCalculationMasterService.updateGuidanceValue(tenantId, guidanceValueRequest);
	}

	/**
	 * Description : This api for getting guidancevalue details
	 * 
	 * @param tenantId
	 * @param guidanceValueSearchCriteria
	 * @return GuidanceValueResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/guidancevalue/_search", method = RequestMethod.POST)
	public GuidanceValueResponse getGuidanceValue(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid GuidanceValueSearchCriteria guidanceValueSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}

		return taxCalculationMasterService.getGuidanceValue(requestInfo.getRequestInfo(), guidanceValueSearchCriteria);
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
			@Valid @RequestBody TaxRatesRequest taxRatesRequest) throws Exception {

		return taxCalculationMasterService.createTaxRate(tenantId, taxRatesRequest);
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
			@Valid @RequestBody TaxRatesRequest taxRatesRequest) throws Exception {

		return taxCalculationMasterService.updateTaxRate(tenantId, taxRatesRequest);
	}

	/**
	 * Description : This api for getting taxRate details
	 * 
	 * @param requestInfo
	 * @param taxRatesSearchCriteria
	 * @return TaxRatesResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxrates/_search", method = RequestMethod.POST)
	public TaxRatesResponse getTaxRate(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid TaxRatesSearchCriteria taxRatesSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return taxCalculationMasterService.getTaxRate(requestInfo.getRequestInfo(), taxRatesSearchCriteria);
	}

	/**
	 * Description : This will create the tax period
	 * 
	 * @param tenantId
	 * @param TaxPeriodRequest
	 * @return {@link TaxPeriodResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxperiods/_create", method = RequestMethod.POST)
	public TaxPeriodResponse createTaxPeriod(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody(required = true) TaxPeriodRequest taxPeriodRequest) throws Exception {

		return taxCalculationMasterService.createTaxPeriod(tenantId, taxPeriodRequest);
	}

	/**
	 * Description : This will update the tax period with the given request for
	 * the given tenantId
	 * 
	 * @param tenantId
	 * @param TaxPeriodRequest
	 * @return {@link TaxPeriodResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxperiods/_update", method = RequestMethod.POST)
	public TaxPeriodResponse updateTaxPeriod(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody TaxPeriodRequest taxPeriodRequest) throws Exception {

		return taxCalculationMasterService.updateTaxPeriod(tenantId, taxPeriodRequest);
	}

	/**
	 * Description : This will search the tax periods based on the given
	 * parameter
	 * 
	 * @param requestInfo
	 * @param taxPeriodSearchCriteria
	 * @return TaxPeriodResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/taxperiods/_search", method = RequestMethod.POST)
	public TaxPeriodResponse getTaxPeriod(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid TaxPeriodSearchCriteria taxPeriodSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return taxCalculationMasterService.getTaxPeriod(requestInfo.getRequestInfo(), taxPeriodSearchCriteria);
	}

	/**
	 * Description : This will create Transfer Fee Rates
	 * 
	 * @param transferFeeRatesRequest
	 * @param tenantId
	 * @return TransferFeeRatesResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/transferfeerates/_create", method = RequestMethod.POST)
	public TransferFeeRatesResponse createTransferFeeRate(@RequestBody TransferFeeRatesRequest transferFeeRatesRequest,
			@RequestParam(required = true) String tenantId) throws Exception {
		return taxCalculationMasterService.createTransferFeeRate(transferFeeRatesRequest, tenantId);
	}

	/**
	 * Description : This will update Transfer Fee Rates
	 * 
	 * @param transferFeeRatesRequest
	 * @param tenantId
	 * @return TransferFeeRatesResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/transferfeerates/_update", method = RequestMethod.POST)
	public TransferFeeRatesResponse updateTransferFeeRate(@RequestBody TransferFeeRatesRequest transferFeeRatesRequest,
			@RequestParam(required = true) String tenantId) throws Exception {
		return taxCalculationMasterService.updateTransferFeeRate(transferFeeRatesRequest, tenantId);
	}

	/**
	 * Description : This will search Transfer Fee Rate based on given
	 * parameters
	 * 
	 * @param requestInfo
	 * @param transferFeeRateSearchCriteria
	 * @return TransferFeeRatesResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/transferfeerates/_search", method = RequestMethod.POST)
	public TransferFeeRatesResponse getTransferFeeRate(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid TransferFeeRateSearchCriteria transferFeeRateSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return taxCalculationMasterService.getTransferFeeRate(requestInfo.getRequestInfo(),
				transferFeeRateSearchCriteria);
	}
}