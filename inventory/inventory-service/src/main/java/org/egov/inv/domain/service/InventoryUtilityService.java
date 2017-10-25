package org.egov.inv.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryUtilityService {

    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public InventoryUtilityService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long getNextId(final String sequence) {

        Map<String, Object> paramValues = new HashMap<>();

        final String query = "SELECT NEXTVAL('" + sequence + "')";
        Integer result;
        try {
            result = namedParameterJdbcTemplate.queryForObject(query, paramValues, Integer.class);
            return result.longValue();
        } catch (final Exception ex) {
            throw new RuntimeException("Next id is not generated.");
        }
    }


    public List<Long> getIdList(int size, String sequenceName) {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("size", size);
        String getIdListQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1,:size)";
        List<Long> idList;
        try {
            idList = namedParameterJdbcTemplate.queryForList(getIdListQuery, paramValues, Long.class);
        } catch (Exception e) {
            throw new RuntimeException("Next id is not generated.");
        }
        return idList;
    }

}
