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

import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.springframework.stereotype.Component;

@Component
public class MeterStatusQueryBuilder {

    public static final String BASE_QUERY = "Select ms.id as ms_id,ms.code as ms_code,"
            + "ms.status as ms_status,ms.active as ms_active,ms.description as ms_description,ms.createdby as"
            + " ms_createdby,ms.createddate as ms_createddate,ms.lastmodifiedby as ms_"
            + "lastmodifiedby,ms.lastmodifieddate as ms_lastmodifieddate,ms.tenantid as"
            + " ms_tenantid FROM egwtr_meterstatus ms";

    public String getCreateMeterStatusQuery() {
        return "Insert into egwtr_meterstatus(id,code,status,active,description,createdby,createddate,"
                + "lastmodifiedby,lastmodifieddate,tenantId)"
                + " values(:id,:code,:status,:active,:description,:createdby,:createddate,"
                + ":lastmodifiedby,:lastmodifieddate,:tenantId)";
    }

    public String getUpdateMeterStatusQuery() {
        return "Update egwtr_meterstatus set status= :status,active= :active,description= :description,lastmodifiedby="
                + " :lastmodifiedby,lastmodifieddate= :lastmodifieddate where code =:code and tenantId  =:tenantId ";

    }

    public String getQuery(final MeterStatusGetRequest meterStatusGetRequest, final Map<String, Object> preparedStatementValues) {
        final StringBuilder queryString = new StringBuilder(BASE_QUERY);
        addWhereClause(meterStatusGetRequest, preparedStatementValues, queryString);
        addOrderByClause(meterStatusGetRequest, queryString);
        return queryString.toString();
    }

    private void addOrderByClause(final MeterStatusGetRequest meterStatusGetRequest, final StringBuilder queryString) {
        final String sortBy = meterStatusGetRequest.getSortBy() == null ? "ms.id"
                : "ms." + meterStatusGetRequest.getSortBy();
        final String sortOrder = meterStatusGetRequest.getSortOrder() == null ? "DESC"
                : meterStatusGetRequest.getSortOrder();
        queryString.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    private void addWhereClause(final MeterStatusGetRequest meterStatusGetRequest,
            final Map<String, Object> preparedStatementValues, final StringBuilder queryString) {
        if (meterStatusGetRequest.getTenantId() == null)
            return;
        queryString.append(" WHERE");
        boolean isAppendAndClause = false;
        if (meterStatusGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            queryString.append(" ms.tenantid = :tenantId");
            preparedStatementValues.put("tenantId", meterStatusGetRequest.getTenantId());
        }
        if (meterStatusGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, queryString);
            queryString.append(" ms.code = :code");
            preparedStatementValues.put("code", meterStatusGetRequest.getCode());
        }
        if (meterStatusGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, queryString);
            queryString.append(" ms.active = :active");
            preparedStatementValues.put("active", meterStatusGetRequest.getActive());
        }
        if (meterStatusGetRequest.getMeterStatus() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, queryString);
            queryString.append(" ms.status = :status");
            preparedStatementValues.put("status", meterStatusGetRequest.getMeterStatus());
        }
        if (meterStatusGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, queryString);
            queryString.append(" ms.id IN (:ids)");
            preparedStatementValues.put("ids", meterStatusGetRequest.getIds());
        }
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

}
