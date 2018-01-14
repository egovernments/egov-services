package org.egov.lams.repository.builder;

import org.egov.lams.model.NoticeCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NoticeQueryBuilderTest {

    private static final String BASE_QUERY = "SELECT * FROM eglams_notice notice WHERE notice.id is not null";
    private static final String ORDER_BY_QUERY = " ORDER BY notice.ID LIMIT :pageSize OFFSET :pageNumber";

    @Test
    public void test_query_without_criteria() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        Map params = new HashMap<>();
        assertEquals(BASE_QUERY + ORDER_BY_QUERY, NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(0, params.size() - 2);
    }

    @Test
    public void test_query_with_criteria() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        Set ids = new HashSet<>();
        ids.add("1");
        ids.add("2");
        noticeCriteria.setId(ids);
        noticeCriteria.setAgreementNumber("1");
        noticeCriteria.setAckNumber("ack");
        noticeCriteria.setAssetCategory(1l);
        noticeCriteria.setNoticeNo("Notice1");
        noticeCriteria.setTenantId("1");
        Map params = new HashMap();
        assertEquals(getQueryWithCriteria(noticeCriteria), NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(6, params.size() - 2);
    }

    @Test
    public void test_query_with_id() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        Set ids = new HashSet<>();
        ids.add("1");
        ids.add("2");
        noticeCriteria.setId(ids);
        Map params = new HashMap();
        assertEquals(getQueryWithCriteria(noticeCriteria), NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_agreementNumber() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        noticeCriteria.setAgreementNumber("1");
        Map params = new HashMap();
        assertEquals(getQueryWithCriteria(noticeCriteria), NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_acknowlegement() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        noticeCriteria.setAckNumber("ack");
        Map params = new HashMap();
        assertEquals(getQueryWithCriteria(noticeCriteria), NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_assetCategory() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        noticeCriteria.setAssetCategory(1l);
        Map params = new HashMap();
        assertEquals(getQueryWithCriteria(noticeCriteria), NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_noticeno() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        noticeCriteria.setNoticeNo("Notice1");
        Map params = new HashMap();
        assertEquals(getQueryWithCriteria(noticeCriteria), NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(1, params.size() - 2);
    }

    @Test
    public void test_query_with_tenantid() {
        NoticeCriteria noticeCriteria = new NoticeCriteria();
        noticeCriteria.setTenantId("1");
        Map params = new HashMap();
        assertEquals(getQueryWithCriteria(noticeCriteria), NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params));
        assertEquals(1, params.size() - 2);
    }

    private String getQueryWithCriteria(NoticeCriteria noticeCriteria) {
        StringBuffer query = new StringBuffer(BASE_QUERY);
        if (noticeCriteria.getId() != null) {
            query.append("and notice.id IN (:noticeId)");
        }
        if (noticeCriteria.getAgreementNumber() != null) {
            query.append(" and notice.AgreementNumber = :agreementNumber");
        }
        if (noticeCriteria.getAckNumber() != null) {
            query.append(" and notice.AckNumber = :ackNumber");
        }
        if (noticeCriteria.getAssetCategory() != null) {
            query.append(" and notice.AssetCategory = :assetCategory");
        }
        if (noticeCriteria.getNoticeNo() != null) {
            query.append(" and notice.NoticeNo = :noticeNo");
            
        }
        if (noticeCriteria.getNoticeType() != null) {
            query.append(" and notice.noticetype = :noticeType");
        }
        
        if (noticeCriteria.getTenantId() != null) {
            query.append(" and notice.TenantId = :tenantId");
        }
        return query.append(ORDER_BY_QUERY).toString();
    }
}