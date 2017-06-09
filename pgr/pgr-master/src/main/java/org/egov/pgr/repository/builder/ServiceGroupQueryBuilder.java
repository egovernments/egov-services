/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.repository.builder;

import java.util.List;

import org.egov.pgr.web.contract.ServiceGroupGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceGroupQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ServiceGroupQueryBuilder.class);

    private static final String BASE_QUERY = "SELECT id, code, description, tenantId "
            + " FROM egpgr_complainttype_category ";

    @SuppressWarnings("rawtypes")
    public String getQuery(final ServiceGroupGetRequest categoryGetRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, categoryGetRequest);
        addOrderByClause(selectQuery, categoryGetRequest);
        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final ServiceGroupGetRequest serviceGroupRequest) {

        if (serviceGroupRequest.getId() == null && serviceGroupRequest.getName() == null
                && serviceGroupRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (serviceGroupRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" tenantId = ?");
            preparedStatementValues.add(serviceGroupRequest.getTenantId());
        }

        if (serviceGroupRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" id IN " + getIdQuery(serviceGroupRequest.getId()));
        }

        if (serviceGroupRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" name = ?");
            preparedStatementValues.add(serviceGroupRequest.getName());
        }

    }

    private void addOrderByClause(final StringBuilder selectQuery, final ServiceGroupGetRequest serviceGroupRequest) {
        final String sortBy = serviceGroupRequest.getSortBy() == null ? "category.code"
                : "category." + serviceGroupRequest.getSortBy();
        final String sortOrder = serviceGroupRequest.getSortOrder() == null ? "DESC" : serviceGroupRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }


    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

    public static String insertCategoryQuery() {
        return "INSERT INTO egpgr_complainttype_category(name,description,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid,version) values "
                + "(?,?,?,?,?,?,?,?)";
    }


}
