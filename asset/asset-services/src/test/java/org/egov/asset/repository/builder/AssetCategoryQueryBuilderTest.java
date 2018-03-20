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

import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.service.AssetCommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetCategoryQueryBuilderTest {

    @InjectMocks
    private AssetCategoryQueryBuilder assetCategoryQueryBuilder;

    @Mock
    private AssetCommonService assetCommonService;

    @Test
    public void getQueryWithTenantIdTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder()
                .tenantId("ap.kurnool").build();
        final String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? ORDER BY "
                + "assetcategory.name LIMIT ? OFFSET ?";
        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add(500);
        expectedPreparedStatementValues.add(Long.valueOf(0));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithIdTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder()
                .tenantId("ap.kurnool").id(Long.valueOf(20)).build();
        final String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND "
                + "assetcategory.id = ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";

        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add(20L);
        expectedPreparedStatementValues.add(500);
        expectedPreparedStatementValues.add(Long.valueOf(0));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithNameTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder()
                .tenantId("ap.kurnool").name("Land").build();
        final String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND "
                + "assetcategory.name = ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";
        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add("Land");
        expectedPreparedStatementValues.add(500);
        expectedPreparedStatementValues.add(Long.valueOf(0));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithCodeTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder()
                .tenantId("ap.kurnool").code("560042").build();
        final String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND "
                + "assetcategory.code = ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";
        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add("560042");
        expectedPreparedStatementValues.add(500);
        expectedPreparedStatementValues.add(Long.valueOf(0));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithAssetCategoryTypeTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<String> assetCategorytype = new ArrayList<>();
        assetCategorytype.add(AssetCategoryType.LAND.toString());

        final AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder()
                .tenantId("ap.kurnool").assetCategoryType(assetCategorytype).build();
        final String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND "
                + "assetcategory.assetcategorytype IN ('LAND') ORDER BY assetcategory.name LIMIT ? OFFSET ?";

        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add(500);
        expectedPreparedStatementValues.add(Long.valueOf(0));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<String> assetCategorytype = new ArrayList<>();
        assetCategorytype.add(AssetCategoryType.LAND.toString());

        final AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder()
                .tenantId("ap.kurnool").id(Long.valueOf(20)).name("Land").code("560042")
                .assetCategoryType(assetCategorytype).build();
        final String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND "
                + "assetcategory.id = ? AND assetcategory.name = ? AND assetcategory.code = ? AND assetcategory.assetcategorytype IN "
                + "('LAND') ORDER BY assetcategory.name LIMIT ? OFFSET ?";
        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add(20L);
        expectedPreparedStatementValues.add("Land");
        expectedPreparedStatementValues.add("560042");
        expectedPreparedStatementValues.add(500);
        expectedPreparedStatementValues.add(Long.valueOf(0));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getInsertQuery() {
        final String insertQuery = "INSERT into egasset_assetcategory (id,name,code,parentid,assetcategorytype,depreciationmethod,"
                + "depreciationrate,assetaccount,accumulateddepreciationaccount,revaluationreserveaccount,depreciationexpenseaccount,"
                + "unitofmeasurement,customfields,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate,isassetallow,version,usedforlease)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        assertEquals(insertQuery, assetCategoryQueryBuilder.getInsertQuery());

    }

    @Test
    public void getUpdateQuery() {
        final String updateQuery = "UPDATE egasset_assetcategory SET parentid=?,assetcategorytype=?,depreciationmethod=?,depreciationrate=?,"
                + "assetaccount=?,accumulateddepreciationaccount=?,revaluationreserveaccount=?,depreciationexpenseaccount=?,"
                + "unitofmeasurement=?,customfields=?,lastmodifiedby=?,lastmodifieddate=?,isassetallow=?,version=?,usedforlease=? WHERE code=? and "
                + "tenantid=?";
        assertEquals(updateQuery, assetCategoryQueryBuilder.getUpdateQuery());
    }

}
