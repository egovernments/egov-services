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
package org.egov.wcms.repository.rowmapper;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;

import org.egov.wcms.model.ServiceCharge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceChargeRowMapperTest {

    @Mock
    private ResultSet rs;

    @InjectMocks
    private ServiceChargeRowMapper serviceChargeRowMapper;

    @Test
    public void test_should_map_result_set_to_entity() throws Exception {
        Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getLong("sc_id")).thenReturn(1L);
        when(rs.getString("sc_code")).thenReturn("1");
        when(rs.getString("sc_servicetype")).thenReturn("No Due Certificate");
        when((Boolean) rs.getObject("sc_servicechargeapplicable")).thenReturn(true);
        when(rs.getString("sc_servicechargetype")).thenReturn("slab");
        when(rs.getString("sc_description")).thenReturn("certificate will be issued");
        when((Boolean) rs.getObject("sc_active")).thenReturn(true);
        when((Long) rs.getObject("sc_effectivefrom")).thenReturn(15678956L);
        when((Long) rs.getObject("sc_effectiveto")).thenReturn(15678968L);
        when((Boolean) rs.getObject("sc_outsideulb")).thenReturn(false);
        when(rs.getString("sc_tenantid")).thenReturn("default");
        when((Long) rs.getObject("sc_createdby")).thenReturn(1L);
        when((Long) rs.getObject("sc_createddate")).thenReturn(15678956L);
        when((Long) rs.getObject("sc_lastmodifiedby")).thenReturn(1L);
        when((Long) rs.getObject("sc_lastmodifieddate")).thenReturn(15678956L);
        final ServiceCharge expectedServiceCharge = getServiceCharge();
        final ServiceCharge actualServiceCharge = serviceChargeRowMapper.mapRow(rs, 1);
        assertTrue(expectedServiceCharge.equals(actualServiceCharge));

    }

    private ServiceCharge getServiceCharge() {
        return ServiceCharge.builder().id(1L).code("1").serviceType("No Due Certificate").serviceChargeApplicable(true)
                .serviceChargeType("slab").description("certificate will be issued").active(true).effectiveFrom(15678956L)
                .effectiveTo(15678968L).outsideUlb(false).tenantId("default").createdBy(1L).lastModifiedBy(1L)
                .createdDate(15678956L).lastModifiedDate(15678956L).build();
    }
}