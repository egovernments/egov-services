package org.egov.lams.repository.builder;

import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LamsConfigurationQueryBuilderTest {

    private final String BASE_QUERY = "SELECT ck.keyName as key, cv.value as value " +
            "FROM eglams_lamsConfiguration ck JOIN eglams_lamsConfigurationValues cv ON ck.id = cv.keyId";

    @InjectMocks
    private LamsConfigurationQueryBuilder lamsConfigurationQueryBuilder;

    @Test
    public void test_query_without_criteria() {
        LamsConfigurationGetRequest request = new LamsConfigurationGetRequest();
        StringBuffer query = new StringBuffer(BASE_QUERY);
        prepareQueryWithSorting(query, request);
        Map params = new HashMap();
        assertEquals(query.toString(), lamsConfigurationQueryBuilder.getQuery(request, params));
        assertEquals(0, params.size() - 2);
    }

    @Test
    public void test_query_with_criteria() {
        LamsConfigurationGetRequest request = new LamsConfigurationGetRequest();
        request.setTenantId("1");
        request.setEffectiveFrom(new Date());
        request.setName("Test");
        request.setId(Arrays.asList(1l));
        StringBuffer query = new StringBuffer(BASE_QUERY);
        prepareQueryWithParams(query, request);
        Map params = new HashMap();
        assertEquals(query.toString(), lamsConfigurationQueryBuilder.getQuery(request, params));
        assertEquals(4, params.size() - 2);
    }

    @Test
    public void test_query_with_criteria_sortby_sortorder() {
        LamsConfigurationGetRequest request = new LamsConfigurationGetRequest();
        request.setTenantId("1");
        request.setEffectiveFrom(new Date());
        request.setName("Test");
        request.setId(Arrays.asList(1l));
        request.setSortBy("EffectiveFrom");
        request.setSortOrder("DESC");
        StringBuffer query = new StringBuffer(BASE_QUERY);
        prepareQueryWithParams(query, request);
        Map params = new HashMap();
        assertEquals(query.toString(), lamsConfigurationQueryBuilder.getQuery(request, params));
        assertEquals(6, params.size());
    }

    @Test
    public void test_query_with_effectivefrom() {
        LamsConfigurationGetRequest request = new LamsConfigurationGetRequest();
        request.setEffectiveFrom(new Date());
        StringBuffer query = new StringBuffer(BASE_QUERY);
        prepareQueryWithParams(query, request);
        Map params = new HashMap();
        assertEquals(query.toString(), lamsConfigurationQueryBuilder.getQuery(request, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_id() {
        LamsConfigurationGetRequest request = new LamsConfigurationGetRequest();
        request.setId(Arrays.asList(1l));
        StringBuffer query = new StringBuffer(BASE_QUERY);
        prepareQueryWithParams(query, request);
        Map params = new HashMap();
        assertEquals(query.toString(), lamsConfigurationQueryBuilder.getQuery(request, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_name() {
        LamsConfigurationGetRequest request = new LamsConfigurationGetRequest();
        request.setName("Test");
        StringBuffer query = new StringBuffer(BASE_QUERY);
        prepareQueryWithParams(query, request);
        Map params = new HashMap();
        assertEquals(query.toString(), lamsConfigurationQueryBuilder.getQuery(request, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_tenantid() {
        LamsConfigurationGetRequest request = new LamsConfigurationGetRequest();
        request.setTenantId("1");
        StringBuffer query = new StringBuffer(BASE_QUERY);
        prepareQueryWithParams(query, request);
        Map params = new HashMap();
        assertEquals(query.toString(), lamsConfigurationQueryBuilder.getQuery(request, params));
        assertEquals(1, params.size() - 2);
    }

    private void prepareQueryWithParams(StringBuffer query, LamsConfigurationGetRequest request) {
        query.append(" WHERE ck.id is not null");
        if (request.getTenantId() != null) {
            query.append(" and ck.tenantId = :tenantId and cv.tenantId = :tenantId");
        }
        if (request.getId() != null) {
            query.append(" and ck.id IN (:id)");
        }
        if (request.getName() != null) {
            query.append(" and ck.keyName = :keyName");
        }
        if (request.getEffectiveFrom() != null) {
            query.append(" and cv.effectiveFrom = :effectiveFrom");
        }
        prepareQueryWithSorting(query, request);
    }

    private void prepareQueryWithSorting(StringBuffer query, LamsConfigurationGetRequest request) {
        query.append(" ORDER BY");
        if (request.getSortBy() != null) {
            query.append(" ").append(request.getSortBy());
        } else {
            query.append(" keyName");
        }
        if (request.getSortOrder() != null) {
            query.append(" ").append(request.getSortOrder());
        } else {
            query.append(" ASC");
        }
        query.append(" LIMIT :pageSize OFFSET :pageNumber");
    }

}