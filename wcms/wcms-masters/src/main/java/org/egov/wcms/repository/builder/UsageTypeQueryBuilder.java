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

import java.util.Map;

import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.springframework.stereotype.Component;

@Component
public class UsageTypeQueryBuilder {

    public static final String BASE_USAGE_QUERY = "Select ut.id as ut_id,ut.code as ut_code,ut.name "
            + "as ut_name,ut.description as ut_description,"
            + "ut.active as ut_active,ut.parent as ut_parent,ut.tenantid as ut_tenantid,"
            + "ut.createdby as ut_createdby,ut.createddate as ut_createddate,ut.lastmodifiedby"
            + " as ut_lastmodifiedby,ut.lastmodifieddate as ut_lastmodifieddate from" + " egwtr_usage_type ut";

    public static final String BASE_SUBUSAGE_QUERY = "Select ut.id as ut_id,ut.code as ut_code,ut.name as ut_name,"
            + "uts.name as uts_name,ut.description as ut_description,"
            + "ut.active as ut_active,ut.parent as ut_parent,ut.tenantid as ut_tenantid,"
            + "ut.createdby as ut_createdby,ut.createddate as ut_createddate,ut.lastmodifiedby"
            + " as ut_lastmodifiedby,ut.lastmodifieddate as ut_lastmodifieddate from"
            + " egwtr_usage_type ut,egwtr_usage_type uts where ut.parent=uts.code";

    public String getQuery(final UsageTypeGetRequest usageTypeGetRequest,
            final Map<String, Object> preparedStatementValues) {
        final StringBuilder selectQuery;
        if (!usageTypeGetRequest.getIsSubUsageType())
            selectQuery = new StringBuilder(BASE_USAGE_QUERY);
        else
            selectQuery = new StringBuilder(BASE_SUBUSAGE_QUERY);
        addWhereClause(usageTypeGetRequest, preparedStatementValues, selectQuery);
        addOrderByClause(usageTypeGetRequest, selectQuery);
        return selectQuery.toString();
    }

    private void addOrderByClause(final UsageTypeGetRequest usageTypeGetRequest, final StringBuilder selectQuery) {
        final String sortBy = usageTypeGetRequest.getSortBy() == null ? "ut.id"
                : "ut." + usageTypeGetRequest.getSortBy();
        final String sortOrder = usageTypeGetRequest.getSortOrder() == null ? "DESC"
                : usageTypeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    private void addWhereClause(final UsageTypeGetRequest usageTypeGetRequest,
            final Map<String, Object> preparedStatementValues, final StringBuilder selectQuery) {
        if (usageTypeGetRequest.getTenantId() == null)
            return;

        boolean isAppendAndClause = false;
        if (usageTypeGetRequest.getIsSubUsageType()) {
            if (usageTypeGetRequest.getParent() != null) {
                isAppendAndClause = true;
                selectQuery.append(" AND ut.parent = :parent");
                preparedStatementValues.put("parent", usageTypeGetRequest.getParent());
            } else {
                isAppendAndClause = true;
                selectQuery.append(" AND ut.parent is not null");
            }
        } else {
            selectQuery.append(" WHERE");
            isAppendAndClause = true;
            selectQuery.append(" ut.parent is null");
        }

        if (usageTypeGetRequest.getTenantId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ut.tenantid = :tenantId");
            preparedStatementValues.put("tenantId", usageTypeGetRequest.getTenantId());
            if (usageTypeGetRequest.getIsSubUsageType()) {
                isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
                selectQuery.append(" uts.tenantid = :tenantId");
                preparedStatementValues.put("tenantId", usageTypeGetRequest.getTenantId());
            }
        }
        if (usageTypeGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ut.id IN (:ids)");
            preparedStatementValues.put("ids", usageTypeGetRequest.getIds());
        }

        if (usageTypeGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ut.name = :name");
            preparedStatementValues.put("name", usageTypeGetRequest.getName());
        }
        if (usageTypeGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ut.code = :code");
            preparedStatementValues.put("code", usageTypeGetRequest.getCode());
        }
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

    public String getUsageTypeInsertQuery() {
        return "Insert into egwtr_usage_type (id,code,name,description,parent,active,createdby,createddate,"
                + "lastModifiedby,lastmodifieddate,tenantid)values(:id,:code,:name,:description,:parent,"
                + ":active,:createdby,:createddate," + ":lastmodifiedby,:lastmodifieddate,:tenantid)";
    }

    public String getUpdateUsageTypeQuery() {
        return "Update egwtr_usage_type set name=:name,description=:description,parent=:parent,active=:active,"
                + "lastmodifiedby=:lastmodifiedby,lastmodifieddate=:lastmodifieddate where code=:code and"
                + " tenantid=:tenantid";
    }

    public String getUsageTypeIdQuery() {
        return " select id FROM egwtr_usage_type  where name= :name and tenantId = :tenantId ";
    }

    public String getUsageTypeIdQueryWithCode() {
        return " select id FROM egwtr_usage_type  where name= :name and tenantId = :tenantId" + " and code != :code";

    }

}
