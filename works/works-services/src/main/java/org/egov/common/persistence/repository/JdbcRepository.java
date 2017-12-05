package org.egov.common.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class JdbcRepository {
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void validateSortByOrder(final String sortBy) {
        List<String> sortByList = new ArrayList<String>();
        Map<String, String> messages = new HashMap<>();
        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }
        for (String s : sortByList) {
            if (s.contains(" ")
                    && (!s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))) {
                messages.put(s.split(" ")[0], "Please send the proper sortBy order for the field " + s.split(" ")[0]);
                throw new CustomException(messages);
            }
        }

    }

    public void validateEntityFieldName(String sortBy, final Class<?> object) {
        Map<String, String> messages = new HashMap<>();
        List<String> sortByList = new ArrayList<String>();
        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }
        Boolean isFieldExist = Boolean.FALSE;
        for (String s : sortByList) {
            for (int i = 0; i < object.getDeclaredFields().length; i++) {
                if (object.getDeclaredFields()[i].getName().equals(s.contains(" ") ? s.split(" ")[0] : s)) {
                    isFieldExist = Boolean.TRUE;
                    break;
                } else {
                    isFieldExist = Boolean.FALSE;
                }
            }
            if (!isFieldExist) {
                messages.put(s.contains(" ") ? s.split(" ")[0] : s, "Please send the proper Field Names ");
                throw new CustomException(messages);
            }
        }

    }

    protected void addAnd(StringBuilder params) {
        if (params.length() > 0) {
            params.append(" and ");
        }
    }

}
