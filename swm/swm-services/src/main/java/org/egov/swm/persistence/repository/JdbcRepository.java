package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class JdbcRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcRepository.class);

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public void delete(final String tableName, final String tenantId, final String fieldName, final String fieldValue) {

        final String delQuery = "delete from " + tableName + " where tenantId = '" + tenantId + "' and " + fieldName + " = '"
                + fieldValue + "'";

        jdbcTemplate.execute(delQuery);
    }

    protected Pagination<?> getPagination(final String searchQuery, final Pagination<?> page,
            final Map<String, Object> paramValues) {

        final String countQuery = "select count(*) from (" + searchQuery + ") as x";
        final Long count = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), paramValues, Long.class);
        final Integer totalpages = (int) Math.ceil((double) count / page.getPageSize());
        page.setTotalResults(count.intValue());
        page.setTotalPages(totalpages);
        page.setCurrentPage(page.getOffset());

        return page;
    }

    protected void validateSortByOrder(final String sortBy) {

        List<String> sortByList = new ArrayList<>();
        if (sortBy.contains(","))
            sortByList = Arrays.asList(sortBy.split(","));
        else
            sortByList = Arrays.asList(sortBy);
        for (final String s : sortByList)
            if (s.contains(" ")
                    && !s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))
                throw new CustomException(s.split(" ")[0],
                        "Please send the proper sortBy order for the field " + s.split(" ")[0]);

    }

    protected void validateEntityFieldName(final String sortBy, final Class<?> object) {

        List<String> sortByList = new ArrayList<>();
        if (sortBy.contains(","))
            sortByList = Arrays.asList(sortBy.split(","));
        else
            sortByList = Arrays.asList(sortBy);
        Boolean isFieldExist = Boolean.FALSE;
        Boolean isAuditableFieldExist = Boolean.FALSE;
        for (final String s : sortByList) {
            for (int i = 0; i < object.getDeclaredFields().length; i++) {
                if (object.getDeclaredFields()[i].getName().equalsIgnoreCase("auditDetails")) {
                    for (int j = 0; j < object.getDeclaredFields()[i].getType().getDeclaredFields().length; j++) {
                        if (object.getDeclaredFields()[i].getType().getDeclaredFields()[j].getName()
                                .equals(s.contains(" ") ? s.split(" ")[0] : s)) {
                            isAuditableFieldExist = Boolean.TRUE;
                            break;
                        } else
                            isAuditableFieldExist = Boolean.FALSE;
                    }
                }
                if (object.getDeclaredFields()[i].getName().equals(s.contains(" ") ? s.split(" ")[0] : s)) {
                    isFieldExist = Boolean.TRUE;
                    break;
                } else
                    isFieldExist = Boolean.FALSE;
            }
            if (!isFieldExist && !isAuditableFieldExist)
                throw new CustomException(s.contains(" ") ? s.split(" ")[0] : s, "Please send the proper Field Names ");
        }

    }

    protected Boolean uniqueCheck(final String tableName, final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName, final String uniqueFieldValue) {

        LOG.info("Unique Checking for combination of fields tenantId & " + fieldValue);

        LOG.info("Unique Checking for " + fieldName + "=" + fieldValue + ", " + uniqueFieldName + "="
                + uniqueFieldValue);

        final Map<String, Object> paramValues = new HashMap<>();

        final StringBuffer uniqueQuery = new StringBuffer("select count(*) as count from " + tableName
                + " where tenantId=:tenantId and " + fieldName + "=:fieldValue ");

        if (uniqueFieldValue != null) {
            uniqueQuery.append(" and " + uniqueFieldName + "!=:uniqueFieldValue");
            paramValues.put("uniqueFieldValue", uniqueFieldValue);
        }

        paramValues.put("tenantId", tenantId);
        paramValues.put("fieldValue", fieldValue);

        final Long count = namedParameterJdbcTemplate.queryForObject(uniqueQuery.toString(), paramValues, Long.class);

        LOG.info("Unique Checking Query " + uniqueQuery);

        LOG.info("Record Count for combination of fields " + count);

        return count >= 1 ? false : true;

    }

    protected void addAnd(final StringBuffer params) {
        if (params.length() > 0)
            params.append(" and ");
    }

    protected void addOr(final StringBuffer params) {
        if (params.length() > 0)
            params.append(" or ");
    }
}
