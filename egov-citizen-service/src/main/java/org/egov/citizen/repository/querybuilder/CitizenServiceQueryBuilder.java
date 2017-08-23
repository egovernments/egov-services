package org.egov.citizen.repository.querybuilder;

import java.util.List;

import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CitizenServiceQueryBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(CitizenServiceQueryBuilder.class);


	private static final String BASE_QUERY = "SELECT jsonvalue as servicerequest, tenantid, id FROM egov_citizen_service_req";

	@SuppressWarnings("rawtypes")
	public String getQuery(ServiceRequestSearchCriteria serviceRequestSearchCriteria, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, serviceRequestSearchCriteria);
		addOrderByClause(selectQuery, serviceRequestSearchCriteria);
	//	addPagingClause(selectQuery, preparedStatementValues, serviceRequestSearchCriteria);

		logger.info("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			ServiceRequestSearchCriteria serviceRequestSearchCriteria) {

		if (serviceRequestSearchCriteria.getServiceRequestId() == null && serviceRequestSearchCriteria.getTenantId() == null)
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

		if (serviceRequestSearchCriteria.getUserId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" userid = ?");
			preparedStatementValues.add(serviceRequestSearchCriteria.getUserId());

		}
	}

	private void addOrderByClause(StringBuilder selectQuery, ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
		String sortBy = (serviceRequestSearchCriteria.getSortBy() == null ? "id"
				: serviceRequestSearchCriteria.getSortBy());
		String sortOrder = (serviceRequestSearchCriteria.getSortOrder() == null ? "ASC"
				: serviceRequestSearchCriteria.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

/*	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = 500l;
		//FIXME GET PAGESIZE FROM APPLICATION PROPERTIEs 
		//Integer.parseInt(applicationProperties.collectionSearchPageSizeDefault());
		if (collectionConfigGetRequest.getPageSize() != null)
			pageSize = collectionConfigGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (collectionConfigGetRequest.getPageNumber() != null)
			pageNumber = collectionConfigGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to pageNo * pageSize
	} */

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

/*	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = new StringBuilder("(");
		if (!idList.isEmpty()) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append(", " + idList.get(i));
			}
		}
		return query.append(")").toString();
	} */

}
