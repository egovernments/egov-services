package org.egov.mseva.repository;

import java.util.Map;

import org.egov.mseva.web.contract.EventSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class MsevaEventsQueryBuilder {
	
	public static final String EVENT_SEARCH_QUERY = "SELECT id, tenantid, source, eventtype, description, status, eventdetails, actions, "
			+ "recepient, createdby, createdtime, lastmodifiedby, lastmodifiedtime FROM eg_men_events ";
	
	public static final String EVENT_INNER_SEARCH_QUERY = "id IN (SELECT eventid FROM eg_men_recepnt_event_registry WHERE ";
	
	public String getSearchQuery(EventSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		String query = EVENT_SEARCH_QUERY;
		return addWhereClause(query, criteria, preparedStatementValues);
	}
	
	private String addWhereClause(String query, EventSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(query);		
		if(!CollectionUtils.isEmpty(criteria.getIds())) {
            addClauseIfRequired(preparedStatementValues, queryBuilder);
			queryBuilder.append("id IN (:id)");
			preparedStatementValues.put("id", criteria.getIds());
		}
		if(!CollectionUtils.isEmpty(criteria.getStatus())) {
            addClauseIfRequired(preparedStatementValues, queryBuilder);
			queryBuilder.append("status IN (:status)");
			preparedStatementValues.put("status", criteria.getStatus());
		}
		if(!CollectionUtils.isEmpty(criteria.getPostedBy())) {
            addClauseIfRequired(preparedStatementValues, queryBuilder);
			queryBuilder.append("AND source IN (:source)");
			preparedStatementValues.put("source", criteria.getPostedBy());
		}

		if(!CollectionUtils.isEmpty(criteria.getRecepients())) {
            addClauseIfRequired(preparedStatementValues, queryBuilder);
            queryBuilder.append(EVENT_INNER_SEARCH_QUERY);
    		queryBuilder.append("recepients IN (:recepients)");
    		preparedStatementValues.put("recepients", criteria.getRecepients());
    		queryBuilder.append(" )");

		}
				
		return queryBuilder.toString();
		
	}
	
    private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
        if (values.isEmpty())
            queryString.append(" WHERE ");
        else {
            queryString.append(" AND");
        }
    }

}
