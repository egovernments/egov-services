package org.egov.lcms.repository.builder;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.lcms.models.RegisterSearchCriteria;

/** 
 * 
 * @author			Date			eGov-JIRA ticket			Commit message
 * ---------------------------------------------------------------------------
 * Yosadhara	31st Oct 2017								Initial commit of  Register QueryBuilder
 * Yosadhara    03rd Nov 2017                               Modified Register search query condition
 * Yosadhara    08th Nov 2017                               Modified ORDERY BY clause
 */
public class RegisterBuilder {

	private static final String BASE_QUERY = "SELECT * from egov_lcms_register ";
	
	/**
	 * This method is to build SELECT query to serch registers
	 * 
	 * @param registerSearchCriteria
	 * @param preparedStatementValues
	 * @return String
	 */
	public static String getSearchQuery(RegisterSearchCriteria registerSearchCriteria,
			List<Object> preparedStatementValues) {

		StringBuffer selectQuery = new StringBuffer(BASE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, registerSearchCriteria);
		addOrderByClause(selectQuery, registerSearchCriteria);
		addPagingClause(selectQuery, preparedStatementValues, registerSearchCriteria);

		return selectQuery.toString();
	}
	
	/**
	 * This method is to append WHERE clause and condtions to SELECT Query
	 * 
	 * @param selectQuery
	 * @param preparedStatementValues
	 * @param registerSearchCriteria
	 */
	private static void addWhereClause(StringBuffer selectQuery, List<Object> preparedStatementValues,
			RegisterSearchCriteria registerSearchCriteria) {

		if (registerSearchCriteria.getCode() == null && registerSearchCriteria.getRegister() == null
				&& registerSearchCriteria.getTenantId() == null && registerSearchCriteria.getIsActive())
			return;

		selectQuery.append(" WHERE");

		if (registerSearchCriteria.getTenantId() != null) {

			selectQuery.append(" LOWER(tenantId) =LOWER(?)");
			preparedStatementValues.add(registerSearchCriteria.getTenantId());
		}

		if (registerSearchCriteria.getIsActive() != null) {

			selectQuery.append(" AND isActive = ?");
			preparedStatementValues.add(registerSearchCriteria.getIsActive());

		}

		if (registerSearchCriteria.getRegister() != null) {
			
			StringJoiner register = new StringJoiner("", "%", "%");
			register.add(registerSearchCriteria.getRegister());
            selectQuery.append(" AND LOWER(register) LIKE ? ");
            preparedStatementValues.add(register.toString().toLowerCase());

		}

		if (registerSearchCriteria.getCode() != null) {
			
			selectQuery.append(" AND code IN ("
					+ Stream.of(registerSearchCriteria.getCode()).collect(Collectors.joining("','", "'", "'")) + ")");
			
		}
	}
	
	/**
	 * This method is to append ORDER BY clause to SELECT query
	 * 
	 * @param selectQuery
	 * @param registerSearchCriteria
	 */
	private static void addOrderByClause(StringBuffer selectQuery, RegisterSearchCriteria registerSearchCriteria) {

		selectQuery.append(" ORDER BY ");

		if (registerSearchCriteria.getSort() != null && registerSearchCriteria.getSort().length > 0) {

			int orderBycount = 1;
			StringBuffer orderByCondition = new StringBuffer();

			for (String order : registerSearchCriteria.getSort()) {
				if (orderBycount < registerSearchCriteria.getSort().length)
					orderByCondition.append(order + ",");
				else
					orderByCondition.append(order);
				orderBycount++;
			}

			if (orderBycount > 1)
				orderByCondition.append(" desc");

			selectQuery.append(orderByCondition.toString());
		}

		selectQuery.append(" lastmodifiedtime desc ");
	}
	
	/**
	 * This method is to append offset, and limit to SELECT query
	 * 
	 * @param selectQuery
	 * @param preparedStatementValues
	 * @param registerSearchCriteria
	 */
	private static void addPagingClause(StringBuffer selectQuery, List<Object> preparedStatementValues,
			RegisterSearchCriteria registerSearchCriteria) {
		
		int pageNumber = registerSearchCriteria.getPageNumber();
		int pageSize = registerSearchCriteria.getPageSize();
		int offset = 0;
		int limit = 0;			
		limit = pageNumber * pageSize;

		if (pageNumber <= 1)
			offset = (limit - pageSize);
		else
			offset = (limit - pageSize) + 1;

		selectQuery.append(" offset ?  limit ?");
		preparedStatementValues.add(offset);
		preparedStatementValues.add(limit);

	}
}