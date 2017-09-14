package org.egov.tl.masters.persistence.repository;

import java.sql.Timestamp;
import java.util.List;

import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixSearch;
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

	public FeeMatrix getFeeMatrix(String tenantId, Long financialYearId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean validateFeeMatrixByIdAndTenantID(Long id, String tenantId) {
		Long count = 0l;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String sqlQuery = FeeMatrixQueryBuilder.getQueryForIdValidation(id, tenantId, parameters);
		count = namedParameterJdbcTemplate.queryForObject(sqlQuery, parameters, Long.class);

		return count == 0 ? false : true;
	}

	public boolean checkWhetherFeeMatrixExistsWithGivenFieds(String tenantId, ApplicationTypeEnum applicationTypeEnum,
			FeeTypeEnum feeTypeEnum, BusinessNatureEnum businessNatureEnum, Long categoryId, Long subCategoryId,
			String financialYear) {
		Long count = 0l;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String sqlQuery = FeeMatrixQueryBuilder.getQueryForUniquenessValidation(tenantId,
				applicationTypeEnum.toString(), feeTypeEnum.toString(), businessNatureEnum.toString(), categoryId,
				subCategoryId, financialYear, parameters);

		count = namedParameterJdbcTemplate.queryForObject(sqlQuery, parameters, Long.class);

		return count == 0L ? false : true;
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

		return count == 0 ? false : true;
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
		parameters.addValue("categoryId", searchEntity.getCategoryId());
		parameters.addValue("subcategoryId", searchEntity.getSubCategoryId());
		if (searchEntity.getApplicationType() != null) {
			parameters.addValue("applicationType", searchEntity.getApplicationType().toString());
		}
		if (searchEntity.getBusinessNature() != null) {
			parameters.addValue("businessNature", searchEntity.getBusinessNature().toString());
		}
		parameters.addValue("effectiveFrom", new Timestamp(searchEntity.getEffectiveFrom()));
		return parameters;
	}

	public List<FeeMatrixEntity> search(FeeMatrixSearchEntity searchEntity) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = FeeMatrixQueryBuilder.buildSearchQuery(searchEntity, parameters);
		BeanPropertyRowMapper<FeeMatrixEntity> row = new BeanPropertyRowMapper<FeeMatrixEntity>(FeeMatrixEntity.class);
		return namedParameterJdbcTemplate.query(query, parameters, row);

	}

}