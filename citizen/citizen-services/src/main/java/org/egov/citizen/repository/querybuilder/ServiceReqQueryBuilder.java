package org.egov.citizen.repository.querybuilder;

import java.util.List;

import org.egov.citizen.service.CitizenPersistService;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceReqQueryBuilder {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ServiceReqQueryBuilder.class);
	
	private static final String BASE_QUERY = "SELECT jsonvalue as serviceReq, tenantid, id FROM egov_citizen_service_req";
	private static final String SEARCH_DETAILS_BASE_QUERY = "SELECT comment, commentfrom, commentaddressedto, commentdate, filestoreid "
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
	public String getDetailsQuery(ServiceRequestSearchCriteria serviceRequestSearchCriteria, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(SEARCH_DETAILS_BASE_QUERY);
		addWhereClauseForDetailsSearch(selectQuery, preparedStatementValues, serviceRequestSearchCriteria);
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
			LOGGER.info("tenantd id appended");

		}

		if (serviceRequestSearchCriteria.getServiceRequestId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" id = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getServiceRequestId());
			LOGGER.info("svc req id appended");
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
	private void addWhereClauseForDetailsSearch(StringBuilder selectQuery, List preparedStatementValues,
			ServiceRequestSearchCriteria serviceRequestSearchCriteria) {

		if (serviceRequestSearchCriteria.getTenantId() == null)
			return;

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

}
