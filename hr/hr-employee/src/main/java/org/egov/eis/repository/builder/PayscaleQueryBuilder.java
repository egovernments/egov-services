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
import org.egov.eis.web.contract.PayscaleGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayscaleQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PayscaleQueryBuilder.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    public static String selectPayscaleHeaderByNameQuery() {
        return " select * from egeis_payscaleheader where upper(payscale) = ? and tenantId = ? ";
    }

    public static String selectPayscaleHeaderByNameAndIdNotInQuery() {
        return " select * from egeis_payscaleheader where upper(payscale) = ? and tenantId = ? and id != ? ";
    }

    private static final String BASE_QUERY = "SELECT ph.id AS ph_id, ph.payscale AS ph_payscale, ph.paycommission AS ph_paycommission,"
            + " ph.amountfrom AS ph_amountFrom,  ph.amountto AS ph_amountTo, ph.createdby AS ph_createdBy,"
            + " ph.createddate AS ph_createdDate, ph.lastmodifiedby AS ph_lastmodifiedBy, ph.lastmodifieddate AS ph_lastmodifieddate,"
            + " ph.tenantId AS ph_tenantId, pd.id AS pd_id, pd.basicfrom AS pd_basicFrom, pd.basicto AS pd_basicTo,"
            + " pd.increment AS pd_increment from egeis_payscaleheader ph LEFT JOIN egeis_payscaledetails pd ON ph.id = pd.payscaleheaderid ";


    @SuppressWarnings("rawtypes")
    public String getQuery(final PayscaleGetRequest payscaleGetRequest,
                           final List preparedStatementValues, final RequestInfo requestInfo) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, payscaleGetRequest, requestInfo);
        addOrderByClause(selectQuery, payscaleGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, payscaleGetRequest);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                final PayscaleGetRequest payscaleGetRequest, final RequestInfo requestInfo) {

        if (payscaleGetRequest.getTenantId() != null) {
            selectQuery.append(" and ph.tenantId = ?");
            preparedStatementValues.add(payscaleGetRequest.getTenantId());
        }
        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (payscaleGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" pd.tenantId = ?");
            preparedStatementValues.add(payscaleGetRequest.getTenantId());
        }

        if (payscaleGetRequest.getId() != null && !payscaleGetRequest.getId().isEmpty()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ph.id IN " + getIdQuery(payscaleGetRequest.getId()));
        }

        if (payscaleGetRequest.getPaycommission() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ph.paycommission = ?");
            preparedStatementValues.add(payscaleGetRequest.getPaycommission());
        }

        if (payscaleGetRequest.getPayscale() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ph.payscale = ?");
            preparedStatementValues.add(payscaleGetRequest.getPayscale());
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
                                  final PayscaleGetRequest payscaleGetRequest) {
        final String sortBy = payscaleGetRequest.getSortBy() == null ? "ph.payscale"
                : payscaleGetRequest.getSortBy();
        final String sortOrder = payscaleGetRequest.getSortOrder() == null ? "ASC"
                : payscaleGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                 final PayscaleGetRequest payscaleGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
        if (payscaleGetRequest.getPageSize() != null)
            pageSize = payscaleGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (payscaleGetRequest.getPageNumber() != null)
            pageNumber = payscaleGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }
}
