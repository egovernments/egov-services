package org.egov.tradelicense.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategoryDetail;
import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.persistence.repository.builder.CategoryQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository class for create/update/search category master
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Repository
public class CategoryRepository {



	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Description : this method will create category in database
	 * 
	 * @param Category
	 * @return categoryId
	 */
	public Long createCategory(Category category) {
		
		final KeyHolder holder = new GeneratedKeyHolder();
		AuditDetails auditDetails = category.getAuditDetails();
		String categoryInsert = CategoryQueryBuilder.INSERT_CATEGORY_QUERY;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", category.getTenantId());
		parameters.addValue("name", category.getName());
		parameters.addValue("code", category.getCode());
		parameters.addValue("active", (category.getActive() == null ? true : category.getActive()));
		parameters.addValue("parent", category.getParent());
		parameters.addValue("businessNature", category.getBusinessNature() == null ? null : category.getBusinessNature().name());
		parameters.addValue("validityYears", category.getValidityYears() == null ? 0 : category.getValidityYears());
		parameters.addValue("createdBy", auditDetails.getCreatedBy());
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("createdTime", auditDetails.getCreatedTime());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		namedParameterJdbcTemplate.update(categoryInsert, parameters, holder, new String[] { "id" });
		
		return Long.valueOf(holder.getKey().intValue());

	}

	/**
	 * Description : this method will create categoryDetail in database
	 * 
	 * @param CategoryDetail
	 * @return CategoryDetailId
	 */
	public Long createCategoryDetail(CategoryDetail categoryDetail) {
	
		final KeyHolder holder = new GeneratedKeyHolder();
		String categoryDetailInsert = CategoryQueryBuilder.INSERT_CATEGORY_DETAIL_QUERY;
		
		AuditDetails auditDetails = categoryDetail.getAuditDetails();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("category",  categoryDetail.getCategory());
		parameters.addValue("tenantId",  categoryDetail.getTenantId());
		parameters.addValue("feeType",  categoryDetail.getFeeType().toString());
		parameters.addValue("rateType",  categoryDetail.getRateType().toString());
		parameters.addValue("uom",  categoryDetail.getUom());
		parameters.addValue("createdBy",  auditDetails.getCreatedBy());
		parameters.addValue("lastModifiedBy",  auditDetails.getLastModifiedBy());
		parameters.addValue("createdTime",  auditDetails.getCreatedTime());
		parameters.addValue("lastModifiedTime",  auditDetails.getLastModifiedTime());
		
		namedParameterJdbcTemplate.update(categoryDetailInsert, parameters, holder, new String[] { "id" });

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method for update category in database
	 * 
	 * @param Category
	 * @return Category
	 */
	public Category updateCategory(Category category) {
		
		AuditDetails auditDetails = category.getAuditDetails();
		String categoryUpdateSql = CategoryQueryBuilder.UPDATE_CATEGORY_QUERY;
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId",  category.getTenantId());
		parameters.addValue("name",  category.getName());
		parameters.addValue("code",  category.getCode());
		parameters.addValue("active", (category.getActive() == null ? true : category.getActive()));
		parameters.addValue("parent",  category.getParent());
		parameters.addValue("businessNature", (category.getBusinessNature() == null ? null : category.getBusinessNature().name()));
		parameters.addValue("validityYears",  (category.getValidityYears() == null ? 0 : category.getValidityYears()));
		parameters.addValue("lastModifiedBy",  auditDetails.getLastModifiedBy());
		parameters.addValue("lastModifiedTime",  auditDetails.getLastModifiedTime());
		parameters.addValue("id",  category.getId());
	
		namedParameterJdbcTemplate.update(categoryUpdateSql, parameters);

		return category;
	}

	/**
	 * Description : this method for update categoryDetail in database
	 * 
	 * @param CategoryDetail
	 * @return CategoryDetail
	 */
	public CategoryDetail updateCategoryDetail(CategoryDetail categoryDetail) {

		String categoryDetailsUpdateSql = CategoryQueryBuilder.UPDATE_CATEGORY_DETAIL_QUERY;
		AuditDetails auditDetails = categoryDetail.getAuditDetails();
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("category",  categoryDetail.getCategory());
		parameters.addValue("tenantId",  categoryDetail.getTenantId());
		parameters.addValue("feeType",  categoryDetail.getFeeType().toString());
		parameters.addValue("rateType",  categoryDetail.getRateType().toString());
		parameters.addValue("uom", categoryDetail.getUom());
		parameters.addValue("lastModifiedBy",  auditDetails.getLastModifiedBy());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		parameters.addValue("id",  categoryDetail.getId());

		namedParameterJdbcTemplate.update(categoryDetailsUpdateSql, parameters);
		
		return categoryDetail;
	}

	/**
	 * Description : this method for search category
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param pageSize
	 * @param offSet
	 * @return List<Category>
	 */
	public List<CategorySearch> searchCategory(String tenantId, Integer[] ids, String[] codes, String name, String active,
			String type, String businessNature, String category, String rateType, String feeType,
			String uom, Integer pageSize, Integer offSet) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();

		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}

