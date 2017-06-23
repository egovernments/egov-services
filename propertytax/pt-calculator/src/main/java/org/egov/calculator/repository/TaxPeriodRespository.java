package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.egov.calculator.repository.builder.TaxPeriodBuilder;
import org.egov.calculator.util.TimeStampUtil;
import org.egov.models.TaxPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h1>TaxPeriodRespository</h1> This class have the create ,update & search
 * API's for the tax period
 * <P>
 * All the DB operations for the tax period will be handled here
 * 
 * @author Prasad
 * 
 *
 */
@Repository
public class TaxPeriodRespository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * This will persist the given tax period object
	 * 
	 * @param taxperiod
	 *            tax period object
	 * @param tenantId
	 * @return {@link Long} the id stored in the database
	 */
	@Transactional
	public Long saveTaxPeriod(TaxPeriod taxperiod, String tenantId) {

		Long createdTime = new Date().getTime();

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(TaxPeriodBuilder.INSERT_TAX_PERIOD_QUERY,
						new String[] { "id" });
				ps.setString(1, taxperiod.getTenantId());
				ps.setTimestamp(2, TimeStampUtil.getTimeStamp(taxperiod.getFromDate()));
				ps.setTimestamp(3, TimeStampUtil.getTimeStamp(taxperiod.getToDate()));
				ps.setString(4, taxperiod.getCode());
				ps.setString(5, taxperiod.getPeriodType());
				ps.setString(6, taxperiod.getFinancialYear());
				ps.setString(7, taxperiod.getAuditDetails().getCreatedBy());
				ps.setString(8, taxperiod.getAuditDetails().getLastModifiedBy());
				ps.setLong(9, createdTime);
				ps.setLong(10, createdTime);
				return ps;
			}

		};

		final KeyHolder holder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(psc, holder);
		} catch (Exception e) {
			System.out.println(e);
		}
		taxperiod.getAuditDetails().setCreatedTime(createdTime);
		;
		taxperiod.getAuditDetails().setLastModifiedTime(createdTime);
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This will update the tax period object ,with the given tax period for the
	 * given tenatId
	 * 
	 * @param taxperiod
	 * @param tenantId
	 */

	@Transactional
	public void updateTaxPeriod(TaxPeriod taxperiod, String tenantId) {

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(TaxPeriodBuilder.UPDATE_TAX_PERIOD_QUERY);
				ps.setString(1, taxperiod.getTenantId());
				ps.setTimestamp(2, TimeStampUtil.getTimeStamp(taxperiod.getFromDate()));
				ps.setTimestamp(3, TimeStampUtil.getTimeStamp(taxperiod.getToDate()));
				ps.setString(4, taxperiod.getCode());
				ps.setString(5, taxperiod.getPeriodType());
				ps.setString(6, taxperiod.getFinancialYear());
				ps.setString(7, taxperiod.getAuditDetails().getCreatedBy());
				ps.setString(8, taxperiod.getAuditDetails().getLastModifiedBy());
				ps.setLong(9, taxperiod.getAuditDetails().getCreatedTime());
				ps.setLong(10, taxperiod.getAuditDetails().getLastModifiedTime());
				ps.setString(11, tenantId);
				return ps;
			}

		};
		jdbcTemplate.update(psc);

	}

	/**
	 * This will search the tax periods with the given paramters
	 * 
	 * @param tenantId
	 * @param validDate
	 * @param code
	 * @return {@link TaxPeriod} List of taxPeriods
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public List<TaxPeriod> searchTaxPeriod(String tenantId, String validDate, String code) {

		String searchQuery = TaxPeriodBuilder.getSearchQuery(tenantId, validDate, code);

		List<TaxPeriod> taxPeriods = null;
		try {
			taxPeriods = jdbcTemplate.query(searchQuery, new BeanPropertyRowMapper(TaxPeriod.class));
		} catch (Exception e) {

		}
		return taxPeriods;
	}
}
