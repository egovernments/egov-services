package org.egov.tradelicense.persistence.repository;

import java.sql.Timestamp;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.FeeMatrix;
import org.egov.tl.commons.web.contract.FeeMatrixDetail;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.validator.FeeMatrixValidator;
import org.egov.tradelicense.persistence.repository.builder.FeeMatrixQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository class for create/update/search FeeMatrix master
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Repository
public class FeeMatrixRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	FeeMatrixValidator feeMatrixValidator;

	@Autowired
	private PropertiesManager propertiesManager;

	/**
	 * Description : this method will create FeeMatrix in database
	 * 
	 * @param FeeMatrix
	 * @return feeMatrixId
	 */
	public Long createFeeMatrix(FeeMatrix feeMatrix) {

		final KeyHolder holder = new GeneratedKeyHolder();
		AuditDetails auditDetails = feeMatrix.getAuditDetails();
		String feeMatrixInsertQuery = FeeMatrixQueryBuilder.INSERT_FEE_MATRIX_QUERY;

		 MapSqlParameterSource parameters = new MapSqlParameterSource();
		 parameters.addValue("tenantId", feeMatrix.getTenantId());
		 parameters.addValue("applicationType", feeMatrix.getApplicationType().name());
		 parameters.addValue("categoryId", feeMatrix.getCategoryId());
		 parameters.addValue("businessNature",  feeMatrix.getBusinessNature().name());
		 parameters.addValue("subCategoryId", feeMatrix.getSubCategoryId());
		 parameters.addValue("financialYear", feeMatrix.getFinancialYear());
		 parameters.addValue("effectiveFrom", new Timestamp(feeMatrix.getEffectiveFrom()));
		 parameters.addValue("effectiveTo", new Timestamp(feeMatrix.getEffectiveTo()));
		 parameters.addValue("createdBy", auditDetails.getCreatedBy());
		 parameters.addValue("lastModifiedBy",  auditDetails.getLastModifiedBy());
		 parameters.addValue("createdTime", auditDetails.getCreatedTime());
		 parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		 namedParameterJdbcTemplate.update(feeMatrixInsertQuery, parameters, holder, new String[] { "id" });


		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method will create FeeMatrixDetail in database
	 * 
	 * @param FeeMatrixDetail
	 * @return feeMatrixDetailId
	 */
	public Long createFeeMatrixDetails(FeeMatrixDetail feeMatrixDetail) {

		final KeyHolder holder = new GeneratedKeyHolder();
		String feeMatrixInsertQuery = FeeMatrixQueryBuilder.INSERT_FEE_MATRIX_DETAIL_QUERY;
		AuditDetails auditDetails = feeMatrixDetail.getAuditDetails();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("feeMatrixId", feeMatrixDetail.getFeeMatrixId());
		parameters.addValue("tenantId", feeMatrixDetail.getTenantId());
		parameters.addValue("uomFrom", feeMatrixDetail.getUomFrom());
		parameters.addValue("uomTo", feeMatrixDetail.getUomTo());
		parameters.addValue("amount", feeMatrixDetail.getAmount());
		parameters.addValue("createdBy", auditDetails.getCreatedBy());
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("createdTime", auditDetails.getCreatedTime());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		 namedParameterJdbcTemplate.update(feeMatrixInsertQuery, parameters, holder, new String[] { "id" });

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method will update FeeMatrix in database
	 * 
	 * @param FeeMatrix
	 * @return feeMatrix
	 */
	public FeeMatrix updateFeeMatrix(FeeMatrix feeMatrix) {

		AuditDetails auditDetails = feeMatrix.getAuditDetails();
		String feeMatrixUpdateQuery = FeeMatrixQueryBuilder.UPDATE_FEE_MATRIX_QUERY;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", feeMatrix.getTenantId());
		parameters.addValue("applicationType", feeMatrix.getApplicationType().name());
		parameters.addValue("categoryId", feeMatrix.getCategoryId());
		parameters.addValue("businessNature", feeMatrix.getBusinessNature().name());
		parameters.addValue("subCategoryId", feeMatrix.getSubCategoryId());
		parameters.addValue("financialYear", feeMatrix.getFinancialYear());
		parameters.addValue("effectiveFrom", new Timestamp(feeMatrix.getEffectiveFrom()));
		parameters.addValue("effectiveTo", new Timestamp(feeMatrix.getEffectiveTo()));
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		parameters.addValue("id", feeMatrix.getId());
		namedParameterJdbcTemplate.update(feeMatrixUpdateQuery, parameters);
		

		return feeMatrix;
	}

	/**
	 * Description : this method for update FeeMatrixDetail in database
	 * 
	 * @param FeeMatrixDetail
	 * @return FeeMatrixDetail
	 */
	public FeeMatrixDetail updateFeeMatrixDetail(FeeMatrixDetail feeMatrixDetail) {

		String feeMatrixDetailUpdateSql = FeeMatrixQueryBuilder.UPDATE_FEE_MATRIX_DETAIL_QUERY;
		AuditDetails auditDetails = feeMatrixDetail.getAuditDetails();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("feeMatrixId", feeMatrixDetail.getFeeMatrixId());
		parameters.addValue("tenantId", feeMatrixDetail.getTenantId());
		parameters.addValue("uomFrom", feeMatrixDetail.getUomFrom());
		parameters.addValue("uomTo", feeMatrixDetail.getUomTo());
		parameters.addValue("amount", feeMatrixDetail.getAmount());
		parameters.addValue("lastModifiedBy", auditDetails == null ? null : auditDetails.getLastModifiedBy());
		parameters.addValue("lastModifiedTime",   auditDetails == null ? null : auditDetails.getLastModifiedTime());
		parameters.addValue("id", feeMatrixDetail.getId());
		namedParameterJdbcTemplate.update(feeMatrixDetailUpdateSql, parameters);

		return feeMatrixDetail;
	}

	/**
	 * Description : this method will search FeeMatrix
	 * 
	 * @param tenantId
	 * @param ids
	 * @param categoryId
	 * @param subcategoryId
	 * @param financialYear
	 * @param applicationType
	 * @param businessNature
	 * @param pageSize
	 * @param offSet
	 * @return FeeMatrixResponse
	 * @return List<FeeMatrix>
	 */
	public List<FeeMatrix> searchFeeMatrix(String tenantId, Integer[] ids, Integer categoryId, Integer subCategoryId,
			String financialYear, String applicationType, String businessNature, Integer pageSize, Integer offSet) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String feeMatrixSearchQuery = FeeMatrixQueryBuilder.buildSearchQuery(tenantId, ids, categoryId, subCategoryId,
				financialYear, applicationType, businessNature, pageSize, offSet, parameters);
		List<FeeMatrix> feeMatrices = feeMatrixValidator.getFeeMatrices(feeMatrixSearchQuery.toString(),
				parameters);

		return feeMatrices;
	}

}