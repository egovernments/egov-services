package org.egov.access.persistence.repository;


import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.egov.access.persistence.repository.rowmapper.ActionRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.List;

@Repository
public class ActionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ActionRepository.class);

    private JdbcTemplate jdbcTemplate;

    public ActionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BASE_QUERY = "SELECT a.id AS a_id, a.name AS a_name, a.url AS "
            + "a_url, a.servicecode AS a_servicecode, a.queryparams AS a_queryparams, a.parentmodule AS a_parentmodule, a.displayname AS a_displayname, a.enabled AS a_enabled, " +
            " a.createdby AS a_createdby, a.createddate AS a_createddate, a.lastmodifiedby"
            + " AS a_lastmodifiedby, a.lastmodifieddate AS a_lastmodifieddate,a.ordernumber AS a_ordernumber, a.tenantId AS a_tenantId, "
            + "ra.actionid AS ra_action, ra.roleid AS ra_role FROM eg_action AS a JOIN eg_roleaction AS ra ON a.id = ra.actionid";

    public List<Action> findForCriteria(final ActionSearchCriteria actionSearchCriteria) throws ParseException {
        final String queryStr = getQuery(actionSearchCriteria);
        final List<Action> actions = jdbcTemplate.query(queryStr,
                new ActionRowMapper());
        return actions;
    }

    private String getQuery(ActionSearchCriteria actionSearchCriteria) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, actionSearchCriteria);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery,
                                  final ActionSearchCriteria actionSearchCriteria) {

        if (actionSearchCriteria != null && actionSearchCriteria.getRoleIds() == null && actionSearchCriteria.getRoleIds().isEmpty())
            return;

        selectQuery.append(" WHERE");

        if (actionSearchCriteria.getRoleIds() != null) {
            selectQuery.append(" ra.roleid in "+getIdQuery(actionSearchCriteria.getRoleIds()));
        }
    }

    private static String getIdQuery(List<Long> idList) {
        StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++) {
                query.append(", " + idList.get(i));
            }
        }
        return query.append(")").toString();
    }
}
