package org.egov.swm.persistence.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefillingPumpStationJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_refillingpumpstation";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RefillingPumpStationJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Boolean checkForUniqueRecords(String tenantId, String fieldName, String fieldValue,
                                          String uniqueFieldName, String uniqueFieldValue){
        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }
}
