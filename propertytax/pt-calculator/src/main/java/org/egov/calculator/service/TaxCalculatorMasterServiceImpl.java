package org.egov.calculator.service;

import java.util.Date;
import java.util.List;

import org.egov.calculator.config.PropertiesManager;
import org.egov.calculator.exception.DuplicateIdException;
import org.egov.calculator.exception.InvalidInputException;
import org.egov.calculator.exception.InvalidPenaltyDataException;
import org.egov.calculator.repository.FactorRepository;
import org.egov.calculator.repository.GuidanceValueRepostory;
import org.egov.calculator.repository.PropertyRepository;
import org.egov.calculator.repository.TaxPeriodRespository;
import org.egov.calculator.repository.TaxRatesRepository;
import org.egov.calculator.repository.TransferFeeRateRepository;
import org.egov.calculator.utility.ConstantUtility;
import org.egov.calculator.utility.TimeStampUtil;
import org.egov.models.AuditDetails;
import org.egov.models.CalculationFactor;
import org.egov.models.CalculationFactorRequest;
import org.egov.models.CalculationFactorResponse;
import org.egov.models.CalculationFactorSearchCriteria;
import org.egov.models.GuidanceValue;
import org.egov.models.GuidanceValueRequest;
import org.egov.models.GuidanceValueResponse;
import org.egov.models.GuidanceValueSearchCriteria;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.TaxPeriod;
import org.egov.models.TaxPeriodRequest;
import org.egov.models.TaxPeriodResponse;
import org.egov.models.TaxPeriodSearchCriteria;
import org.egov.models.TaxRates;
import org.egov.models.TaxRatesRequest;
import org.egov.models.TaxRatesResponse;
import org.egov.models.TaxRatesSearchCriteria;
import org.egov.models.TransferFeeRate;
import org.egov.models.TransferFeeRateSearchCriteria;
import org.egov.models.TransferFeeRatesRequest;
import org.egov.models.TransferFeeRatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * Description : CalculatorService interface implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
@Slf4j
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
	TransferFeeRateRepository transferFeeRateRepository;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PropertyRepository propertyRepository;

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

			switch (calculationFactor.getFactorType()) {

			case OCCUPANCY:
				log.info("TaxCalculatorMasterServiceImpl,  entered OCCUPANCY ");
				propertyRepository.getOccuapancyMaster(calculationFactor, calculationFactorRequest, "occuapancies");

				break;
			case USAGE:
				log.info("TaxCalculatorMasterServiceImpl,  entered USAGE ");
				propertyRepository.getUsageMaster(calculationFactor, calculationFactorRequest, "usages");

				break;
			case STRUCTURE:
				log.info("TaxCalculatorMasterServiceImpl,  entered STRUCTURE ");
				propertyRepository.getStructure(calculationFactor, calculationFactorRequest, "structureclasses");

				break;
			case PROPERTYTYPE:
				log.info("TaxCalculatorMasterServiceImpl,  entered PROPERTYTYPE ");
				propertyRepository.getPropertyType(calculationFactor, calculationFactorRequest, "propertytypes");

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
			AuditDetails auditDetails = getUpdatedAuditDetails(calculationFactorRequest.getRequestInfo(),
					ConstantUtility.FACTOR_TABLE_NAME, calculationFactor.getId());
			try {
				long id = calculationFactor.getId();
				calculationFactor.setAuditDetails(auditDetails);
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
	public CalculationFactorResponse getFactor(RequestInfo requestInfo,
			CalculationFactorSearchCriteria calculationFactorSearchCriteria) {

		CalculationFactorResponse calculationFactorResponse = new CalculationFactorResponse();

		try {

			List<CalculationFactor> calculationFactors = factorRepository.searchFactor(
					calculationFactorSearchCriteria.getTenantId(), calculationFactorSearchCriteria.getFactorType(),
					calculationFactorSearchCriteria.getValidDate(), calculationFactorSearchCriteria.getCode());
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
			AuditDetails auditDetails = getUpdatedAuditDetails(guidanceValueRequest.getRequestInfo(),
					ConstantUtility.GUIDANCEVALUE_TABLE_NAME, guidanceValue.getId());
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
	public GuidanceValueResponse getGuidanceValue(RequestInfo requestInfo,
			GuidanceValueSearchCriteria guidanceValueSearchCriteria) throws Exception {
		GuidanceValueResponse guidanceValueResponse = new GuidanceValueResponse();

		try {

			List<GuidanceValue> guidanceValues = guidanceValueRepostory.searchGuidanceValue(
					guidanceValueSearchCriteria.getTenantId(), guidanceValueSearchCriteria.getBoundary(),
					guidanceValueSearchCriteria.getStructure(), guidanceValueSearchCriteria.getUsage(),
					guidanceValueSearchCriteria.getSubUsage(), guidanceValueSearchCriteria.getOccupancy(),
					guidanceValueSearchCriteria.getValidDate());
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
			AuditDetails auditDetails = getUpdatedAuditDetails(taxRatesRequest.getRequestInfo(),
					ConstantUtility.TAXRATE_TABLE_NAME, taxRates.getId());
			try {
				taxRates.setAuditDetails(auditDetails);
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
	public TaxRatesResponse getTaxRate(RequestInfo requestInfo, TaxRatesSearchCriteria taxRatesSearchCriteria)
			throws Exception {

		TaxRatesResponse taxRatesResponse = new TaxRatesResponse();

		try {

			List<TaxRates> listOfTaxRates = taxRatesRepository.searchTaxRates(taxRatesSearchCriteria.getTenantId(),
					taxRatesSearchCriteria.getTaxHead(), taxRatesSearchCriteria.getValidDate(),
					taxRatesSearchCriteria.getValidARVAmount(), taxRatesSearchCriteria.getParentTaxHead(),
					taxRatesSearchCriteria.getUsage(), taxRatesSearchCriteria.getPropertyType());
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
			AuditDetails auditDetails = getUpdatedAuditDetails(taxPeriodRequest.getRequestInfo(),
					ConstantUtility.TAXPERIODS_TABLE_NAME, taxPeriod.getId());
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
	public TaxPeriodResponse getTaxPeriod(RequestInfo requestInfo, TaxPeriodSearchCriteria taxPeriodSearchCriteria)
			throws Exception {

		List<TaxPeriod> taxPeriods = null;
		if (taxPeriodSearchCriteria.getFromDate() != null || taxPeriodSearchCriteria.getToDate() != null) {
			if (taxPeriodSearchCriteria.getFromDate() != null && taxPeriodSearchCriteria.getToDate() == null)
				throw new InvalidPenaltyDataException(propertiesManager.getInvalidToDate(), requestInfo,
						propertiesManager.getInvalidToDate());
			if (taxPeriodSearchCriteria.getToDate() != null && taxPeriodSearchCriteria.getFromDate() == null)
				throw new InvalidPenaltyDataException(propertiesManager.getInvalidFormDate(), requestInfo,
						propertiesManager.getInvalidToDate());

			Boolean isValidPeriod = TimeStampUtil.compareDates(taxPeriodSearchCriteria.getFromDate(),
					taxPeriodSearchCriteria.getToDate());
			if (!isValidPeriod) {
				throw new InvalidPenaltyDataException(propertiesManager.getInvalidTodateGreaterthanFromDate(),
						requestInfo, propertiesManager.getInvalidTodateGreaterthanFromDate());
			}
		}
		try {
			taxPeriods = taxPeriodRespository.searchTaxPeriod(taxPeriodSearchCriteria.getTenantId(),
					taxPeriodSearchCriteria.getValidDate(), taxPeriodSearchCriteria.getCode(),
					taxPeriodSearchCriteria.getFromDate(), taxPeriodSearchCriteria.getToDate(),
					taxPeriodSearchCriteria.getSortTaxPeriod());
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		TaxPeriodResponse taxPeriodResponse = new TaxPeriodResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		taxPeriodResponse.setResponseInfo(responseInfo);
		taxPeriodResponse.setTaxPeriods(taxPeriods);

		return taxPeriodResponse;
	}

	@Override
	public TransferFeeRatesResponse createTransferFeeRate(TransferFeeRatesRequest transferFeeRatesRequest,
			String tenantId) throws Exception {
		for (TransferFeeRate transferFeeRate : transferFeeRatesRequest.getTransferFeeRates()) {
			Boolean checkOverlapping = checkOverlappingRecord(transferFeeRate.getTenantId(),
					transferFeeRate.getFeeFactor().toString(), transferFeeRate.getFromValue(),
					transferFeeRate.getToValue(), transferFeeRate.getFromDate(), transferFeeRate.getToDate(),
					ConstantUtility.TRANSFERFEERATES_TABLE_NAME, null);
			if (checkOverlapping)
				throw new DuplicateIdException(propertiesManager.getInvalidTenantIdOrFeeFactor(), null,
						transferFeeRatesRequest.getRequestInfo());

			AuditDetails auditDetails = getAuditDetail(transferFeeRatesRequest.getRequestInfo());
			transferFeeRate.setAuditDetails(auditDetails);
			try {
				Long id = transferFeeRateRepository.saveTransferFeeRate(transferFeeRate, tenantId);
				transferFeeRate.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(transferFeeRatesRequest.getRequestInfo());
			}
		}
		TransferFeeRatesResponse transferFeeRatesResponse = new TransferFeeRatesResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(transferFeeRatesRequest.getRequestInfo(), true);
		transferFeeRatesResponse.setResponseInfo(responseInfo);
		transferFeeRatesResponse.setTransferFeeRates(transferFeeRatesRequest.getTransferFeeRates());
		return transferFeeRatesResponse;
	}

	@Override
	public TransferFeeRatesResponse updateTransferFeeRate(TransferFeeRatesRequest transferFeeRatesRequest,
			String tenantId) throws Exception {
		for (TransferFeeRate transferFeeRate : transferFeeRatesRequest.getTransferFeeRates()) {
			Boolean checkOverlapping = checkOverlappingRecord(transferFeeRate.getTenantId(),
					transferFeeRate.getFeeFactor().toString(), transferFeeRate.getFromValue(),
					transferFeeRate.getToValue(), transferFeeRate.getFromDate(), transferFeeRate.getToDate(),
					ConstantUtility.TRANSFERFEERATES_TABLE_NAME, transferFeeRate.getId());
			if (checkOverlapping)
				throw new DuplicateIdException(propertiesManager.getInvalidTenantIdOrFeeFactor(), null,
						transferFeeRatesRequest.getRequestInfo());

			AuditDetails auditDetails = getUpdatedAuditDetails(transferFeeRatesRequest.getRequestInfo(),
					ConstantUtility.TRANSFERFEERATES_TABLE_NAME, transferFeeRate.getId());
			transferFeeRate.setAuditDetails(auditDetails);
			try {
				transferFeeRateRepository.updateTransferFeeRate(transferFeeRate, tenantId);
			} catch (Exception e) {
				throw new InvalidInputException(transferFeeRatesRequest.getRequestInfo());
			}
		}
		TransferFeeRatesResponse transferFeeRatesResponse = new TransferFeeRatesResponse();
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(transferFeeRatesRequest.getRequestInfo(), true);
		transferFeeRatesResponse.setResponseInfo(responseInfo);
		transferFeeRatesResponse.setTransferFeeRates(transferFeeRatesRequest.getTransferFeeRates());
		return transferFeeRatesResponse;
	}

	@Override
	public TransferFeeRatesResponse getTransferFeeRate(RequestInfo requestInfo,
			TransferFeeRateSearchCriteria transferFeeRateSearchCriteria) throws Exception {
		TransferFeeRatesResponse transferFeeRatesResponse = new TransferFeeRatesResponse();
		try {
			List<TransferFeeRate> transferFeeRates = transferFeeRateRepository.getTransferFeeRates(
					transferFeeRateSearchCriteria.getTenantId(), transferFeeRateSearchCriteria.getFeeFactor(),
					transferFeeRateSearchCriteria.getValidDate(), transferFeeRateSearchCriteria.getValidValue());
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			transferFeeRatesResponse.setTransferFeeRates(transferFeeRates);
			transferFeeRatesResponse.setResponseInfo(responseInfo);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		return transferFeeRatesResponse;
	}

	public Boolean checkOverlappingRecord(String tenantId, String feeFactor, Double fromValue, Double toValue,
			String fromDate, String toDate, String tableName, Long id) {
		return transferFeeRateRepository.checkWhetherSlabRecordExits(tenantId, feeFactor, fromValue, toValue, fromDate,
				toDate, tableName, id);
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

	private AuditDetails getUpdatedAuditDetails(RequestInfo requestInfo, String tableName, Long id) {

		String userId = requestInfo.getUserInfo().getId().toString();
		Long currEpochDate = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setLastModifiedBy(userId);
		auditDetails.setLastModifiedTime(currEpochDate);

		factorRepository.getCreatedAuditDetails(auditDetails, tableName, id);
		return auditDetails;
	}

}
