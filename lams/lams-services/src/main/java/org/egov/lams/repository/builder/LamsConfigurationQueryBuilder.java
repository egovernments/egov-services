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

package org.egov.lams.repository.builder;

import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LamsConfigurationQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(LamsConfigurationQueryBuilder.class);


    private static final String BASE_QUERY = "SELECT ck.keyName as key, cv.value as value"
            + " FROM eglams_lamsConfiguration ck JOIN eglams_lamsConfigurationValues cv ON ck.id = cv.keyId";

    @SuppressWarnings("rawtypes")
    public String getQuery(LamsConfigurationGetRequest lamsConfigurationGetRequest, Map params) {
        StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, params, lamsConfigurationGetRequest);
        addOrderByClause(selectQuery, lamsConfigurationGetRequest);
        addPagingClause(selectQuery, params, lamsConfigurationGetRequest);

        logger.info("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(StringBuilder selectQuery, Map params,
                                LamsConfigurationGetRequest lamsConfigurationGetRequest) {

        if (lamsConfigurationGetRequest.getId() == null && lamsConfigurationGetRequest.getEffectiveFrom() == null
                && lamsConfigurationGetRequest.getName() == null && lamsConfigurationGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE ck.id is not null");

        if (lamsConfigurationGetRequest.getTenantId() != null) {
            selectQuery.append(" and ck.tenantId = :tenantId");
            params.put("tenantId", lamsConfigurationGetRequest.getTenantId());
            selectQuery.append(" and cv.tenantId = :tenantId");
            params.put("tenantId", lamsConfigurationGetRequest.getTenantId());
        }

        if (lamsConfigurationGetRequest.getId() != null) {
            selectQuery.append(" and ck.id IN (:id)");
            params.put("id", lamsConfigurationGetRequest.getId());
        }

        if (lamsConfigurationGetRequest.getName() != null) {
            selectQuery.append(" and ck.keyName = :keyName");
            params.put("keyName", lamsConfigurationGetRequest.getName());
        }

        if (lamsConfigurationGetRequest.getEffectiveFrom() != null) {
            selectQuery.append(" and cv.effectiveFrom = :effectiveFrom");
            params.put("effectiveFrom", lamsConfigurationGetRequest.getEffectiveFrom());
        }
    }

    private void addOrderByClause(StringBuilder selectQuery, LamsConfigurationGetRequest lamsConfigurationGetRequest) {
        String sortBy = (lamsConfigurationGetRequest.getSortBy() == null ? "keyName"
                : lamsConfigurationGetRequest.getSortBy());
        String sortOrder = (lamsConfigurationGetRequest.getSortOrder() == null ? "ASC"
                : lamsConfigurationGetRequest.getSortOrder());
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addPagingClause(StringBuilder selectQuery, Map params,
                                 LamsConfigurationGetRequest lamsConfigurationGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT :pageSize");
        long pageSize = 500l;
        //FIXME GET PAGESIZE FROM APPLICATION PROPERTIEs
        //Integer.parseInt(applicationProperties.lamsSearchPageSizeDefault());
        if (lamsConfigurationGetRequest.getPageSize() != null)
            pageSize = lamsConfigurationGetRequest.getPageSize();
        params.put("pageSize", pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET :pageNumber");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (lamsConfigurationGetRequest.getPageNumber() != null)
            pageNumber = lamsConfigurationGetRequest.getPageNumber() - 1;
        params.put("pageNumber", pageNumber * pageSize); // Set offset to pageNo * pageSize
    }

}
