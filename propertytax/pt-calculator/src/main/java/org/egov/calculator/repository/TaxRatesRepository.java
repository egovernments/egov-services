package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.calculator.repository.builder.TaxRatesBuilder;
import org.egov.calculator.util.TimeStampUtil;
import org.egov.models.TaxRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
/**
 * Description : TaxRatesRepository class
 * 
 * @author Yosodhara P
 *
 */
@Repository
public class TaxRatesRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Description: This method creates Taxrates
	 * 
	 * @param tenantId
	 * @param taxRatesRequest
	 * @return taxRatesResponse
	 * @throws Exception
	 */
	public Long createTaxRates(String tenantId, TaxRates taxRates) {

		Long createdTime = new Date().getTime();
		String taxRatesInsert = TaxRatesBuilder.INSERT_TAXRATES_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(
					final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(taxRatesInsert, new String[]{"id"});
				ps.setString(1, taxRates.getTenantId());
				ps.setString(2, taxRates.getTaxHead());
				ps.setString(3, taxRates.getDependentTaxHead());
				ps.setTimestamp(4,
						TimeStampUtil.getTimeStamp(taxRates.getFromDate()));
				ps.setTimestamp(5,
						TimeStampUtil.getTimeStamp(taxRates.getToDate()));
				ps.setDouble(6, taxRates.getFromValue());
				ps.setDouble(7, taxRates.getToValue());
				ps.setDouble(8, taxRates.getRatePercentage());
				ps.setDouble(9, taxRates.getTaxFlatValue());
				ps.setString(10, taxRates.getAuditDetails().getCreatedBy());
				ps.setString(11,
						taxRates.getAuditDetails().getLastModifiedBy());
				ps.setLong(12, createdTime);
				ps.setLong(13, createdTime);
				return ps;
			}
		};
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
		taxRates.getAuditDetails().setCreatedTime(createdTime);
		taxRates.getAuditDetails().setLastModifiedTime(createdTime);
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description: This method updates Taxrates
	 * 
	 * @param tenantId
	 * @param taxRatesRequest
	 * @return taxRatesResponse
	 * @throws Exception
	 */
	public void updateTaxRates(String tenantId, TaxRates taxRates) {

		Long updatedTime = new Date().getTime();
		String taxRatesUpdate = TaxRatesBuilder.UPDATE_TAXRATES_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(
					final Connection connection) throws SQLException {
				final PreparedStatement ps = connection
						.prepareStatement(taxRatesUpdate);
				ps.setString(1, taxRates.getTenantId());
				ps.setString(2, taxRates.getTaxHead());
				ps.setString(3, taxRates.getDependentTaxHead());
				ps.setTimestamp(4,
						TimeStampUtil.getTimeStamp(taxRates.getFromDate()));
				ps.setTimestamp(5,
						TimeStampUtil.getTimeStamp(taxRates.getToDate()));
				ps.setDouble(6, taxRates.getFromValue());
				ps.setDouble(7, taxRates.getToValue());
				ps.setDouble(8, taxRates.getRatePercentage());
				ps.setDouble(9, taxRates.getTaxFlatValue());
				ps.setString(10,
						taxRates.getAuditDetails().getLastModifiedBy());
				ps.setLong(11, updatedTime);
				ps.setLong(12, taxRates.getId());
				return ps;
			}
		};
		jdbcTemplate.update(psc);
	}

	/**
	 * Description : This method for getting taxRate details
	 * 
	 * @param tenantId
	 * @param taxHead
	 * @param validDate
	 * @param validARVAmount
	 * @param parentTaxHead
	 * @param requestInfo
	 * @return TaxRatesResponse
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<TaxRates> searchTaxRates(String tenantId, String taxHead,
			String validDate, Double validARVAmount, String parentTaxHead) {

		String taxRatesSelect = TaxRatesBuilder.getTaxRatesSearchQuery(tenantId,
				taxHead, validDate, validARVAmount, parentTaxHead);
		List<TaxRates> listOfTaxRates = new ArrayList();
		listOfTaxRates = jdbcTemplate.query(taxRatesSelect,
				new BeanPropertyRowMapper(TaxRates.class));

		return listOfTaxRates;
	}
}
