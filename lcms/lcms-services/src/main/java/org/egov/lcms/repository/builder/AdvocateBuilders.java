package org.egov.lcms.repository.builder;

import java.util.List;
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
		
            selectQuery.append(" LOWER(tenantId) =LOWER(?)");
            preparedStatementValues.add(advocateSearchCriteria.getTenantId());
		}
		
		if (advocateSearchCriteria.getIsIndividual() != null) {
			
            selectQuery.append(" AND isindividual = ?");
            preparedStatementValues.add(advocateSearchCriteria.getIsIndividual());			
		}
		
		if (advocateSearchCriteria.getAdvocateName() != null) {
		
            selectQuery.append(" AND LOWER(name) = LOWER(?)");
            preparedStatementValues.add(advocateSearchCriteria.getAdvocateName());			
		}
		
		if (advocateSearchCriteria.getOrganizationName() != null) {
			
            selectQuery.append(" AND organizationname = ?");
            preparedStatementValues.add(advocateSearchCriteria.getOrganizationName());			
		}
		
		if (advocateSearchCriteria.getIsActive() != null) {
			
            selectQuery.append(" isactive = ?");
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
				orderByCondition.append(" asc");

			selectQuery.append(orderByCondition.toString());
		}
		
		selectQuery.append(" code ");
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
}
