package org.egov.tradelicense.persistence.repository;

import java.util.List;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.domain.model.TradePartnerSearch;
import org.egov.tradelicense.persistence.entity.TradePartnerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class TradePartnerJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TradePartnerJdbcRepository.class);

	static {
		LOG.debug("init SupportDocument");
		init(TradePartnerEntity.class);
		LOG.debug("end init SupportDocument");
	}

	public TradePartnerEntity create(TradePartnerEntity entity) {

		super.create(entity);

		return entity;
	}

	public TradePartnerEntity update(TradePartnerEntity entity) {

		super.update(entity);

		return entity;
	}

	public List<TradePartnerEntity> search(TradePartnerSearch domain) {

		MapSqlParameterSource paramValues = new MapSqlParameterSource();
		String searchQuery = buildLicensePartnerSearchQuery(domain, paramValues);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TradePartnerEntity> tradePartnerDetails = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, new BeanPropertyRowMapper(TradePartnerEntity.class));

		return tradePartnerDetails;
	}

	private String buildLicensePartnerSearchQuery(TradePartnerSearch domain, MapSqlParameterSource paramValues) {

		final TradePartnerEntity tradePartnerEntity = new TradePartnerEntity();
		tradePartnerEntity.toSearchEntity(domain);

		String searchQuery = "select :selectfields from :tablename :condition  ";

		final StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", TradePartnerEntity.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specific search
		if (tradePartnerEntity.getTenantId() != null) {

			params.append("tenantId =:tenantId");
			paramValues.addValue("tenantId", tradePartnerEntity.getTenantId());

		}

		if (tradePartnerEntity.getLicenseId() != null) {

			params.append(" AND licenseId =:licenseId");
			paramValues.addValue("licenseId", tradePartnerEntity.getLicenseId());

		}

		if (tradePartnerEntity.getId() != null) {

			params.append(" AND id =:id");
			paramValues.addValue("id", tradePartnerEntity.getId());

		}

		searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		return searchQuery.toString();
	}
}