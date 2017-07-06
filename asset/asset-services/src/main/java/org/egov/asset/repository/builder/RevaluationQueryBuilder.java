package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.RevaluationCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RevaluationQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(RevaluationQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	public static final String INSERT_QUERY = "INSERT into egasset_revalution "
			+ "(id,tenantid,assetid,currentcapitalizedvalue,typeofchange,revaluationamount,valueafterrevaluation,"
			+ "revaluationdate,reevaluatedby,reasonforrevaluation,fixedassetswrittenoffaccount,"
			+ "function,fund,scheme,subscheme,comments,status,createdby,createddate,lastmodifiedby,lastmodifieddate,voucherreference)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public String BASE_QUERY = "SELECT "
			+ "id,tenantid,assetid,currentcapitalizedvalue,typeofchange,revaluationamount,valueafterrevaluation,"
			+ "revaluationdate,reevaluatedby,reasonforrevaluation,fixedassetswrittenoffaccount,"
			+ "function,fund,scheme,subscheme,comments,status,createdby,createddate,lastmodifiedby,lastmodifieddate,voucherreference FROM"
			+ " egasset_revalution as revalution";

	@SuppressWarnings("rawtypes")
	public String getQuery(final RevaluationCriteria revaluationCriteria, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		logger.info("get query");
		addWhereClause(selectQuery, preparedStatementValues, revaluationCriteria);
		addPagingClause(selectQuery, preparedStatementValues, revaluationCriteria);
		logger.info("Query from asset querybuilde for search : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final RevaluationCriteria revaluationCriteria) {

		System.out.println("revaluationCriteria>>" + revaluationCriteria);
		boolean isAppendAndClause = false;

		selectQuery.append(" WHERE");
		if (revaluationCriteria.getStatus() != null) {
			isAppendAndClause = true;
			selectQuery.append(" revalution.status = ?");
			preparedStatementValues.add(revaluationCriteria.getStatus());
		} else {
			isAppendAndClause = true;
			selectQuery.append(" revalution.status = ?");
			preparedStatementValues.add("ACTIVE");
		}

		if (revaluationCriteria.getId() == null && revaluationCriteria.getAssetId() == null)
			return;

		if (revaluationCriteria.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" revalution.tenantId = ?");
			preparedStatementValues.add(revaluationCriteria.getTenantId());
		}

		if (revaluationCriteria.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" revalution.id IN (" + getIdQuery(revaluationCriteria.getId()));
		}

		if (revaluationCriteria.getAssetId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" revalution.assetid IN (" + getIdQuery(revaluationCriteria.getAssetId()));
		}
		if(revaluationCriteria.getFromDate() != null && revaluationCriteria.getToDate() != null){
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" revalution.revaluationDate BETWEEN "+revaluationCriteria.getFromDate() +" AND "+revaluationCriteria.getToDate());
		}

		selectQuery.append(" ORDER BY revalution.revaluationdate desc");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final RevaluationCriteria revaluationCriteria) {
		// handle limit(also called pageSize) here

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
		if (revaluationCriteria.getSize() != null)
			pageSize = revaluationCriteria.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (revaluationCriteria.getOffset() != null)
			pageNumber = revaluationCriteria.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}

	/**
	 *
	 *
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
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
		StringBuilder query = null;
		if (idList.size() >= 1) {
			query = new StringBuilder(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++)
				query.append("," + idList.get(i));
		}
		return query.append(")").toString();
	}

}
