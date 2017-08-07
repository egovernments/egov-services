package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.DisposalCriteria;
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
@WebMvcTest(DisposalQueryBuilder.class)
@Import(TestConfiguration.class)
public class DisposalQueryBuilderTest {

	@MockBean
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private DisposalQueryBuilder disposalQueryBuilder;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getQueryWithTenantIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
		String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference FROM egasset_disposal as disposal WHERE disposal.tenantId = ? AND disposal.assetid IN (10,20) LIMIT ? OFFSET ?";

		List<Long> assetIds = new ArrayList<>();
		assetIds.add(10L);
		assetIds.add(20L);
		DisposalCriteria disposalCriteria = DisposalCriteria.builder().tenantId("ap.kurnool").assetId(assetIds).build();

		assertEquals(expectedQueryWithTenantId,
				disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues));
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("500"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithIdTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
		String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference FROM egasset_disposal as disposal WHERE disposal.tenantId = ? AND disposal.id IN (2,6) LIMIT ? OFFSET ?";
		List<Long> ids = new ArrayList<>();
		ids.add(2L);
		ids.add(6L);
		DisposalCriteria disposalCriteria = DisposalCriteria.builder().tenantId("ap.kurnool").id(ids).build();
		assertEquals(expectedQueryWithTenantId,
				disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues));
		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("500"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}

	@Test
	public void getQueryWithSizeTest() {
		List<Object> preparedStatementValues = new ArrayList<>();
		Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
		String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference FROM egasset_disposal as disposal WHERE disposal.tenantId = ? LIMIT ? OFFSET ?";
		DisposalCriteria disposalCriteria = DisposalCriteria.builder().tenantId("ap.kurnool").size(Long.valueOf(80))
				.build();

		assertEquals(expectedQueryWithTenantId,
				disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues));

		List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add("ap.kurnool");
		expectedPreparedStatementValues.add(Long.valueOf("80"));
		expectedPreparedStatementValues.add(Long.valueOf("0"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}
}
