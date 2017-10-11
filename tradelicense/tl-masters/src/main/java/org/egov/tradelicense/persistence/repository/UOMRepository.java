package org.egov.tradelicense.persistence.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.persistence.repository.builder.UomQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	/**
	 * Description : this method will create UOM in database
	 * 
	 * @param UOM
	 * @return UOMId
	 */
	public Long createUom(UOM uom) {

		Long createdTime = new Date().getTime();
		String uomInsert = UomQueryBuilder.INSERT_UOM_QUERY;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		final KeyHolder holder = new GeneratedKeyHolder();
		parameters.addValue("tenantId", uom.getTenantId());
		parameters.addValue("code", uom.getCode());
		parameters.addValue("name", uom.getName());
		parameters.addValue("active", uom.getActive() == null ? null : uom.getActive());
		parameters.addValue("createdBy", uom.getAuditDetails().getCreatedBy());
		parameters.addValue("lastModifiedBy", uom.getAuditDetails().getLastModifiedBy());
		parameters.addValue("createdTime", createdTime);
		parameters.addValue("lastModifiedTime", createdTime);
		namedParameterJdbcTemplate.update(uomInsert, parameters, holder, new String[] { "id" });

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
		final KeyHolder holder = new GeneratedKeyHolder();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", uom.getTenantId());
		parameters.addValue("code", uom.getCode());
		parameters.addValue("name", uom.getName());
		parameters.addValue("active", uom.getActive() == null ? null : uom.getActive());
		parameters.addValue("createdBy", uom.getAuditDetails().getCreatedBy());
		parameters.addValue("lastModifiedBy", uom.getAuditDetails().getLastModifiedBy());
		parameters.addValue("createdTime", updatedTime);
		parameters.addValue("lastModifiedTime", updatedTime);
		parameters.addValue("id", uom.getId());
		namedParameterJdbcTemplate.update(uomUpdateSql, parameters, holder, new String[] { "id" });

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
	public List<UOM> searchUom(String tenantId, Integer[] ids, String name, String[] codes, String active,
			Integer pageSize, Integer offSet) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String uomSearchQuery = UomQueryBuilder.buildSearchQuery(tenantId, ids, name, codes, active, pageSize, offSet,
				parameters);
		List<UOM> uoms = getUoms(uomSearchQuery.toString(), parameters);

		return uoms;
	}

	/**
	 * This method will execute the given query & will build the UOM object
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link UOM} List of UOM
	 */
	private List<UOM> getUoms(String query, MapSqlParameterSource paremeters) {

		List<UOM> uoms = new ArrayList<>();

		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, paremeters);

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
		return object == null ? null : object.toString();
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
		return object == null ? null : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}