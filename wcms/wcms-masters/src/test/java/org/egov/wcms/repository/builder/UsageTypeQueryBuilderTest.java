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

import java.util.HashMap;
import java.util.Map;

import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UsageTypeQueryBuilderTest {
    @Test
    public void no_input_test() {
        final UsageTypeQueryBuilder usageTypeQueryBuilder = new UsageTypeQueryBuilder();
        final UsageTypeGetRequest usageTypeGetRequest = new UsageTypeGetRequest();
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        assertEquals("Select ut.id as ut_id,ut.code as ut_code,ut.name "
                + "as ut_name,ut.description as ut_description,"
                + "ut.active as ut_active,ut.parent as ut_parent,ut.tenantid as ut_tenantid,"
                + "ut.createdby as ut_createdby,ut.createddate as ut_createddate,ut.lastmodifiedby"
                + " as ut_lastmodifiedby,ut.lastmodifieddate as ut_lastmodifieddate from"
                + " egwtr_usage_type ut ORDER BY ut.id DESC",
                usageTypeQueryBuilder.getQuery(usageTypeGetRequest, preparedStatementValues));
    }

    /*
     * @Test public void all_input_test() { final UsageTypeQueryBuilder usageTypeQueryBuilder = new UsageTypeQueryBuilder(); final
     * UsageTypeGetRequest usageTypeGetRequest = new UsageTypeGetRequest(); final Map<String, Object> preparedStatementValues =
     * new HashMap<>(); usageTypeGetRequest.setIds(Arrays.asList(2L)); usageTypeGetRequest.setCode("2");
     * usageTypeGetRequest.setName("School"); usageTypeGetRequest.setSortBy("name"); usageTypeGetRequest.setParent("1");
     * usageTypeGetRequest.setSortOrder("desc"); usageTypeGetRequest.setTenantId("default"); assertEquals(
     * "Select ut.id as ut_id,ut.code as ut_code,ut.name " + "as ut_name,ut.description as ut_description," +
     * "ut.active as ut_active,ut.parent as ut_parent,ut.tenantid as ut_tenantid," +
     * "ut.createdby as ut_createdby,ut.createddate as ut_createddate,ut.lastmodifiedby" +
     * " as ut_lastmodifiedby,ut.lastmodifieddate as ut_lastmodifieddate from" +
     * " egwtr_usage_type ut WHERE ut.tenantid = :tenantId AND ut.id IN (:ids)" + " AND ut.name = :name AND ut.code = :code" +
     * " AND ut.parent = :parent ORDER BY ut.name desc", usageTypeQueryBuilder.getQuery(usageTypeGetRequest,
     * preparedStatementValues)); }
     */
}
