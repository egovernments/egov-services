package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.ReservationCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AgreementMasterRepository {

	public static final Logger logger = LoggerFactory.getLogger(AgreementMasterRepository.class);

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<ReservationCategory> getReservationCategoryList(String tenantId) {

		List<ReservationCategory> reservationCatList = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();

		String query = "select * from eglams_reservation_category where isactive=true and tenantid=:tenantId ";

		if (tenantId != null) {
			params.put("tenantId", tenantId);

		} else {
			params.put("tenantId", "default");

		}
		try {
			reservationCatList = namedParameterJdbcTemplate.query(query, params,
					new BeanPropertyRowMapper<>(ReservationCategory.class));

		} catch (Exception ex) {
			logger.info("exception occured :" + ex);
			logger.info("exception occured while fetching reservation category for tenant" + tenantId);
		}

		return reservationCatList;

	}

}
