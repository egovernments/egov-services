package org.egov.works.workorder.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.NoticeDetailHelper;
import org.egov.works.workorder.web.contract.NoticeDetail;
import org.egov.works.workorder.web.contract.NoticeDetailSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeDetailRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_notice_details nod";


    public List<NoticeDetail> searchNoticeDetails(final NoticeDetailSearchContract noticeDetailSearchContract) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (noticeDetailSearchContract.getSortBy() != null
                && !noticeDetailSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(noticeDetailSearchContract.getSortBy());
			validateEntityFieldName(noticeDetailSearchContract.getSortBy(), NoticeDetailHelper.class);
        }

        String orderBy = "order by nod.notice";
        if (noticeDetailSearchContract.getSortBy() != null
                && !noticeDetailSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by nod." + noticeDetailSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (noticeDetailSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("nod.tenantId =:tenantId");
            paramValues.put("tenantId", noticeDetailSearchContract.getTenantId());
        }
        if (noticeDetailSearchContract.getIds() != null) {
            addAnd(params);
            params.append("nod.id in(:ids) ");
            paramValues.put("ids", noticeDetailSearchContract.getIds());
        }


        if (noticeDetailSearchContract.getNotices() != null && !noticeDetailSearchContract.getNotices().isEmpty()) {
            addAnd(params);
            params.append("nod.notice in(:notices)");
            paramValues.put("notices", noticeDetailSearchContract.getNotices());
        }

        params.append(" and nod.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(NoticeDetailHelper.class);

        List<NoticeDetailHelper> noticeDetailHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<NoticeDetail> noticeDetails = new ArrayList<>();

        for (NoticeDetailHelper noticeDetailHelper : noticeDetailHelpers) {
            noticeDetails.add(noticeDetailHelper.toDomain());
        }
        return noticeDetails;
    }
}
