package org.egov.tradelicense.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.PenaltyRate;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.persistence.repository.builder.PenaltyRateQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository class for create/update/search PenaltyRate master
 * 
 * @author Pavan Kumar Kamma
 *
 */

@Repository
public class PenaltyRateRepository {

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	/**
	 * Description : this method will create PenaltyRate in database
	 * 
	 * @param tenantId
	 * @param PenaltyRate
	 * @return penaltyRateId
	 */
	public Long craeatePenaltyRate(String tenantId, PenaltyRate penaltyRate) {

		AuditDetails auditDetails = penaltyRate.getAuditDetails();
		String penaltyRateInsertQuery = PenaltyRateQueryBuilder.INSERT_PENALTY_RATE_QUERY;

		final KeyHolder holder = new GeneratedKeyHolder();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", penaltyRate.getTenantId());
		parameters.addValue("applicationType",
				penaltyRate.getApplicationType() == null ? null : penaltyRate.getApplicationType().toString());
		parameters.addValue("fromRange", penaltyRate.getFromRange());
		parameters.addValue("toRange", penaltyRate.getToRange());
		parameters.addValue("rate", penaltyRate.getRate());
		parameters.addValue("createdBy", auditDetails.getCreatedBy());
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("createdTime", auditDetails.getCreatedTime());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		// executing the insert query
		namedParameterJdbcTemplate.update(penaltyRateInsertQuery, parameters, holder, new String[] { "id" });

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method for update PenaltyRate in database
	 * 
	 * @param PenaltyRate
	 * @return PenaltyRate
	 */
	public PenaltyRate updatePenaltyRate(PenaltyRate penaltyRate) {

		AuditDetails auditDetails = penaltyRate.getAuditDetails();
		String penaltyRateUpdateQuery = PenaltyRateQueryBuilder.UPDATE_PENALTY_RATE_QUERY;

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", penaltyRate.getTenantId());
		parameters.addValue("applicationType",
				penaltyRate.getApplicationType() == null ? null : penaltyRate.getApplicationType().toString());
		parameters.addValue("fromRange", penaltyRate.getFromRange());
		parameters.addValue("toRange", penaltyRate.getToRange());
		parameters.addValue("rate", penaltyRate.getRate());
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		parameters.addValue("id", penaltyRate.getId());
		// executing the insert query
		namedParameterJdbcTemplate.update(penaltyRateUpdateQuery, parameters);

		return penaltyRate;
	}

	/**
	 * Description : this method to search PenaltyRate
	 * 
	 * @param tenantId
	 * @param ids
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return List<PenaltyRate>
	 * @throws Exception
	 */
	public List<PenaltyRate> searchPenaltyRate(String tenantId, Integer[] ids, String applicationType, Integer pageSize,
			Integer offSet) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String penaltyRateSearchQuery = PenaltyRateQueryBuilder.buildSearchQuery(tenantId, ids, applicationType,
				pageSize, offSet, parameters);
		List<PenaltyRate> penaltyRates = getPenaltyRates(penaltyRateSearchQuery.toString(), parameters);

		return penaltyRates;
	}

	/**
	 * This method will execute the given query & will build the PenaltyRate
	 * object
	 * 
	 * @param query
	 * @return {@link PenaltyRate} List of Category
	 */
	public List<PenaltyRate> getPenaltyRates(String query, MapSqlParameterSource parameters) {

		List<PenaltyRate> penaltyRates = new ArrayList<>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameters);

		for (Map<String, Object> row : rows) {
			PenaltyRate penaltyRate = new PenaltyRate();
			penaltyRate.setId(getLong(row.get("id")));
			penaltyRate.setTenantId(getString(row.get("tenantid")));
			if(row.get("applicationType") != null){
				penaltyRate.setApplicationType(ApplicationTypeEnum.fromValue(getString(row.get("applicationType"))));
			} else {
				penaltyRate.setApplicationType(null);
			}
			penaltyRate.setFromRange(getLong(row.get("fromRange")));
			penaltyRate.setToRange(getLong(row.get("toRange")));
			penaltyRate.setRate(getDouble(row.get("rate")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			penaltyRate.setAuditDetails(auditDetails);

			penaltyRates.add(penaltyRate);

		}

		return penaltyRates;
	}

	/**
	 * This method will delete the penaltyRate from Db based on id
	 */
	public Long deletePenaltyRates(Long id) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = PenaltyRateQueryBuilder.getQueryToDeletePenalty(id, parameters);
		namedParameterJdbcTemplate.update(query, parameters);
		return id;
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