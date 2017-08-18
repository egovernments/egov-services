package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.calculator.repository.builder.GuidanceValueBuilder;
import org.egov.calculator.utility.TimeStampUtil;
import org.egov.models.AuditDetails;
import org.egov.models.GuidanceValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Description : CalculatorRepostory class
 * 
 * @author Anil Kumar S
 *
 */
@Repository
public class GuidanceValueRepostory {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Description: save guidance for tax calculate
	 * 
	 * @param tenantId
	 * @param guidanceValue
	 * @return
	 */
	public Long saveGuidanceValue(String tenantId, GuidanceValue guidanceValue) {
		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
				// TODO Auto-generated method stub
				final PreparedStatement ps = con.prepareStatement(GuidanceValueBuilder.INSERT_GUIDANCEVALUE_QUERY,
						new String[] { "id" });
				ps.setString(1, guidanceValue.getTenantId());
				ps.setString(2, guidanceValue.getName());
				ps.setString(3, guidanceValue.getBoundary());
				ps.setString(4, guidanceValue.getStructure());
				ps.setString(5, guidanceValue.getUsage());
				ps.setString(6, guidanceValue.getSubUsage());
				ps.setString(7, guidanceValue.getOccupancy());
				ps.setDouble(8, getDouble(guidanceValue.getValue()));
				ps.setTimestamp(9, TimeStampUtil.getTimeStamp(guidanceValue.getFromDate()));
				ps.setTimestamp(10, TimeStampUtil.getTimeStamp(guidanceValue.getToDate()));
				ps.setString(11, guidanceValue.getAuditDetails().getCreatedBy());
				ps.setString(12, guidanceValue.getAuditDetails().getLastModifiedBy());
				ps.setLong(13, getLong(guidanceValue.getAuditDetails().getCreatedTime()));
				ps.setLong(14, getLong(guidanceValue.getAuditDetails().getLastModifiedTime()));

				return ps;
			}
		};
		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);
		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description: update guidance value in tax caluculation
	 * 
	 * @param tenantId
	 * @param guidanceValue
	 * @param id
	 */
	public void udpateGuidanceValue(String tenantId, GuidanceValue guidanceValue) {
		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
				// TODO Auto-generated method stub
				final PreparedStatement ps = con.prepareStatement(GuidanceValueBuilder.UPDATE_GUIDANCEVALUE_QUERY,
						new String[] { "id" });
				ps.setString(1, guidanceValue.getTenantId());
				ps.setString(2, guidanceValue.getName());
				ps.setString(3, guidanceValue.getBoundary());
				ps.setString(4, guidanceValue.getStructure());
				ps.setString(5, guidanceValue.getUsage());
				ps.setString(6, guidanceValue.getSubUsage());
				ps.setString(7, guidanceValue.getOccupancy());
				ps.setDouble(8, getDouble(guidanceValue.getValue()));
				ps.setTimestamp(9, TimeStampUtil.getTimeStamp(guidanceValue.getFromDate()));
				ps.setTimestamp(10, TimeStampUtil.getTimeStamp(guidanceValue.getToDate()));
				ps.setString(11, guidanceValue.getAuditDetails().getCreatedBy());
				ps.setString(12, guidanceValue.getAuditDetails().getLastModifiedBy());
				ps.setLong(13, getLong(guidanceValue.getAuditDetails().getCreatedTime()));
				ps.setLong(14, getLong(guidanceValue.getAuditDetails().getLastModifiedTime()));
				ps.setLong(15, getLong(guidanceValue.getId()));

				return ps;
			}
		};
		jdbcTemplate.update(psc);

	}

	/**
	 * Description: search guidance
	 * 
	 * @param tenantId
	 * @param boundary
	 * @param validDate
	 * @return List<GuidanceValue>
	 */
	public List<GuidanceValue> searchGuidanceValue(String tenantId, String boundary, String structure, String usage,
			String subUsage, String occupancy, String validDate) {

		List<Object> preparedStatementValues = new ArrayList<Object>();
		String guidanceValueSearchSql = GuidanceValueBuilder.getGuidanceValueSearchQuery(tenantId, boundary, structure,
				usage, subUsage, occupancy, validDate, preparedStatementValues);

		List<GuidanceValue> guidanceValues = new ArrayList<GuidanceValue>();

		guidanceValues = getGuidanceValues(guidanceValueSearchSql, preparedStatementValues);

		return guidanceValues;

	}

	/**
	 * This method will execute the given query & will build the GuidanceValue
	 * object
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link GuidanceValue} List of GuidanceValue
	 */
	@SuppressWarnings("rawtypes")
	public List<GuidanceValue> getGuidanceValues(String query, List<Object> preparedStatementValues) {

		List<GuidanceValue> guidanceValues = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map row : rows) {
			GuidanceValue guidanceValue = new GuidanceValue();
			guidanceValue.setId(getLong(getString(row.get("id"))));
			guidanceValue.setTenantId(getString(row.get("tenantid")));
			guidanceValue.setName(getString(row.get("name")));
			guidanceValue.setBoundary(getString(row.get("boundary")));
			guidanceValue.setStructure(getString(row.get("structure")));
			guidanceValue.setUsage(getString(row.get("usage")));
			guidanceValue.setSubUsage(getString(row.get("subusage")));
			guidanceValue.setOccupancy(getString(row.get("occupancy")));
			guidanceValue.setValue(getDouble(row.get("value")));
			guidanceValue.setFromDate(getString(row.get("fromdate")));
			guidanceValue.setToDate(getString(row.get("todate")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			guidanceValue.setAuditDetails(auditDetails);

			guidanceValues.add(guidanceValue);

		}

		return guidanceValues;
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
