package org.egov.tradelicense.persistence.repository;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.LicenseApplicationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class LicenseApplicationJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(LicenseApplicationJdbcRepository.class);
	static {
		LOG.debug("init licenseApplication");
		init(LicenseApplicationEntity.class);
		LOG.debug("end init licenseApplication");
	}

	public LicenseApplicationEntity create(LicenseApplicationEntity entity) {

		super.create(entity);

		return entity;
	}

	public LicenseApplicationEntity update(LicenseApplicationEntity entity) {

		super.update(entity);

		return entity;
	}

	public String getSequence(String seqName) {
		String seqQuery = "select nextval('" + LicenseApplicationEntity.SEQUENCE_NAME + "')";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		return String.valueOf(namedParameterJdbcTemplate.queryForObject(seqQuery, parameters, Long.class) + 1);
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
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
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
	 * This method will cast the given object to Boolean
	 * 
	 * @param object
	 *            that need to be cast to Boolean
	 * @return {@link boolean}
	 */
	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : (Boolean) object;
	}

}
