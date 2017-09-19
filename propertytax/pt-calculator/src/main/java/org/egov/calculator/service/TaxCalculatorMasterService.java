package org.egov.calculator.service;

import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.RequestInfo;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.egov.models.TransferFeeRatesRequest;
import org.egov.models.TransferFeeRatesResponse;

/**
 * 
 * @author Pavan Kumar Kamma
 * 
 *         This Class will have all the service methods used in tax calculator
 *         master api's
 *
 */
public interface TaxCalculatorMasterService {

	/**
	 * Description : This service for creating new factor(s)
	 * 
	 * @param tenantId
	 * @param calculationFactorRequest
	 * @return calculationFactorResponse
	 * @throws Exception
	 */
	public CalculationFactorResponse createFactor(String tenantId, CalculationFactorRequest calculationFactorRequest)
			throws Exception;

	/**
	 * Description : This service to Update any of the factor
	 * 
	 * @param tenantId
	 * @param calculationFactorRequest
	 * @return calculationFactorResponse
	 * @throws Exception
	 */
	public CalculationFactorResponse updateFactor(String tenantId, CalculationFactorRequest calculationFactorRequest)
			throws Exception;

	/**
	 * Description : This service for search factor details
	 * 
	 * @param tenantId
	 * @param factorType
	 * @param validDate
	 * @param code
	 * @param requestInfo
	 * @return calculationFactorResponse
	 * @throws Exception
	 */
	public CalculationFactorResponse getFactor(RequestInfo requestInfo, String tenantId, String factorType,
			String validDate, String code) throws Exception;

	/**
	 * Description: create guidance
	 * 
	 * @param tenantId
	 * @param guidanceValueRequest
	 * @return
	 * @throws Exception
	 */
	public GuidanceValueResponse createGuidanceValue(String tenantId, GuidanceValueRequest guidanceValueRequest)
			throws Exception;

	/**
	 * Description: update guidance
	 * 
	 * @param tenantId
	 * @param guidanceValueRequest
	 * @return GuidanceValueResponse
	 * @throws Exception
	 */
	public GuidanceValueResponse updateGuidanceValue(String tenantId, GuidanceValueRequest guidanceValueRequest)
			throws Exception;

	/**
	 * Description: search guidance
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param boundary
	 * @param structure
	 * @param usage
	 * @param subUsage
	 * @param occupancy
	 * @param validDate
	 * @return GuidanceValueResponse
	 * @throws Exception
	 */
	public GuidanceValueResponse getGuidanceValue(RequestInfo requestInfo, String tenantId, String boundary,
			String structure, String usage, String subUsage, String occupancy, String validDate) throws Exception;

	/**
	 * Description : This service for creating new taxRate(s)
	 * 
	 * @param tenantId
	 * @param taxRatesRequest
	 * @return TaxRatesResponse
	 * @throws Exception
	 */
	public TaxRatesResponse createTaxRate(String tenantId, TaxRatesRequest taxRatesRequest) throws Exception;

	/**
	 * Description : This service for updating taxRate(s)
	 * 
	 * @param tenantId
	 * @param taxRatesRequest
	 * @return TaxRatesResponse
	 * @throws Exception
	 */
	public TaxRatesResponse updateTaxRate(String tenantId, TaxRatesRequest taxRatesRequest) throws Exception;

	/**
	 * Description : This service for search taxRate details
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
	public TaxRatesResponse getTaxRate(RequestInfo requestInfo, String tenantId, String taxHead, String validDate,
			Double validARVAmount, String parentTaxHead, String usage, String propretyType) throws Exception;

	/**
	 * This will create the tax period
	 * 
	 * @param tenantId
	 * @param taxPeriodRequest
	 * @return {@link TaxPeriodResponse}
	 * @throws Exception
	 */
	public TaxPeriodResponse createTaxPeriod(String tenantId, TaxPeriodRequest taxPeriodRequest) throws Exception;

	/**
	 * This will update the tax period with the given request for the given
	 * tenantId
	 * 
	 * @param tenantId
	 * @param taxPeriodRequest
	 * @return {@link TaxPeriodResponse}
	 * @throws Exception
	 */
	public TaxPeriodResponse updateTaxPeriod(String tenantId, TaxPeriodRequest taxPeriodRequest) throws Exception;

	/**
	 * This will search the tax periods based on the given parameter
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param validDate
	 * @param code
	 * @return {@link TaxPeriodResponse}
	 * @throws Exception
	 */
	public TaxPeriodResponse getTaxPeriod(RequestInfo requestInfo, String tenantId, String validDate, String code,
			String fromDate, String toDate, String sortTaxPeriod) throws Exception;
	
	/**
	 * This will create Transfer Fee Rates
	 * 
	 * @param transferFeeRatesRequest
	 * @param tenantId
	 * @return TransferFeeRatesResponse
	 * @throws Exception
	 */
	public TransferFeeRatesResponse createTransferFeeRate(TransferFeeRatesRequest transferFeeRatesRequest,
			String tenantId) throws Exception;
	
	/**
	 * This will update Transfer Fee Rates
	 * 
	 * @param transferFeeRatesRequest
	 * @param tenantId
	 * @return TransferFeeRatesResponse
	 * @throws Exception
	 */
	public TransferFeeRatesResponse updateTransferFeeRate(TransferFeeRatesRequest transferFeeRatesRequest,
			String tenantId) throws Exception;
	
	/**
	 * This will search Transfer Fee Rates based on given parameters
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param feeFactor
	 * @param validDate
	 * @param validValue
	 * @return TransferFeeRatesResponse
	 * @throws Exception
	 */
	public TransferFeeRatesResponse getTransferFeeRate(RequestInfo requestInfo, String tenantId, String feeFactor,
			String validDate, Double validValue) throws Exception;

}