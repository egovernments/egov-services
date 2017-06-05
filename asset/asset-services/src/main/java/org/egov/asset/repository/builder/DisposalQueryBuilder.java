package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.DisposalCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DisposalQueryBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(RevaluationQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	
	public static final String INSERT_QUERY = "INSERT into egasset_disposal " 
	  + "(id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,"
	  + "aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,"
	  + "createdby,createddate,lastmodifiedby,lastmodifieddate)"
	  + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public String BASE_QUERY = "SELECT "
			+ "id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,"
			+ "aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,"
			+ "createdby,createddate,lastmodifiedby,lastmodifieddate FROM"
			+ " egasset_disposal as disposal";
	
	@SuppressWarnings("rawtypes")
	public String getQuery(DisposalCriteria disposalCriteria, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		logger.info("get query");
		addWhereClause(selectQuery, preparedStatementValues, disposalCriteria);
		addPagingClause(selectQuery, preparedStatementValues, disposalCriteria);
		logger.info("Query from asset querybuilde for search : " + selectQuery);
		return selectQuery.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues, DisposalCriteria disposalCriteria) {

		System.out.println("revaluationCriteria>>"+disposalCriteria);
		boolean isAppendAndClause = false;
		
		selectQuery.append(" WHERE");
	
		if (disposalCriteria.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.tenantId = ?");
			preparedStatementValues.add(disposalCriteria.getTenantId());
		}
		
		if (disposalCriteria.getId() == null && disposalCriteria.getAssetId() == null)
			return;
		
		if (disposalCriteria.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.id IN (" + getIdQuery(disposalCriteria.getId()));
		}
		if (disposalCriteria.getAssetId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.assetid IN (" + getIdQuery(disposalCriteria.getAssetId()));
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,DisposalCriteria disposalCriteria) {
		// handle limit(also called pageSize) here
		

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
		if (disposalCriteria.getSize() != null)
			pageSize = disposalCriteria.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (disposalCriteria.getOffset() != null)
			pageNumber = disposalCriteria.getOffset() - 1;
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
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");
		return true;
	}

	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = null;
		if (idList.size() >= 1) {
			query = new StringBuilder(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + idList.get(i));
			}
		}
		return query.append(")").toString();
	}
	  

}
