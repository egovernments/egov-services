package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class DepreciationReportQueryBuilderTest {

	@InjectMocks
	private DepreciationReportQueryBuilder depreciationReportQueryBuilder;

	public static final String BASE_QUERY = "SELECT *,asset.id AS assetId,assetcategory.id AS assetcategoryId,"
			+ "asset.name as assetname,asset.code as assetcode,"
			+ "assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode,ywd.id as ywd_id,ywd.depreciationrate as "
			+ "ywd_depreciationrate,assetcategory.depreciationrate as assetcategory_depreciationrate"
			+ " FROM egasset_asset asset " + "INNER JOIN egasset_assetcategory assetcategory "
			+ "ON asset.assetcategory = assetcategory.id LEFT OUTER JOIN egasset_yearwisedepreciation ywd "
			+ "ON asset.id = ywd.assetid WHERE "
			+ "asset.id in (SELECT depreciation.assetid FROM egasset_depreciation depreciation WHERE "
			+ "depreciation.tenantId = ?)";

	@Test
	public void getQueryWithTenantIdTest() {
		final String expectedQueryWithTenantId = BASE_QUERY + " AND asset.tenantId = ?";

		final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
				.tenantId("ap.kurnool").build();

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add("ap.kurnool");
		preparedStatementValues.add("ap.kurnool");

		final String actualQueryWithTenantId = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
				preparedStatementValues);

		assertEquals(expectedQueryWithTenantId, actualQueryWithTenantId);

	}

	@Test
	public void getQueryWithAssetCategoryNameAndTenantIdTest() {
		final String expectedQueryWithTenantId = BASE_QUERY + " AND asset.tenantId = ? AND assetcategory.name ilike ?";

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
				+ " AND asset.tenantId = ? AND assetcategory.assetcategorytype =?";

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
		final String expectedQueryWithTenantId = BASE_QUERY + " AND asset.tenantId = ? AND asset.name ilike ?";

		final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
				.assetName("park").tenantId("ap.kurnool").build();

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		preparedStatementValues.add("%" + "park" + "%");

		final String actualQueryWithTenantId = depreciationReportQueryBuilder.getQuery(depreciationReportCriteria,
				preparedStatementValues);

		assertEquals(expectedQueryWithTenantId, actualQueryWithTenantId);

	}

}
