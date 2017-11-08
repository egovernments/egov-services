package org.egov.lcms.repository.builder;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.AdvocateSearchCriteria;

public class AdvocateBuilders {
	
	static
	PropertiesManager propertiesManager;
	
	private static final String BASE_QUERY = "SELECT * from egov_lcms_advocate ";
	
	public static String getSearchQuery(AdvocateSearchCriteria advocateSearchCriteria,
			List<Object> preparedStatementValues) {
		
		StringBuffer selectQuery = new StringBuffer(BASE_QUERY);		
		addWhereClause(selectQuery, preparedStatementValues, advocateSearchCriteria);
        addOrderByClause(selectQuery, advocateSearchCriteria);
        addPagingClause(selectQuery, preparedStatementValues, advocateSearchCriteria);		
		
		return selectQuery.toString();
	}

	private static void addWhereClause(StringBuffer selectQuery, List<Object> preparedStatementValues,
			AdvocateSearchCriteria advocateSearchCriteria) {
		
		if (advocateSearchCriteria.getCode() == null && advocateSearchCriteria.getAdvocateName() == null
                && advocateSearchCriteria.getOrganizationName() == null && advocateSearchCriteria.getTenantId() == null
                && advocateSearchCriteria.getIsActive() && advocateSearchCriteria.getIsIndividual() == null)
            return;
		
		selectQuery.append(" WHERE");        
		if (advocateSearchCriteria.getTenantId() != null) {
		
            selectQuery.append(" tenantid =LOWER(?)");
            preparedStatementValues.add(advocateSearchCriteria.getTenantId());
		}
		
		if (advocateSearchCriteria.getIsIndividual() != null) {
			
            selectQuery.append(" AND isindividual = ?");
            preparedStatementValues.add(advocateSearchCriteria.getIsIndividual());			
		}
		
		if (advocateSearchCriteria.getAdvocateName() != null) {
			
			StringJoiner advocateName = new StringJoiner("", "%", "%");
			advocateName.add(advocateSearchCriteria.getAdvocateName());
            selectQuery.append(" AND LOWER(name) LIKE ? ");
            preparedStatementValues.add(advocateName.toString().toLowerCase());			
		}
		
		if (advocateSearchCriteria.getOrganizationName() != null) {
			
			StringJoiner organizationName = new StringJoiner("", "%", "%");
			organizationName.add(advocateSearchCriteria.getOrganizationName());
            selectQuery.append(" AND LOWER(organizationname) LIKE ?");
            preparedStatementValues.add(organizationName.toString().toLowerCase());			
		}
		
		if (advocateSearchCriteria.getIsActive() != null) {
			
            selectQuery.append(" AND isactive = ?");
            preparedStatementValues.add(advocateSearchCriteria.getIsActive());			
		}
		
		if (advocateSearchCriteria.getCode() != null) {
			
			selectQuery.append(" AND code IN ("
					+ Stream.of(advocateSearchCriteria.getCode()).collect(Collectors.joining("','", "'", "'")) + ")");			
		}		
	}

	private static void addOrderByClause(StringBuffer selectQuery, AdvocateSearchCriteria advocateSearchCriteria) {

		selectQuery.append(" ORDER BY ");

		if (advocateSearchCriteria.getSort() != null && advocateSearchCriteria.getSort().length > 0) {

			int orderBycount = 1;
			StringBuffer orderByCondition = new StringBuffer();
			
			for (String order : advocateSearchCriteria.getSort()) {
				if (orderBycount < advocateSearchCriteria.getSort().length)
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

	private static void addPagingClause(StringBuffer selectQuery, List<Object> preparedStatementValues,
			AdvocateSearchCriteria advocateSearchCriteria) {
		
		int pageNumber = advocateSearchCriteria.getPageNumber();
		int pageSize = advocateSearchCriteria.getPageSize();
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
	
	
	public static final String SEARCH_CASE_CODE_BY_ADVOCATE ="select casecode from egov_lcms_case_advocate where advocate->>'code'=?";
}
