/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository.builder;

import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DesignationQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DesignationQueryBuilder.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT id, name, code, description, chartOfAccounts, active, tenantId"
            + " FROM egeis_designation";

    @SuppressWarnings("rawtypes")
    public String getQuery(DesignationGetRequest designationGetRequest, List preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, designationGetRequest);
        addOrderByClause(selectQuery, designationGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, designationGetRequest);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
            DesignationGetRequest designationGetRequest) {

        if (designationGetRequest.getId() == null && designationGetRequest.getName() == null
                && designationGetRequest.getCode() == null && designationGetRequest.getActive() == null
                && designationGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (designationGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" tenantId = ?");
            preparedStatementValues.add(designationGetRequest.getTenantId());
        }

        if (designationGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" id IN " + getIdQuery(designationGetRequest.getId()));
        }

        if (designationGetRequest.getCodes() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" code IN " + getCodesQuery(designationGetRequest.getCodes()));
        }

        if (designationGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" name = ?");
            preparedStatementValues.add(designationGetRequest.getName());
        }

        if (designationGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" code = ?");
            preparedStatementValues.add(designationGetRequest.getCode());
        }

        if (designationGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" active = ?");
            preparedStatementValues.add(designationGetRequest.getActive());
        }
    }

    private void addOrderByClause(StringBuilder selectQuery,
            DesignationGetRequest designationGetRequest) {
        String sortBy = (designationGetRequest.getSortBy() == null ? "name"
                : designationGetRequest.getSortBy());
        String sortOrder = (designationGetRequest.getSortOrder() == null ? "ASC"
                : designationGetRequest.getSortOrder());
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
            DesignationGetRequest designationGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.hrSearchPageSizeDefault());
        if (designationGetRequest.getPageSize() != null)
            pageSize = designationGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (designationGetRequest.getPageNumber() != null)
            pageNumber = designationGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to pageNo * pageSize
    }

    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     * 
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
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

    private static String getCodesQuery(List<String> codeList) {
        StringBuilder query = new StringBuilder("(");
        if (codeList.size() >= 1) {
            query.append("'");
            query.append(codeList.get(0).toString());
            query.append("'");
            for (int i = 1; i < codeList.size(); i++) {
                query.append(",");
                query.append("'");
                query.append(codeList.get(i));
                query.append("'");
            }
        }
        return query.append(")").toString();
    }
}
