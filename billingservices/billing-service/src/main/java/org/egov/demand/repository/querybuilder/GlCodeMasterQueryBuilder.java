package org.egov.demand.repository.querybuilder;

import java.util.List;
import java.util.Set;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlCodeMasterQueryBuilder {

	@Autowired
	private ApplicationProperties applicationProperties;
	
	private static final String BASE_QUERY="select * from egbs_glcodemaster";
	public String getQuery(final GlCodeMasterCriteria searchGlCode, final List<Object> preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		log.info(":::::::get query:::::::");
	    addWhereClause(selectQuery, preparedStatementValues, searchGlCode);
		addPagingClause(selectQuery, preparedStatementValues, searchGlCode);
		log.info("Query from asset querybuilde for search : " + selectQuery);
		return selectQuery.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final GlCodeMasterCriteria searchGlCode) {
		if(searchGlCode.getTenantId()==null&&searchGlCode.getService()==null&&searchGlCode.getTaxHead()==null
				&&searchGlCode.getFromDate()==null&&searchGlCode.getToDate()==null && searchGlCode.getId()==null)
			return;
		boolean isAppendAndClause = false;
		selectQuery.append(" WHERE ");
		
		if (searchGlCode.getTenantId()!=null) {
			isAppendAndClause=true;
            selectQuery.append("tenantId = ? ");
            preparedStatementValues.add(searchGlCode.getTenantId());
        }
		
		if (searchGlCode.getService() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" service = ?");
			preparedStatementValues.add(searchGlCode.getService());
		}
		
		if (searchGlCode.getId()!=null && !searchGlCode.getId().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" id IN (" + getInQuery(searchGlCode.getId()));
		}else if (searchGlCode.getTaxHead()!=null && !searchGlCode.getTaxHead().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" taxhead IN (" + getInQuery(searchGlCode.getTaxHead()));
		}
		
		if (searchGlCode.getFromDate() != null && searchGlCode.getToDate() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" fromdate >= ?");
			preparedStatementValues.add(searchGlCode.getFromDate());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" todate <= ?");
			preparedStatementValues.add(searchGlCode.getToDate());
		}
		
	}
	
	
	private static String getInQuery(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {

			String[] list = idList.toArray(new String[idList.size()]);
			query.append("'"+list[0]+"'");
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + "'"+list[i]+"'");
			}
		}
		return query.append(")").toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final GlCodeMasterCriteria searchTaxHeads) {
		
		selectQuery.append(" ORDER BY fromdate");

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
	
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append("AND");
		return true;
	}
}
