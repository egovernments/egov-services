package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.AssetCategoryCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetCategoryQueryBuilder.class)
public class AssetCategoryQueryBuilderTest {

	@MockBean
	private AssetCategoryCriteria assetCategoryCriteria;

	@InjectMocks
	private AssetCategoryQueryBuilder assetCategoryQueryBuilder;

	@Test
	public void getQueryWithTenantIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder().tenantId("ap.kurnool")
				.build();
		String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";
		assertEquals(queryWithTenantId,
				assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(500);
		expectedPreparedStatementValues.add(Long.valueOf(0));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder().tenantId("ap.kurnool")
				.id(Long.valueOf(20)).build();
		String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND assetcategory.id = ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";

		assertEquals(queryWithTenantId,
				assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(20L);
		expectedPreparedStatementValues.add(500);
		expectedPreparedStatementValues.add(Long.valueOf(0));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithNameTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder().tenantId("ap.kurnool")
				.name("Land").build();
		String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND assetcategory.name = ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";
		assertEquals(queryWithTenantId,
				assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add("Land");
		expectedPreparedStatementValues.add(500);
		expectedPreparedStatementValues.add(Long.valueOf(0));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithCodeTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder().tenantId("ap.kurnool")
				.code("560042").build();
		String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND assetcategory.code = ? ORDER BY assetcategory.name LIMIT ? OFFSET ?";
		assertEquals(queryWithTenantId,
				assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add("560042");
		expectedPreparedStatementValues.add(500);
		expectedPreparedStatementValues.add(Long.valueOf(0));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithAssetCategoryTypeTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		List<String> assetCategorytype = new ArrayList<>();
		assetCategorytype.add("LAND");

		AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder().tenantId("ap.kurnool")
				.assetCategoryType(assetCategorytype).build();
		String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND assetcategory.assetcategorytype IN ('LAND') ORDER BY assetcategory.name LIMIT ? OFFSET ?";
		assertEquals(queryWithTenantId,
				assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(500);
		expectedPreparedStatementValues.add(Long.valueOf(0));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		List<String> assetCategorytype = new ArrayList<>();
		assetCategorytype.add("LAND");

		AssetCategoryCriteria assetCategorySearchWithTenantId = AssetCategoryCriteria.builder().tenantId("ap.kurnool")
				.id(Long.valueOf(20)).name("Land").code("560042").assetCategoryType(assetCategorytype).build();
		String queryWithTenantId = "SELECT * FROM egasset_assetcategory assetcategory  WHERE assetcategory.tenantId = ? AND assetcategory.id = ? AND assetcategory.name = ? AND assetcategory.code = ? AND assetcategory.assetcategorytype IN ('LAND') ORDER BY assetcategory.name LIMIT ? OFFSET ?";
		assertEquals(queryWithTenantId,
				assetCategoryQueryBuilder.getQuery(assetCategorySearchWithTenantId, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
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
		String insertQuery = "INSERT into egasset_assetcategory (id,name,code,parentid,assetcategorytype,depreciationmethod,depreciationrate,assetaccount,accumulateddepreciationaccount,revaluationreserveaccount,depreciationexpenseaccount,unitofmeasurement,customfields,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate,isassetallow,version)values(nextval('seq_egasset_assetcategory'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		assertEquals(insertQuery, assetCategoryQueryBuilder.getInsertQuery());

	}

	@Test
	public void getUpdateQuery() {
		String updateQuery = "UPDATE egasset_assetcategory SET parentid=?,assetcategorytype=?,depreciationmethod=?,depreciationrate=?,assetaccount=?,accumulateddepreciationaccount=?,revaluationreserveaccount=?,depreciationexpenseaccount=?,unitofmeasurement=?,customfields=?,lastmodifiedby=?,lastmodifieddate=?,isassetallow=?,version=?WHERE code=? and tenantid=?";
		assertEquals(updateQuery, assetCategoryQueryBuilder.getUpdateQuery());
	}

}
