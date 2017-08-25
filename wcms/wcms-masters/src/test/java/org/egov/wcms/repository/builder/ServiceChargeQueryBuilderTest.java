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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.egov.wcms.web.contract.ServiceChargeGetRequest;
import org.junit.Test;

public class ServiceChargeQueryBuilderTest {
    @Test
    public void no_input_test() {
        final ServiceChargeQueryBuilder serviceChargeQueryBuilder = new ServiceChargeQueryBuilder();
        final ServiceChargeGetRequest serviceChargeGetRequest = new ServiceChargeGetRequest();
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        assertEquals("Select sc.id as sc_id,sc.code as sc_code,sc.servicetype "
                + "as sc_servicetype,sc.servicechargeapplicable as sc_servicechargeapplicable,"
                + "sc.servicechargetype as sc_servicechargetype,sc.description as sc_description,"
                + "sc.active as sc_active,sc.effectivefrom as sc_effectivefrom,sc.effectiveto"
                + " as sc_effectiveto,sc.outsideulb as sc_outsideulb,sc.tenantid as sc_tenantid,"
                + "sc.createdby as sc_createdby,sc.createddate as sc_createddate,sc.lastmodifiedby"
                + " as sc_lastmodifiedby,sc.lastmodifieddate as sc_lastmodifieddate from"
                + " egwtr_servicecharge sc ORDER BY sc.id DESC",
                serviceChargeQueryBuilder.getQuery(serviceChargeGetRequest, preparedStatementValues));
    }

    @Test
    public void all_input_test() {
        final ServiceChargeQueryBuilder serviceChargeQueryBuilder = new ServiceChargeQueryBuilder();
        final ServiceChargeGetRequest serviceChargeGetRequest = new ServiceChargeGetRequest();
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        serviceChargeGetRequest.setIds(Arrays.asList(2L, 3L));
        serviceChargeGetRequest.setServiceType("No Due Certificate");
        serviceChargeGetRequest.setServiceChargeType("slab");
        serviceChargeGetRequest.setOutsideUlb(false);
        serviceChargeGetRequest.setSortBy("serviceChargeType");
        serviceChargeGetRequest.setSortOrder("desc");
        serviceChargeGetRequest.setTenantId("default");
        assertEquals("Select sc.id as sc_id,sc.code as sc_code,sc.servicetype "
                + "as sc_servicetype,sc.servicechargeapplicable as sc_servicechargeapplicable,"
                + "sc.servicechargetype as sc_servicechargetype,sc.description as sc_description,"
                + "sc.active as sc_active,sc.effectivefrom as sc_effectivefrom,sc.effectiveto"
                + " as sc_effectiveto,sc.outsideulb as sc_outsideulb,sc.tenantid as sc_tenantid,"
                + "sc.createdby as sc_createdby,sc.createddate as sc_createddate,sc.lastmodifiedby"
                + " as sc_lastmodifiedby,sc.lastmodifieddate as sc_lastmodifieddate from"
                + " egwtr_servicecharge sc WHERE sc.id IN (:ids) AND sc.tenantid = :tenantId"
                + " AND sc.servicetype = :serviceType AND sc.servicechargetype = :serviceChargeType"
                + " AND sc.outsideulb = :outsideUlb ORDER BY sc.serviceChargeType desc",
                serviceChargeQueryBuilder.getQuery(serviceChargeGetRequest, preparedStatementValues));

    }
}
