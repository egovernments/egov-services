package org.egov.lams.repository.builder;

import org.egov.lams.model.NoticeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NoticeQueryBuilder {

    public static final Logger logger = LoggerFactory.getLogger(NoticeQueryBuilder.class);

    public static final String INSERT_NOTICE_QUERY = "INSERT INTO eglams_notice"
            + " (id, noticeno, noticedate, agreementno, assetcategory, acknowledgementnumber, assetno, allotteename,"
            + " allotteeaddress, allotteemobilenumber, agreementperiod, commencementdate, templateversion, expirydate, rent,"
            + " securitydeposit, commissionername, zone, ward, street, electionward, locality, block, createdby,"
			+ " createddate, lastmodifiedby ,lastmodifieddate, tenantId, filestore, noticetype)"
			+ " VALUES (nextval('seq_eglams_notice'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public final static String SEQ_NOTICE_NO = "SELECT nextval('seq_eglams_noticeno')";

    public final static String SEQ_NOTICE_ID = "SELECT nextval('seq_eglams_notice')";

    public final static String RENTINCREMENTTYPEQUERY = "SELECT * FROM eglams_rentincrementtype rent WHERE rent.id = :rentId";

    @SuppressWarnings("unchecked")
    public static String getNoticeQuery(NoticeCriteria noticeCriteria,
                                        @SuppressWarnings("rawtypes") Map params) {

        StringBuilder selectQuery = new StringBuilder("SELECT * FROM eglams_notice notice");

        selectQuery.append(" WHERE notice.id is not null");

        if (noticeCriteria.getId() != null) {
            selectQuery.append("and notice.id IN (:noticeId)");
            params.put("noticeId", noticeCriteria.getId());
        }

        if (noticeCriteria.getAgreementNumber() != null) {
            selectQuery.append(" and notice.AgreementNumber = :agreementNumber");
            params.put("agreementNumber", noticeCriteria.getAgreementNumber());
        }
        if (noticeCriteria.getAckNumber() != null) {
            selectQuery.append(" and notice.AckNumber = :ackNumber");
            params.put("ackNumber", noticeCriteria.getAckNumber());
        }
        if (noticeCriteria.getAssetCategory() != null) {
            selectQuery.append(" and notice.AssetCategory = :assetCategory");
            params.put("assetCategory", noticeCriteria.getAssetCategory());
        }
        if (noticeCriteria.getNoticeNo() != null) {
            selectQuery.append(" and notice.NoticeNo = :noticeNo");
            params.put("noticeNo", noticeCriteria.getNoticeNo());
        }
        
        if (noticeCriteria.getNoticeType() != null) {
            selectQuery.append(" and notice.noticetype = :noticeType");
            params.put("noticeType", noticeCriteria.getNoticeType());
        }
        
        if (noticeCriteria.getRevenueWard() != null) {
            selectQuery.append(" and notice.ward = :revenueWard");
            params.put("revenueWard", noticeCriteria.getRevenueWard());
        }

        if (noticeCriteria.getTenantId() != null) {
            selectQuery.append(" and notice.TenantId = :tenantId");
            params.put("tenantId", noticeCriteria.getTenantId());
        }

        selectQuery.append(" ORDER BY notice.ID");
        selectQuery.append(" LIMIT :pageSize");
        if (noticeCriteria.getSize() != null)
            params.put("pageSize", noticeCriteria.getSize());
        else
            params.put("pageSize", 20);

        selectQuery.append(" OFFSET :pageNumber");

        if (noticeCriteria.getOffset() != null)
            params.put("pageNumber", noticeCriteria.getOffset());
        else
            params.put("pageNumber", 0);

        logger.debug("the select query in notice querybuilder ::" + selectQuery.toString());

        return selectQuery.toString();
    }

}

