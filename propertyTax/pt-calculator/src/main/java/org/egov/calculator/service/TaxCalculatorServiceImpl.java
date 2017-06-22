package org.egov.calculator.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.calculator.exception.InvalidInputException;
import org.egov.calculator.repository.CalculatorRepostory;
import org.egov.calculator.repository.TaxFactorRepository;
import org.egov.calculator.repository.TaxRatesRepository;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.GuidanceValue;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxRates;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description : CalculatorService interface implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Service
public class TaxCalculatorServiceImpl implements TaxCalculatorService {

	@Autowired
	TaxFactorRepository taxFactorRepository;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	CalculatorRepostory calculatorRepository;

	@Autowired
	TaxRatesRepository taxRatesRepository;

	@Override
	@Transactional
	public CalculationFactorResponse createFactor(String tenantId,
			CalculationFactorRequest calculationFactorRequest) {

		for (CalculationFactor calculationFactor : calculationFactorRequest
				.getCalculationFactors()) {

			try {

				Long id = taxFactorRepository.saveFactor(tenantId,
						calculationFactor);
				calculationFactor.setId(id);

			} catch (Exception e) {

				throw new InvalidInputException(
						calculationFactorRequest.getRequestInfo());

			}
		}

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						calculationFactorRequest.getRequestInfo(), true);
		calculationFactorResponse.setCalculationFactors(
				calculationFactorRequest.getCalculationFactors());
		calculationFactorResponse.setResponseInfo(responseInfo);

