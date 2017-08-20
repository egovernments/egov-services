package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.calculator.repository.builder.TaxPeriodBuilder;
import org.egov.calculator.utility.TimeStampUtil;
import org.egov.models.AuditDetails;
import org.egov.models.TaxPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
@SuppressWarnings({ "unchecked", "rawtypes" })
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
	public Long saveTaxPeriod(TaxPeriod taxperiod, String tenantId) {

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
				ps.setLong(9, getLong(taxperiod.getAuditDetails().getCreatedTime()));
				ps.setLong(10, getLong(taxperiod.getAuditDetails().getLastModifiedTime()));
				return ps;
			}

		};

		final KeyHolder holder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(psc, holder);
		} catch (Exception e) {
			System.out.println(e);
		}
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * This will update the tax period object ,with the given tax period for the
	 * given tenatId
	 * 
	 * @param taxperiod
	 * @param tenantId
	 */
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
				ps.setLong(9, getLong(taxperiod.getAuditDetails().getCreatedTime()));
				ps.setLong(10, getLong(taxperiod.getAuditDetails().getLastModifiedTime()));
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
	public List<TaxPeriod> searchTaxPeriod(String tenantId, String validDate, String code, String fromDate,
			String toDate, String sortTaxPeriod) {

		List<Object> preparedStatementValues = new ArrayList<Object>();
		String searchQuery = TaxPeriodBuilder.getSearchQuery(tenantId, validDate, code, fromDate, toDate, sortTaxPeriod,
				preparedStatementValues);

		List<TaxPeriod> taxPeriods = null;
		try {
			taxPeriods = geTaxPeriods(searchQuery, preparedStatementValues);
		} catch (Exception e) {

		}
		return taxPeriods;
	}

	/**
	 * This method will execute the given query & will build the TaxPeriod
	 * object
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link TaxPeriod} List of TaxPeriod
	 */
	public List<TaxPeriod> geTaxPeriods(String query, List<Object> preparedStatementValues) {

		List<TaxPeriod> taxPeriods = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map row : rows) {
			TaxPeriod taxPeriod = new TaxPeriod();
			taxPeriod.setCode(getString(row.get("code")));
			taxPeriod.setId(getLong(getString(row.get("id"))));
			taxPeriod.setFinancialYear(getString(row.get("financialYear")));
			taxPeriod.setFromDate(getString(row.get("fromdate")));
			taxPeriod.setToDate(getString(row.get("todate")));
			taxPeriod.setPeriodType(getString(row.get("periodtype")));
			taxPeriod.setTenantId(getString(row.get("tenantid")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			taxPeriod.setAuditDetails(auditDetails);

			taxPeriods.add(taxPeriod);

		}

		return taxPeriods;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	public List<TaxPeriod> searchTaxPeriodsByTenantAndDate(String tenantId, String fromDate, String toDate) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String searchQuery = TaxPeriodBuilder.getTaxperiodsByDateAndTenantId(tenantId, fromDate, toDate,
				preparedStatementValues);
		List<TaxPeriod> taxPeriods = null;

		taxPeriods = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(TaxPeriod.class));
		return taxPeriods;
	}

	public TaxPeriod getToDateForTaxCalculation(String tenantId, String date) {
		List<Object> preparedStatemtValues = new ArrayList<>();
		String searchQuery = TaxPeriodBuilder.getToDateForTaxCalculation(tenantId, date, preparedStatemtValues);
		List<TaxPeriod> taxPeriods = null;
		taxPeriods = jdbcTemplate.query(searchQuery, preparedStatemtValues.toArray(),
				new BeanPropertyRowMapper(TaxPeriod.class));
		return taxPeriods.get(0);
	}

	public TaxPeriod getFromDateForTaxCalculation(String tenantId, String date) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String searchQuery = TaxPeriodBuilder.getFromDateForTaxCalculation(tenantId, date, preparedStatementValues);
		List<TaxPeriod> taxPeriods = null;
		taxPeriods = jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(TaxPeriod.class));
		return taxPeriods.get(0);
	}
}
