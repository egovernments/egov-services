package org.egov.repository.querybuilder;

import java.util.List;

import org.egov.config.ApplicationProperties;
import org.egov.model.criteria.DisposalCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DisposalQueryBuilder {
	
	@Autowired
	private ApplicationProperties applicationProperties;



	public String BASE_QUERY = "SELECT * FROM egasset_disposal as disposal";

	@SuppressWarnings("rawtypes")
	public String getQuery(final DisposalCriteria disposalCriteria, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		log.info("get query");
		addWhereClause(selectQuery, preparedStatementValues, disposalCriteria);
		addPagingClause(selectQuery, preparedStatementValues, disposalCriteria);
		log.info("Query from asset querybuilde for search : " + selectQuery);
		log.info("pstmtvalues : " + preparedStatementValues);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final DisposalCriteria disposalCriteria) {

		System.out.println("revaluationCriteria>>" + disposalCriteria);
		boolean isAppendAndClause = false;

		selectQuery.append(" WHERE");

		if (disposalCriteria.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.tenantId = ?");
			preparedStatementValues.add(disposalCriteria.getTenantId());
		}

		if (disposalCriteria.getId() == null && disposalCriteria.getAssetId() == null)
			return;

		if (disposalCriteria.getId() != null && !disposalCriteria.getId().isEmpty()) {
			
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.id IN (" + getIdQuery(disposalCriteria.getId()));
		}
		if (disposalCriteria.getAssetId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.assetid IN (" + getIdQuery(disposalCriteria.getAssetId()));
		}
		if(disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() != null){
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.disposalDate BETWEEN "+disposalCriteria.getFromDate() +" AND "+disposalCriteria.getToDate());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final DisposalCriteria disposalCriteria) {
		// handle limit(also called pageSize) here

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.getSearchPageSizeDefault());

		if (disposalCriteria.getSize() != null)
			pageSize = disposalCriteria.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (disposalCriteria.getOffset() != null)
		   	pageNumber = disposalCriteria.getOffset() < 1 ? 0 : disposalCriteria.getOffset() - 1;
        preparedStatementValues.add( pageNumber * pageSize); 
	/*		pageNumber = disposalCriteria.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize);*/ // Set offset to
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
		if (!idList.isEmpty()) {
			query = new StringBuilder(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++)
				query.append("," + idList.get(i));
		}
		return query.append(")").toString();
	}

}
