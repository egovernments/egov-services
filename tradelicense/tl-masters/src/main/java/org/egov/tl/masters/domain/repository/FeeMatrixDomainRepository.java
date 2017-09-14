package org.egov.tl.masters.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.CategoryDetail;
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
import org.egov.tradelicense.persistence.repository.CategoryRepository;
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
	CategoryRepository categoryRepository;
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
	public void add(FeeMatrix feeMatrix) {
		FeeMatrixEntity feeMatrixEntity = new FeeMatrixEntity();
		feeMatrixJdbcRepository.create(feeMatrixEntity.toEntity(feeMatrix));
		for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {
			feeMatrixDetailDomainRepository.add(new FeeMatrixDetailEntity().toEntity(feeMatrixDetail));
		}
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
	public boolean validateCategory(Long id, Long parentId, String tenantId) {

		return categoryJdbcRepository.validateIdExistance(id, parentId, tenantId);
	}

	public boolean validateInputId(Long id, String tenantId) {
		return feeMatrixJdbcRepository.validateFeeMatrixByIdAndTenantID(id, tenantId);
	}

	public boolean checkUniquenessOfFeeMatrix(String tenantId, ApplicationTypeEnum applicationTypeEnum,
			FeeTypeEnum feeTypeEnum, BusinessNatureEnum businessNatureEnum, Long categoryId, Long subCategoryId,
			String financialYear) {

		return feeMatrixJdbcRepository.checkWhetherFeeMatrixExistsWithGivenFieds(tenantId, applicationTypeEnum,
				feeTypeEnum, businessNatureEnum, categoryId, subCategoryId, financialYear);

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

	public void update(FeeMatrix previousFeeMatrix) {
		feeMatrixJdbcRepository.update(new FeeMatrixEntity().toEntity(previousFeeMatrix));

	}

	public List<FeeMatrixSearch> search(FeeMatrixSearchCriteria feeMatrixSearchCriteria) {
		return null;
	}

	public List<FeeMatrixDetail> getFeeMatrixDetailsByFeeMatrixId(Long feeMatrixId) {
		return feeMatrixDetailDomainRepository.getFeeMatrixDetailsByFeeMatrixId(feeMatrixId);
	}

	public CategoryDetail getSubCategoryDetail(Long subCategoryId) {
		return null;
	}
}
