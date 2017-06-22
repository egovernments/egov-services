package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.calculator.repository.builder.FactorQueryBuilder;
import org.egov.calculator.util.TimeStampUtil;
import org.egov.models.CalculationFactor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Description : TaxFactorRepository class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class TaxFactorRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Description : This method for creating new factor(s)
	 * 
	 * @param tenantId
	 * @param calculationFactorRequest
	 * @return calculationFactorResponse
	 * @throws Exception
	 */
	public Long saveFactor(String tenantId,
			CalculationFactor calculationFactor) {

		Long createdTime = new Date().getTime();
		String factorInsertSql = FactorQueryBuilder.FACTOR_CREATE_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(
					final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(factorInsertSql, new String[]{"id"});

				ps.setString(1, calculationFactor.getTenantId());
				ps.setString(2, calculationFactor.getFactorCode());
				ps.setString(3, calculationFactor.getFactorType());
				ps.setDouble(4, calculationFactor.getFactorValue());
				ps.setTimestamp(5, TimeStampUtil
						.getTimeStamp(calculationFactor.getFromDate()));
				ps.setTimestamp(6, TimeStampUtil
						.getTimeStamp(calculationFactor.getToDate()));
				ps.setString(7,
						calculationFactor.getAuditDetails().getCreatedBy());
				ps.setString(8, calculationFactor.getAuditDetails()
						.getLastModifiedBy());
				ps.setLong(9, createdTime);
				ps.setLong(10, createdTime);
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
		calculationFactor.getAuditDetails().setCreatedTime(createdTime);
		calculationFactor.getAuditDetails().setLastModifiedTime(createdTime);
		return Long.valueOf(holder.getKey().intValue());

	}

	/**
	 * Description : This method to Update any of the factor
	 * 
	 * @param tenantId
	 * @param calculationFactorRequest
	 * @return calculationFactorResponse
	 * @throws Exception
	 */
	public void updateFactor(String tenantId, Long id,
			CalculationFactor calculationFactor) {

		String factorInsertSql = FactorQueryBuilder.FACTOR_UPDATE_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(
					final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(factorInsertSql, new String[]{"id"});

				ps.setString(1, calculationFactor.getTenantId());
				ps.setString(2, calculationFactor.getFactorCode());
				ps.setObject(3, calculationFactor.getFactorType());
				ps.setDouble(4, calculationFactor.getFactorValue());
				ps.setTimestamp(5, TimeStampUtil
						.getTimeStamp(calculationFactor.getFromDate()));
				ps.setTimestamp(6, TimeStampUtil
						.getTimeStamp(calculationFactor.getToDate()));
				ps.setLong(7, calculationFactor.getAuditDetails()
						.getLastModifiedTime());
				ps.setLong(8, calculationFactor.getId());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
	}

	/**
	 * Description : This method for search factor details
	 * 
	 * @param tenantId
	 * @param factorType
	 * @param validDate
	 * @param code
	 * @param requestInfo
	 * @return calculationFactorResponse
	 * @throws Exception
	 */

	public List<CalculationFactor> searchFactor(String tenantId,
			String factorType, String validDate, String code) {

		String factorSearchSql = FactorQueryBuilder
				.getFactorSearchQuery(tenantId, factorType, validDate, code);

		List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();

		calculationFactors = jdbcTemplate.query(factorSearchSql.toString(),
				new BeanPropertyRowMapper(CalculationFactor.class));

		return calculationFactors;

	}

	/**
	 * Description : This method for getting factors
	 * 
	 * @param tenantId
	 * @param validDate
	 * @param requestInfo
	 * @return calculationFactorResponse
	 * @throws Exception
	 */

	public List<CalculationFactor> getFactorsByTenantIdAndValidDate(
			String tenantId, String validDate) {

		String factorSearchSql = FactorQueryBuilder
				.getFactorSearchQueryByTenantIdAndValidDate(tenantId,
						validDate);

		List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();

		calculationFactors = jdbcTemplate.query(factorSearchSql.toString(),
				new BeanPropertyRowMapper(CalculationFactor.class));

		return calculationFactors;

	}

}
