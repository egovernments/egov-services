package org.egov.tl.masters.persistence.repository;

import java.sql.Timestamp;
import java.util.List;

import org.egov.tl.masters.domain.model.Pagination;
import org.egov.tl.masters.persistence.entity.FeeMatrixEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixSearchEntity;
import org.egov.tradelicense.persistence.repository.builder.FeeMatrixQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeeMatrixJdbcRepository extends JdbcRepository {

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	static {

		init(FeeMatrixEntity.class);

	}

	/**
	 * 
	 * @param FeeMatrixEntity
	 * @return FeeMatrixEntity
	 * 
	 *         this method will call the JdbcRepository create method that will
	 *         insert the data into database and returns FeeMatrixEntity
	 *         whatever it receives from JdbcRepository create method
	 */
	public FeeMatrixEntity create(FeeMatrixEntity entity) {

		super.create(entity);
		return entity;
	}

	/**
	 * this method will call the JdbcRepository update method that will update
	 * the data in the database and returns FeeMatrixEntity whatever it receives
	 * from JdbcRepository update method
	 * 
	 * @param entity
	 * @return
	 */
	public FeeMatrixEntity update(FeeMatrixEntity entity) {

		super.update(entity);
		return entity;
	}

	/**
	 * 
	 * @param categoryId
	 * @param subcategoryId
	 * @return
	 * 
	 * 		this will be used to validate Existance of category and
	 *         SubCategory
	 */
	public boolean validateIdExistance(Long categoryId, Long subcategoryId, String tenatId) {

		return true;
	}

	public Long getNextSequence() {

		String id = getSequence(FeeMatrixEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public boolean validateFeeMatrixByIdAndTenantID(Long id, String tenantId) {
		Long count = 0l;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String sqlQuery = FeeMatrixQueryBuilder.getQueryForIdValidation(id, tenantId, parameters);
		count = namedParameterJdbcTemplate.queryForObject(sqlQuery, parameters, Long.class);

		return count.equals(0l) ? false : true;
	}

	public boolean checkWhetherFeeMatrixExistsWithGivenFieds(FeeMatrixSearchEntity entity) {

		List<FeeMatrixEntity> feeMatrixEntities = uniqueSearch(entity);
		if (feeMatrixEntities.size()==0) {
			return false;
		}

		return true;
	}

	/**
	 * Checks whether Fee matrix details exists with the following parameters
	 * 
	 * @param uomFrom
	 * @param uomTo
	 * @return
	 */
	public boolean checkWhetherFeeMatrixExistsWithGivenUom(Long uomFrom, Long uomTo) {

		Long count = 0l;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String sqlQuery = FeeMatrixQueryBuilder.getQueryForFeeMatrixUomValidation();

		parameters.addValue("uomFrom", uomFrom);
		parameters.addValue("uomTo", uomTo);

		count = namedParameterJdbcTemplate.queryForObject(sqlQuery, parameters, Long.class);

		return count.equals(0l) ? false : true;
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	@SuppressWarnings("unused")
	private Double getDouble(Object object) {
		return object == null ? null : Double.parseDouble(object.toString());
	}

	public Pagination<FeeMatrixEntity> getFeeMatrixForNextFinancialYear(FeeMatrixSearchEntity searchEntity) {
		String query = FeeMatrixQueryBuilder.getNextFeematrixBasedOnEffectiveFrom(searchEntity);

		return getFeeMatrixEntitiesWithQuery(searchEntity, query);
	}

	public Pagination<FeeMatrixEntity> getFeeMatrixForPreviousFinancialYear(FeeMatrixSearchEntity searchEntity) {
		String query = FeeMatrixQueryBuilder.getPreviousFeematrixBasedOnEffictiveFrom(searchEntity);

		return getFeeMatrixEntitiesWithQuery(searchEntity, query);
	}

	@SuppressWarnings("unchecked")
	public Pagination<FeeMatrixEntity> getFeeMatrixEntitiesWithQuery(FeeMatrixSearchEntity searchEntity, String query) {
		Pagination<FeeMatrixEntity> page = new Pagination<>();
		if (searchEntity.getOffSet() != null) {
			page.setOffset(searchEntity.getOffSet());
		}
		if (searchEntity.getPageSize() != null) {
			page.setPageSize(searchEntity.getPageSize());
		}
		MapSqlParameterSource parameters = buildParamters(searchEntity);
		page = (Pagination<FeeMatrixEntity>) getPagination(query, page, parameters);

		BeanPropertyRowMapper<FeeMatrixEntity> row = new BeanPropertyRowMapper<FeeMatrixEntity>(FeeMatrixEntity.class);

		List<FeeMatrixEntity> feeMatrixEntities = namedParameterJdbcTemplate.query(query.toString(), parameters, row);

		page.setTotalResults(feeMatrixEntities.size());
		page.setPagedData(feeMatrixEntities);

		return page;
	}

	public MapSqlParameterSource buildParamters(FeeMatrixSearchEntity searchEntity) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", searchEntity.getTenantId());
		parameters.addValue("feeType", searchEntity.getFeeType().toString());
		parameters.addValue("category", searchEntity.getCategory());
		parameters.addValue("subcategory", searchEntity.getSubCategory());
		if (searchEntity.getApplicationType() != null) {
			parameters.addValue("applicationType", searchEntity.getApplicationType().toString());
		}
		if (searchEntity.getBusinessNature() != null) {
			parameters.addValue("businessNature", searchEntity.getBusinessNature().toString());
		}
		parameters.addValue("effectiveFrom", new Timestamp(searchEntity.getEffectiveFrom()));
		return parameters;
	}
	
	public List<FeeMatrixEntity> uniqueSearch(FeeMatrixSearchEntity searchEntity) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + FeeMatrixEntity.TABLE_NAME + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId", searchEntity.getTenantId());
		
		if (searchEntity.getCategory() != null) {
			searchSql.append(" AND category = :category ");
			parameters.addValue("category", searchEntity.getCategory());
		}

		if (searchEntity.getSubCategory() != null) {
			searchSql.append(" AND subCategory = :subCategory ");
			parameters.addValue("subCategory", searchEntity.getSubCategory());
		}

		if (searchEntity.getFinancialYear() != null && !searchEntity.getFinancialYear().isEmpty()) {
			searchSql.append(" AND financialYear = :financialYear ");
			parameters.addValue("financialYear", searchEntity.getFinancialYear());
		}
		
		if (searchEntity.getApplicationType() != null && !searchEntity.getApplicationType().isEmpty()) {
			searchSql.append(" AND lower(applicationType) = :applicationType ");
			parameters.addValue("applicationType", searchEntity.getApplicationType().toLowerCase());
		} else {
			searchSql.append(" AND applicationType IS NULL ");
		}

		if (searchEntity.getBusinessNature() != null && !searchEntity.getBusinessNature().isEmpty()) {
			searchSql.append(" AND lower(businessNature) = :businessNature");
			parameters.addValue("businessNature", searchEntity.getBusinessNature().toLowerCase());
		} else {
			searchSql.append(" AND businessNature IS NULL ");
		}

		if (searchEntity.getFeeType() != null && !searchEntity.getFeeType().isEmpty()) {
			searchSql.append(" AND lower(feeType) = :feeType");
			parameters.addValue("feeType", searchEntity.getFeeType().toLowerCase());
		}

		if (searchEntity.getPageSize() != null) {
			searchSql.append(" limit :limit ");
			parameters.addValue("limit", searchEntity.getPageSize());
		}

		if (searchEntity.getOffSet() != null) {
			searchSql.append(" offset :offSet ");
			parameters.addValue("offSet", searchEntity.getOffSet());
		}

		BeanPropertyRowMapper<FeeMatrixEntity> row = new BeanPropertyRowMapper<FeeMatrixEntity>(FeeMatrixEntity.class);
		List<FeeMatrixEntity> feeMatrixEntity = namedParameterJdbcTemplate.query(searchSql.toString(), parameters, row);
		return feeMatrixEntity;
		
	}

	public List<FeeMatrixEntity> search(FeeMatrixSearchEntity searchEntity) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + FeeMatrixEntity.TABLE_NAME + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameters.addValue("tenantId", searchEntity.getTenantId());

		if (searchEntity.getIds() != null && searchEntity.getIds().length > 0) {

			String searchIds = "";
			int count = 1;
			for (Integer id : searchEntity.getIds()) {

				if (count < searchEntity.getIds().length)
					searchIds = searchIds + id + ",";
				else
					searchIds = searchIds + id;

				count++;
			}
			searchSql.append(" AND id IN (" + searchIds + ") ");
		}

		if (searchEntity.getCategory() != null) {
			searchSql.append(" AND category = :category ");
			parameters.addValue("category", searchEntity.getCategory());
		}

		if (searchEntity.getSubCategory() != null) {
			searchSql.append(" AND subCategory = :subCategory ");
			parameters.addValue("subCategory", searchEntity.getSubCategory());
		}

		if (searchEntity.getFinancialYear() != null && !searchEntity.getFinancialYear().isEmpty()) {
			searchSql.append(" AND financialYear = :financialYear ");
			parameters.addValue("financialYear", searchEntity.getFinancialYear());
		}

		if (searchEntity.getApplicationType() != null && !searchEntity.getApplicationType().isEmpty()) {
			searchSql.append(" AND lower(applicationType) = :applicationType ");
			parameters.addValue("applicationType", searchEntity.getApplicationType().toLowerCase());
		} else if(searchEntity.getFallBack() != null && searchEntity.getFallBack()==Boolean.TRUE){
			searchSql.append(" AND applicationType IS NULL ");
		}

		if (searchEntity.getBusinessNature() != null && !searchEntity.getBusinessNature().isEmpty()) {
			searchSql.append(" AND lower(businessNature) = :businessNature");
			parameters.addValue("businessNature", searchEntity.getBusinessNature().toLowerCase());
		} else if(searchEntity.getFallBack() != null && searchEntity.getFallBack()==Boolean.TRUE){
			searchSql.append(" AND businessNature IS NULL ");
		}

		if (searchEntity.getFeeType() != null && !searchEntity.getFeeType().isEmpty()) {
			searchSql.append(" AND lower(feeType) = :feeType");
			parameters.addValue("feeType", searchEntity.getFeeType().toLowerCase());
		}

		if (searchEntity.getPageSize() != null) {
			searchSql.append(" limit :limit ");
			parameters.addValue("limit", searchEntity.getPageSize());
		}

		if (searchEntity.getOffSet() != null) {
			searchSql.append(" offset :offSet ");
			parameters.addValue("offSet", searchEntity.getOffSet());
		}

		BeanPropertyRowMapper<FeeMatrixEntity> row = new BeanPropertyRowMapper<FeeMatrixEntity>(FeeMatrixEntity.class);
		List<FeeMatrixEntity> feeMatrixEntity = namedParameterJdbcTemplate.query(searchSql.toString(), parameters, row);
		return feeMatrixEntity;
	}

}