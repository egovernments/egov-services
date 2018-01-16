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

    public static final String BASE_QUERY = "SELECT  depreciation.id as depreciationId,depreciation.tenantId as tenantId, depreciation.assetid as assetId,assetcategory.id as assetcategoryId,"
            + "asset.name as assetname,asset.code as assetcode,asset.department as department,depreciation.depreciationrate as depreciationrate,"
            + "depreciation.depreciationvalue as depreciationvalue,depreciation.valueafterdepreciation as valueafterdepreciation,asset.grossvalue as grossvalue, "
            + "assetcategory.name as assetcategoryname,assetcategory.code as assetcategorycode,assetcategory.assetcategorytype as assetcategorytype,assetcategory.parentid as parentid, "
            + "assetcategory.depreciationrate as assetcategory_depreciationrate,depreciation.financialyear as financialyear "
            + " FROM egasset_depreciation depreciation,egasset_asset asset, egasset_assetcategory assetcategory "
            + "WHERE asset.assetcategory = assetcategory.id and "
            + " depreciation.assetid = asset.id and depreciation.tenantId =asset.tenantId "
            + " and assetcategory.id= asset.assetcategory and assetcategory.tenantId = asset.tenantId ";

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
