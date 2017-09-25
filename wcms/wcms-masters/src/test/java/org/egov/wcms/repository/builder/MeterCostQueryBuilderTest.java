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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.egov.wcms.web.contract.MeterCostGetRequest;
import org.junit.Test;

public class MeterCostQueryBuilderTest {

    @Test
    public void no_input_test() {
        final MeterCostQueryBuilder meterCostQueryBuilder = new MeterCostQueryBuilder();
        final MeterCostGetRequest meterCostGetRequest = new MeterCostGetRequest();
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        assertEquals(
                "Select wmc.id as wmc_id,wmc.code as wmc_code,"
                        + "wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
                        + "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
                        + "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
                        + "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc ORDER BY wmc.metermake ASC",
                meterCostQueryBuilder.getQuery(meterCostGetRequest, preparedStatementValues));
    }

    @Test
    public void all_input_test() {
        final MeterCostQueryBuilder meterCostQueryBuilder = new MeterCostQueryBuilder();

        final MeterCostGetRequest meterCostGetRequest = new MeterCostGetRequest();
        meterCostGetRequest.setActive(true);
        meterCostGetRequest.setCode("MC");
        meterCostGetRequest.setIds(Arrays.asList(1L, 2L));
        meterCostGetRequest.setMeterMake("MeterMake");
        meterCostGetRequest.setSortBy("code");
        meterCostGetRequest.setSortOrder("desc");
        meterCostGetRequest.setTenantId("default");

        assertEquals(
                "Select wmc.id as wmc_id,wmc.code as wmc_code,"
                        + "wmc.metermake as wmc_metermake,wmc.amount as wmc_amount,"
                        + "wmc.active as wmc_active,wmc.createdby as wmc_createdby,wmc.createddate as wmc_createddate,"
                        + "wmc.lastmodifiedby as wmc_lastmodifiedby,wmc.lastmodifieddate as wmc_lastmodifieddate,"
                        + "wmc.tenantid as wmc_tenantid from egwtr_metercost wmc WHERE wmc.tenantId = :tenantId AND "
                        + "wmc.code = :code AND wmc.metermake = :metermake AND wmc.active = :active AND"
                        + " wmc.id IN (:ids) ORDER BY wmc.code desc",
                meterCostQueryBuilder.getQuery(meterCostGetRequest, new HashMap<>()));

    }
}
