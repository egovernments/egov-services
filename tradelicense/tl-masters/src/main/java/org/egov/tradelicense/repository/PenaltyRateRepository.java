package org.egov.tradelicense.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.PenaltyRate;
import org.egov.tradelicense.repository.builder.PenaltyRateQueryBuilder;
import org.egov.tradelicense.repository.helper.PenaltyRateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository class for create/update/search PenaltyRate master
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Repository
public class PenaltyRateRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PenaltyRateHelper penaltyRateHelper;

	/**
	 * Description : this method will create PenaltyRate in database
	 * 
	 * @param tenantId
	 * @param PenaltyRate
	 * @return penaltyRateId
	 */
	public Long craeatePenaltyRate(String tenantId, PenaltyRate penaltyRate) {

		AuditDetails auditDetails = penaltyRate.getAuditDetails();
		String penaltyRateInsertQuery = PenaltyRateQueryBuilder.INSERT_PENALTY_RATE_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(penaltyRateInsertQuery, new String[] { "id" });

				ps.setString(1, penaltyRate.getTenantId());
				ps.setString(2, penaltyRate.getApplicationType().toString());
				ps.setLong(3, penaltyRate.getFromRange());
				ps.setLong(4, penaltyRate.getToRange());
				ps.setDouble(5, penaltyRate.getRate());
				ps.setString(6, auditDetails.getCreatedBy());
				ps.setString(7, auditDetails.getLastModifiedBy());
				ps.setLong(8, auditDetails.getCreatedTime());
				ps.setLong(9, auditDetails.getLastModifiedTime());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method for update PenaltyRate in database
	 * 
	 * @param PenaltyRate
	 * @return PenaltyRate
	 */
	public PenaltyRate updatePenaltyRate(PenaltyRate penaltyRate) {

		AuditDetails auditDetails = penaltyRate.getAuditDetails();
		String penaltyRateUpdateQuery = PenaltyRateQueryBuilder.UPDATE_PENALTY_RATE_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(penaltyRateUpdateQuery);

				ps.setString(1, penaltyRate.getTenantId());
				ps.setString(2, penaltyRate.getApplicationType().toString());
				ps.setLong(3, penaltyRate.getFromRange());
				ps.setLong(4, penaltyRate.getToRange());
				ps.setDouble(5, penaltyRate.getToRange());
				ps.setString(6, auditDetails.getLastModifiedBy());
				ps.setLong(7, auditDetails.getLastModifiedTime());
				ps.setLong(8, penaltyRate.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
		return penaltyRate;
	}

	/**
	 * Description : this method to search PenaltyRate
	 * 
	 * @param tenantId
	 * @param ids
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return List<PenaltyRate>
	 * @throws Exception
	 */
	public List<PenaltyRate> searchPenaltyRate(String tenantId, Integer[] ids, String applicationType,
			Integer pageSize, Integer offSet) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String penaltyRateSearchQuery = PenaltyRateQueryBuilder.buildSearchQuery(tenantId, ids, applicationType,
				pageSize, offSet, preparedStatementValues);
		List<PenaltyRate> penaltyRates = penaltyRateHelper.getPenaltyRates(penaltyRateSearchQuery.toString(),
				preparedStatementValues);

		return penaltyRates;
	}
}