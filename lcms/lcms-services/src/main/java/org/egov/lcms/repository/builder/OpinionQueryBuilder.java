package org.egov.lcms.repository.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/** 
 * 
 * @author			Date			eGov-JIRA ticket			Commit message
 * ---------------------------------------------------------------------------
 * Prasad   	28th Oct 2017								Initial commit of  Opinion QueryBuilder
 * Prasad       02nd Nov 2017                               Modified search condition for list of codes
 * Prasad       03rd Nov 2017								Modified opinion search criteria condition
 * Prasad       08th Nov 2017                               Added ORDER BY clause
 * Prasad       10th Nov 2017                               Added search caseNo SELECT query * 
 */
@Component
@Slf4j
public class OpinionQueryBuilder {

	@Autowired
	PropertiesManager propertiesManager;

	public static final String BASE_SEARCH_QUERY = "select * from " + ConstantUtility.OPINION_TABLE_NAME + " WHERE";
	
	/**
	 * This method is to build SELECT query to serch Opinion
	 * 
	 * @param opinionSearchCriteria
	 * @param preparedStatementValues
	 * @return String
	 * @throws Exception
	 */
	public String getOpinionSearchQuery(final OpinionSearchCriteria opinionSearchCriteria,
			final List<Object> preparedStatementValues) throws Exception {
		final StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, opinionSearchCriteria);
		addOrderByClause(selectQuery, preparedStatementValues, opinionSearchCriteria);
		addPagingClause(selectQuery, preparedStatementValues, opinionSearchCriteria);
		log.info("selectQuery::" + selectQuery);
		log.info("preparedstmt values : " + preparedStatementValues);
		return selectQuery.toString();
	}
	
	/**
	 * This method is to append WHERE clause and condtions to SELECT Query
	 * 
	 * @param selectQuery
	 * @param preparedStatementValues
	 * @param opinionSearchCriteria
	 * @throws Exception
	 */
	private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final OpinionSearchCriteria opinionSearchCriteria) throws Exception {

		if (opinionSearchCriteria.getCodes() == null && opinionSearchCriteria.getTenantId() == null
				&& opinionSearchCriteria.getDepartmentName() == null
				&& opinionSearchCriteria.getOpinionRequestDate() == null
				&& opinionSearchCriteria.getOpinionsBy() == null)
			return;

		selectQuery.append(" tenantId=?");
		preparedStatementValues.add(opinionSearchCriteria.getTenantId());

		String[] codes = opinionSearchCriteria.getCodes();
		if (codes != null && codes.length > 0) {
			selectQuery.append(" AND code IN ("
					+ Stream.of(opinionSearchCriteria.getCodes()).collect(Collectors.joining("','", "'", "'")) + ")");
		}

		if (opinionSearchCriteria.getOpinionRequestDate() != null) {
			selectQuery.append(" AND opinionRequestDate=?");
			preparedStatementValues.add(opinionSearchCriteria.getOpinionRequestDate());
		}
		if (opinionSearchCriteria.getDepartmentName() != null && !opinionSearchCriteria.getDepartmentName().isEmpty()) {
			selectQuery.append(" AND departmentName->>'code'=?");
			preparedStatementValues.add(opinionSearchCriteria.getDepartmentName());
		}

		if (opinionSearchCriteria.getOpinionsBy() != null && !opinionSearchCriteria.getOpinionsBy().isEmpty()) {
			selectQuery.append(" AND opinionsby->>'code'=?");
			preparedStatementValues.add(opinionSearchCriteria.getOpinionsBy());
		}

		if (opinionSearchCriteria.getFromDate() != null) {
			selectQuery.append(" AND opinionRequestDate >= ?");
			preparedStatementValues.add(opinionSearchCriteria.getFromDate());
		}

		if (opinionSearchCriteria.getToDate() != null) {
			selectQuery.append(" AND opinionRequestDate <= ?");
			preparedStatementValues.add(opinionSearchCriteria.getToDate());
		}
	}
	
	/**
	 * This method is to append ORDER BY clause to SELECT query
	 * 
	 * @param selectQuery
	 * @param preparedStatementValues
	 * @param opinionSearchCriteria
	 */
	private void addOrderByClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final OpinionSearchCriteria opinionSearchCriteria) {
		String sort = opinionSearchCriteria.getSort();
		
		if (sort != null && !sort.isEmpty()) {
			selectQuery.append(" ORDER BY " + sort + " DESC");
		} else {
			selectQuery.append(" ORDER BY " + propertiesManager.getLastModifiedTime() + " DESC");
		}
	}
	
	/**
	 * This method is to append offset, and limit to SELECT query
	 * 
	 * @param selectQuery
	 * @param preparedStatementValues
	 * @param opinionSearchCriteria
	 */
	private void addPagingClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final OpinionSearchCriteria opinionSearchCriteria) {

		if (opinionSearchCriteria.getPageNumber() != null && opinionSearchCriteria.getPageSize() != null) {
			int offset = 0;
			int limit = opinionSearchCriteria.getPageNumber() * opinionSearchCriteria.getPageSize();

			if (opinionSearchCriteria.getPageNumber() <= 1)
				offset = (limit - opinionSearchCriteria.getPageSize());
			else
				offset = (limit - opinionSearchCriteria.getPageSize()) + 1;

			selectQuery.append(" offset ?  limit ?");
			preparedStatementValues.add(offset);
			preparedStatementValues.add(limit);
		}
	}
	
	/**
	 * This method is to build SELECT query to fetch caseNo
	 * 
	 * @param summonReferenceNo
	 * @param tenantId
	 * @param preparedStatementValues
	 * @return String
	 */
	public String getCaseNo(String summonReferenceNo, String tenantId, List<Object> preparedStatementValues) {

		StringBuilder searchQuery = new StringBuilder();

		searchQuery.append("SELECT caseno FROM " + ConstantUtility.CASE_TABLE_NAME);

		searchQuery.append(" WHERE tenantId=?");
		preparedStatementValues.add(tenantId);

		searchQuery.append(" AND summonreferenceno=?");
		preparedStatementValues.add(summonReferenceNo);

		return searchQuery.toString();
	}
}
