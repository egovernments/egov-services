package org.egov.tl.masters.domain.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.tl.commons.web.contract.FeeMatrixContract;
import org.egov.tl.commons.web.contract.FeeMatrixDetailContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.masters.contract.repository.FinancialRepository;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixDetail;
import org.egov.tl.masters.domain.model.FeeMatrixSearch;
import org.egov.tl.masters.domain.model.FeeMatrixSearchCriteria;
import org.egov.tl.masters.domain.repository.FeeMatrixDetailDomainRepository;
import org.egov.tl.masters.domain.repository.FeeMatrixDomainRepository;
import org.egov.tl.masters.persistence.entity.FeeMatrixDetailEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixEntity;
import org.egov.tl.masters.persistence.queue.FeeMatrixQueueRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.domain.model.FinancialYearContract;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FeeMatrixService implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class FeeMatrixService {

	@Autowired
	FeeMatrixDomainRepository feeMatrixDomainRepository;

	@Autowired
	FeeMatrixDetailDomainRepository feeMatrixDetailDomainRepository;

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	FinancialRepository financialRepository;

	@Autowired
	FeeMatrixQueueRepository feeMatrixQueueRepository;

	@Transactional
	public List<FeeMatrix> createFeeMatrixMaster(List<FeeMatrix> feeMatrices, RequestInfo requestInfo) {

		validateFeeMatrixRequest(feeMatrices, requestInfo);
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		for (FeeMatrix feeMatrix : feeMatrices) {

			Long id = feeMatrixDomainRepository.getFeeMatrixNextSequence();
			feeMatrix.setId(id);
			feeMatrix = populateEffectiveFromAndToDates(feeMatrix, requestInfoWrapper);
			for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {
				Long feematrixDetalsId = feeMatrixDetailDomainRepository.getFeeDetailMatrixNextSequence();
				feeMatrixDetail.setId(feematrixDetalsId);
				feeMatrixDetail.setFeeMatrixId(id);
			}
		}

		FeeMatrixRequest feeMatrixRequest = buildFeeMatixRequest(feeMatrices, requestInfo);
		addToQue(feeMatrixRequest, propertiesManager.getFeeMatrixCreateValidated());
		return feeMatrices;
	}

	public void validateFeeMatrixRequest(List<FeeMatrix> feeMatrixes, RequestInfo requestInfo) {

		for (FeeMatrix feeMatrix : feeMatrixes) {
			// validating the categoryId
			validateCategory(feeMatrix.getCategory(), null, feeMatrix.getTenantId(), requestInfo);
			// validating the subCategoryId
			validateCategory(feeMatrix.getSubCategory(), feeMatrix.getCategory(), feeMatrix.getTenantId(), requestInfo);
			
			String  financialYearId = validateFinancialYearFinRange( feeMatrix.getTenantId(), feeMatrix.getFinancialYear(), requestInfo);
			feeMatrix.setFinancialYear(financialYearId);
			boolean isExists = validateUniqueness(feeMatrix.getTenantId(), feeMatrix.getApplicationType(),
					feeMatrix.getFeeType(), feeMatrix.getBusinessNature(), feeMatrix.getCategory(),
					feeMatrix.getSubCategory(), financialYearId, requestInfo);

			if (isExists) {
				throw new InvalidInputException(propertiesManager.getUniquenessErrorMsg(), requestInfo);
			}

			validateFeeMatrixDetails(feeMatrix, requestInfo, Boolean.TRUE);
		}
	}

	private String validateFinancialYearFinRange (String tenantId, String financialYearFinRange, RequestInfo requestInfo){
		String financialYear = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		if( financialYearFinRange != null ){
			FinancialYearContract financialYearContract = financialRepository.findFinancialYearByFinRange(tenantId,
					financialYearFinRange, requestInfoWrapper);
			if (financialYearContract != null) {
				financialYear = financialYearContract.getId().toString();
			}else{
				throw new InvalidInputException(propertiesManager.getInvalidFinancialYearMsg(), requestInfo);
			}
		}
		
		return financialYear;
	}
	@Transactional
	public List<FeeMatrix> updateFeeMatrixMaster(List<FeeMatrix> feeMatrices, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		for (FeeMatrix feeMatrix : feeMatrices) {

			if (feeMatrix.getId() != null) {
				validateIdOfFeeMatrices(feeMatrix.getId(), feeMatrix.getTenantId(), requestInfo);
			} else {
				throw new InvalidInputException(propertiesManager.getInvalidIdMsg(), requestInfo);
			}

			String  financialYearId = validateFinancialYearFinRange( feeMatrix.getTenantId(), feeMatrix.getFinancialYear(), requestInfo);
			feeMatrix.setFinancialYear(financialYearId);
			
			List<FeeMatrixDetail> updatedFeeMatrices = validateFeeMatrixDetails(feeMatrix, requestInfo, Boolean.FALSE);
			feeMatrix.setFeeMatrixDetails(updatedFeeMatrices);
		}

		FeeMatrixRequest feeMatrixRequest = buildFeeMatixRequest(feeMatrices, requestInfo);
		addToQue(feeMatrixRequest, propertiesManager.getFeeMatrixUpdateValidated());
		return feeMatrices;
	}

	private boolean validateUniqueness(String tenantId, ApplicationTypeEnum applicationTypeEnum,
			FeeTypeEnum feeTypeEnum, BusinessNatureEnum businessNatureEnum, String category, String subCategory,
			String financialYear, RequestInfo requestInfo) {
		
		return feeMatrixDomainRepository.checkUniquenessOfFeeMatrix(tenantId, applicationTypeEnum, feeTypeEnum,
				businessNatureEnum, category, subCategory, financialYear);
	}

	public List<FeeMatrixDetail> validateFeeMatrixDetails(FeeMatrix feeMatrix, RequestInfo requestInfo,
			boolean validateNew) {

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		List<FeeMatrixDetail> newFeeMatrixDetails = new ArrayList<>();
		List<FeeMatrixDetail> feeMatrixDetailsDB = new ArrayList<>();

		if (validateNew) {

			feeMatrixDetails = feeMatrix.getFeeMatrixDetails();
		} else {

			validateFeeMatrixEquality(feeMatrix, requestInfo);
			feeMatrixDetailsDB = feeMatrixDetailDomainRepository.getFeeMatrixDetailsByFeeMatrixId(feeMatrix.getId());
			List<FeeMatrixDetail> tempFeeMatrixDetail = new ArrayList<FeeMatrixDetail>();
			tempFeeMatrixDetail.addAll(feeMatrixDetailsDB);
			feeMatrixDetails = validateUpdateFeeMatrixDetails(feeMatrix, requestInfo, tempFeeMatrixDetail);
			for (FeeMatrixDetail updatedDetail : feeMatrixDetails) {
				// Add to list if new request with no id is passed.
				int isNew = feeMatrixDetailsDB.stream().filter(details -> details.getId()!=null && details.getId().equals(updatedDetail.getId()))
						.collect(Collectors.toList()).size();
				if (isNew == 0) {
					newFeeMatrixDetails.add(updatedDetail);
				}
			}
		}

		if (feeMatrixDetails.size() > 1) {
			feeMatrixDetails.sort((r1, r2) -> r1.getUomFrom().compareTo(r2.getUomFrom()));
		}

		Long uomFrom = null;
		Long oldUomTo = null;
		Long uomTo = null;
		int count = 0;

		for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails) {

			uomFrom = feeMatrixDetail.getUomFrom();
			uomTo = feeMatrixDetail.getUomTo();
			if (count == 0) {
				if (uomFrom != 0) {
					throw new InvalidRangeException(propertiesManager.getInvalidSequenceRangeMsg(), requestInfo);
				}
			}
			if (uomTo != null && uomFrom >= uomTo) {
				throw new InvalidRangeException(propertiesManager.getInvalidSequenceRangeMsg(), requestInfo);
			} else if (uomTo == null && count != (feeMatrixDetails.size() - 1)) {
				throw new InvalidRangeException(propertiesManager.getInvalidSequenceRangeMsg(), requestInfo);
			}
			feeMatrixDetail.setAuditDetails(feeMatrix.getAuditDetails());
			if (count > 0) {
				if (!uomFrom.equals(oldUomTo)) {
					throw new InvalidRangeException(propertiesManager.getInvalidSequenceRangeMsg(), requestInfo);
				}
			}
			oldUomTo = feeMatrixDetail.getUomTo();
			count++;
		}

		if (newFeeMatrixDetails != null && newFeeMatrixDetails.size() > 0) {
			for (FeeMatrixDetail feeMatrixDetail : newFeeMatrixDetails) {
				feeMatrixDetailDomainRepository.add(new FeeMatrixDetailEntity().toEntity(feeMatrixDetail));
			}
		}

		// Deleting if we send less number of objects than DB objects
		if (!validateNew) {
			for (FeeMatrixDetail detail : feeMatrixDetailsDB) {
				int idCount = feeMatrixDetails.stream().filter(details -> details.getId() !=null &&  details.getId().equals(detail.getId()))
						.collect(Collectors.toList()).size();
				if (idCount == 0) {
					Map<String, Object> message = new HashMap<>();
					ModelMapper mapper = new ModelMapper();
					FeeMatrixDetailContract domain = mapper.map(detail, FeeMatrixDetailContract.class);
					message.put(propertiesManager.getDeleteFeeMatrixDetailsKey(), domain);
					feeMatrixQueueRepository.add(message);
				}
			}
		}
		return feeMatrixDetails;
	}

	private void validateFeeMatrixEquality(FeeMatrix feeMatrix, RequestInfo requestInfo) {

		FeeMatrix feeMatrixDbObject = feeMatrixDomainRepository.getFeeMatrixById(feeMatrix.getId(),
				feeMatrix.getTenantId());
		if (!feeMatrix.equals(feeMatrixDbObject)) {
			throw new InvalidInputException(propertiesManager.getInvalidFeeMatrixMsg(), requestInfo);
		}
	}

	private List<FeeMatrixDetail> validateUpdateFeeMatrixDetails(FeeMatrix feeMatrix, RequestInfo requestInfo,
			List<FeeMatrixDetail> feeMatrixDetails) {

		List<FeeMatrixDetail> newFeeMatrixDetails = new ArrayList<FeeMatrixDetail>();

		for (FeeMatrixDetail feeMatrixRequestDetail : feeMatrix.getFeeMatrixDetails()) {
			Long id = feeMatrixRequestDetail.getId();

			if (id != null) {

				Boolean foundRecord = Boolean.FALSE;
				for (int i = 0; i < feeMatrixDetails.size(); i++) {

					if (id.equals(feeMatrixDetails.get(i).getId())) {
						FeeMatrixDetail newFeeMatrixDetail = feeMatrix.getFeeMatrixDetails().stream()
								.filter(details -> details.getId() !=null && details.getId().equals(id)).collect(Collectors.toList()).get(0);
						feeMatrixDetails.set(i, newFeeMatrixDetail);
						newFeeMatrixDetails.add(feeMatrixDetails.get(i));
						foundRecord = true;
						break;
					}
				}
				// Id is not existed in DB details.
				if (!foundRecord) {
					throw new InvalidInputException(propertiesManager.getFeeMatrixDetailsIdNotFoundMsg(), requestInfo);
				}
			} else {
				// add new details record to DB.
				Long feematrixDetalsId = feeMatrixDetailDomainRepository.getFeeDetailMatrixNextSequence();
				feeMatrixRequestDetail.setId(feematrixDetalsId);
				feeMatrixRequestDetail.setFeeMatrixId(feeMatrix.getId());
				newFeeMatrixDetails.add(feeMatrixRequestDetail);
			}
		}

		return newFeeMatrixDetails;
	}

	/**
	 * Checks whether record with given id is exists or not
	 * 
	 * @param id
	 * @param requestInfo
	 */
	private void validateIdOfFeeMatrices(Long id, String tenantId, RequestInfo requestInfo) {

		boolean isExists = feeMatrixDomainRepository.validateInputId(id, tenantId);
		if (!isExists) {
			throw new InvalidInputException(propertiesManager.getInvalidIdAndTenantIdMsg(), requestInfo);
		}
	}

	private void validateCategory(String code, String parent, String tenatId, RequestInfo requestInfo) {

		boolean isExists = feeMatrixDomainRepository.validateCategory(code, parent, tenatId);
		if (!isExists) {
			throw new InvalidInputException(propertiesManager.getCategoryIdValidationMsg(), requestInfo);
		}
	}

	/**
	 * 
	 * @param financialYearId
	 * 
	 *            This will validate the FinancialYear existance, make
	 *            restTemplate call and check the Existance of financial year
	 */
	private FinancialYearContract validateFinancialYear(Long financialYearId, String tenantId,
			RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		return financialRepository.findFinancialYearById(tenantId, financialYearId.toString(), requestInfoWrapper);

	}

	public FeeMatrix save(FeeMatrix feeMatrix) {
		return feeMatrixDomainRepository.add(feeMatrix);
	}

	public FeeMatrix update(FeeMatrix feeMatrix) {
		return feeMatrixDomainRepository.update(feeMatrix);
	}

	public void delete(FeeMatrixDetail feeMatrixDetail) {
		feeMatrixDetailDomainRepository.deleteFeeMatrixDetail(feeMatrixDetail);
	}

	public void addToQue(FeeMatrixRequest request, String key) {
		Map<String, Object> message = new HashMap<>();
		message.put(key, request);
		feeMatrixQueueRepository.add(message);
	}

	private FeeMatrix populateEffectiveFromAndToDates(FeeMatrix feeMatrix, RequestInfoWrapper requestInfoWrapper) {


		FinancialYearContract financialYearContract = validateFinancialYear(Long.valueOf(feeMatrix.getFinancialYear()),
				feeMatrix.getTenantId(), requestInfoWrapper.getRequestInfo());

		if (financialYearContract != null && financialYearContract.getStartingDate() != null) {
			feeMatrix.setEffectiveFrom(financialYearContract.getStartingDate().getTime());
			FeeMatrixEntity feeMatrixEntity = new FeeMatrixEntity().toEntity(feeMatrix);
			FeeMatrixSearchCriteria feeMatrixSeach = buildSearchCriteria(feeMatrixEntity);
			FeeMatrix nextFeeMatrix = feeMatrixDomainRepository.getFeeMatrixForNextFinancialYear(feeMatrixSeach);
			FeeMatrix previousFeeMatrix = feeMatrixDomainRepository
					.getFeeMatrixForPreviousFinancialYear(feeMatrixSeach);
			if (nextFeeMatrix == null) {
				feeMatrix.setEffectiveTo(null);
			} else {
				Date effectiveTo = new Date(nextFeeMatrix.getEffectiveFrom() - (1000 * 60 * 60 * 24));
				feeMatrix.setEffectiveTo(effectiveTo.getTime());
			}
			if (previousFeeMatrix != null) {
				Date effectiveTo = new Date(feeMatrix.getEffectiveFrom() - (1000 * 60 * 60 * 24));
				previousFeeMatrix.setEffectiveTo(effectiveTo.getTime());
				previousFeeMatrix.getAuditDetails().setLastModifiedTime(new Date().getTime());
				String modifiedBy = requestInfoWrapper.getRequestInfo().getUserInfo().getId().toString();
				previousFeeMatrix.getAuditDetails().setLastModifiedBy(modifiedBy);
				feeMatrixDomainRepository.update(previousFeeMatrix);
			}
		}
		return feeMatrix;

	}

	private FeeMatrixRequest buildFeeMatixRequest(List<FeeMatrix> feeMatrices, RequestInfo requestInfo) {
		ModelMapper modelMapper = new ModelMapper();
		Type targetListType = new TypeToken<List<FeeMatrixContract>>() {
		}.getType();
		List<FeeMatrixContract> feeMatricesContact = modelMapper.map(feeMatrices, targetListType);
		FeeMatrixRequest feeMatrixContract = new FeeMatrixRequest();
		feeMatrixContract.setFeeMatrices(feeMatricesContact);
		feeMatrixContract.setRequestInfo(requestInfo);
		return feeMatrixContract;
	}

	public FeeMatrixSearchCriteria buildSearchCriteria(FeeMatrixEntity feeMatrixEntity) {
		FeeMatrixSearchCriteria feeMatrixSearchCriteria = new FeeMatrixSearchCriteria();
		String applcationType = feeMatrixEntity.getApplicationType() == null ? null
				: feeMatrixEntity.getApplicationType().toString();
		feeMatrixSearchCriteria.setApplicationType(applcationType);
		String businessNature = feeMatrixEntity.getBusinessNature() == null ? null
				: feeMatrixEntity.getBusinessNature().toString();
		feeMatrixSearchCriteria.setBusinessNature(businessNature);
		feeMatrixSearchCriteria.setTenantId(feeMatrixEntity.getTenantId());
		feeMatrixSearchCriteria.setCategory(feeMatrixEntity.getCategory());
		feeMatrixSearchCriteria.setSubCategory(feeMatrixEntity.getSubCategory());
		feeMatrixSearchCriteria.setEffectiveFrom(feeMatrixEntity.getEffectiveFrom().getTime());
		String feeType = feeMatrixEntity.getFeeType() == null ? null : feeMatrixEntity.getFeeType();
		feeMatrixSearchCriteria.setFeeType(feeType);
		return feeMatrixSearchCriteria;
	}

	public List<FeeMatrixSearch> search(FeeMatrixSearchCriteria feeMatrixSearchCriteria, RequestInfo requestInfo) {
		String financialYearId = validateFinancialYearFinRange( feeMatrixSearchCriteria.getTenantId(), feeMatrixSearchCriteria.getFinancialYear(),requestInfo);
		feeMatrixSearchCriteria.setFinancialYear(financialYearId);
		return feeMatrixDomainRepository.search(feeMatrixSearchCriteria, requestInfo);

	}
}