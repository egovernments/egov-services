package org.egov.tradelicense.persistence.repository;

import java.util.List;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.domain.model.TradeShiftSearch;
import org.egov.tradelicense.persistence.entity.TradeShiftEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class TradeShiftJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TradeShiftJdbcRepository.class);

	static {
		LOG.debug("init SupportDocument");
		init(TradeShiftEntity.class);
		LOG.debug("end init SupportDocument");
	}

	public TradeShiftEntity create(TradeShiftEntity entity) {

		super.create(entity);

		return entity;
	}

	public TradeShiftEntity update(TradeShiftEntity entity) {

		super.update(entity);

		return entity;
	}

	public List<TradeShiftEntity> search(TradeShiftSearch domain) {

		MapSqlParameterSource paramValues = new MapSqlParameterSource();
		String searchQuery = buildLicenseShiftSearchQuery(domain, paramValues);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TradeShiftEntity> tradeShiftDetails = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				new BeanPropertyRowMapper(TradeShiftEntity.class));

		return tradeShiftDetails;
	}

	private String buildLicenseShiftSearchQuery(TradeShiftSearch domain, MapSqlParameterSource paramValues) {

		final TradeShiftEntity tradeShiftEntity = new TradeShiftEntity();
		tradeShiftEntity.toSearchEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  ";

		final StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", TradeShiftEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specific search
		if (tradeShiftEntity.getTenantId() != null) {

			params.append("tenantId =:tenantId");
			paramValues.addValue("tenantId", tradeShiftEntity.getTenantId());

		}

		if (tradeShiftEntity.getLicenseId() != null) {

			params.append(" AND licenseId =:licenseId");
			paramValues.addValue("licenseId", tradeShiftEntity.getLicenseId());

		}

		if (tradeShiftEntity.getId() != null) {

			params.append(" AND id =:id");
			paramValues.addValue("id", tradeShiftEntity.getId());

		}

		searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		return searchQuery.toString();
	}
}