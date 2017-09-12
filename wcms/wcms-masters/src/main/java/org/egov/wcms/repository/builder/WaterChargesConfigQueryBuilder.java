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
package org.egov.wcms.repository.builder;

import java.util.Map;

import org.egov.wcms.web.contract.WaterChargesConfigGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WaterChargesConfigQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(WaterChargesConfigQueryBuilder.class);

    private static final String BASE_QUERY = "SELECT c.keyname as key, cv.value as value "
            + "FROM egwtr_configuration c JOIN egwtr_configurationvalues cv ON c.id = cv.keyid";

    @SuppressWarnings("rawtypes")
    public String getQuery(final WaterChargesConfigGetRequest waterchargeConfigGetRequest,
            final Map<String, Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, waterchargeConfigGetRequest);
        addOrderByClause(selectQuery, waterchargeConfigGetRequest);

        logger.info("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final Map<String, Object> preparedStatementValues,
            final WaterChargesConfigGetRequest waterchargeConfigGetRequest) {
        if (waterchargeConfigGetRequest.getId() == null && waterchargeConfigGetRequest.getEffectiveFrom() == null
                && waterchargeConfigGetRequest.getName() == null && waterchargeConfigGetRequest.getTenantId() == null)
            return;
        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;
        if (waterchargeConfigGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" cv.tenantId = :tenantId");
            preparedStatementValues.put("tenantId", waterchargeConfigGetRequest.getTenantId());
        }
        if (waterchargeConfigGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" c.id IN (:ids)");
            preparedStatementValues.put("ids", waterchargeConfigGetRequest.getId());
        }
        if (waterchargeConfigGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" c.keyname = :keyname");
            preparedStatementValues.put("keyname", waterchargeConfigGetRequest.getName());
        }
        if (waterchargeConfigGetRequest.getEffectiveFrom() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" cv.effectiveFrom = :effectiveFrom");
            preparedStatementValues.put("effectiveFrom", waterchargeConfigGetRequest.getEffectiveFrom());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final WaterChargesConfigGetRequest waterchargeConfigGetRequest) {
        final String sortBy = waterchargeConfigGetRequest.getSortBy() == null ? "keyname"
                : waterchargeConfigGetRequest.getSortBy();
        final String sortOrder = waterchargeConfigGetRequest.getSortOrder() == null ? "ASC"
                : waterchargeConfigGetRequest.getSortOrder();
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

}
