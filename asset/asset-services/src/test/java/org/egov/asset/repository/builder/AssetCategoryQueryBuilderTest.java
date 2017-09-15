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
                + "assetcategory.name ilike ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";
        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add("%Land%");
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
                + "assetcategory.id = ? AND assetcategory.name ilike ? AND assetcategory.code = ? AND assetcategory.assetcategorytype IN "
                + "('LAND') ORDER BY assetcategory.name LIMIT ? OFFSET ?";
        assertEquals(queryWithTenantId,
                assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add(20L);
        expectedPreparedStatementValues.add("%Land%");
        expectedPreparedStatementValues.add("560042");
        expectedPreparedStatementValues.add(500);
        expectedPreparedStatementValues.add(Long.valueOf(0));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getInsertQuery() {
        final String insertQuery = "INSERT into egasset_assetcategory (id,name,code,parentid,assetcategorytype,depreciationmethod,"
                + "depreciationrate,assetaccount,accumulateddepreciationaccount,revaluationreserveaccount,depreciationexpenseaccount,"
                + "unitofmeasurement,customfields,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate,isassetallow,version)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        assertEquals(insertQuery, assetCategoryQueryBuilder.getInsertQuery());

    }

    @Test
    public void getUpdateQuery() {
        final String updateQuery = "UPDATE egasset_assetcategory SET parentid=?,assetcategorytype=?,depreciationmethod=?,depreciationrate=?,"
                + "assetaccount=?,accumulateddepreciationaccount=?,revaluationreserveaccount=?,depreciationexpenseaccount=?,"
                + "unitofmeasurement=?,customfields=?,lastmodifiedby=?,lastmodifieddate=?,isassetallow=?,version=?WHERE code=? and "
                + "tenantid=?";
        assertEquals(updateQuery, assetCategoryQueryBuilder.getUpdateQuery());
    }

}
