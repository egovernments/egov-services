/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products AS by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License AS published by
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
 *         Legal Notic
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways AS different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository.builder;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.NomineeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class NomineeQueryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NomineeQueryBuilder.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT n.id AS n_id, n.employeeId AS n_employeeId, n.name AS n_name," +
            " n.gender AS n_gender, n.dateOfBirth AS n_dateOfBirth, n.maritalStatus AS n_maritalStatus," +
            " n.relationship AS n_relationship, n.bankId AS n_bankId, n.bankBranchId AS n_bankBranchId," +
            " n.bankAccount AS n_bankAccount, n.nominated AS n_nominated, n.employed AS n_employed," +
            " n.createdBy AS n_createdBy, n.createdDate AS n_createdDate, n.lastModifiedBy AS n_lastModifiedBy," +
            " n.lastModifiedDate AS n_lastModifiedDate, n.tenantId AS n_tenantId," +
            " e.id AS e_id, e.code AS e_code" +
            " FROM egeis_nominee n" +
            " JOIN egeis_employee e ON e.id = n.employeeId AND e.tenantId = n.tenantId";

    @SuppressWarnings("rawtypes")
    public String getQuery(NomineeGetRequest nomineeGetRequest, Map<String, Object> namedParameters) {
        StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, namedParameters, nomineeGetRequest);
        addOrderByClause(selectQuery, nomineeGetRequest);
        addPagingClause(selectQuery, namedParameters, nomineeGetRequest);

        LOGGER.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(StringBuilder selectQuery, Map namedParameters,
                                NomineeGetRequest nomineeGetRequest) {
        if (isEmpty(nomineeGetRequest.getId()) && isEmpty(nomineeGetRequest.getEmployeeId())
                && isEmpty(nomineeGetRequest.getName()) && isEmpty(nomineeGetRequest.getNominated())
                && isEmpty(nomineeGetRequest.getTenantId()))
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (!isEmpty(nomineeGetRequest.getTenantId())) {
            isAppendAndClause = true;
            selectQuery.append(" n.tenantId = :tenantId");
            namedParameters.put("tenantId", nomineeGetRequest.getTenantId());
        }

        if (!isEmpty(nomineeGetRequest.getId())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" n.id IN (:ids)");
            namedParameters.put("ids", nomineeGetRequest.getId());
        }

        if (!isEmpty(nomineeGetRequest.getEmployeeId())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" n.employeeId IN (:employeeIds)");
            namedParameters.put("employeeIds", nomineeGetRequest.getEmployeeId());
        }

        if (!isEmpty(nomineeGetRequest.getNominated())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" n.nominated = :nominated");
            namedParameters.put("nominated", nomineeGetRequest.getNominated());
        }

        if (!isEmpty(nomineeGetRequest.getName())) {
            addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" n.name = :name");
            namedParameters.put("name", nomineeGetRequest.getName());
        }
    }

    private void addOrderByClause(StringBuilder selectQuery, NomineeGetRequest nomineeGetRequest) {
        String sortBy = (isEmpty(nomineeGetRequest.getSortBy()) ? "n.name" : nomineeGetRequest.getSortBy());
        String sortOrder = (isEmpty(nomineeGetRequest.getSortOrder()) ? "ASC" : nomineeGetRequest.getSortOrder());
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addPagingClause(StringBuilder selectQuery, Map namedParameters,
                                 NomineeGetRequest nomineeGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT :pageSize");
        long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
        if (!isEmpty(nomineeGetRequest.getPageSize()))
            pageSize = nomineeGetRequest.getPageSize();
        namedParameters.put("pageSize", pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET :pageNumber");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (!isEmpty(nomineeGetRequest.getPageNumber()))
            pageNumber = nomineeGetRequest.getPageNumber() - 1;
        namedParameters.put("pageNumber", pageNumber * pageSize); // Set offset to pageNo * pageSize
    }

    /**
     * This method is always called at the beginning of the method so that and
     * is prepended before the field's predicate is handled.
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
}