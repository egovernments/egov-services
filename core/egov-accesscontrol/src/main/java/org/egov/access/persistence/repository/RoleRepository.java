package org.egov.access.persistence.repository;


import org.egov.access.domain.model.Role;
import org.egov.access.domain.model.RoleSearchCriteria;
import org.egov.access.persistence.repository.rowmapper.RoleRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepository {

    private static final Logger logger = LoggerFactory.getLogger(RoleRepository.class);

    private JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_QUERY = "SELECT r.name as r_name, r.description as r_description from eg_ms_role r";

    public List<Role> findForCriteria(final RoleSearchCriteria roleSearchCriteria) {
        final String queryStr = getQuery(roleSearchCriteria);
        return jdbcTemplate.query(queryStr, new RoleRowMapper());
    }

    private String getQuery(RoleSearchCriteria roleSearchCriteria) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, roleSearchCriteria);
        selectQuery.append(" order by r_name");

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery,
                                final RoleSearchCriteria roleSearchCriteria) {

        if (roleSearchCriteria != null && (roleSearchCriteria.getCodes() == null || roleSearchCriteria.getCodes().isEmpty()))
            return;

        if (roleSearchCriteria.getCodes() != null) {
            selectQuery.append(" WHERE r.code in ").append(getListQuery(roleSearchCriteria.getCodes()));
        }
    }

    private static String getListQuery(List<String> list) {
        StringBuilder query = new StringBuilder("(");
        if (list.size() >= 1) {
            query.append("'").append(list.get(0)).append("'");
            for (int i = 1; i < list.size(); i++) {
                query.append(", ").append("'").append(list.get(i)).append("'");
            }
        }
        return query.append(")").toString();
    }
}
