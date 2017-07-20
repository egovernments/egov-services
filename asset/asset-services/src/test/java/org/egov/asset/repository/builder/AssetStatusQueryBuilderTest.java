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
