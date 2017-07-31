package org.egov.tradelicense.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.CategoryDetail;
import org.egov.models.FeeMatrix;
import org.egov.models.FeeMatrixDetail;
import org.egov.tradelicense.repository.builder.CategoryQueryBuilder;
import org.egov.tradelicense.repository.builder.FeeMatrixQueryBuilder;
import org.egov.tradelicense.repository.helper.FeeMatrixHelper;
import org.egov.tradelicense.utility.TimeStampUtil;
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
	FeeMatrixHelper feeMatrixHelper;

	/**
	 * Description : this method will create FeeMatrix in database
	 * 
	 * @param FeeMatrix
	 * @return feeMatrixId
	 */
	public Long createFeeMatrix(String tenantId, FeeMatrix feeMatrix) {

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
				ps.setTimestamp(7, TimeStampUtil.getTimeStamp(feeMatrix.getEffectiveFrom()));
				ps.setTimestamp(8, TimeStampUtil.getTimeStamp(feeMatrix.getEffectiveTo()));
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
	public Long createFeeMatrixDetails(String tenantId, FeeMatrixDetail feeMatrixDetail) {

		String feeMatrixInsertQuery = FeeMatrixQueryBuilder.INSERT_FEE_MATRIX_DETAIL_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(feeMatrixInsertQuery, new String[] { "id" });

				ps.setLong(1, feeMatrixDetail.getFeeMatrixId());
				ps.setLong(2, feeMatrixDetail.getUomFrom());
				ps.setLong(3, feeMatrixDetail.getUomTo());
				ps.setDouble(4, feeMatrixDetail.getAmount());

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
	public FeeMatrix updateFeeMatrix(String tenantId, FeeMatrix feeMatrix) {

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
				ps.setTimestamp(7, TimeStampUtil.getTimeStamp(feeMatrix.getEffectiveFrom()));
				ps.setTimestamp(8, TimeStampUtil.getTimeStamp(feeMatrix.getEffectiveTo()));
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
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(feeMatrixDetailUpdateSql);

				ps.setLong(1, feeMatrixDetail.getFeeMatrixId());
				ps.setLong(2, feeMatrixDetail.getUomFrom());
				ps.setLong(3, feeMatrixDetail.getUomTo());
				ps.setDouble(4, feeMatrixDetail.getAmount());
				ps.setLong(5, feeMatrixDetail.getId());

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
		String feeMatrixSearchQuery = FeeMatrixQueryBuilder.buildSearchQuery(tenantId, ids, categoryId, subCategoryId,
				financialYear, applicationType, businessNature, pageSize, offSet, preparedStatementValues);
		List<FeeMatrix> feeMatrices = feeMatrixHelper.getFeeMatrices(feeMatrixSearchQuery.toString(),
				preparedStatementValues);

		return feeMatrices;
	}
	
	/**
	 * Description : this method for search FeeMatrixDetail of a feeMatrixId
	 * 
	 * @param feeMatrixId
	 * @return List<FeeMatrixDetail>
	 */
	public List<FeeMatrixDetail> getFeeMatrixDetailsByFeeMatrixId(Long feeMatrixId) {
		
		List<Object> preparedStatementValues = new ArrayList<>();
		String feeMatrixDetailSearchQuery = FeeMatrixQueryBuilder.buildFeeMatrixDetailSearchQuery(feeMatrixId, preparedStatementValues);
		List<FeeMatrixDetail> feeMatrixDetails = feeMatrixHelper.getFeeMatrixDetails(feeMatrixDetailSearchQuery.toString(),
				preparedStatementValues);

		return feeMatrixDetails;
	}
}