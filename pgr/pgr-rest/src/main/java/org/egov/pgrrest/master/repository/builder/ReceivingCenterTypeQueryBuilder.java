package org.egov.pgrrest.master.repository.builder;

import java.util.List;

import org.egov.pgrrest.master.web.contract.ReceivingCenterTypeGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReceivingCenterTypeQueryBuilder {
	
    private static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeQueryBuilder.class);
    
    
    private static final String BASE_QUERY = "SELECT centerType.id,centerType.code,centerType.name,centerType.description,centerType.active,centerType.tenantId,centerType.visible from egpgr_receivingcenter_type centerType";
    
    @SuppressWarnings("rawtypes")
    public String getQuery(final ReceivingCenterTypeGetReq centerTypeRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, centerTypeRequest);
        addOrderByClause(selectQuery, centerTypeRequest);
        addPagingClause(selectQuery, preparedStatementValues, centerTypeRequest);
        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final ReceivingCenterTypeGetReq centerTypeRequest) {

        if (centerTypeRequest.getId() == null && centerTypeRequest.getName() == null && centerTypeRequest.getActive() == null
                && centerTypeRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (centerTypeRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" centerType.tenantId = ?");
            preparedStatementValues.add(centerTypeRequest.getTenantId());
        }

        if (centerTypeRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" centerType.id IN " + getIdQuery(centerTypeRequest.getId()));
        }

        if (centerTypeRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" centerType.name = ?");
            preparedStatementValues.add(centerTypeRequest.getName());
        }

        if (centerTypeRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" centerType.code = ?");
            preparedStatementValues.add(centerTypeRequest.getCode());
        }

        if (centerTypeRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" centerType.active = ?");
            preparedStatementValues.add(centerTypeRequest.getActive());
        }
    }
    
    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
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
    
    private void addOrderByClause(final StringBuilder selectQuery, final ReceivingCenterTypeGetReq centerTypeGetRequest) {
        final String sortBy = centerTypeGetRequest.getSortBy() == null ? "centerType.code"
                : "centerType." + centerTypeGetRequest.getSortBy();
        final String sortOrder = centerTypeGetRequest.getSortOrder() == null ? "DESC" : centerTypeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }
   
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final ReceivingCenterTypeGetReq centerTypeGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt("100");
        if (centerTypeGetRequest.getPageSize() != null)
            pageSize = centerTypeGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (centerTypeGetRequest.getPageNumber() != null)
            pageNumber = centerTypeGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }

           
    public static String insertReceivingCenterTypeQuery() {
        return "INSERT INTO egpgr_receivingcenter_type(code,name,description,active,visible,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateReceivingCenterTypeQuery() {
        return "UPDATE egpgr_receivingcenter_type SET name = ?,description = ?,"
                + "active = ?,lastmodifiedby = ?,lastmodifieddate = ?,visible=? where code = ?";
    }
    

}
