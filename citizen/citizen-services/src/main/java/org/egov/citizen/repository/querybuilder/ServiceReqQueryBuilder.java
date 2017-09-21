package org.egov.citizen.repository.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceReqQueryBuilder {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ServiceReqQueryBuilder.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String BASE_QUERY = "SELECT jsonvalue as serviceReq, tenantid, id FROM egov_citizen_service_req";
	private static final String SEARCH_DETAILS_BASE_QUERY = "SELECT ec.srn as srn, comment, commentfrom, commentdate, uploadedby, uploaddate, filestoreid, uploadedbyrole "
			+ "FROM egov_citizen_service_req_comments ec JOIN egov_citizen_service_req_documents ed ON ec.srn = ed.srn ";


	@SuppressWarnings("rawtypes")
	public String getQuery(ServiceRequestSearchCriteria serviceRequestSearchCriteria, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, serviceRequestSearchCriteria);
		//addOrderByClause(selectQuery, serviceRequestSearchCriteria);
	//	addPagingClause(selectQuery, preparedStatementValues, serviceRequestSearchCriteria);

		
		log.info("Query : " + selectQuery);
		return selectQuery.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public String getDetailsQuery(ServiceRequestSearchCriteria serviceRequestSearchCriteria, List preparedStatementValuesForDetailsSearch) {
		StringBuilder selectQuery = new StringBuilder(SEARCH_DETAILS_BASE_QUERY);
		addWhereClauseForDetailsSearch(selectQuery, preparedStatementValuesForDetailsSearch, serviceRequestSearchCriteria);
		log.info("Query : " + selectQuery);
		return selectQuery.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public String getDetailsQueryForAnonymous(ServiceRequestSearchCriteria serviceRequestSearchCriteria, List preparedStatementValuesForDetailsSearch) {
		StringBuilder selectQuery = new StringBuilder(SEARCH_DETAILS_BASE_QUERY);
		addWhereClauseForAnonymous(selectQuery, preparedStatementValuesForDetailsSearch, serviceRequestSearchCriteria);
		log.info("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			ServiceRequestSearchCriteria serviceRequestSearchCriteria) {

		if (serviceRequestSearchCriteria.getTenantId() == null){
			LOGGER.info("NO tenant found");
			return;
		}

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (serviceRequestSearchCriteria.getTenantId() != null) {
			isAppendAndClause = true;			
			selectQuery.append(" tenantid = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getTenantId());

		}

		if (serviceRequestSearchCriteria.getServiceRequestId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" id = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getServiceRequestId());
		}
		
		if (serviceRequestSearchCriteria.getConsumerCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" consumercode = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getConsumerCode());
		}
		
		if (serviceRequestSearchCriteria.getServiceCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" servicecode = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getServiceCode());
		}
		
		if (serviceRequestSearchCriteria.getStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" status = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getStatus());
		}
		
		if (serviceRequestSearchCriteria.getAssignedTo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" assignedto = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getAssignedTo());
		}
		System.out.println("serviceRequestSearchCriteria.getUserId():"+serviceRequestSearchCriteria.getUserId());
		if (serviceRequestSearchCriteria.getUserId() != null) {
			System.out.println("serviceRequestSearchCriteria.getUserId():"+serviceRequestSearchCriteria.getUserId());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" userid = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getUserId());

		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClauseForDetailsSearch(StringBuilder selectQuery, List preparedStatementValuesForDetailsSearch,
			ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
		List<String> srnList = new ArrayList<>();
		if (serviceRequestSearchCriteria.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (serviceRequestSearchCriteria.getTenantId() != null) {
			isAppendAndClause = true;			
			selectQuery.append(" ec.tenantid = ?");
			preparedStatementValuesForDetailsSearch.add(serviceRequestSearchCriteria.getTenantId());
		}
		
		if (serviceRequestSearchCriteria.getServiceRequestId() != null) {
			srnList.add(serviceRequestSearchCriteria.getServiceRequestId());
		}
				
		if (serviceRequestSearchCriteria.getConsumerCode() != null) {
			List<String> srNos = jdbcTemplate.queryForList("select id from egov_citizen_service_req where consumercode = ? ", 
					new Object[]{serviceRequestSearchCriteria.getConsumerCode()}, String.class);
			srnList.addAll(srNos);
		}
		
		if (serviceRequestSearchCriteria.getServiceCode() != null) {
			List<String> srNos = jdbcTemplate.queryForList("select id from egov_citizen_service_req where servicecode = ? ", 
					new Object[]{serviceRequestSearchCriteria.getServiceCode()}, String.class);
			srnList.addAll(srNos);
		}
		
		if (serviceRequestSearchCriteria.getStatus()!= null) {
			List<String> srNos = jdbcTemplate.queryForList("select id from egov_citizen_service_req where status = ? ", 
					new Object[]{serviceRequestSearchCriteria.getStatus()}, String.class);
			srnList.addAll(srNos);
		}
		
		if (serviceRequestSearchCriteria.getAssignedTo()!= null) {
			List<String> srNos = jdbcTemplate.queryForList("select id from egov_citizen_service_req where assignedto = ? ", 
					new Object[]{serviceRequestSearchCriteria.getAssignedTo()}, String.class);
			srnList.addAll(srNos);
		}
		LOGGER.info("SRNs to be searched for: "+srnList);
		if(!srnList.isEmpty()){
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ec.srn ilike any "+getNumberQuery(srnList));
		}else{
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ec.srn = 'invalidSrn' ");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClauseForAnonymous(StringBuilder selectQuery, List preparedStatementValues,
			ServiceRequestSearchCriteria serviceRequestSearchCriteria) {

		if (serviceRequestSearchCriteria.getTenantId() == null){
			LOGGER.info("NO tenant found");
			return;
		}

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (serviceRequestSearchCriteria.getTenantId() != null) {
			isAppendAndClause = true;			
			selectQuery.append(" ec.tenantid = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getTenantId());

		}

		if (serviceRequestSearchCriteria.getServiceRequestId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ec.srn = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getServiceRequestId());
		}
		isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
		selectQuery.append(" ed.isfinal = ?");
		preparedStatementValues.add(true);
		isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
		selectQuery.append(" ed.uploadedbyrole != ?");
		preparedStatementValues.add("CITIZEN");

	}

	/*private void addOrderByClause(StringBuilder selectQuery, ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
		String sortBy = (serviceRequestSearchCriteria.getSortBy() == null ? "id"
				: serviceRequestSearchCriteria.getSortBy());
		String sortOrder = (serviceRequestSearchCriteria.getSortOrder() == null ? "ASC"
				: serviceRequestSearchCriteria.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}*/
	
	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 * 
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}
	
	private static String getNumberQuery(List<String> srnList) {
		StringBuilder query = new StringBuilder("(array [");

		if (srnList.size() >= 1) {
			query.append("'%").append(srnList.get(0).toString()).append("%'");
			for (int i = 1; i < srnList.size(); i++) {
				query.append(", '%" + srnList.get(i) + "%'");
			}
		}
		return query.append("])").toString();
	}

}
