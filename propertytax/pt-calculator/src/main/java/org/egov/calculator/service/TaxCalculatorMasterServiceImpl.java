package org.egov.calculator.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.calculator.config.PropertiesManager;
import org.egov.calculator.exception.InvalidInputException;
import org.egov.calculator.repository.FactorRepository;
import org.egov.calculator.repository.GuidanceValueRepostory;
import org.egov.calculator.repository.TaxPeriodRespository;
import org.egov.calculator.repository.TaxRatesRepository;
import org.egov.models.AuditDetails;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.GuidanceValue;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.OccuapancyMasterResponse;
import org.egov.models.PropertyTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.StructureClassResponse;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxRates;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.egov.models.UsageMasterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Description : CalculatorService interface implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class TaxCalculatorMasterServiceImpl implements TaxCalculatorMasterService {

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	FactorRepository factorRepository;

	@Autowired
	GuidanceValueRepostory guidanceValueRepostory;

	@Autowired
	TaxRatesRepository taxRatesRepository;

	@Autowired
	private TaxPeriodRespository taxPeriodRespository;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	@Transactional
	public CalculationFactorResponse createFactor(String tenantId, CalculationFactorRequest calculationFactorRequest) {

		for (CalculationFactor calculationFactor : calculationFactorRequest.getCalculationFactors()) {

			validateFactorCode(calculationFactor, calculationFactorRequest);
			AuditDetails auditDetails = getAuditDetail(calculationFactorRequest.getRequestInfo());
			try {
				calculationFactor.setAuditDetails(auditDetails);
				Long id = factorRepository.saveFactor(tenantId, calculationFactor);
				calculationFactor.setId(id);

			} catch (Exception e) {

				throw new InvalidInputException(calculationFactorRequest.getRequestInfo());

			}
		}

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(calculationFactorRequest.getRequestInfo(), true);
		calculationFactorResponse.setCalculationFactors(calculationFactorRequest.getCalculationFactors());
		calculationFactorResponse.setResponseInfo(responseInfo);

		return calculationFactorResponse;
	}

	/**
	 * Method validates the factorcode
	 * 
	 * @param calculationFactor
	 * @param calculationFactorRequest
	 * @return
	 * @exception InvalidInputException
	 */
	private void validateFactorCode(CalculationFactor calculationFactor,
			CalculationFactorRequest calculationFactorRequest) {

		if (calculationFactor.getFactorType() != null) {

			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper.setRequestInfo(calculationFactorRequest.getRequestInfo());
			RestTemplate restTemplate = new RestTemplate();
			StringBuilder uri = new StringBuilder();
			Map<String, String> params = new HashMap<String, String>();
			uri.append(propertiesManager.getPropertyHostName());
			uri.append(propertiesManager.getPropertyBasepath());
			uri.append(propertiesManager.getPropertySearch());
			params.put("tenantId", calculationFactor.getTenantId());
			params.put("code", calculationFactor.getFactorCode());

			switch (calculationFactor.getFactorType()) {

			case OCCUPANCY:

				params.put("factorType", "occuapancies");
				OccuapancyMasterResponse occuapancyMasterResponse = restTemplate.postForObject(uri.toString(),
						requestInfoWrapper, OccuapancyMasterResponse.class, params);
				if (occuapancyMasterResponse.getOccuapancyMasters().size() == 0) {
					throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
				}
				break;
			case USAGE:

				params.put("factorType", "usages");
				UsageMasterResponse usageMasterResponse = restTemplate.postForObject(uri.toString(), requestInfoWrapper,
						UsageMasterResponse.class, params);
				if (usageMasterResponse.getUsageMasters().size() == 0) {
					throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
				}
				break;
			case STRUCTURE:

				params.put("factorType", "structureclasses");
				StructureClassResponse structureClassResponse = restTemplate.postForObject(uri.toString(),
						requestInfoWrapper, StructureClassResponse.class, params);
				if (structureClassResponse.getStructureClasses().size() == 0) {
					throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
				}
				break;
			case PROPERTYTYPE:

				params.put("factorType", "propertytypes");
				PropertyTypeResponse propertyTypeResponse = restTemplate.postForObject(uri.toString(),
						requestInfoWrapper, PropertyTypeResponse.class, params);
				if (propertyTypeResponse.getPropertyTypes().size() == 0) {
					throw new InvalidInputException(calculationFactorRequest.getRequestInfo());
				}
				break;
			default:
				break;

			}
		}
	}

	@Override
	@Transactional
	public CalculationFactorResponse updateFactor(String tenantId, CalculationFactorRequest calculationFactorRequest) {

		for (CalculationFactor calculationFactor : calculationFactorRequest.getCalculationFactors()) {
			AuditDetails auditDetails = getAuditDetail(calculationFactorRequest.getRequestInfo());
			try {
				long id = calculationFactor.getId();
				calculationFactor.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
				calculationFactor.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
				factorRepository.updateFactor(tenantId, id, calculationFactor);

			} catch (Exception e) {

				throw new InvalidInputException(calculationFactorRequest.getRequestInfo());

			}
		}

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(calculationFactorRequest.getRequestInfo(), true);
		calculationFactorResponse.setCalculationFactors(calculationFactorRequest.getCalculationFactors());
		calculationFactorResponse.setResponseInfo(responseInfo);

		return calculationFactorResponse;
	}

	@Override
	public CalculationFactorResponse getFactor(RequestInfo requestInfo, String tenantId, String factorType,
			String validDate, String code) {

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		try {

			List<CalculationFactor> calculationFactors = factorRepository.searchFactor(tenantId, factorType, validDate,
					code);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

			calculationFactorResponse.setCalculationFactors(calculationFactors);
			calculationFactorResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return calculationFactorResponse;
	}

	@Override
	@Transactional
	public GuidanceValueResponse createGuidanceValue(String tenantId, GuidanceValueRequest guidanceValueRequest)
			throws Exception {
		// TODO Auto-generated method stub

		for (GuidanceValue guidanceValue : guidanceValueRequest.getGuidanceValues()) {

			AuditDetails auditDetails = getAuditDetail(guidanceValueRequest.getRequestInfo());
			guidanceValue.setAuditDetails(auditDetails);
			Long id = guidanceValueRepostory.saveGuidanceValue(tenantId, guidanceValue);
			guidanceValue.setId(id);
		}

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(guidanceValueRequest.getRequestInfo(), true);
		GuidanceValueResponse guidanceValueResponce = new GuidanceValueResponse();
		guidanceValueResponce.setGuidanceValues(guidanceValueRequest.getGuidanceValues());
		guidanceValueResponce.setResponseInfo(responseInfo);
		return guidanceValueResponce;
	}

	@Override
	@Transactional
	public GuidanceValueResponse updateGuidanceValue(String tenantId, GuidanceValueRequest guidanceValueRequest)
			throws Exception {
		// TODO Auto-generated method stub
		for (GuidanceValue guidanceValue : guidanceValueRequest.getGuidanceValues()) {
			AuditDetails auditDetails = getAuditDetail(guidanceValueRequest.getRequestInfo());
			guidanceValue.setAuditDetails(auditDetails);
			guidanceValueRepostory.udpateGuidanceValue(tenantId, guidanceValue);
		}

		ResponseInfo requestInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(guidanceValueRequest.getRequestInfo(), true);
		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();
		guidanceValueResponse.setGuidanceValues(guidanceValueRequest.getGuidanceValues());
		guidanceValueResponse.setResponseInfo(requestInfo);
		return guidanceValueResponse;
	}

	@Override
	public GuidanceValueResponse getGuidanceValue(RequestInfo requestInfo, String tenantId, String boundary,
			String structure, String usage, String subUsage, String occupancy, String validDate) throws Exception {
		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();

		try {

			List<GuidanceValue> guidanceValues = guidanceValueRepostory.searchGuidanceValue(tenantId, boundary,
					structure, usage, subUsage, occupancy, validDate);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

			guidanceValueResponse.setGuidanceValues(guidanceValues);
			guidanceValueResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return guidanceValueResponse;
	}

	@Override
	@Transactional
	public TaxRatesResponse createTaxRate(String tenantId, TaxRatesRequest taxRatesRequest) throws Exception {

		for (TaxRates taxRates : taxRatesRequest.getTaxRates()) {
			AuditDetails auditDetails = getAuditDetail(taxRatesRequest.getRequestInfo());
			try {
				taxRates.setAuditDetails(auditDetails);
				Long id = taxRatesRepository.createTaxRates(tenantId, taxRates);
				taxRates.setId(id);

			} catch (Exception e) {

				throw new InvalidInputException(taxRatesRequest.getRequestInfo());
			}
		}

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(taxRatesRequest.getRequestInfo(), true);
		taxRatesResponse.setTaxRates(taxRatesRequest.getTaxRates());
		taxRatesResponse.setResponseInfo(responseInfo);
		return taxRatesResponse;
	};

	@Override
	@Transactional
	public TaxRatesResponse updateTaxRate(String tenantId, TaxRatesRequest taxRatesRequest) throws Exception {

		for (TaxRates taxRates : taxRatesRequest.getTaxRates()) {
			AuditDetails auditDetails = getAuditDetail(taxRatesRequest.getRequestInfo());
			try {
				taxRates.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
				taxRates.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
				taxRatesRepository.updateTaxRates(tenantId, taxRates);

			} catch (Exception e) {

				throw new InvalidInputException(taxRatesRequest.getRequestInfo());
			}
		}
		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();

		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(taxRatesRequest.getRequestInfo(), true);
		taxRatesResponse.setTaxRates(taxRatesRequest.getTaxRates());
		taxRatesResponse.setResponseInfo(responseInfo);

		return taxRatesResponse;
	};

	@Override
	public TaxRatesResponse getTaxRate(RequestInfo requestInfo, String tenantId, String taxHead, String validDate,
			Double validARVAmount, String parentTaxHead) throws Exception {

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();

		try {

			List<TaxRates> listOfTaxRates = taxRatesRepository.searchTaxRates(tenantId, taxHead, validDate,
					validARVAmount, parentTaxHead);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			taxRatesResponse.setTaxRates(listOfTaxRates);
			taxRatesResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {

			throw new InvalidInputException(requestInfo);

		}

		return taxRatesResponse;
	};

	@Override
	@Transactional
	public TaxPeriodResponse createTaxPeriod(String tenantId, TaxPeriodRequest taxPeriodRequest) throws Exception {

		for (TaxPeriod taxPeriod : taxPeriodRequest.getTaxPeriods()) {
			AuditDetails auditDetails = getAuditDetail(taxPeriodRequest.getRequestInfo());
			try {
				taxPeriod.setAuditDetails(auditDetails);
				Long id = taxPeriodRespository.saveTaxPeriod(taxPeriod, tenantId);
				taxPeriod.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(taxPeriodRequest.getRequestInfo());
			}

		}

		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(taxPeriodRequest.getRequestInfo(), true);
		taxPeriodResponse.setResponseInfo(responseInfo);
		taxPeriodResponse.setTaxPeriods(taxPeriodRequest.getTaxPeriods());

		return taxPeriodResponse;
	};

	@Override
	@Transactional
	public TaxPeriodResponse updateTaxPeriod(String tenantId, TaxPeriodRequest taxPeriodRequest) throws Exception {

		for (TaxPeriod taxPeriod : taxPeriodRequest.getTaxPeriods()) {
			AuditDetails auditDetails = getAuditDetail(taxPeriodRequest.getRequestInfo());
			try {
				taxPeriod.setAuditDetails(auditDetails);
				taxPeriodRespository.updateTaxPeriod(taxPeriod, tenantId);
			} catch (Exception e) {
				throw new InvalidInputException(taxPeriodRequest.getRequestInfo());
			}

		}

		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(taxPeriodRequest.getRequestInfo(), true);
		taxPeriodResponse.setResponseInfo(responseInfo);
		taxPeriodResponse.setTaxPeriods(taxPeriodRequest.getTaxPeriods());

		return taxPeriodResponse;
	};

	@Override
	public TaxPeriodResponse getTaxPeriod(RequestInfo requestInfo, String tenantId, String validDate, String code)
			throws Exception {

		List<TaxPeriod> taxPeriods = null;
		try {
			taxPeriods = taxPeriodRespository.searchTaxPeriod(tenantId, validDate, code);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		taxPeriodResponse.setResponseInfo(responseInfo);
		taxPeriodResponse.setTaxPeriods(taxPeriods);

		return taxPeriodResponse;
	}

	private AuditDetails getAuditDetail(RequestInfo requestInfo) {

		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();

		AuditDetails auditDetail = new AuditDetails();
		auditDetail.setCreatedBy(userId);
		auditDetail.setCreatedTime(currEpochDate);
		auditDetail.setLastModifiedBy(userId);
		auditDetail.setLastModifiedTime(currEpochDate);
		return auditDetail;
	}

}
