package org.egov.wf.repository.querybuilder;

import org.egov.wf.config.WorkflowConfig;
import org.egov.wf.web.models.ProcessInstanceSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Component
public class WorkflowQueryBuilder {

    private WorkflowConfig config;

    @Autowired
    public WorkflowQueryBuilder(WorkflowConfig config) {
        this.config = config;
    }

    private static final String INNER_JOIN_STRING = " INNER JOIN ";
    private static final String LEFT_OUTER_JOIN_STRING = " LEFT OUTER JOIN ";

    private static final String baseQuery = "SELECT pi.*,doc.*,pi.id as wf_id," +
            " pi.lastModifiedTime as wf_lastModifiedTime,pi.createdTime as wf_createdTime," +
            " pi.createdBy as wf_createdBy,pi.lastModifiedBy as wf_lastModifiedBy," +
            " doc.lastModifiedTime as doc_lastModifiedTime,doc.createdTime as doc_createdTime," +
            " doc.createdBy as doc_createdBy,doc.lastModifiedBy as doc_lastModifiedBy," +
            " doc.tenantid as doc_tenantid,doc.id as doc_id ";

    private static final String docQuery = LEFT_OUTER_JOIN_STRING +
            " eg_wf_document doc " +
            " ON doc.processinstanceid = pi.id ";

    private static final String QUERY = "SELECT pi.*,doc.*,pi.id as wf_id," +
            "pi.lastModifiedTime as wf_lastModifiedTime,pi.createdTime as wf_createdTime," +
            "pi.createdBy as wf_createdBy,pi.lastModifiedBy as wf_lastModifiedBy," +
            "doc.lastModifiedTime as doc_lastModifiedTime,doc.createdTime as doc_createdTime," +
            "doc.createdBy as doc_createdBy,doc.lastModifiedBy as doc_lastModifiedBy," +
            "doc.tenantid as doc_tenantid,doc.id as doc_id " +
            " FROM eg_wf_processinstance pi " +
            LEFT_OUTER_JOIN_STRING +
            " eg_wf_document doc " +
            " ON doc.processinstanceid = pi.id WHERE ";

    private final String paginationWrapper = "SELECT * FROM " +
            "(SELECT *, DENSE_RANK() OVER (ORDER BY wf_id) offset_ FROM " +
            "({})" +
            " result) result_offset " +
            "WHERE offset_ > ? AND offset_ <= ?";


    /**
     * Creates the query according to the search params
     * @param criteria The criteria containg fields to search on
     * @param preparedStmtList The List of object to store the search params
     * @return
     */
    public String getTLSearchQuery(ProcessInstanceSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder builder = new StringBuilder(QUERY);

        builder.append(" pi.tenantid=? ");
        preparedStmtList.add(criteria.getTenantId());

        List<String> ids = criteria.getIds();
        if(!CollectionUtils.isEmpty(ids)) {
            builder.append("and tl.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList,ids);
        }

        if(criteria.getBusinessId()!=null){
            builder.append(" and pi.businessId = ? ");
            preparedStmtList.add(criteria.getBusinessId());
        }

        String query = addPaginationWrapper(builder.toString(),preparedStmtList,criteria);
        query = addOrderByCreatedTime(query);

        return query;

    }


    /**
     * Creates preparedStatement
     * @param ids The ids to search on
     * @return Query with prepares statement
     */
    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for( int i = 0; i< length; i++){
            builder.append(" ?");
            if(i != length -1) builder.append(",");
        }
        return builder.toString();
    }


    /**
     * Add ids to preparedStatement list
     * @param preparedStmtList The list containing the values of search params
     * @param ids The ids to be searched
     */
    private void addToPreparedStatement(List<Object> preparedStmtList,List<String> ids)
    {
        ids.forEach(id ->{ preparedStmtList.add(id);});
    }


    /**
     * Wraps pagination around the base query
     * @param query The query for which pagination has to be done
     * @param preparedStmtList The object list to send the params
     * @param criteria The object containg the search params
     * @return Query with pagination
     */
    private String addPaginationWrapper(String query,List<Object> preparedStmtList,
                                        ProcessInstanceSearchCriteria criteria){
        int limit = config.getDefaultLimit();
        int offset = config.getDefaultOffset();
        String finalQuery = paginationWrapper.replace("{}",query);

        if(criteria.getLimit()!=null && criteria.getLimit()<=config.getMaxSearchLimit())
            limit = criteria.getLimit();

        if(criteria.getLimit()!=null && criteria.getLimit()>config.getMaxSearchLimit())
            limit = config.getMaxSearchLimit();

        if(criteria.getOffset()!=null)
            offset = criteria.getOffset();

        preparedStmtList.add(offset);
        preparedStmtList.add(limit+offset);

        return finalQuery;
    }


    /**
     * Adds orderBy clause to the query with limit 1
     * @param query The query to be modified
     * @return Query ordered descending by createTime returning the tp entry
     */
    private String addOrderByCreatedTime(String query){
        StringBuilder builder = new StringBuilder(query);
        builder.append(" ORDER BY result_offset.wf_lastModifiedTime DESC LIMIT 1");
        return builder.toString();
    }

    /**
     * Creates query to search processInstance assigned to user
     * @return search query based on assignee
     */
    public String getAssigneeSearchQuery(ProcessInstanceSearchCriteria criteria, List<Object> preparedStmtList){
        String query = QUERY +" assignee = ? "+
                " AND pi.tenantid = ? " +
                " AND pi.lastmodifiedTime IN  (SELECT max(lastmodifiedTime) from eg_wf_processinstance GROUP BY businessid)";
        preparedStmtList.add(criteria.getAssignee());
        preparedStmtList.add(criteria.getTenantId());
        return query;
    }


    /**
     * Creates query to search processInstance based on user roles
     * @return search query based on assignee
     */
    public String getStatusBasedProcessInstance(ProcessInstanceSearchCriteria criteria, List<Object> preparedStmtList){
        String query = QUERY +" pi.tenantid = ? " +
                " AND pi.lastmodifiedTime IN  (SELECT max(lastmodifiedTime) from eg_wf_processinstance GROUP BY businessid)";

        StringBuilder builder = new StringBuilder(query);
        preparedStmtList.add(criteria.getTenantId());
        List<String> statuses = criteria.getStatus();
        if(!CollectionUtils.isEmpty(statuses)) {
            builder.append(" and status IN (").append(createQuery(statuses)).append(")");
            addToPreparedStatement(preparedStmtList,statuses);
        }

        return builder.toString();
    }








}
