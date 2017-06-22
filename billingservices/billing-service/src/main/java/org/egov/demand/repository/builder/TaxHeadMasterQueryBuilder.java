package org.egov.demand.repository.builder;

import java.util.List;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaxHeadMasterQueryBuilder {
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	private static final Logger logger = LoggerFactory.getLogger(TaxHeadMasterQueryBuilder.class);

	private static final String BASE_QUERY="select * from egbs_taxheadmaster";
	public String getQuery(final TaxHeadMasterCriteria searchTaxHead, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		logger.info("get query");
	    addWhereClause(selectQuery, preparedStatementValues, searchTaxHead);
		addPagingClause(selectQuery, preparedStatementValues, searchTaxHead);
		logger.info("Query from asset querybuilde for search : " + selectQuery);
		return selectQuery.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final TaxHeadMasterCriteria searchTaxHead) {
		if(searchTaxHead.getTenantId()==null&&searchTaxHead.getService()==null&&searchTaxHead.getName()==null
				&&searchTaxHead.getCode()==null&&searchTaxHead.getCategory()==null)
			return;
		
		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;
		
		if (searchTaxHead.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" tenantId = ?");
			preparedStatementValues.add(searchTaxHead.getTenantId());
		}
		if (searchTaxHead.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" name like ?");
			preparedStatementValues.add("%" + searchTaxHead.getName() + "%");
		}
		
		if (searchTaxHead.getService() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" service = ?");
			preparedStatementValues.add(searchTaxHead.getService());
		}
		if (searchTaxHead.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" code = ?");
			preparedStatementValues.add("%" + searchTaxHead.getCode() + "%");
		}
		if (searchTaxHead.getCategory() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" category = ?");
			preparedStatementValues.add("%" + searchTaxHead.getCategory() + "%");
		}
		if (searchTaxHead.getIsActualDemand() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" isActualDemand = ?");
			preparedStatementValues.add("%" + searchTaxHead.getIsActualDemand() + "%");
		}
		if (searchTaxHead.getIsDebit() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" isDebit = ?");
			preparedStatementValues.add("%" + searchTaxHead.getIsDebit() + "%");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final TaxHeadMasterCriteria searchTaxHeads) {
		
		selectQuery.append(" ORDER BY name");

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
		if (searchTaxHeads.getSize() != null)
			pageSize = searchTaxHeads.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (searchTaxHeads.getOffset() != null)
			pageNumber = searchTaxHeads.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}
	
	public String getUpdateQuery() {
		return "UPDATE public.egbs_taxheadmaster SET "
				+ "category=?, service=?, name=?, code=?, glcode=?,isdebit=?, isactualdemand=?,"
				+ "lastmodifiedby=?, taxperiod=?, lastmodifiedtime=? "
				+ "WHERE tenantid=?";

	}
	
	public String getInsertQuery() {
		return "INSERT INTO public.egbs_taxheadmaster(id, tenantid, category,"
				+ "service, name, code, glcode, isdebit,isactualdemand, createdby, createdtime,"
				+ "lastmodifiedby, lastmodifiedtime,taxperiod) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?);";
	}
	
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");
		return true;
	}
}
