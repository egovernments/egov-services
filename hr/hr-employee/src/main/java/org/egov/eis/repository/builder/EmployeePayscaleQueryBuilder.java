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

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.EmployeePayscaleGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeePayscaleQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(EmployeePayscaleQueryBuilder.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "select ep.id as ep_id, ep.effectivefrom as ep_effectiveFrom, ep.incrementmonth as ep_incrementMonth,"
            + " ep.basicamount as ep_basicAmount, ep.reason as ep_reason, ep.createdby as ep_createdBy,"
            + " ep.createddate as ep_createdDate, ep.lastmodifiedby as ep_lastmodifiedBy, ep.lastmodifieddate as ep_lastmodifiedDate,"
            + " ep.tenantid as ep_tenantId, emp.id as emp_id, emp.code as emp_code, ph.id as ph_id, ph.paycommission as ph_paycommission,"
            + " ph.payscale as ph_payscale, ph.amountfrom as ph_amountFrom, ph.amountto as ph_amountTo from egeis_employeepayscale ep "
            + " LEFT JOIN egeis_employee emp ON ep.employeeid = emp.id and emp.tenantid = ep.tenantid "
            + " LEFT JOIN egeis_payscaleheader ph ON ep.payscaleheaderid = ph.id and ph.tenantid = ep.tenantid ";


    @SuppressWarnings("rawtypes")
    public String getQuery(final EmployeePayscaleGetRequest employeePayscaleGetRequest,
                           final List preparedStatementValues, final RequestInfo requestInfo) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, employeePayscaleGetRequest, requestInfo);
        addOrderByClause(selectQuery, employeePayscaleGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, employeePayscaleGetRequest);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                final EmployeePayscaleGetRequest employeePayscaleGetRequest, final RequestInfo requestInfo) {


        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (employeePayscaleGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" ep.tenantId = ?");
            preparedStatementValues.add(employeePayscaleGetRequest.getTenantId());
        }

        if (employeePayscaleGetRequest.getId() != null && !employeePayscaleGetRequest.getId().isEmpty()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ep.id IN " + getIdQuery(employeePayscaleGetRequest.getId()));
        }

        if (employeePayscaleGetRequest.getEmployee() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ep.employeeId = ?");
            preparedStatementValues.add(employeePayscaleGetRequest.getEmployee());
        }

        if (employeePayscaleGetRequest.getPayscaleHeader() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ep.payscaleHeaderId = ?");
            preparedStatementValues.add(employeePayscaleGetRequest.getPayscaleHeader());
        }

        if (employeePayscaleGetRequest.getEffectiveFrom() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ep.effectiveFrom = ?");
            preparedStatementValues.add(employeePayscaleGetRequest.getEffectiveFrom());
        }

        if (employeePayscaleGetRequest.getIncrementMonth() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ep.incrementMonth = ?");
            preparedStatementValues.add(employeePayscaleGetRequest.getIncrementMonth());
        }
    }

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

    private void addOrderByClause(final StringBuilder selectQuery,
                                  final EmployeePayscaleGetRequest employeePayscaleGetRequest) {
        final String sortBy = employeePayscaleGetRequest.getSortBy() == null ? "ph.payscale"
                : employeePayscaleGetRequest.getSortBy();
        final String sortOrder = employeePayscaleGetRequest.getSortOrder() == null ? "ASC"
                : employeePayscaleGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                 final EmployeePayscaleGetRequest employeePayscaleGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
        if (employeePayscaleGetRequest.getPageSize() != null)
            pageSize = employeePayscaleGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (employeePayscaleGetRequest.getPageNumber() != null)
            pageNumber = employeePayscaleGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }
}
