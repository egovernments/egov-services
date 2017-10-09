package org.egov.property.repository;

import org.egov.models.Notice;
import org.egov.property.repository.builder.NoticeQueryBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class NoticeRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private NoticeQueryBuilder noticeQueryBuilder;

    public NoticeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, NoticeQueryBuilder noticeQueryBuilder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.noticeQueryBuilder = noticeQueryBuilder;
    }

    public void save(Notice notice){
        namedParameterJdbcTemplate.update(noticeQueryBuilder.getInsertQuery(),
                getInsertNamedQueryMap(notice));
    }

    private HashMap getInsertNamedQueryMap(Notice notice){
        HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("tenantid", notice.getTenantId());
        parametersMap.put("applicationnumber", notice.getApplicationNo());
        parametersMap.put("noticedate", notice.getNoticeDate());
        parametersMap.put("noticenumber", notice.getNoticeNumber());
        parametersMap.put("noticetype", notice.getNoticeType());
        parametersMap.put("upicnumber", notice.getUpicNumber());
        parametersMap.put("fileStoreId", notice.getFileStoreId());
        parametersMap.put("createdby",notice.getAuditDetails().getCreatedBy());
        parametersMap.put("createdtime",notice.getAuditDetails().getCreatedTime());
        parametersMap.put("lastmodifiedby",notice.getAuditDetails().getLastModifiedBy());
        parametersMap.put("lastmodifiedtime",notice.getAuditDetails().getLastModifiedTime());

        return parametersMap;
    }
}
