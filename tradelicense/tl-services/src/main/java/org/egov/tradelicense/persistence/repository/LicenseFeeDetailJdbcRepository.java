package org.egov.tradelicense.persistence.repository;

import java.util.List;

import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class LicenseFeeDetailJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(LicenseFeeDetailJdbcRepository.class);
	static {
		LOG.debug("init SupportDocument");
		init(LicenseFeeDetailEntity.class);
		LOG.debug("end init SupportDocument");
	}

	public LicenseFeeDetailEntity create(LicenseFeeDetailEntity entity) {

		super.create(entity);

		return entity;
	}

	public LicenseFeeDetailEntity update(LicenseFeeDetailEntity entity) {

		super.update(entity);

		return entity;
	}

	public List<LicenseFeeDetailEntity> findByApplicationId(Long applicationId) {

		MapSqlParameterSource parameter = new MapSqlParameterSource();
		StringBuffer searchQuery = new StringBuffer();
		searchQuery.append("select * from egtl_fee_details where ");
		searchQuery.append(" applicationid = :applicationid ");
		parameter.addValue("applicationid", applicationId.intValue());
		List<LicenseFeeDetailEntity> tradeFeeDetails = namedParameterJdbcTemplate.query(searchQuery.toString(),
				parameter, new BeanPropertyRowMapper(LicenseFeeDetailEntity.class));

		if (tradeFeeDetails.isEmpty()) {

			return null;

		} else {

			return tradeFeeDetails;
		}
	}
}