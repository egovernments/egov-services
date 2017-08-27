package org.egov.citizen.repository.querybuilder;

import java.util.List;

import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceReqQueryBuilder {

	private static final String BASE_QUERY = "SELECT jsonvalue as serviceReq, tenantid, id FROM egov_citizen_service_req";

	@SuppressWarnings("rawtypes")
	public String getQuery(ServiceRequestSearchCriteria serviceRequestSearchCriteria, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, serviceRequestSearchCriteria);
		//addOrderByClause(selectQuery, serviceRequestSearchCriteria);
	//	addPagingClause(selectQuery, preparedStatementValues, serviceRequestSearchCriteria);

		log.info("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			ServiceRequestSearchCriteria serviceRequestSearchCriteria) {

		if (serviceRequestSearchCriteria.getTenantId() == null)
			return;

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
		System.out.println("serviceRequestSearchCriteria.getUserId():"+serviceRequestSearchCriteria.getUserId());
		if (serviceRequestSearchCriteria.getUserId() != null) {
			System.out.println("serviceRequestSearchCriteria.getUserId():"+serviceRequestSearchCriteria.getUserId());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" userid = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getUserId());

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
