package org.egov.lams.repository;

import org.egov.lams.model.RentIncrementType;
import org.egov.lams.repository.builder.AgreementQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RentIncrementRepository {

    public static final Logger logger = LoggerFactory.getLogger(RentIncrementRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<RentIncrementType> getRentIncrements() {
        String query = "select * from eglams_rentincrementtype rentincrement";
        List<RentIncrementType> rentIncrements = null;
        try {
            rentIncrements = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(RentIncrementType.class));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("No records found for given criteria");
        }
        return rentIncrements;
    }

    public List<RentIncrementType> getRentIncrementById(Long rentID) {

        String rentIncrementTypeqQuery = AgreementQueryBuilder.RENT_INCREMENT_TYPE_QUERY;
        Map params = new HashMap();
        params.put("rentId", rentID);
        List<RentIncrementType> rentIncrements = null;
        try {
            rentIncrements = namedParameterJdbcTemplate.query(rentIncrementTypeqQuery, params, new BeanPropertyRowMapper<>(RentIncrementType.class));
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
            // FIXME put apt exception
        }
        return rentIncrements;

    }

}
