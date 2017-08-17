package org.egov.asset.repository.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.RevaluationCriteria;
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
@WebMvcTest(RevaluationQueryBuilder.class)
@Import(TestConfiguration.class)
public class RevaluationQueryBuilderTest {

    @MockBean
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private RevaluationQueryBuilder revaluationQueryBuilder;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getQueryWithTenantIdTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,currentcapitalizedvalue,typeofchange,revaluationamount,valueafterrevaluation,revaluationdate,reevaluatedby,reasonforrevaluation,fixedassetswrittenoffaccount,function,fund,scheme,subscheme,comments,status,createdby,createddate,lastmodifiedby,lastmodifieddate,voucherreference FROM egasset_revalution as revalution WHERE revalution.status = ? LIMIT ? OFFSET ?";
        final RevaluationCriteria revaluationCriteria = RevaluationCriteria.builder().tenantId("ap.kurnool").build();
        assertEquals(expectedQueryWithTenantId,
                revaluationQueryBuilder.getQuery(revaluationCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add(Status.APPROVED.toString());
        expectedPreparedStatementValues.add(Long.valueOf("500"));
        expectedPreparedStatementValues.add(Long.valueOf("0"));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithIdTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,currentcapitalizedvalue,typeofchange,revaluationamount,valueafterrevaluation,revaluationdate,reevaluatedby,reasonforrevaluation,fixedassetswrittenoffaccount,function,fund,scheme,subscheme,comments,status,createdby,createddate,lastmodifiedby,lastmodifieddate,voucherreference FROM egasset_revalution as revalution WHERE revalution.status = ? AND revalution.tenantId = ? AND revalution.assetid IN (10,20) ORDER BY revalution.revaluationdate desc LIMIT ? OFFSET ?";
        final List<Long> assetIds = new ArrayList<>();
        assetIds.add(10L);
        assetIds.add(20L);
        final RevaluationCriteria revaluationCriteria = RevaluationCriteria.builder().tenantId("ap.kurnool")
                .assetId(assetIds).build();
        assertEquals(expectedQueryWithTenantId,
                revaluationQueryBuilder.getQuery(revaluationCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add(Status.APPROVED.toString());
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add(Long.valueOf("500"));
        expectedPreparedStatementValues.add(Long.valueOf("0"));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithAssetIdsTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,currentcapitalizedvalue,typeofchange,revaluationamount,valueafterrevaluation,revaluationdate,reevaluatedby,reasonforrevaluation,fixedassetswrittenoffaccount,function,fund,scheme,subscheme,comments,status,createdby,createddate,lastmodifiedby,lastmodifieddate,voucherreference FROM egasset_revalution as revalution WHERE revalution.status = ? AND revalution.tenantId = ? AND revalution.assetid IN (10,20) ORDER BY revalution.revaluationdate desc LIMIT ? OFFSET ?";
        final List<Long> assetIds = new ArrayList<>();
        assetIds.add(10L);
        assetIds.add(20L);
        final RevaluationCriteria revaluationCriteria = RevaluationCriteria.builder().tenantId("ap.kurnool")
                .assetId(assetIds).build();
        assertEquals(expectedQueryWithTenantId,
                revaluationQueryBuilder.getQuery(revaluationCriteria, preparedStatementValues));
        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add(Status.APPROVED.toString());
        expectedPreparedStatementValues.add("ap.kurnool");
        expectedPreparedStatementValues.add(Long.valueOf("500"));
        expectedPreparedStatementValues.add(Long.valueOf("0"));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }

    @Test
    public void getQueryWithSizeTest() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        Mockito.doReturn("500").when(applicationProperties).getSearchPageSizeDefault();
        final String expectedQueryWithTenantId = "SELECT id,tenantid,assetid,currentcapitalizedvalue,typeofchange,revaluationamount,valueafterrevaluation,revaluationdate,reevaluatedby,reasonforrevaluation,fixedassetswrittenoffaccount,function,fund,scheme,subscheme,comments,status,createdby,createddate,lastmodifiedby,lastmodifieddate,voucherreference FROM egasset_revalution as revalution WHERE revalution.status = ? LIMIT ? OFFSET ?";
        final RevaluationCriteria revaluationCriteria = RevaluationCriteria.builder().tenantId("ap.kurnool")
                .size(Long.valueOf(80)).build();

        assertEquals(expectedQueryWithTenantId,
                revaluationQueryBuilder.getQuery(revaluationCriteria, preparedStatementValues));

        final List<Object> expectedPreparedStatementValues = new ArrayList<>();
        expectedPreparedStatementValues.add(Status.APPROVED.toString());
        expectedPreparedStatementValues.add(Long.valueOf("80"));
        expectedPreparedStatementValues.add(Long.valueOf("0"));
        assertTrue(preparedStatementValues.equals(expectedPreparedStatementValues));
    }
}
