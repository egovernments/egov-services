package org.egov.mseva.repository;

import java.util.Map;

import org.egov.mseva.web.contract.EventSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class MsevaEventsQueryBuilder {
	
	public static final String EVENT_SEARCH_QUERY = "SELECT id, tenantid, source, eventtype, description, status, eventdetails, actions, "
			+ "recepient, createdby, createdtime, lastmodifiedby, lastmodifiedtime FROM eg_men_events WHERE id IN "
			+ "(SELECT eventid FROM eg_men_recepnt_event_registry";
	
	public String getSearchQuery(EventSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		String query = EVENT_SEARCH_QUERY;
		return addWhereClause(query, criteria, preparedStatementValues);
	}
	
	private String addWhereClause(String query, EventSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(query);
		queryBuilder.append("WHERE recepients IN (:recepients)");
		preparedStatementValues.put("recepients", criteria.getRecepients());
		queryBuilder.append(" )");
		
		return queryBuilder.toString();
		
	}

}
