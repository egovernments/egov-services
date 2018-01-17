/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetStatusQueryBuilder.class)
@Import(TestConfiguration.class)
public class AssetStatusQueryBuilderTest {

    @MockBean
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private AssetStatusQueryBuilder assetStatusQueryBuilder;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getQueryWithTenantIdTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT objectname,code,name,description,tenantid,createdby,createddate,lastmodifiedby,"
                + "lastmodifieddate FROM egasset_statuses as status WHERE status.tenantId = ?";
        final AssetStatusCriteria assetStatusCriteria = AssetStatusCriteria.builder().tenantId("ap.kurnool").build();
        assertEquals(expectedQueryWithTenantId,
                assetStatusQueryBuilder.getQuery(assetStatusCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithObjectNameTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT objectname,code,name,description,tenantid,createdby,createddate,lastmodifiedby,"
                + "lastmodifieddate FROM egasset_statuses as status WHERE status.objectname = ?";
        final AssetStatusCriteria assetStatusCriteria = AssetStatusCriteria.builder()
                .objectName(AssetStatusObjectName.ASSETMASTER.toString()).build();
        assertEquals(expectedQueryWithTenantId,
                assetStatusQueryBuilder.getQuery(assetStatusCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add(AssetStatusObjectName.ASSETMASTER.toString());
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithCodeTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT objectname,code,name,description,tenantid,createdby,createddate,lastmodifiedby,"
                + "lastmodifieddate FROM egasset_statuses as status WHERE status.code = ?";
        final AssetStatusCriteria assetStatusCriteria = AssetStatusCriteria.builder()
                .code(Status.CAPITALIZED.toString()).build();
        assertEquals(expectedQueryWithTenantId,
                assetStatusQueryBuilder.getQuery(assetStatusCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add(Status.CAPITALIZED.toString());
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

}
