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

import org.egov.wcms.web.contract.PropertyTypeUsageTypeGetReq;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertyUsageTypeQueryBuilder {

    private static final String BASE_QUERY = "SELECT * FROM egwtr_property_usage_type proUseType";

    public String getPersistQuery() {
        return "INSERT into egwtr_property_usage_type (id,code,propertytypeid, usagetypeid, active, tenantid, createdby,lastmodifiedby,createddate,lastmodifieddate) "
                + "values (:id,:code,:propertytypeid, :usagetypeid, :active, :tenantid, :createdby,:lastmodifiedby,:createddate,:lastmodifieddate )";
    }

    public static String updatePropertyUsageQuery() {
        return "UPDATE egwtr_property_usage_type SET propertytypeid = :propertytypeid,usagetypeid = :usagetypeid,"
                + "active = :active,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code ";
    }

    @SuppressWarnings("rawtypes")
    public String getQuery(final PropertyTypeUsageTypeGetReq propUsageTypeGetRequest,
            final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, propUsageTypeGetRequest);
        addOrderByClause(selectQuery, propUsageTypeGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final PropertyTypeUsageTypeGetReq propUsageTypeGetRequest) {

        if (propUsageTypeGetRequest.getId() == null && propUsageTypeGetRequest.getPropertyType() == null &&
                propUsageTypeGetRequest.getUsageType() == null && propUsageTypeGetRequest.getTenantId() == null)
            return;
        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;
        if (propUsageTypeGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" proUseType.tenantid = ?");
            preparedStatementValues.add(propUsageTypeGetRequest.getTenantId());
        }
        if (propUsageTypeGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" proUseType.id IN " + getIdQuery(propUsageTypeGetRequest.getId()));
        }
        if (propUsageTypeGetRequest.getUsageType() != null || propUsageTypeGetRequest.getUsageCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" proUseType.usagetypeid = ?");
            preparedStatementValues.add(propUsageTypeGetRequest.getUsageTypeId());
        }

        if (propUsageTypeGetRequest.getPropertyType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" proUseType.propertytypeid = ?");
            preparedStatementValues.add(propUsageTypeGetRequest.getPropertyTypeId());
        }

        if (propUsageTypeGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" proUseType.active = ?");
            preparedStatementValues.add(propUsageTypeGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final PropertyTypeUsageTypeGetReq propUsageTypeGetRequest) {
        final String sortBy = propUsageTypeGetRequest.getSortBy() == null ? "proUseType.id"
                : "proUseType." + propUsageTypeGetRequest.getSortBy();
        final String sortOrder = propUsageTypeGetRequest.getSortOrder() == null ? "DESC"
                : propUsageTypeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
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

    public static String selectPropertyByUsageTypeQuery() {
        return " select code FROM egwtr_property_usage_type where propertytypeid = ? and usagetypeid = ? and tenantId = ?";
    }

    public static String selectPropertyByUsageTypeNotInQuery() {
        return " select code from egwtr_property_usage_type where propertytypeid = ? and usagetypeid = ? and tenantId = ? and code != ? ";
    }

}