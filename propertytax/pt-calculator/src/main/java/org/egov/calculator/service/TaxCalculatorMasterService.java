package org.egov.calculator.service;

import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationFactorSearchCriteria;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.GuidanceValueSearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxPeriodSearchCriteria;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.egov.models.TaxRatesSearchCriteria;
import org.egov.models.TransferFeeRateSearchCriteria;
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
	 * @param requestInfo
	 * @param calculationFactorSearchCriteria
	 * @return calculationFactorResponse
	 * @throws Exception
	 */
	public CalculationFactorResponse getFactor(RequestInfo requestInfo,
			CalculationFactorSearchCriteria calculationFactorSearchCriteria) throws Exception;

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
	 * @param guidanceValueSearchCriteria
	 * @return GuidanceValueResponse
	 * @throws Exception
	 */
	public GuidanceValueResponse getGuidanceValue(RequestInfo requestInfo,
			GuidanceValueSearchCriteria guidanceValueSearchCriteria) throws Exception;

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
	 * @param requestInfo
	 * @param taxRatesSearchCriteria
	 * @return TaxRatesResponse
	 * @throws Exception
	 */
	public TaxRatesResponse getTaxRate(RequestInfo requestInfo, TaxRatesSearchCriteria taxRatesSearchCriteria)
			throws Exception;

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
	 * @param taxPeriodSearchCriteria
	 * @return {@link TaxPeriodResponse}
	 * @throws Exception
	 */
	public TaxPeriodResponse getTaxPeriod(RequestInfo requestInfo, TaxPeriodSearchCriteria taxPeriodSearchCriteria)
			throws Exception;

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
	 * @param transferFeeRateSearchCriteria
	 * @return TransferFeeRatesResponse
	 * @throws Exception
	 */
	public TransferFeeRatesResponse getTransferFeeRate(RequestInfo requestInfo,
			TransferFeeRateSearchCriteria transferFeeRateSearchCriteria) throws Exception;

}