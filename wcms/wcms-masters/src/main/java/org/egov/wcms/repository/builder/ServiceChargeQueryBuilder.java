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

import org.egov.wcms.web.contract.ServiceChargeGetRequest;
import org.springframework.stereotype.Component;

@Component
public class ServiceChargeQueryBuilder {

    public static final String BASE_QUERY = "Select sc.id as sc_id,sc.code as sc_code,sc.servicetype "
            + "as sc_servicetype,sc.servicechargeapplicable as sc_servicechargeapplicable,"
            + "sc.servicechargetype as sc_servicechargetype,sc.description as sc_description,"
            + "sc.active as sc_active,sc.effectivefrom as sc_effectivefrom,sc.effectiveto"
            + " as sc_effectiveto,sc.outsideulb as sc_outsideulb,sc.tenantid as sc_tenantid,"
            + "sc.createdby as sc_createdby,sc.createddate as sc_createddate,sc.lastmodifiedby"
            + " as sc_lastmodifiedby,sc.lastmodifieddate as sc_lastmodifieddate from"
            + " egwtr_servicecharge sc";

    public String getServiceChargeDetailsQuery() {
        return "Select scd.id as scd_id,scd.code as scd_code,"
                + "scd.uomfrom as scd_uomfrom,scd.uomto as scd_uomto,"
                + "scd.amountorpercentage as scd_amountorpercentage,scd.servicecharge as scd_servicecharge,"
                + "scd.tenantid as scd_tenantid from egwtr_servicecharge_details scd"
                + " where scd.servicecharge = :servicecharge and scd.tenantid = :tenantid";
    }

    public String getQuery(final ServiceChargeGetRequest serviceChargeGetRequest,
            final Map<String, Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(serviceChargeGetRequest, preparedStatementValues, selectQuery);
        addOrderByClause(serviceChargeGetRequest, selectQuery);
        return selectQuery.toString();
    }

    private void addOrderByClause(final ServiceChargeGetRequest serviceChargeGetRequest,
            final StringBuilder selectQuery) {
        final String sortBy = serviceChargeGetRequest.getSortBy() == null ? "sc.id"
                : "sc." + serviceChargeGetRequest.getSortBy();
        final String sortOrder = serviceChargeGetRequest.getSortOrder() == null ? "DESC" : serviceChargeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    private void addWhereClause(final ServiceChargeGetRequest serviceChargeGetRequest,
            final Map<String, Object> preparedStatementValues,
            final StringBuilder selectQuery) {
        if (serviceChargeGetRequest.getTenantId() == null)
            return;
        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (serviceChargeGetRequest.getIds() != null) {
            isAppendAndClause = true;
            selectQuery.append(" sc.id IN (:ids)");
            preparedStatementValues.put("ids", serviceChargeGetRequest.getIds());
        }
        if (serviceChargeGetRequest.getTenantId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" sc.tenantid = :tenantId");
            preparedStatementValues.put("tenantId", serviceChargeGetRequest.getTenantId());
        }
        if (serviceChargeGetRequest.getServiceType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" sc.servicetype = :serviceType");
            preparedStatementValues.put("serviceType", serviceChargeGetRequest.getServiceType());
        }
        if (serviceChargeGetRequest.getServiceChargeType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" sc.servicechargetype = :serviceChargeType");
            preparedStatementValues.put("serviceChargeType", serviceChargeGetRequest.getServiceChargeType());
        }
        if (serviceChargeGetRequest.getOutsideUlb() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" sc.outsideulb = :outsideUlb");
            preparedStatementValues.put("outsideUlb", serviceChargeGetRequest.getOutsideUlb());
        }
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

    public String insertServiceChargeData() {
        return "Insert into egwtr_servicecharge (id,code,servicetype,servicechargeapplicable,servicechargetype"
                + ",description,active,effectivefrom,effectiveto,outsideulb,tenantid,createdby,createddate,lastmodifiedby,"
                + "lastmodifieddate) values (:id,:code,:servicetype,:servicechargeapplicable,:servicechargetype"
                + ",:description,:active,:effectivefrom,:effectiveto,:outsideulb,:tenantid,:createdby,:createddate,:lastmodifiedby,"
                + ":lastmodifieddate)";
    }

    public String insertServiceChargeDetailsData() {
        return "Insert into egwtr_servicecharge_details (id,code,uomfrom,uomto,amountorpercentage,servicecharge,tenantid)"
                + " values(:id,:code,:uomfrom,:uomto,:amountorpercentage,:servicecharge,:tenantid)";
    }

    public String updateServiceChargeData() {
        return "Update egwtr_servicecharge set servicetype =:servicetype,servicechargeapplicable =:servicechargeapplicable,"
                + "servicechargetype =:servicechargetype,description =:description,active =:active,effectivefrom =:effectivefrom,"
                + "effectiveto =:effectiveto,outsideulb =:outsideulb,lastmodifiedby =:lastmodifiedby,lastmodifieddate"
                + " =:lastmodifieddate where code =:code and tenantid =:tenantid";
    }

    public String deleteServiceChargeDetailsData() {
        return "Delete from egwtr_servicecharge_details where servicecharge =:servicecharge and tenantid =:tenantid";
    }

}
