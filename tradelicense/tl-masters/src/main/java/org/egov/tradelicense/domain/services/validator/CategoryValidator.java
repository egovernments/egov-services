package org.egov.tradelicense.domain.services.validator;

import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.Category;
import org.egov.models.CategoryDetail;
import org.egov.models.CategoryRequest;
import org.egov.models.RequestInfo;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.persistence.repository.builder.UtilityBuilder;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;

	public void validateCategoryRequest(CategoryRequest categoryRequest, Boolean isNewCategory) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();
		
		for (Category category : categoryRequest.getCategories()) {

			Long ParentId = category.getParentId();
			Long categoryId = null;
			
			if(isNewCategory){
				AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
				category.setAuditDetails(auditDetails);
			} else {
				AuditDetails auditDetails = category.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				category.setAuditDetails(auditDetails);
				categoryId = category.getId();
				if (categoryId == null) {
					throw new InvalidInputException(requestInfo);
				}
			}
			
			// checking for existence of duplicate record
			Boolean isDuplicateRecordExists = utilityHelper.checkWhetherDuplicateRecordExits(category.getTenantId(),
					category.getCode(), ConstantUtility.CATEGORY_TABLE_NAME, categoryId);

			if (isDuplicateRecordExists) {
				throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
			}

			if (ParentId != null) {
				validateSubCategory(category, requestInfo, isNewCategory);
			}
			
			
		}
	}

	public void validateSubCategory(Category category, RequestInfo requestInfo, Boolean isNewCategory) {

		Boolean isParentExists = checkWhetherParentRecordExits(category.getParentId(),
				ConstantUtility.CATEGORY_TABLE_NAME);

		if (isParentExists) {
			
			for (CategoryDetail categoryDetail : category.getDetails()) {

				Long categoryDetailId = null;
				Boolean isCategoryDetailDuplicateExists = null;
				
				if(isNewCategory){
					
					isCategoryDetailDuplicateExists = false;
				} else {
					
					categoryDetailId = categoryDetail.getId();
					isCategoryDetailDuplicateExists = checkWhetherDuplicateCategoryDetailRecordExits(categoryDetail,
							ConstantUtility.CATEGORY_DETAIL_TABLE_NAME, categoryDetailId);
				}

				if (isCategoryDetailDuplicateExists) {
					throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
				}

				Boolean isUomExists = checkWhetherUomExists(categoryDetail);

				if (!isUomExists) {

					throw new InvalidInputException(requestInfo);
				}
			}

		} else {
			throw new InvalidInputException(requestInfo);
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * This will check whether any record exists with the given parentId in
	 * database or not
	 * 
	 * @param tenantId
	 * @param parentId
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherParentRecordExits(Long parentId, String tableName) {

		Boolean isExists = Boolean.TRUE;
		String query = UtilityBuilder.getCategoryParentValidationQuery(tableName, parentId);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	/**
	 * This will check whether any record exists with the same categoryDetails
	 * 
	 * @param CategoryDetail
	 * @param tableName
	 * @param id
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherDuplicateCategoryDetailRecordExits(CategoryDetail categoryDetail, String tableName,
			Long id) {

		Boolean isExists = Boolean.TRUE;
		Long categoryId = categoryDetail.getCategoryId();
		String feeType = categoryDetail.getFeeType().toString();
		String rateType = categoryDetail.getRateType().toString();
		String query = UtilityBuilder.getCategoryDetailValidationQuery(tableName, categoryId, feeType, rateType, id);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	/**
	 * This will check whether any uom record exists or not
	 * 
	 * @param CategoryDetail
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherUomExists(CategoryDetail categoryDetail) {

		Boolean isExists = Boolean.FALSE;
		String tableName = ConstantUtility.UOM_TABLE_NAME;
		Long uomId = categoryDetail.getUomId();
		String query = UtilityBuilder.getUomValidationQuery(tableName, uomId);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count > 0) {
			isExists = Boolean.TRUE;
		}

		return isExists;
	}
}