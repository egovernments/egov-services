package org.egov.access.persistence.repository;


import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.egov.access.persistence.repository.rowmapper.ActionRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ActionRepository.class);

    private JdbcTemplate jdbcTemplate;

    public ActionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_QUERY = "SELECT distinct a.id AS a_id, a.name AS a_name, a.url AS "
            + "a_url, a.servicecode AS a_servicecode, a.queryparams AS a_queryparams, a.parentmodule AS a_parentmodule, a.displayname AS a_displayname, a.enabled AS a_enabled, " +
            " a.createdby AS a_createdby, a.createddate AS a_createddate, a.lastmodifiedby"
            + " AS a_lastmodifiedby, a.lastmodifieddate AS a_lastmodifieddate,a.ordernumber AS a_ordernumber, a.tenantId AS a_tenantId, "
            + "ra.actionid AS ra_action, ra.rolecode AS ra_rolecode FROM eg_action AS a JOIN eg_roleaction AS ra ON a.id = ra.actionid";

    public List<Action> findForCriteria(final ActionSearchCriteria actionSearchCriteria) throws ParseException {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(actionSearchCriteria.getTenantId());
        final String queryStr = getQuery(actionSearchCriteria);
        final List<Action> actions = jdbcTemplate.query(queryStr,preparedStatementValues.toArray(),
                new ActionRowMapper());
        return actions;
    }

    private String getQuery(ActionSearchCriteria actionSearchCriteria) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery,actionSearchCriteria);
        selectQuery.append(" order by a.name");

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery,
                                  final ActionSearchCriteria actionSearchCriteria) {

        if (actionSearchCriteria != null && actionSearchCriteria.getRoleCodes() == null && actionSearchCriteria.getRoleCodes().isEmpty())
            return;

        selectQuery.append(" WHERE ra.tenantId = a.tenantId and a.tenantId = ? ");

        if (actionSearchCriteria.getRoleCodes() != null) {
            selectQuery.append(" and ra.rolecode in "+getIdQuery(actionSearchCriteria.getRoleCodes()));
        }
    }

    private static String getIdQuery(List<String> idList) {
        StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append("'").append(idList.get(0).toString()).append("'");
            for (int i = 1; i < idList.size(); i++) {
                query.append(", ").append("'").append(idList.get(i)).append("'");
            }
        }
        return query.append(")").toString();
    }
}
