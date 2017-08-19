package org.egov.tradelicense.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.FeeMatrix;
import org.egov.tl.commons.web.contract.FeeMatrixDetail;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.validator.FeeMatrixValidator;
import org.egov.tradelicense.persistence.repository.builder.FeeMatrixQueryBuilder;
import org.egov.tradelicense.util.TimeStampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
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
	private JdbcTemplate jdbcTemplate;

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

		AuditDetails auditDetails = feeMatrix.getAuditDetails();
		String feeMatrixInsertQuery = FeeMatrixQueryBuilder.INSERT_FEE_MATRIX_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(feeMatrixInsertQuery, new String[] { "id" });

				ps.setString(1, feeMatrix.getTenantId());
				ps.setString(2, feeMatrix.getApplicationType().toString());
				ps.setLong(3, feeMatrix.getCategoryId());
				ps.setString(4, feeMatrix.getBusinessNature().toString());
				ps.setLong(5, feeMatrix.getSubCategoryId());
				ps.setString(6, feeMatrix.getFinancialYear());
				ps.setTimestamp(7, new Timestamp(feeMatrix.getEffectiveFrom()*1000));
				ps.setTimestamp(8, new Timestamp(feeMatrix.getEffectiveTo()*1000));
				ps.setString(9, auditDetails.getCreatedBy());
				ps.setString(10, auditDetails.getLastModifiedBy());
				ps.setLong(11, auditDetails.getCreatedTime());
				ps.setLong(12, auditDetails.getLastModifiedTime());

				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method will create FeeMatrixDetail in database
	 * 
	 * @param FeeMatrixDetail
	 * @return feeMatrixDetailId
	 */
	public Long createFeeMatrixDetails(FeeMatrixDetail feeMatrixDetail) {

		String feeMatrixInsertQuery = FeeMatrixQueryBuilder.INSERT_FEE_MATRIX_DETAIL_QUERY;
		AuditDetails auditDetails = feeMatrixDetail.getAuditDetails();
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(feeMatrixInsertQuery, new String[] { "id" });

				ps.setLong(1, feeMatrixDetail.getFeeMatrixId());
				ps.setLong(2, feeMatrixDetail.getUomFrom());
				ps.setLong(3, feeMatrixDetail.getUomTo());
				ps.setDouble(4, feeMatrixDetail.getAmount());
				ps.setString(5, auditDetails.getCreatedBy());
				ps.setString(6, auditDetails.getLastModifiedBy());
				ps.setLong(7, auditDetails.getCreatedTime());
				ps.setLong(8, auditDetails.getLastModifiedTime());

				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

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
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(feeMatrixUpdateQuery, new String[] { "id" });

				ps.setString(1, feeMatrix.getTenantId());
				ps.setString(2, feeMatrix.getApplicationType().toString());
				ps.setLong(3, feeMatrix.getCategoryId());
				ps.setString(4, feeMatrix.getBusinessNature().toString());
				ps.setLong(5, feeMatrix.getSubCategoryId());
				ps.setString(6, feeMatrix.getFinancialYear());
				ps.setTimestamp(7, new Timestamp(feeMatrix.getEffectiveFrom()*1000));
				ps.setTimestamp(8, new Timestamp(feeMatrix.getEffectiveTo()*1000));
				ps.setString(9, auditDetails.getLastModifiedBy());
				ps.setLong(10, auditDetails.getLastModifiedTime());
				ps.setLong(11, feeMatrix.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
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

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(feeMatrixDetailUpdateSql);

				ps.setLong(1, feeMatrixDetail.getFeeMatrixId());
				ps.setLong(2, feeMatrixDetail.getUomFrom());
				ps.setLong(3, feeMatrixDetail.getUomTo());
				ps.setDouble(4, feeMatrixDetail.getAmount());
				ps.setString(5, auditDetails.getLastModifiedBy());
				ps.setLong(6, auditDetails.getLastModifiedTime());
				ps.setLong(7, feeMatrixDetail.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
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

		List<Object> preparedStatementValues = new ArrayList<>();
		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String feeMatrixSearchQuery = FeeMatrixQueryBuilder.buildSearchQuery(tenantId, ids, categoryId, subCategoryId,
				financialYear, applicationType, businessNature, pageSize, offSet, preparedStatementValues);
		List<FeeMatrix> feeMatrices = feeMatrixValidator.getFeeMatrices(feeMatrixSearchQuery.toString(),
				preparedStatementValues);

		return feeMatrices;
	}

}