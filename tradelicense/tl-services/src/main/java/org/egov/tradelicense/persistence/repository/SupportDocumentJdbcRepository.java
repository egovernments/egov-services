package org.egov.tradelicense.persistence.repository;

import java.util.List;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.domain.model.SupportDocumentSearch;
import org.egov.tradelicense.persistence.entity.LicenseSearchEntity;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
import org.egov.tradelicense.persistence.entity.SupportDocumentSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class SupportDocumentJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(SupportDocumentJdbcRepository.class);
	
	static {
		LOG.debug("init SupportDocument");
		init(SupportDocumentEntity.class);
		LOG.debug("end init SupportDocument");
	}

	public SupportDocumentEntity create(SupportDocumentEntity entity) {

		super.create(entity);

		return entity;
	}

	public SupportDocumentEntity update(SupportDocumentEntity entity) {

		super.update(entity);

		return entity;
	}

	public List<SupportDocumentEntity> search(SupportDocumentSearch domain) {

		MapSqlParameterSource paramValues = new MapSqlParameterSource();
		String searchQuery = buildSupportDocumentSearchQuery(domain, paramValues);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<SupportDocumentEntity> tradeSupportDocuments = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, new BeanPropertyRowMapper(SupportDocumentEntity.class));

		return tradeSupportDocuments;
	}

	private String buildSupportDocumentSearchQuery(SupportDocumentSearch domain, MapSqlParameterSource paramValues) {

		final SupportDocumentSearchEntity supportDocumentSearchEntity = new SupportDocumentSearchEntity();
		supportDocumentSearchEntity.toEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  ";

		final StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", SupportDocumentSearchEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specific search
		if (supportDocumentSearchEntity.getTenantId() != null) {

			params.append("tenantId =:tenantId");
			paramValues.addValue("tenantId", supportDocumentSearchEntity.getTenantId());

		}

		if (supportDocumentSearchEntity.getApplicationId() != null) {

			params.append(" AND applicationId =:applicationId");
			paramValues.addValue("applicationId", supportDocumentSearchEntity.getApplicationId());

		}

		if (supportDocumentSearchEntity.getId() != null) {

			params.append(" AND id =:id");
			paramValues.addValue("id", supportDocumentSearchEntity.getId());

		}

		searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		return searchQuery.toString();
	}

	public List<SupportDocumentEntity> findByApplicationId(Long applicationId) {

		MapSqlParameterSource parameter = new MapSqlParameterSource();
		StringBuffer searchQuery = new StringBuffer();
		searchQuery.append("select * from egtl_support_document where ");
		searchQuery.append(" applicationid = :applicationid ");
		parameter.addValue("applicationid", applicationId.intValue());
		List<SupportDocumentEntity> tradeSupportDocuments = namedParameterJdbcTemplate.query(searchQuery.toString(),
				parameter, new BeanPropertyRowMapper(SupportDocumentEntity.class));

		if (tradeSupportDocuments.isEmpty()) {

			return null;

		} else {

			return tradeSupportDocuments;
		}
	}
}