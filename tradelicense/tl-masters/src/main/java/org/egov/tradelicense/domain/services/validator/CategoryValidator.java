package org.egov.tradelicense.domain.services.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategoryDetail;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.CategoryRequest;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.DuplicateNameException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.persistence.repository.builder.UtilityBuilder;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CategoryValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void validateCategoryRequest(CategoryRequest categoryRequest, Boolean isNewCategory, String type) {

		RequestInfo requestInfo = categoryRequest.getRequestInfo();

		for (Category category : categoryRequest.getCategories()) {
			category.setName((category.getName() == null) ? null : category.getName().trim());
			category.setCode((category.getCode() == null) ? null : category.getCode().trim());
			category.setTenantId((category.getTenantId() == null) ? null : category.getTenantId().trim());
			Long parentId = category.getParentId();
			Long categoryId = null;

			if (isNewCategory) {
				AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
				category.setAuditDetails(auditDetails);
			} else {
				AuditDetails auditDetails = category.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				category.setAuditDetails(auditDetails);
				categoryId = category.getId();
				if (categoryId == null) {
					throw new InvalidInputException(propertiesManager.getInvalidCategoryIdMsg(), requestInfo);
				}
			}

			// checking for existence of duplicate record
			Boolean isDuplicateRecordExists = utilityHelper.checkWhetherDuplicateRecordExits(category.getTenantId(),
					category.getCode(), null, ConstantUtility.CATEGORY_TABLE_NAME, categoryId);

			if (isDuplicateRecordExists) {
				throw new DuplicateIdException(propertiesManager.getCategoryCustomMsg(), requestInfo);
			}

			// checking for existence of duplicate record
			isDuplicateRecordExists = utilityHelper.checkWhetherDuplicateRecordExits(category.getTenantId(), null,
					category.getName(), ConstantUtility.CATEGORY_TABLE_NAME, categoryId);

			if (isDuplicateRecordExists) {
				throw new DuplicateNameException(propertiesManager.getCategoryNameDuplicate(), requestInfo);
			}

			if (type != null && type.equals(ConstantUtility.SUB_CATEGORY_TYPE)) {
				if (parentId == null) {
					throw new InvalidInputException(propertiesManager.getInvalidParentIdMsg(), requestInfo);
				} else {
					validateSubCategory(category, requestInfo, isNewCategory);
				}

			} else {
				category.setValidityYears(0l);
				category.setParentId(null);
				category.setDetails(null);
			}

		}
	}

	public void validateSubCategory(Category category, RequestInfo requestInfo, Boolean isNewCategory) {

		Boolean isParentExists = checkWhetherParentRecordExits(category.getParentId(),
				ConstantUtility.CATEGORY_TABLE_NAME);

		if (isParentExists) {

			if (category.getValidityYears() == null || category.getValidityYears() < 1
					|| category.getValidityYears() > 10) {
				throw new InvalidInputException(propertiesManager.getInvalidValidityYears(), requestInfo);
			}
			Map<String, Integer> occurrences = new HashMap<String, Integer>();

			for (CategoryDetail categoryDetail : category.getDetails()) {

				Long categoryDetailId = null;
				Boolean isCategoryDetailDuplicateExists = null;
				Boolean duplicateFeeType = Boolean.FALSE;
				if (isNewCategory) {

					isCategoryDetailDuplicateExists = false;
				} else {

					categoryDetailId = categoryDetail.getId();
					isCategoryDetailDuplicateExists = checkWhetherDuplicateCategoryDetailRecordExits(categoryDetail,
							ConstantUtility.CATEGORY_DETAIL_TABLE_NAME, categoryDetailId);
				}

				occurrences.put(categoryDetail.getFeeType().toString(),
						occurrences.containsKey(categoryDetail.getFeeType().toString())
								? occurrences.get(categoryDetail.getFeeType().toString()) + 1 : 1);

				duplicateFeeType = (occurrences.get(categoryDetail.getFeeType().toString()) > 1);
				if (isCategoryDetailDuplicateExists || duplicateFeeType) {
					throw new DuplicateIdException(propertiesManager.getDuplicateSubCategoryDetail(), requestInfo);
				}

				Boolean isUomExists = checkWhetherUomExists(categoryDetail);

				if (!isUomExists) {

					throw new InvalidInputException(propertiesManager.getInvalidUomIdMsg(), requestInfo);
				}
				categoryDetail.setAuditDetails(category.getAuditDetails());
			}

		} else {
			throw new InvalidInputException(propertiesManager.getInvalidParentIdMsg(), requestInfo);
		}
	}

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
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
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

			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
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
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
		}

		if (count > 0) {
			isExists = Boolean.TRUE;
		}

		return isExists;
	}
}