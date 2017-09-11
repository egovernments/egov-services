package org.egov.tradelicense.persistence.repository;

import java.util.List;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
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