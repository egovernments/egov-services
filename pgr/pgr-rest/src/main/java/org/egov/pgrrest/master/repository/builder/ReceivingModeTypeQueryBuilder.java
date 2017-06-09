package org.egov.pgrrest.master.repository.builder;

import java.util.List;

import org.egov.pgrrest.master.web.contract.ReceivingModeTypeGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReceivingModeTypeQueryBuilder {
	
    private static final Logger logger = LoggerFactory.getLogger(ReceivingModeTypeQueryBuilder.class);
    
 private static final String BASE_QUERY = "SELECT modeType.id,modeType.code,modeType.name,modeType.description,modeType.active,modeType.tenantId,modeType.visible from egpgr_receivingmode_type modeType";
    
    @SuppressWarnings("rawtypes")
    public String getQuery(final ReceivingModeTypeGetReq modeTypeRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, modeTypeRequest);
        addOrderByClause(selectQuery, modeTypeRequest);
        addPagingClause(selectQuery, preparedStatementValues, modeTypeRequest);
        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final ReceivingModeTypeGetReq modeTypeRequest) {

        if (modeTypeRequest.getId() == null && modeTypeRequest.getName() == null && modeTypeRequest.getActive() == null
                && modeTypeRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (modeTypeRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" modeType.tenantId = ?");
            preparedStatementValues.add(modeTypeRequest.getTenantId());
        }

        if (modeTypeRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" modeType.id IN " + getIdQuery(modeTypeRequest.getId()));
        }

        if (modeTypeRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" modeType.name = ?");
            preparedStatementValues.add(modeTypeRequest.getName());
        }

        if (modeTypeRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" modeType.code = ?");
            preparedStatementValues.add(modeTypeRequest.getCode());
        }

        if (modeTypeRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" modeType.active = ?");
            preparedStatementValues.add(modeTypeRequest.getActive());
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
    
    private void addOrderByClause(final StringBuilder selectQuery, final ReceivingModeTypeGetReq modeTypeGetRequest) {
        final String sortBy = modeTypeGetRequest.getSortBy() == null ? "modeType.code"
                : "modeType." + modeTypeGetRequest.getSortBy();
        final String sortOrder = modeTypeGetRequest.getSortOrder() == null ? "DESC" : modeTypeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }
   
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final ReceivingModeTypeGetReq modeTypeGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt("100");
        if (modeTypeGetRequest.getPageSize() != null)
            pageSize = modeTypeGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (modeTypeGetRequest.getPageNumber() != null)
            pageNumber = modeTypeGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }

           
    public static String insertReceivingModeTypeQuery() {
        return "INSERT INTO egpgr_receivingmode_type(id,code,name,description,active,visible,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateReceivingModeTypeQuery() {
        return "UPDATE egpgr_receivingmode_type SET name = ?,description = ?,"
                + "active = ?,lastmodifiedby = ?,lastmodifieddate = ?,visible=? where code = ?";
    }
    


}