		String categorySearchQuery = CategoryQueryBuilder.buildSearchQuery(tenantId, ids, codes, name, active, type,
				businessNature, category, rateType, feeType, uom, pageSize, offSet, parameters);
		
		List<CategorySearch> categories = getCategories(categorySearchQuery.toString(), parameters);

		return categories;
	}

	/**
	 * Description : this method for search CategoryDetail of a category
	 * 
	 * @param categoryId
	 * @param pageSize
	 * @param offSet
	 * @return List<CategoryDetail>
	 */
	public List<CategoryDetailSearch> getCategoryDetailsByCategoryId(String category, Integer pageSize, Integer offSet) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();

		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String categoryDetailSearchQuery = CategoryQueryBuilder.buildCategoryDetailSearchQuery(category, pageSize,
				offSet, parameters);
		List<CategoryDetailSearch> categoryDetails = getCategoryDetails(categoryDetailSearchQuery.toString(),
				parameters);

		return categoryDetails;
	}

	/**
	 * This method will execute the given query & will build the CategoryDetail
	 * object
	 * 
	 * @param query
	 * @return {@link CategoryDetail} List of CategoryDetail
	 */
	private List<CategoryDetailSearch> getCategoryDetails(String query, MapSqlParameterSource parameter) {

		List<CategoryDetailSearch> categoryDetails = new ArrayList<>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameter);

		for (Map<String, Object> row : rows) {

			CategoryDetailSearch categoryDetail = new CategoryDetailSearch();
			categoryDetail.setId(getLong(row.get("id")));
			categoryDetail.setCategory(getString(row.get("category")));
			categoryDetail.setTenantId(getString(row.get("tenantId")));
			if(row.get("feeType") != null){
				categoryDetail.setFeeType(FeeTypeEnum.fromValue(getString(row.get("feeType"))));
			} else {
				categoryDetail.setFeeType(null);
			}
			if(row.get("rateType") != null){
				categoryDetail.setRateType(RateTypeEnum.fromValue(getString(row.get("rateType"))));
			} else {
				categoryDetail.setRateType(null);
			}
			categoryDetail.setUom(getString(row.get("uom")));
			categoryDetail.setUomName(getString(row.get("uomname")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			categoryDetail.setAuditDetails(auditDetails);

			categoryDetails.add(categoryDetail);
		}

		return categoryDetails;
	}

	/**
	 * This method will execute the given query & will build the Category object
	 * 
	 * @param query
	 * @return {@link Category} List of Category
	 */
	private List<CategorySearch> getCategories(String query, MapSqlParameterSource parameter) {

		List<CategorySearch> categories = new ArrayList<>();
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		
		try {
			
			rows = namedParameterJdbcTemplate.queryForList(query, parameter);
			
		} catch(Exception e){
			
			return categories;
		}
		

		for (Map<String, Object> row : rows) {
			String parent = getString(row.get("parent"));
			CategorySearch category = new CategorySearch();
			category.setId(getLong(row.get("id")));
			category.setTenantId(getString(row.get("tenantid")));
			category.setCode(getString(row.get("code")));
			category.setName(getString(row.get("name")));
			category.setActive(getBoolean(row.get("active")));
			category.setParent(parent == null ? null : parent);
			if(parent != null){
				category.setParentName(getParentName(parent));
			} else {
				category.setParentName(null);
			}
			category.setValidityYears( getLong( row.get("validityYears")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			category.setAuditDetails(auditDetails);

			categories.add(category);

		}

		return categories;
	}

	private String getParentName(String parent) {

		String parentName = null;
		
		if(parent != null){
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			String sql = CategoryQueryBuilder.getQueryForParentName(parent, parameters);
			List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(sql, parameters);
			for (Map<String, Object> row : rows) {
				parentName = getString(row.get("name"));	
			}
			
		}
		return parentName;
	}

	/**
	 * This method will cast the given object to String
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to double
	 * 
	 */
	@SuppressWarnings("unused")
	private Double getDouble(Object object) {
		return object == null ? null : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}

	/**
	 * This method will cast the given object to Boolean
	 */
	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : (Boolean) object;
	}
}
