package org.egov.calculator.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.calculator.repository.builder.GuidanceValueBuilder;
import org.egov.calculator.util.TimeStampUtil;
import org.egov.models.GuidanceValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
public class CalculatorRepostory {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Description: save guidance for tax calculate
	 * 
	 * @param tenantId
	 * @param guidanceValue
	 * @return
	 */
	public Long saveGuidanceValue(String tenantId,
			GuidanceValue guidanceValue) {
		Long createdTime = new Date().getTime();
		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(
					final Connection con) throws SQLException {
				// TODO Auto-generated method stub
				final PreparedStatement ps = con.prepareStatement(
						GuidanceValueBuilder.INSERT_GUIDANCEVALUE_QUERY,
						new String[]{"id"});
				ps.setString(1, guidanceValue.getTenantId());
				ps.setString(2, guidanceValue.getName());
				ps.setString(3, guidanceValue.getBoundary());
				ps.setString(4, guidanceValue.getStructure());
				ps.setString(5, guidanceValue.getUsage());
				ps.setString(6, guidanceValue.getSubUsage());
				ps.setString(7, guidanceValue.getOccupancy());
				ps.setDouble(8, guidanceValue.getValue());
				ps.setTimestamp(9, TimeStampUtil
						.getTimeStamp(guidanceValue.getFromDate()));
				ps.setTimestamp(10,
						TimeStampUtil.getTimeStamp(guidanceValue.getToDate()));
				ps.setString(11,
						guidanceValue.getAuditDetails().getCreatedBy());
				ps.setString(12,
						guidanceValue.getAuditDetails().getLastModifiedBy());
				ps.setLong(13, createdTime);
				ps.setLong(14, createdTime);

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
	public void udpateGuidanceValue(String tenantId,
			GuidanceValue guidanceValue) {
		Long modifiedTime = new Date().getTime();
		final PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(
					final Connection con) throws SQLException {
				// TODO Auto-generated method stub
				final PreparedStatement ps = con.prepareStatement(
						GuidanceValueBuilder.UPDATE_GUIDANCEVALUE_QUERY,
						new String[]{"id"});
				ps.setString(1, guidanceValue.getTenantId());
				ps.setString(2, guidanceValue.getName());
				ps.setString(3, guidanceValue.getBoundary());
				ps.setString(4, guidanceValue.getStructure());
				ps.setString(5, guidanceValue.getUsage());
				ps.setString(6, guidanceValue.getSubUsage());
				ps.setString(7, guidanceValue.getOccupancy());
				ps.setDouble(8, guidanceValue.getValue());
				ps.setTimestamp(9, TimeStampUtil
						.getTimeStamp(guidanceValue.getFromDate()));
				ps.setTimestamp(10,
						TimeStampUtil.getTimeStamp(guidanceValue.getToDate()));
				ps.setString(11,
						guidanceValue.getAuditDetails().getCreatedBy());
				ps.setString(12,
						guidanceValue.getAuditDetails().getLastModifiedBy());
				ps.setLong(13, modifiedTime);
				ps.setLong(14, modifiedTime);
				ps.setLong(15, guidanceValue.getId());

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
	 * @param code
	 * @return List<GuidanceValue>
	 */
	public List<GuidanceValue> searchGuidanceValue(String tenantId,
			String boundary, String structure, String usage, String subUsage,
			String occupancy, String validDate, String code) {

		String guidanceValueSearchSql = GuidanceValueBuilder
				.getGuidanceValueSearchQuery(tenantId, boundary, structure,
						usage, subUsage, occupancy, validDate, code);

		List<GuidanceValue> guidanceValues = new ArrayList<GuidanceValue>();

		guidanceValues = jdbcTemplate.query(guidanceValueSearchSql.toString(),
				new BeanPropertyRowMapper<GuidanceValue>(GuidanceValue.class));

		return guidanceValues;

	}

}
