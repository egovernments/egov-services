package org.egov.property.repository.builder;

import org.egov.models.NoticeSearchCriteria;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class NoticeQueryBuilder {

    public String getInsertQuery() {
        return "INSERT INTO egpt_notice (tenantid, applicationnumber, noticedate, noticenumber, noticetype, upicnumber, fileStoreId, createdtime, createdby)"
                + "VALUES (:tenantid, :applicationnumber, :noticedate, :noticenumber, :noticetype, :upicnumber, :fileStoreId, :createdtime, :createdby)";
    }

    public String getSearchQuery(NoticeSearchCriteria searchCriteria){
        StringBuilder query = new StringBuilder("SELECT * FROM egpt_notice WHERE tenantid = :tenantid");

        if(!isEmpty(searchCriteria.getUpicNumber()))
            addWhereClauseWithAnd(query,"upicnumber", "upicnumber");

        if(!isEmpty(searchCriteria.getApplicationNo()))
            addWhereClauseWithAnd(query,"applicationnumber", "applicationnumber");

        if(!isEmpty(searchCriteria.getNoticeType()))
            addWhereClauseWithAnd(query,"noticetype", "noticetype");

        if(!isEmpty(searchCriteria.getNoticeDate()))
            addWhereClauseWithAnd(query,"noticedate", "noticedate");

        if(!isEmpty(searchCriteria.getFromDate()) && !isEmpty(searchCriteria.getToDate()))
            addFromDateAndToDateWithAnd(query, searchCriteria);

        addPagingClause(query, searchCriteria);

        return query.toString();
    }

    private void addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
         query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }

    private void addFromDateAndToDateWithAnd(StringBuilder query, NoticeSearchCriteria searchCriteria){
        query.append(" AND").append(" createdtime <= ").append("= :").append(searchCriteria.getFromDate())
                .append(" AND").append(" createdtime >= ").append("= :").append(searchCriteria.getToDate());
    }

    private void addPagingClause(final StringBuilder query,
                                 final NoticeSearchCriteria searchCriteria) {

        int pageSize = 0;
        if (searchCriteria.getPageSize() != null) {
            // handle limit(also called pageSize) here
            query.append(" LIMIT ");
            pageSize = searchCriteria.getPageSize();
            query.append(pageSize);
        }

        // handle offset here
        if (searchCriteria.getPageNumber() != null) {
            query.append(" OFFSET ");
            int pageNumber = 0; // Default pageNo is zero meaning first page
            pageNumber = searchCriteria.getPageNumber() - 1;
            query.append(pageNumber * pageSize);
        }
    }
}
