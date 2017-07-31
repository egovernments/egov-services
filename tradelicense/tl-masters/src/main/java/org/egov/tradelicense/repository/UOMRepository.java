package org.egov.tradelicense.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.models.AuditDetails;
import org.egov.models.UOM;
import org.egov.tradelicense.repository.builder.UomQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository class for create/update/search UOM master
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Repository
public class UOMRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Description : this method will create UOM in database
	 * 
	 * @param UOM
	 * @return UOMId
	 */
	public Long createUom(UOM uom) {

		Long createdTime = new Date().getTime();
		String uomInsert = UomQueryBuilder.INSERT_UOM_QUERY;
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(uomInsert, new String[] { "id" });

				ps.setString(1, uom.getTenantId());
				ps.setString(2, uom.getCode());
				ps.setString(3, uom.getName());
				if(uom.getActive() != null){
					ps.setBoolean(4, uom.getActive());
				} else {
					ps.setNull(4, java.sql.Types.NULL);
				}
				ps.setString(5, uom.getAuditDetails().getCreatedBy());
				ps.setString(6, uom.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, createdTime);
				ps.setLong(8, createdTime);
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method will update UOM in database
	 * 
	 * @param UOM
	 * @return UOM
	 */
	public UOM updateUom(UOM uom) {

		Long updatedTime = new Date().getTime();
		String uomUpdateSql = UomQueryBuilder.UPDATE_UOM_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(uomUpdateSql);

				ps.setString(1, uom.getTenantId());
				ps.setString(2, uom.getCode());
				ps.setString(3, uom.getName());
				ps.setBoolean(4, uom.getActive());
				ps.setString(5, uom.getAuditDetails().getLastModifiedBy());
				ps.setLong(6, updatedTime);
				ps.setLong(7, uom.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
		return uom;
	}

	/**
	 * Description : This method for search UOM
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param active
	 * @param pageSize
	 * @param offSet
	 * @return List<UOM>
	 */
	public List<UOM> searchUom(String tenantId, Integer[] ids, String name, String code, Boolean active,
			Integer pageSize, Integer offSet) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String uomSearchQuery = UomQueryBuilder.buildSearchQuery(tenantId, ids, name, code, active, pageSize, offSet,
				preparedStatementValues);
		List<UOM> uoms = getUoms(uomSearchQuery.toString(), preparedStatementValues);

		return uoms;
	}

	/**
	 * This method will execute the given query & will build the UOM object
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link UOM} List of UOM
	 */
	private List<UOM> getUoms(String query, List<Object> preparedStatementValues) {

		List<UOM> uoms = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
		for (Map<String, Object> row : rows) {

			UOM uom = new UOM();
			uom.setId(getLong(row.get("id")));
			uom.setTenantId(getString(row.get("tenantid")));
			uom.setCode(getString(row.get("code")));
			uom.setName(getString(row.get("name")));
			uom.setActive((Boolean) row.get("active"));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			uom.setAuditDetails(auditDetails);

			uoms.add(uom);
		}

		return uoms;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? "" : object.toString();
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	@SuppressWarnings("unused")
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