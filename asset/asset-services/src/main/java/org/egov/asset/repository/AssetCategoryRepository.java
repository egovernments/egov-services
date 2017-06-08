
package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.repository.builder.AssetCategoryQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetCategoryRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class AssetCategoryRepository {

	private static final Logger logger = LoggerFactory.getLogger(AssetCategoryRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	@Autowired
	private AssetCategoryRowMapper assetCategoryRowMapper;

	@Autowired
	private AssetCategoryQueryBuilder assetCategoryQueryBuilder;

	public List<AssetCategory> search(AssetCategoryCriteria assetCategoryCriteria) {

		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = assetCategoryQueryBuilder.getQuery(assetCategoryCriteria, preparedStatementValues);

		List<AssetCategory> assetCategory = null;
		try {
			assetCategory = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
					assetCategoryRowMapper);
		} catch (Exception exception) {
			logger.info("the exception in assetcategory search :" + exception);
		}
		return assetCategory;
	}

	public String getAssetCategoryCode() {
		String query = "SELECT nextval('seq_egasset_categorycode')";
		Integer result = jdbcTemplate.queryForObject(query, Integer.class);
		logger.info("result:" + result);
		// String code=String.format("%03d", result);
		StringBuilder code = null;
		try {
			code = new StringBuilder(String.format("%03d", result));
		} catch (Exception ex) {
			logger.info("the exception in assetcategory code gen :" + ex);
		}
		return code.toString();
	}

	public AssetCategory create(AssetCategoryRequest assetCategoryRequest) {

		RequestInfo requestInfo = assetCategoryRequest.getRequestInfo();
		AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
		String queryStr = assetCategoryQueryBuilder.getInsertQuery();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		AssetCategory assetCategory2 = new AssetCategory();
		assetCategory2.setAssetFieldsDefination(assetCategory.getAssetFieldsDefination());

		String customFields = null;
		String assetCategoryType = null;
		String depreciationMethod = null;

		if (assetCategory.getAssetCategoryType() != null)
			assetCategoryType = assetCategory.getAssetCategoryType().toString();

		if (assetCategory.getDepreciationMethod() != null)
			depreciationMethod = assetCategory.getDepreciationMethod().toString();

		try {
			customFields = mapper.writeValueAsString(assetCategory2);
			logger.info("customFields:::" + customFields);
		} catch (JsonProcessingException e) {
			logger.info("the exception in assetcategory customfileds mapping :" + e);
		}

		// TODO depreciationrate as of now hardcoded as null
		Object[] obj = new Object[] { assetCategory.getName(), assetCategory.getCode(), assetCategory.getParent(),
				assetCategoryType, depreciationMethod, null, assetCategory.getAssetAccount(),
				assetCategory.getAccumulatedDepreciationAccount(), assetCategory.getRevaluationReserveAccount(),
				assetCategory.getDepreciationExpenseAccount(), assetCategory.getUnitOfMeasurement(), customFields,
				assetCategory.getTenantId(), requestInfo.getUserInfo().getId(), new Date(),
				requestInfo.getUserInfo().getId(), new Date(), assetCategory.getIsAssetAllow(),
				assetCategory.getVersion() };

		try {
			jdbcTemplate.update(queryStr, obj);
		} catch (Exception exception) {
			logger.info("the exception in assetcategory insert :" + exception);
		}

		return assetCategory;
	}
	public AssetCategory update(AssetCategoryRequest assetCategoryRequest) {

		RequestInfo requestInfo = assetCategoryRequest.getRequestInfo();
		AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
		String queryStr = assetCategoryQueryBuilder.getUpdateQuery();

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		AssetCategory assetCategory2 = new AssetCategory();
		assetCategory2.setAssetFieldsDefination(assetCategory.getAssetFieldsDefination());

		String customFields = null;
		String assetCategoryType = null;
		String depreciationMethod = null;

		if (assetCategory.getAssetCategoryType() != null)
			assetCategoryType = assetCategory.getAssetCategoryType().toString();

		if (assetCategory.getDepreciationMethod() != null)
			depreciationMethod = assetCategory.getDepreciationMethod().toString();

		try {
			customFields = mapper.writeValueAsString(assetCategory2);
			logger.info("customFields:::" + customFields);
		} catch (JsonProcessingException e) {
			logger.info("the exception in assetcategory customfileds mapping :" + e);
		}
		
		// TODO depreciationrate as of now hardcoded as null
		Object[] obj = new Object[] { assetCategory.getParent(),
				assetCategoryType, depreciationMethod, null, assetCategory.getAssetAccount(),
				assetCategory.getAccumulatedDepreciationAccount(), assetCategory.getRevaluationReserveAccount(),
				assetCategory.getDepreciationExpenseAccount(), assetCategory.getUnitOfMeasurement(), customFields,
				requestInfo.getUserInfo().getId(), new Date(), assetCategory.getIsAssetAllow(),
				assetCategory.getVersion(), assetCategory.getCode(),assetCategory.getTenantId()};

		try {
			jdbcTemplate.update(queryStr, obj);
		} catch (Exception exception) {
			logger.info("the exception in assetcategory insert :" + exception);
		}

		return assetCategory;
	}
}
