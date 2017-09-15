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
package org.egov.wcms.repository.builder;

import java.util.List;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GapcodeQueryBuilder {

    public static String insertQuery() {
        return "INSERT INTO egwtr_gapcode(id,code,name,outSideUlb,noOfLastMonths,logic,active,description,createdBy,lastModifiedBy,createdDate,lastModifiedDate,tenantId) values "
                + "(:id,:code,:name,:outSideUlb,:noOfLastMonths,:logic,:active,:description,:createdBy,:lastUpdatedBy,:createdDate,:lastUpdatedDate,:tenantId)";
    }

    public static String updateQuery() {
        return "UPDATE egwtr_gapcode SET name = :name ,outSideUlb = :outSideUlb ,"
                + "noOfLastMonths = :noOfLastMonths,logic = :logic,description = :description,active = :active,lastModifiedBy = :lastUpdatedBy,lastModifiedDate = :lastUpdatedDate where code = :code ";
    }

    public static String getFormulaQuery() {
        return "SELECT code,name FROM egwtr_gapcode";
    }

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT id, code, name,"
            + " outSideUlb, noOfLastMonths,logic,description,active,createdDate,lastModifiedDate,createdBy,lastModifiedBy,tenantId "
            + " FROM egwtr_gapcode";

    @SuppressWarnings("rawtypes")
    public String getQuery(final GapcodeGetRequest gapcodeGetRequest,
            final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, gapcodeGetRequest);
        addOrderByClause(selectQuery, gapcodeGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, gapcodeGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery,
            final List preparedStatementValues,
            final GapcodeGetRequest gapcodeGetRequest) {

        if (gapcodeGetRequest.getCode() == null
                && gapcodeGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (gapcodeGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" tenantId = ?");
            preparedStatementValues.add(gapcodeGetRequest.getTenantId());
        }

        if (gapcodeGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause,
                    selectQuery);
            selectQuery.append(" id = ?");
            preparedStatementValues.add(gapcodeGetRequest.getIds());
        }

        if (gapcodeGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause,
                    selectQuery);
            selectQuery.append(" code = ?");
            preparedStatementValues.add(gapcodeGetRequest.getCode());
        }

        if (gapcodeGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause,
                    selectQuery);
            selectQuery.append(" name = ?");
            preparedStatementValues.add(gapcodeGetRequest.getName());
        }

    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final GapcodeGetRequest gapcodeGetRequest) {
        final String sortBy = gapcodeGetRequest.getSortBy() == null ? "code"
                : gapcodeGetRequest.getSortBy();
        final String sortOrder = gapcodeGetRequest.getSortOrder() == null ? "DESC"
                : gapcodeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery,
            final List preparedStatementValues,
            final GapcodeGetRequest gapcodeGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties
                .wcmsSearchPageSizeDefault());
        if (gapcodeGetRequest.getPageSize() != null)
            pageSize = gapcodeGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (gapcodeGetRequest.getPageNumber() != null)
            pageNumber = gapcodeGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag,
            final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }
}