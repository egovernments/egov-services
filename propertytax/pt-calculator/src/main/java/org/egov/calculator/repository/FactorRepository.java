package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.calculator.repository.builder.FactorQueryBuilder;
import org.egov.calculator.utility.TimeStampUtil;
import org.egov.enums.CalculationFactorTypeEnum;
import org.egov.models.AuditDetails;
import org.egov.models.CalculationFactor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FactorRepository {

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
	public Long saveFactor(String tenantId, CalculationFactor calculationFactor) {

		String factorInsertSql = FactorQueryBuilder.FACTOR_CREATE_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(factorInsertSql, new String[] { "id" });

				ps.setString(1, calculationFactor.getTenantId());
				ps.setString(2, calculationFactor.getFactorCode());
				ps.setString(3, calculationFactor.getFactorType().toString());
				ps.setDouble(4, getDouble(calculationFactor.getFactorValue()));
				ps.setTimestamp(5, TimeStampUtil.getTimeStamp(calculationFactor.getFromDate()));
				ps.setTimestamp(6, TimeStampUtil.getTimeStamp(calculationFactor.getToDate()));
				ps.setString(7, calculationFactor.getAuditDetails().getCreatedBy());
				ps.setString(8, calculationFactor.getAuditDetails().getLastModifiedBy());
				ps.setLong(9, getLong(calculationFactor.getAuditDetails().getCreatedTime()));
				ps.setLong(10, getLong(calculationFactor.getAuditDetails().getLastModifiedTime()));
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
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
	public void updateFactor(String tenantId, Long id, CalculationFactor calculationFactor) {

		String factorInsertSql = FactorQueryBuilder.FACTOR_UPDATE_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(factorInsertSql, new String[] { "id" });

				ps.setString(1, calculationFactor.getTenantId());
				ps.setString(2, calculationFactor.getFactorCode());
				ps.setObject(3, calculationFactor.getFactorType().toString());
				ps.setDouble(4, getDouble(calculationFactor.getFactorValue()));
				ps.setTimestamp(5, TimeStampUtil.getTimeStamp(calculationFactor.getFromDate()));
				ps.setTimestamp(6, TimeStampUtil.getTimeStamp(calculationFactor.getToDate()));
				ps.setString(7, calculationFactor.getAuditDetails().getLastModifiedBy());
				ps.setLong(8, getLong(calculationFactor.getAuditDetails().getLastModifiedTime()));
				ps.setLong(9, getLong(calculationFactor.getId()));
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
	public List<CalculationFactor> searchFactor(String tenantId, String factorType, String validDate, String code) {

		List<Object> preparedStatementValues = new ArrayList<Object>();

		String factorSearchSql = FactorQueryBuilder.getFactorSearchQuery(tenantId, factorType, validDate, code,
				preparedStatementValues);

		List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();

		calculationFactors = geCalculationFactors(factorSearchSql, preparedStatementValues);

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
	public List<CalculationFactor> getFactorsByTenantIdAndValidDate(String tenantId, String validDate) {

		List<Object> preparedStatementValues = new ArrayList<Object>();

		String factorSearchSql = FactorQueryBuilder.getFactorSearchQueryByTenantIdAndValidDate(tenantId, validDate,
				preparedStatementValues);

		List<CalculationFactor> calculationFactors = geCalculationFactors(factorSearchSql, preparedStatementValues);

		return calculationFactors;
	}

	/**
	 * This method will execute the given query & will build the Calculation
	 * factor object
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link CalculationFactor} List of calculation factor
	 */
	private List<CalculationFactor> geCalculationFactors(String query, List<Object> preparedStatementValues) {

		List<CalculationFactor> calculationFactors = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map<String, Object> row : rows) {
			CalculationFactor calculationFactor = new CalculationFactor();
			calculationFactor.setId(getLong(row.get("id")));
			calculationFactor.setTenantId(getString(row.get("tenantid")));
			calculationFactor.setFactorCode(getString(row.get("factorcode")));
			calculationFactor.setFactorType(CalculationFactorTypeEnum.fromValue(row.get("factortype").toString()));
			calculationFactor.setFactorValue(getDouble(row.get("factorvalue")));
			calculationFactor.setFromDate(getString(row.get("fromdate")));
			calculationFactor.setToDate(getString(row.get("todate")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			calculationFactor.setAuditDetails(auditDetails);

			calculationFactors.add(calculationFactor);

		}

		return calculationFactors;
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

	public void getCreatedAuditDetails(AuditDetails auditDetails, String tableName, Long id) {

		String query = FactorQueryBuilder.getCreatedAuditDetails(tableName, id);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> row : rows) {
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
		}
	}

    public List<CalculationFactor> getFactorByFactorTypeAndFactorCode(String factorType,Integer value,String tenantId, String validDate) {
        List<Object> preparedStatementValues = new ArrayList<Object>();

        String factorSearchSql = FactorQueryBuilder.getFactorsForTaxCalculation(factorType, value, tenantId, validDate,
                        preparedStatementValues);

        List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();

        calculationFactors = geCalculationFactors(factorSearchSql, preparedStatementValues);

        return calculationFactors;

        
    }
}
