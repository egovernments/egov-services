package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.calculator.repository.builder.TaxRatesBuilder;
import org.egov.calculator.utility.TimeStampUtil;
import org.egov.models.AuditDetails;
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

            String taxRatesInsert = TaxRatesBuilder.INSERT_TAXRATES_QUERY;

            final PreparedStatementCreator psc = new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                            final PreparedStatement ps = connection.prepareStatement(taxRatesInsert, new String[] { "id" });
                            ps.setString(1, taxRates.getTenantId());
                            ps.setString(2, taxRates.getTaxHead());
                            ps.setString(3, taxRates.getDependentTaxHead());
                            ps.setTimestamp(4, TimeStampUtil.getTimeStamp(taxRates.getFromDate()));
                            ps.setTimestamp(5, TimeStampUtil.getTimeStamp(taxRates.getToDate()));
                            ps.setDouble(6, getDouble(taxRates.getFromValue()));
                            ps.setDouble(7, getDouble(taxRates.getToValue()));
                            ps.setDouble(8, getDouble(taxRates.getRatePercentage()));
                            ps.setDouble(9, getDouble(taxRates.getTaxFlatValue()));
                            ps.setString(10, taxRates.getUsage());
                            ps.setString(11, taxRates.getPropertyType());
                            ps.setString(12, taxRates.getAuditDetails().getCreatedBy());
                            ps.setString(13, taxRates.getAuditDetails().getLastModifiedBy());
                            ps.setLong(14, getLong(taxRates.getAuditDetails().getCreatedTime()));
                            ps.setLong(15, getLong(taxRates.getAuditDetails().getLastModifiedTime()));
                            return ps;
                    }
            };
            final KeyHolder holder = new GeneratedKeyHolder();
            try {
            jdbcTemplate.update(psc, holder);
            }
            catch (Exception e) {
                    System.out.println(e.getMessage());
            }
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

            String taxRatesUpdate = TaxRatesBuilder.UPDATE_TAXRATES_QUERY;

            final PreparedStatementCreator psc = new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                            final PreparedStatement ps = connection.prepareStatement(taxRatesUpdate);
                            ps.setString(1, taxRates.getTenantId());
                            ps.setString(2, taxRates.getTaxHead());
                            ps.setString(3, taxRates.getDependentTaxHead());
                            ps.setTimestamp(4, TimeStampUtil.getTimeStamp(taxRates.getFromDate()));
                            ps.setTimestamp(5, TimeStampUtil.getTimeStamp(taxRates.getToDate()));
                            ps.setDouble(6, getDouble(taxRates.getFromValue()));
                            ps.setDouble(7, getDouble(taxRates.getToValue()));
                            ps.setDouble(8, getDouble(taxRates.getRatePercentage()));
                            ps.setDouble(9, getDouble(taxRates.getTaxFlatValue()));
                            ps.setString(10, taxRates.getUsage());
                            ps.setString(11, taxRates.getPropertyType());
                            ps.setString(12, taxRates.getAuditDetails().getLastModifiedBy());
                            ps.setLong(13, getLong(taxRates.getAuditDetails().getLastModifiedTime()));
                            ps.setLong(14, getLong(taxRates.getId()));
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<TaxRates> searchTaxRates(String tenantId, String taxHead, String validDate, Double validARVAmount,
                    String parentTaxHead,String usage,String propertyType) {

            List<Object> preparedStatementValues = new ArrayList<Object>();
            String taxRatesSelect = TaxRatesBuilder.getTaxRatesSearchQuery(tenantId, taxHead, validDate, validARVAmount,
                            parentTaxHead,usage,propertyType,preparedStatementValues);
            List<TaxRates> listOfTaxRates = new ArrayList();
            listOfTaxRates = geTaxRates(taxRatesSelect, preparedStatementValues);

            return listOfTaxRates;
    }



	/**
	 * This method will execute the given query & will build the TaxRates object
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link TaxRates} List of TaxRates
	 */
	@SuppressWarnings("rawtypes")
	public List<TaxRates> geTaxRates(String query, List<Object> preparedStatementValues) {

		List<TaxRates> taxRates = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map row : rows) {
			TaxRates taxRate = new TaxRates();
			taxRate.setId(getLong(row.get("id")));
			taxRate.setTenantId(getString(row.get("tenantid")));
			taxRate.setTaxHead(getString(row.get("taxhead")));
			taxRate.setDependentTaxHead(getString(row.get("dependenttaxhead")));
			taxRate.setFromDate(getString(row.get("fromdate")));
			taxRate.setToDate(getString(row.get("todate")));
			taxRate.setFromValue(getDouble(row.get("fromvalue")));
			taxRate.setToValue(getDouble(row.get("tovalue")));
			taxRate.setRatePercentage(getDouble(row.get("ratepercentage")));
			taxRate.setTaxFlatValue(getDouble(row.get("taxflatvalue")));
			taxRate.setUsage(getString(row.get("usage")));
			taxRate.setPropertyType(getString(row.get("propertyType")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong((row.get("createdtime"))));
			auditDetails.setLastModifiedTime(getLong((row.get("lastmodifiedtime"))));
			taxRate.setAuditDetails(auditDetails);

			taxRates.add(taxRate);

		}

		return taxRates;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TaxRates> searchTaxRatesByTenantAndDate(String tenantId, String validDate) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String taxRatesSelect = TaxRatesBuilder.getTaxRatesByTenantAndDate(tenantId, validDate,
				preparedStatementValues);
		List<TaxRates> listOfTaxRates = new ArrayList();
		listOfTaxRates = jdbcTemplate.query(taxRatesSelect, preparedStatementValues.toArray(),
				new BeanPropertyRowMapper(TaxRates.class));

		return listOfTaxRates;
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
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
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

}
