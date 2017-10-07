package org.egov.tl.masters.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.masters.contract.repository.FinancialRepository;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixDetail;
import org.egov.tl.masters.domain.model.FeeMatrixSearch;
import org.egov.tl.masters.domain.model.FeeMatrixSearchCriteria;
import org.egov.tl.masters.domain.model.Pagination;
import org.egov.tl.masters.persistence.entity.FeeMatrixDetailEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixSearchEntity;
import org.egov.tl.masters.persistence.repository.CategoryJdbcRepository;
import org.egov.tl.masters.persistence.repository.FeeMatrixJdbcRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.model.FinancialYearContract;
import org.egov.tradelicense.domain.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeeMatrixDomainRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	FeeMatrixJdbcRepository feeMatrixJdbcRepository;

	@Autowired
	CategoryJdbcRepository categoryJdbcRepository;

	@Autowired
	FeeMatrixDetailDomainRepository feeMatrixDetailDomainRepository;

	@Autowired
	CategoryService categoryService;

	@Autowired
	FinancialRepository financialRepository;
	/*
	 * @Autowired FinancialYearContractRepository financilaYearWebRepo;
	 */

	/**
	 * 
	 * @param FeeMatrix
	 * @return void
	 * 
	 *         this API will call add API of method of FeeMatrixQueueRepository
	 *         to add the FeeMatrixV2Request as message with topic name
	 * 
	 *         this method will be called from feeMatrixService add method
	 */
	@Transactional
	public FeeMatrix add(FeeMatrix feeMatrix) {
		FeeMatrixEntity feeMatrixEntity = new FeeMatrixEntity();
		feeMatrixEntity = feeMatrixJdbcRepository.create(feeMatrixEntity.toEntity(feeMatrix));
		for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {
			feeMatrixDetailDomainRepository.add(new FeeMatrixDetailEntity().toEntity(feeMatrixDetail));
		}

		return feeMatrixEntity.toDomain();
	}

	/**
	 * 
	 * @param categoryId
	 * @return String CategoryName
	 * 
	 *         this will call the FeeMatrixJdbcRepository getCategory
	 */
	public String getCategoryName(Long categoryId) {

		return null;
	}

	/**
	 * 
	 * @param id
	 * @return boolean
	 * 
	 *         this API will call validateCategory method of jdbc repository
	 *         class and returns whatever it gets from that call
	 */
	public boolean validateCategory(String code, String parent, String tenantId) {

		return categoryJdbcRepository.validateCodeExistance(code, parent, tenantId);
	}

	public boolean validateInputId(Long id, String tenantId) {
		return feeMatrixJdbcRepository.validateFeeMatrixByIdAndTenantID(id, tenantId);
	}

	public boolean checkUniquenessOfFeeMatrix(String tenantId, ApplicationTypeEnum applicationTypeEnum,
			FeeTypeEnum feeTypeEnum, BusinessNatureEnum businessNatureEnum, String category, String subCategory,
			String financialYear) {
		FeeMatrixSearchEntity feeMatrixSearchEntity = new FeeMatrixSearchEntity();
		feeMatrixSearchEntity.setTenantId(tenantId);
		feeMatrixSearchEntity.setApplicationType(applicationTypeEnum == null ? null : applicationTypeEnum.toString());
		feeMatrixSearchEntity.setBusinessNature(businessNatureEnum == null ? null : businessNatureEnum.toString());
		feeMatrixSearchEntity.setFeeType(feeTypeEnum.toString());
		feeMatrixSearchEntity.setCategory(category);
		feeMatrixSearchEntity.setSubCategory(subCategory);
		
		feeMatrixSearchEntity.setFinancialYear(financialYear);
		return feeMatrixJdbcRepository.checkWhetherFeeMatrixExistsWithGivenFieds(feeMatrixSearchEntity);
	}

	public boolean validateFeeMatrixDetails(Long uomFrom, Long uomTo) {

		return feeMatrixJdbcRepository.checkWhetherFeeMatrixExistsWithGivenUom(uomFrom, uomTo);
	}

	public Long getFeeMatrixNextSequence() {

		String id = feeMatrixJdbcRepository.getSequence(FeeMatrixEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public FeeMatrix getFeeMatrixForNextFinancialYear(FeeMatrixSearchCriteria domain) {

		ModelMapper modelMapper = new ModelMapper();
		FeeMatrixSearchEntity search = modelMapper.map(domain, FeeMatrixSearchEntity.class);
		Pagination<FeeMatrixEntity> feeMatrixEntities = feeMatrixJdbcRepository
				.getFeeMatrixForNextFinancialYear(search);

		List<FeeMatrix> feeMatrices = new ArrayList<>();
		for (FeeMatrixEntity feeMatrixEntity : feeMatrixEntities.getPagedData()) {

			feeMatrices.add(feeMatrixEntity.toDomain());
		}

		if (feeMatrices.size() > 0)
			return feeMatrices.get(0);
		else
			return null;
	}

	public FeeMatrix getFeeMatrixForPreviousFinancialYear(FeeMatrixSearchCriteria domain) {
		ModelMapper modelMapper = new ModelMapper();
		FeeMatrixSearchEntity search = modelMapper.map(domain, FeeMatrixSearchEntity.class);
		Pagination<FeeMatrixEntity> feeMatrixEntities = feeMatrixJdbcRepository
				.getFeeMatrixForPreviousFinancialYear(search);

		List<FeeMatrix> feeMatrices = new ArrayList<>();
		for (FeeMatrixEntity feeMatrixEntity : feeMatrixEntities.getPagedData()) {

			feeMatrices.add(feeMatrixEntity.toDomain());
		}

		if (feeMatrices.size() > 0)
			return feeMatrices.get(0);
		else
			return null;
	}

	public FeeMatrix update(FeeMatrix previousFeeMatrix) {
		
		FeeMatrixEntity feeMatrixEntity = new FeeMatrixEntity();
		feeMatrixEntity = feeMatrixJdbcRepository.update(feeMatrixEntity.toEntity(previousFeeMatrix));
		
		if(previousFeeMatrix.getFeeMatrixDetails() != null && previousFeeMatrix.getFeeMatrixDetails().size() > 0){
			
			for (FeeMatrixDetail feeMatrixDetail : previousFeeMatrix.getFeeMatrixDetails()) {
				
				feeMatrixDetailDomainRepository.update(new FeeMatrixDetailEntity().toEntity(feeMatrixDetail));
			
			}
		}
		
		return feeMatrixEntity.toDomain();
	}

	public List<FeeMatrixSearch> search(FeeMatrixSearchCriteria feeMatrixSearchCriteria, RequestInfo requestInfo) {

		ModelMapper modelMapper = new ModelMapper();
		FeeMatrixSearchEntity feeMatrixSearchEntity = modelMapper.map(feeMatrixSearchCriteria,
				FeeMatrixSearchEntity.class);
		FeeMatrixSearchEntity fallBackSearchEntity = modelMapper.map(feeMatrixSearchCriteria,
				FeeMatrixSearchEntity.class);
		fallBackSearchEntity.setFallBack(false);
		List<FeeMatrixEntity> feeMatrixEntity = feeMatrixJdbcRepository.search(fallBackSearchEntity);

		if (feeMatrixSearchEntity.getFallBack() != null && feeMatrixSearchEntity.getFallBack() == Boolean.TRUE) {

			if (feeMatrixEntity == null || feeMatrixEntity.size() == 0) {

				fallBackSearchEntity = modelMapper.map(feeMatrixSearchCriteria, FeeMatrixSearchEntity.class);
				if (feeMatrixSearchEntity.getBusinessNature() != null
						&& !feeMatrixSearchEntity.getBusinessNature().isEmpty()) {

					fallBackSearchEntity.setBusinessNature(null);
					feeMatrixEntity = feeMatrixJdbcRepository.search(fallBackSearchEntity);

				}
			}

			if (feeMatrixEntity == null || feeMatrixEntity.size() == 0) {

				fallBackSearchEntity = modelMapper.map(feeMatrixSearchCriteria, FeeMatrixSearchEntity.class);
				if (feeMatrixSearchEntity.getApplicationType() != null
						&& !feeMatrixSearchEntity.getApplicationType().isEmpty()) {

					fallBackSearchEntity.setApplicationType(null);
					feeMatrixEntity = feeMatrixJdbcRepository.search(fallBackSearchEntity);

				}
			}

			if (feeMatrixEntity == null || feeMatrixEntity.size() == 0) {

				fallBackSearchEntity = modelMapper.map(feeMatrixSearchCriteria, FeeMatrixSearchEntity.class);
				if (feeMatrixSearchEntity.getApplicationType() != null
						&& !feeMatrixSearchEntity.getApplicationType().isEmpty()
						&& feeMatrixSearchEntity.getBusinessNature() != null
						&& !feeMatrixSearchEntity.getBusinessNature().isEmpty()) {

					fallBackSearchEntity.setApplicationType(null);
					fallBackSearchEntity.setBusinessNature(null);
					feeMatrixEntity = feeMatrixJdbcRepository.search(fallBackSearchEntity);

				}
			}

		}

		List<FeeMatrixSearch> feeMatrixSearchList = new ArrayList<FeeMatrixSearch>();
		Map<String, FinancialYearContract> finicialYearMap = new HashMap<String, FinancialYearContract>();
		Map<String, CategorySearchResponse> categoryDetailsMap = new HashMap<String, CategorySearchResponse>();
		System.out.println("");
		for (FeeMatrixEntity feeMatrix : feeMatrixEntity) {
			System.out.println("fee matrix id:" + feeMatrix.getId());
			FeeMatrixSearch feeMatrixSearch = new FeeMatrixEntity().toSearchDomain(feeMatrix);
			List<FeeMatrixDetail> feeMatrixDetails = getFeeMatrixDetailsByFeeMatrixId(feeMatrixSearch.getId());
			feeMatrixSearch.setFeeMatrixDetails(feeMatrixDetails);
			CategorySearchResponse categoryResponse = null;
			if (categoryDetailsMap.get(feeMatrix.getSubCategory()) == null) {
				categoryResponse = getSubCategoryDetail(feeMatrix.getSubCategory(), feeMatrix.getTenantId(),
						feeMatrix.getFeeType(), requestInfo);
				categoryDetailsMap.put(feeMatrix.getSubCategory(), categoryResponse);
			} else {
				categoryResponse = categoryDetailsMap.get(feeMatrix.getSubCategory());
			}

			if (categoryResponse != null && categoryResponse.getCategories() != null
					&& categoryResponse.getCategories().size() > 0
					&& categoryResponse.getCategories().get(0).getDetails() != null
					&& categoryResponse.getCategories().get(0).getDetails().size() > 0) {

				System.out.println("fee matrix Subcategory Response" + categoryResponse.toString());
				CategoryDetailSearch categoryDetail = categoryResponse.getCategories().get(0).getDetails().get(0);
				//TODO get the category detail of feeType in feeMatrix
				feeMatrixSearch.setUom(categoryDetail.getUom());
				String rateType = categoryDetail.getRateType() == null ? null : categoryDetail.getRateType().toString();
				feeMatrixSearch.setRateType(rateType);
				feeMatrixSearch.setCategoryName(categoryResponse.getCategories().get(0).getParentName());
				feeMatrixSearch.setSubCategoryName(categoryResponse.getCategories().get(0).getName());
				feeMatrixSearch.setUomName(categoryDetail.getUomName());

			}

			FinancialYearContract financialYearContract = null;
			if (finicialYearMap.get(feeMatrix.getFinancialYear()) == null) {

				RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
				requestInfoWrapper.setRequestInfo(requestInfo);

				financialYearContract = financialRepository.findFinancialYearById(feeMatrix.getTenantId(),
						feeMatrix.getFinancialYear(), requestInfoWrapper);
				if (financialYearContract != null) {
					
					finicialYearMap.put(feeMatrix.getFinancialYear(), financialYearContract);
					feeMatrixSearch.setFinancialYear(financialYearContract.getFinYearRange());
					feeMatrixSearch.setFinancialYearId(financialYearContract.getId().toString());
				}

			} else {

				financialYearContract = finicialYearMap.get(feeMatrix.getFinancialYear());
				feeMatrixSearch.setFinancialYear(financialYearContract.getFinYearRange());
				feeMatrixSearch.setFinancialYearId(financialYearContract.getId().toString());

			}
			System.out.println("feematrix search: " + feeMatrixSearch.toString());
			feeMatrixSearchList.add(feeMatrixSearch);
		}
		return feeMatrixSearchList;
	}

	public List<FeeMatrixDetail> getFeeMatrixDetailsByFeeMatrixId(Long feeMatrixId) {
		return feeMatrixDetailDomainRepository.getFeeMatrixDetailsByFeeMatrixId(feeMatrixId);
	}

	public CategorySearchResponse getSubCategoryDetail(String subCategory, String tenantId, String feeType,
			RequestInfo requestInfo) {
		
		return categoryService.getCategoryMaster(requestInfo, tenantId, null, new String[]{subCategory}, null, null, "SUBCATEGORY", null, null, null,
				feeType, null, null, null);
	}

	public FeeMatrix getFeeMatrixById(Long id, String tenantId) {
		FeeMatrixSearchEntity entity = new FeeMatrixSearchEntity();
		Integer[] ids = new Integer[1];
		ids[0] = id.intValue();

		entity.setIds(ids);
		entity.setTenantId(tenantId);
		List<FeeMatrixEntity> feematrixEntity = feeMatrixJdbcRepository.search(entity);
		if (feematrixEntity != null && feematrixEntity.size() > 0) {
			return feematrixEntity.get(0).toDomain();
		} else {
			return null;
		}
	}

}
