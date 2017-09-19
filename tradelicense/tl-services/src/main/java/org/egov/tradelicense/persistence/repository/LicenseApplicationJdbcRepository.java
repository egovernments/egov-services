package org.egov.tradelicense.persistence.repository;

import java.util.List;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.domain.model.LicenseApplicationSearch;
import org.egov.tradelicense.persistence.entity.LicenseApplicationEntity;
import org.egov.tradelicense.persistence.entity.LicenseApplicationSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

	public List<LicenseApplicationEntity> search(LicenseApplicationSearch domain) {

		MapSqlParameterSource paramValues = new MapSqlParameterSource();
		String searchQuery = buildLicenseApplicationSearchQuery(domain, paramValues);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<LicenseApplicationEntity> tradeApplicationDetails = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, new BeanPropertyRowMapper(LicenseApplicationEntity.class));

		return tradeApplicationDetails;
	}

	private String buildLicenseApplicationSearchQuery(LicenseApplicationSearch domain,
			MapSqlParameterSource paramValues) {

		final LicenseApplicationSearchEntity licenseApplicationSearchEntity = new LicenseApplicationSearchEntity();
		licenseApplicationSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  ";

		final StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", LicenseApplicationSearchEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specific search
		if (licenseApplicationSearchEntity.getTenantId() != null) {

			params.append("tenantId =:tenantId");
			paramValues.addValue("tenantId", licenseApplicationSearchEntity.getTenantId());

		}

		if (licenseApplicationSearchEntity.getLicenseId() != null) {

			params.append(" AND licenseId =:licenseId");
			paramValues.addValue("licenseId", licenseApplicationSearchEntity.getLicenseId());

		}

		if (licenseApplicationSearchEntity.getId() != null) {

			params.append(" AND id =:id");
			paramValues.addValue("id", licenseApplicationSearchEntity.getId());

		}

		if (licenseApplicationSearchEntity.getApplicationNumber() != null) {

			params.append(" AND applicationNumber =:applicationNumber");
			paramValues.addValue("applicationNumber", licenseApplicationSearchEntity.getApplicationNumber());

		}

		if (licenseApplicationSearchEntity.getApplicationType() != null) {

			params.append(" AND applicationType =:applicationType");
			paramValues.addValue("applicationType", licenseApplicationSearchEntity.getApplicationType());

		}

		if (licenseApplicationSearchEntity.getStatus() != null) {

			params.append(" AND status =:status");
			paramValues.addValue("status", licenseApplicationSearchEntity.getStatus());

		}

		searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		return searchQuery.toString();
	}

	public LicenseApplicationEntity findByLicenseId(Long licenseId) {

		MapSqlParameterSource parameter = new MapSqlParameterSource();
		StringBuffer searchQuery = new StringBuffer();
		searchQuery.append("select * from egtl_license_application where ");
		searchQuery.append(" licenseid = :licenseid ");
		parameter.addValue("licenseid", licenseId.intValue());
		List<LicenseApplicationEntity> tradeLicenseApplications = namedParameterJdbcTemplate
				.query(searchQuery.toString(), parameter, new BeanPropertyRowMapper(LicenseApplicationEntity.class));

		if (tradeLicenseApplications.isEmpty()) {

			return null;

		} else {

			return tradeLicenseApplications.get(0);
		}
	}

	public String getSequence(String seqName) {

		String seqQuery = "select nextval('" + LicenseApplicationEntity.SEQUENCE_NAME + "')";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		return String.valueOf(namedParameterJdbcTemplate.queryForObject(seqQuery, parameters, Long.class) + 1);
	}

}
