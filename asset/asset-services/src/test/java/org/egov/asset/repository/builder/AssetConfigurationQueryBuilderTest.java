package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.AssetConfigurationCriteria;
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
@WebMvcTest(AssetConfigurationQueryBuilder.class)
@Import(TestConfiguration.class)
public class AssetConfigurationQueryBuilderTest {

    @MockBean
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private AssetConfigurationQueryBuilder assetConfigurationQueryBuilder;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getQueryWithTenantIdTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();

        final String expectedQueryWithTenantId = "SELECT ck.keyName as key, cv.value as value FROM egasset_assetconfiguration ck JOIN "
                + "egasset_assetconfigurationvalues cv ON ck.id = cv.keyId AND ck.tenantId = cv.tenantId WHERE ck.tenantId = ?";
        final AssetConfigurationCriteria assetConfigurationCriteria = AssetConfigurationCriteria.builder()
                .tenantId("ap.kurnool").build();
        assertEquals(expectedQueryWithTenantId,
                assetConfigurationQueryBuilder.getQuery(assetConfigurationCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("ap.kurnool");
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithIdTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<Long> id = new ArrayList<Long>();
        id.add(Long.valueOf("1"));
        id.add(Long.valueOf("2"));
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT ck.keyName as key, cv.value as value FROM egasset_assetconfiguration ck JOIN "
                + "egasset_assetconfigurationvalues cv ON ck.id = cv.keyId AND ck.tenantId = cv.tenantId WHERE ck.id IN "
                + getIdQuery(id);
        final AssetConfigurationCriteria assetConfigurationCriteria = AssetConfigurationCriteria.builder().id(id)
                .build();
        assertEquals(expectedQueryWithTenantId,
                assetConfigurationQueryBuilder.getQuery(assetConfigurationCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithNameTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final AssetConfigurationCriteria assetConfigurationCriteria = AssetConfigurationCriteria.builder()
                .name("EnableVoucherGeneration").build();
        final String expectedQueryWithId = "SELECT ck.keyName as key, cv.value as value FROM egasset_assetconfiguration ck JOIN "
                + "egasset_assetconfigurationvalues cv ON ck.id = cv.keyId AND ck.tenantId = cv.tenantId WHERE ck.keyName = ?";
        assertEquals(expectedQueryWithId,
                assetConfigurationQueryBuilder.getQuery(assetConfigurationCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add("EnableVoucherGeneration");
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
	public void getQueryWithEffectiveFromTest() {
		final List<Object> preparedStatementValues = new ArrayList<>();
		Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
		final AssetConfigurationCriteria assetConfigurationCriteria = AssetConfigurationCriteria.builder()
				.effectiveFrom(Long.valueOf("1500381058598")).build();
		final String expectedQueryWithId = "SELECT ck.keyName as key, cv.value as value FROM egasset_assetconfiguration ck JOIN "
				+ "egasset_assetconfigurationvalues cv ON ck.id = cv.keyId AND ck.tenantId = cv.tenantId WHERE cv.effectiveFrom = ?";
		assertEquals(expectedQueryWithId,
				assetConfigurationQueryBuilder.getQuery(assetConfigurationCriteria, preparedStatementValues));
		final List<Object> expectedPreparedStatementValues = new ArrayList<>();
		expectedPreparedStatementValues.add(Long.valueOf("1500381058598"));
		assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
	}
    
    @Test
    public void getQueryWithTenantIdAndKeyNameTest() {
            final List<Object> preparedStatementValues = new ArrayList<>();
            Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
            final AssetConfigurationCriteria assetConfigurationCriteria = AssetConfigurationCriteria.builder()
                            .tenantId("ap.kurnool").name("EnableVoucherGeneration").build();
            final String expectedQueryWithId = "SELECT ck.keyName as key, cv.value as value FROM egasset_assetconfiguration ck JOIN "
                            + "egasset_assetconfigurationvalues cv ON ck.id = cv.keyId AND ck.tenantId = cv.tenantId WHERE ck.tenantId = ? "
                            + "AND ck.keyName = ?";
            assertEquals(expectedQueryWithId,
                            assetConfigurationQueryBuilder.getQuery(assetConfigurationCriteria, preparedStatementValues));
            final List<Object> expectedPreparedStatementValues = new ArrayList<>();
            expectedPreparedStatementValues.add("ap.kurnool");
            expectedPreparedStatementValues.add("EnableVoucherGeneration");
            assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }
}
