package org.egov.tradelicense.persistence.repository;

import java.util.List;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.domain.model.LicenseBillSearch;
import org.egov.tradelicense.domain.repository.builder.LicenseBillQueryBuilder;
import org.egov.tradelicense.persistence.entity.LicenseBillSearchEntity;
import org.egov.tradelicense.persistence.entity.LicenseBillEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LicenseBillJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(LicenseBillJdbcRepository.class);
	static {
		LOG.debug("init LicenseBill");
		init(LicenseBillEntity.class);
		LOG.debug("end init LicenseBill");
	}

	public LicenseBillEntity create(LicenseBillEntity entity) {
		entity.setId(Long.valueOf(getNextSequenceId()));
		super.create(entity);

		return entity;
	}

	public LicenseBillEntity update(LicenseBillEntity entity) {

		super.update(entity);

		return entity;
	}

	
	@Transactional
	public void updateTradeLicenseAfterWorkFlowQuery(String applicationNumber, String status) {
	
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String insertquery = LicenseBillQueryBuilder.updateTradeLicenseAfterWorkFlowQuery(new java.util.Date().getTime(),
				applicationNumber, status, parameters);
		
		namedParameterJdbcTemplate.update(insertquery, parameters);
	}
	
	public String getNextSequenceId() {
		String seqQuery = "select nextval('" +LicenseBillEntity.SEQUENCE_NAME + "')";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		return String.valueOf(namedParameterJdbcTemplate.queryForObject(seqQuery, parameters, Long.class));
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

	public List<LicenseBillEntity> search(LicenseBillSearch licenseBillSearch) {
		
		MapSqlParameterSource paramValues = new MapSqlParameterSource();
		String searchQuery = buildBillSearchQuery(licenseBillSearch, paramValues);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<LicenseBillEntity> licenseBills = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, new BeanPropertyRowMapper(LicenseBillEntity.class));

		return licenseBills;
	}


	private String buildBillSearchQuery(LicenseBillSearch domain, MapSqlParameterSource paramValues) {

		final LicenseBillSearchEntity licenseBillSearchEntity = new LicenseBillSearchEntity();
		licenseBillSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  ";

		final StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", LicenseBillSearchEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specific search
		if (licenseBillSearchEntity.getTenantId() != null) {

			params.append("tenantId =:tenantId");
			paramValues.addValue("tenantId", licenseBillSearchEntity.getTenantId());

		}

		if (licenseBillSearchEntity.getApplicationId() != null) {

			params.append(" AND applicationId =:applicationId");
			paramValues.addValue("applicationId", licenseBillSearchEntity.getApplicationId());

		}

		if (licenseBillSearchEntity.getId() != null) {

			params.append(" AND id =:id");
			paramValues.addValue("id", licenseBillSearchEntity.getId());

		}

		searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		return searchQuery.toString();
	}

}