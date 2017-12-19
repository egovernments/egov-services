package org.egov.dataupload.repository;

import java.util.List;

import org.egov.dataupload.model.JobSearchRequest;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUploadQueryBuilder {
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadQueryBuilder.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getQuery(final JobSearchRequest jobSearchRequest, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder("select * from egdu_uploadregistry");
		logger.info("get query");
		addWhereClause(selectQuery, preparedStatementValues, jobSearchRequest);
		logger.debug("Query for job search : " + selectQuery);
		return selectQuery.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final JobSearchRequest jobSearchRequest) {

		if(null == jobSearchRequest.getTenantId()){
			throw new CustomException("400", "No tenantId available");
		}
		selectQuery.append("WHERE TENANTID = ?");
		preparedStatementValues.add(jobSearchRequest.getTenantId());

		if (null != jobSearchRequest.getCodes() && !jobSearchRequest.getCodes().isEmpty()) {
			selectQuery.append(" AND code IN (" + getIdQuery(jobSearchRequest.getCodes()) + ")");
		}
		
		if (null != jobSearchRequest.getStatuses() && !jobSearchRequest.getStatuses().isEmpty()) {
			selectQuery.append(" AND status IN (" + getIdQuery(jobSearchRequest.getStatuses()) + ")");
		}
		
		if (null != jobSearchRequest.getRequesterNames() && !jobSearchRequest.getRequesterNames().isEmpty()) {
			selectQuery.append(" AND requesterName IN (" + getIdQuery(jobSearchRequest.getRequesterNames()) + ")");
		}

		if (jobSearchRequest.getStartDate() != null) {
			selectQuery.append(" AND startTime>?");
			preparedStatementValues.add(jobSearchRequest.getStartDate());
		}
		
		if (jobSearchRequest.getEndDate() != null) {
			selectQuery.append(" AND endTime<?");
			preparedStatementValues.add(jobSearchRequest.getEndDate());
		}

	}
	
	private String getIdQuery(final List<String> codes) {
		StringBuilder query = new StringBuilder();
		query.append("'").append(codes.get(0)).append("'");
		for(int i = 1; i < codes.size(); i++){
			query.append(",").append("'").append(codes.get(0)).append("'");
		}
		
		return query.toString();
	}

}
