package org.egov.tradelicense.persistence.repository;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.domain.repository.builder.LicenseBillQueryBuilder;
import org.egov.tradelicense.persistence.entity.LicenseBillEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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



}