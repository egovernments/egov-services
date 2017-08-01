package org.egov.tradelicense.repository.helper;

import org.egov.models.CategoryDetail;
import org.egov.tradelicense.repository.builder.UtilityBuilder;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryHelper {

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