		return calculationFactorResponse;
	}

	@Override
	@Transactional
	public CalculationFactorResponse updateFactor(String tenantId,
			CalculationFactorRequest calculationFactorRequest) {

		for (CalculationFactor calculationFactor : calculationFactorRequest
				.getCalculationFactors()) {

			try {

				long updatedTime = new Date().getTime();
				long id = calculationFactor.getId();
				calculationFactor.getAuditDetails()
						.setLastModifiedTime(updatedTime);
				taxFactorRepository.updateFactor(tenantId, id,
						calculationFactor);

			} catch (Exception e) {

				throw new InvalidInputException(
						calculationFactorRequest.getRequestInfo());

			}
		}

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						calculationFactorRequest.getRequestInfo(), true);
		calculationFactorResponse.setCalculationFactors(
				calculationFactorRequest.getCalculationFactors());
		calculationFactorResponse.setResponseInfo(responseInfo);

		return calculationFactorResponse;
	}

	@Override
	public CalculationFactorResponse getFactor(RequestInfo requestInfo,
			String tenantId, String factorType, String validDate, String code) {

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		try {

			List<CalculationFactor> calculationFactors = taxFactorRepository
					.searchFactor(tenantId, factorType, validDate, code);
			ResponseInfo responseInfo = responseInfoFactory
					.createResponseInfoFromRequestInfo(requestInfo, true);

			calculationFactorResponse.setCalculationFactors(calculationFactors);
			calculationFactorResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return calculationFactorResponse;
	}

	public List<CalculationFactor> getFactorsByTenantIdAndValidDate(
			String tenantId, String validDate) {

		List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();
		calculationFactors = taxFactorRepository
				.getFactorsByTenantIdAndValidDate(tenantId, validDate);

		return calculationFactors;
	}

	@Override
	@Transactional
	public GuidanceValueResponse createGuidanceValue(String tenantId,
			GuidanceValueRequest guidanceValueRequest) throws Exception {
		// TODO Auto-generated method stub

		for (GuidanceValue guidanceValue : guidanceValueRequest
				.getGuidanceValues()) {

			Long createdTime = new Date().getTime();
			Long id = calculatorRepository.saveGuidanceValue(tenantId,
					guidanceValue);

			guidanceValue.setId(id);
			guidanceValue.getAuditDetails().setCreatedTime(createdTime);
			guidanceValue.getAuditDetails().setLastModifiedTime(createdTime);
		}

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						guidanceValueRequest.getRequestInfo(), true);
		GuidanceValueResponse guidanceValueResponce = new GuidanceValueResponse();
		guidanceValueResponce
				.setGuidanceValues(guidanceValueRequest.getGuidanceValues());
		guidanceValueResponce.setResponseInfo(responseInfo);
		return guidanceValueResponce;
	}

	@Override
	@Transactional
	public GuidanceValueResponse updateGuidanceValue(String tenantId,
			GuidanceValueRequest guidanceValueRequest) throws Exception {
		// TODO Auto-generated method stub
		for (GuidanceValue guidanceValue : guidanceValueRequest
				.getGuidanceValues()) {

			Long modifiedTime = new Date().getTime();
			calculatorRepository.udpateGuidanceValue(tenantId, guidanceValue);
			guidanceValue.getAuditDetails().setLastModifiedTime(modifiedTime);
		}

		ResponseInfo requestInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						guidanceValueRequest.getRequestInfo(), true);
		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();
		guidanceValueResponse
				.setGuidanceValues(guidanceValueRequest.getGuidanceValues());
		guidanceValueResponse.setResponseInfo(requestInfo);
		return guidanceValueResponse;
	}

	@Override
	public GuidanceValueResponse getGuidanceValue(RequestInfo requestInfo,
			String tenantId, String boundary, String structure, String usage,
			String subUsage, String occupancy, String validDate, String code)
			throws Exception {
		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();

		try {

			List<GuidanceValue> guidanceValues = calculatorRepository
					.searchGuidanceValue(tenantId, boundary, structure, usage,
							subUsage, occupancy, validDate, code);
			ResponseInfo responseInfo = responseInfoFactory
					.createResponseInfoFromRequestInfo(requestInfo, true);

			guidanceValueResponse.setGuidanceValues(guidanceValues);
			guidanceValueResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return guidanceValueResponse;
	}

	@Override
	@Transactional
	public TaxRatesResponse createTaxRate(String tenantId,
			TaxRatesRequest taxRatesRequest) throws Exception {

		for (TaxRates taxRates : taxRatesRequest.getTaxRates()) {

			try {

				Long id = taxRatesRepository.createTaxRates(tenantId, taxRates);
				taxRates.setId(id);

			} catch (Exception e) {

				throw new InvalidInputException(
						taxRatesRequest.getRequestInfo());
			}
		}

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						taxRatesRequest.getRequestInfo(), true);
		taxRatesResponse.setTaxRates(taxRatesRequest.getTaxRates());
		taxRatesResponse.setResponseInfo(responseInfo);
		return taxRatesResponse;
	};

	@Override
	@Transactional
	public TaxRatesResponse updateTaxRate(String tenantId,
			TaxRatesRequest taxRatesRequest) throws Exception {

		for (TaxRates taxRates : taxRatesRequest.getTaxRates()) {

			try {
				long updatedTime = new Date().getTime();
				taxRates.getAuditDetails().setLastModifiedTime(updatedTime);
				taxRatesRepository.updateTaxRates(tenantId, taxRates);

			} catch (Exception e) {

				throw new InvalidInputException(
						taxRatesRequest.getRequestInfo());
			}
		}
		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(
						taxRatesRequest.getRequestInfo(), true);
		taxRatesResponse.setTaxRates(taxRatesRequest.getTaxRates());
		taxRatesResponse.setResponseInfo(responseInfo);

		return taxRatesResponse;
	};

	@Override
	public TaxRatesResponse getTaxRate(RequestInfo requestInfo, String tenantId,
			String taxHead, String validDate, Double validARVAmount,
			String parentTaxHead) throws Exception {

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();

		try {

			List<TaxRates> listOfTaxRates = taxRatesRepository.searchTaxRates(
					tenantId, taxHead, validDate, validARVAmount,
					parentTaxHead);
			ResponseInfo responseInfo = responseInfoFactory
					.createResponseInfoFromRequestInfo(requestInfo, true);
			taxRatesResponse.setTaxRates(listOfTaxRates);
			taxRatesResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {

			throw new InvalidInputException(requestInfo);

		}

		return taxRatesResponse;
	};

	@Override
	public TaxPeriodResponse createTaxPeriod(String tenantId,
			TaxPeriodRequest taxPeriodRequest) throws Exception {
		// TODO Auto-generated method stub
		return null;
	};

	@Override
	public TaxPeriodResponse updateTaxPeriod(String tenantId,
			TaxPeriodRequest taxPeriodRequest) throws Exception {
		// TODO Auto-generated method stub
		return null;
	};

	@Override
	public TaxPeriodResponse getTaxPeriod(RequestInfo requestInfo,
			String tenantId, String validDate, String code) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */

	@Override
	public CalculationResponse calculatePropertyTax(
			CalculationRequest calculationRequest) {
		// TODO Auto-generated method stub
		return new CalculationResponse();
	};

}
