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

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(DepreciationReportQueryBuilder.class)
@Import(TestConfiguration.class)
public class DepreciationReportQueryBuilderTest {

    @InjectMocks
    private DepreciationReportQueryBuilder depreciationReportQueryBuilder;

    public static final String BASE_QUERY ="SELECT  depreciation.id as depreciationId,depreciation.tenantId as tenantId, depreciation.assetid as assetId,assetcategory.id as assetcategoryId,"
            + "asset.name as assetname,asset.code as assetcode,asset.department as department,depreciation.depreciationrate as depreciationrate, current.currentamount as currentamount ,"
            + "depreciation.depreciationvalue as depreciationvalue,depreciation.valueafterdepreciation as valueafterdepreciation,asset.grossvalue as grossvalue, "
            + "assetcategory.name as assetcategoryname,assetcategory.code as assetcategorycode,assetcategory.assetcategorytype as assetcategorytype,assetcategory.parentid as parentid, "
            + "assetcategory.depreciationrate as assetcategory_depreciationrate,depreciation.financialyear as financialyear "
            + " FROM egasset_depreciation depreciation,egasset_asset asset,egasset_current_value current ,egasset_assetcategory assetcategory "
            + "WHERE asset.assetcategory = assetcategory.id and "
            + " depreciation.assetid = asset.id and depreciation.tenantId =asset.tenantId "
            + " and assetcategory.id= asset.assetcategory and assetcategory.tenantId = asset.tenantId and"
            + " current.assetid= asset.id and current.tenantId =asset.tenantId";

    @Test
    public void getQueryWithTenantIdTest() {
        final String expectedQueryWithTenantId = BASE_QUERY + " AND depreciation.tenantId = ?";

        final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
                .tenantId("ap.kurnool").build();

        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add("ap.kurnool");
        // preparedStatementValues.add("ap.kurnool");

        final String actualQueryWithTenantId = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
                preparedStatementValues);

        assertEquals(expectedQueryWithTenantId, actualQueryWithTenantId);

    }

    @Test
    public void getQueryWithAssetCategoryNameAndTenantIdTest() {
        final String expectedQueryWithTenantId = BASE_QUERY + " AND depreciation.tenantId = ? AND assetcategory.name ilike ?";

        final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
                .assetCategoryName("Parks").tenantId("ap.kurnool").build();

        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add("%" + "Parks" + "%");

        final String actualQueryWithTenantId = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
                preparedStatementValues);

        assertEquals(expectedQueryWithTenantId, actualQueryWithTenantId);

    }

    @Test
    public void getQueryWithAssetCategoryTypeAndTenantIdTest() {
        final String expectedQueryWithTenantId = BASE_QUERY
                + " AND depreciation.tenantId = ? AND assetcategory.assetcategorytype =?";

        final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
                .assetCategoryType(AssetCategoryType.IMMOVABLE.toString()).tenantId("ap.kurnool").build();

        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(AssetCategoryType.IMMOVABLE.toString());

        final String actualQueryWithTenantId = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
                preparedStatementValues);

        assertEquals(expectedQueryWithTenantId, actualQueryWithTenantId);

    }

    @Test
    public void getQueryWithAssetNameAndTenantIdTest() {
        final String expectedQueryWithTenantId = BASE_QUERY + " AND depreciation.tenantId = ? AND asset.name ilike ?";

        final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
                .assetName("park").tenantId("ap.kurnool").build();

        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add("%" + "park" + "%");

        final String actualQueryWithTenantId = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
                preparedStatementValues);

        assertEquals(expectedQueryWithTenantId, actualQueryWithTenantId);

    }

}
