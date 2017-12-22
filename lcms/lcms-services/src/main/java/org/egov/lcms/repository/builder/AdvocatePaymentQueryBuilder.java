package org.egov.lcms.repository.builder;


import java.util.List;

import org.egov.lcms.models.AdvocatePaymentSearchCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/** 
 * 
 * @author			Date			eGov-JIRA ticket			Commit message
 * ---------------------------------------------------------------------------
 * Shubham		28th Oct 2017								Initial commit of  AdvocatePayment QueryBuilder
 * Shubham      01st Nov 2017                               Modified table name
 * Shubham      03rd Nov 2017                               Modified SELECT query condition
 * Shubham      04th Nov 2017                               Added SELECT query changes
 * Shubham      08th Nov 2017                               Modified ORDER BY clause based on lastmodifiedtime
 */
@Component
@Slf4j
public class AdvocatePaymentQueryBuilder {

    private static final String SELECT_BASE_QUERY = "SELECT * FROM egov_lcms_advocate_payment";
    
    /**
     * This method is to build SELECT query to search Advocate payment
     * 
     * @param advocatePaymentSearchCriteria
     * @param preparedStatementValues
     * @return String
     */
    public String getQuery(final AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria,
            final List<Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(SELECT_BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, advocatePaymentSearchCriteria);
        addOrderByClause(selectQuery, advocatePaymentSearchCriteria);
        addPagingClause(selectQuery, preparedStatementValues, advocatePaymentSearchCriteria);
        log.info("selectQuery::" + selectQuery);
        log.info("preparedstmt values : " + preparedStatementValues);
        return selectQuery.toString();
    }
    
    /**
     * This method is to append WHERE clause and condtions to SELECT Query 
     * 
     * @param selectQuery
     * @param preparedStatementValues
     * @param advocatePaymentSearchCriteria
     */
    private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria) {

        if (advocatePaymentSearchCriteria.getAdvocateName() == null && advocatePaymentSearchCriteria.getCode() == null
                && advocatePaymentSearchCriteria.getFromDate() == null && advocatePaymentSearchCriteria.getTenantId() == null
                && advocatePaymentSearchCriteria.getToDate() == null )
            return;

        selectQuery.append(" WHERE");
        

        if (advocatePaymentSearchCriteria.getTenantId() != null && !advocatePaymentSearchCriteria.getTenantId().isEmpty()) {
            selectQuery.append(" tenantid = ?");
            preparedStatementValues.add(advocatePaymentSearchCriteria.getTenantId());
        }
        
        
        if (advocatePaymentSearchCriteria.getCode() != null && advocatePaymentSearchCriteria.getCode().length > 0) {

			String searchCodes = "";
			int count = 1;
			for (String code : advocatePaymentSearchCriteria.getCode()) {

				if (count < advocatePaymentSearchCriteria.getCode().length)
					searchCodes = searchCodes + "'" + code.trim().toUpperCase() + "',";
				else
					searchCodes = searchCodes +"'" +code.trim().toUpperCase() +"'";

				count++;
			}
			selectQuery.append(" AND upper(code) IN (" + searchCodes + ") ");
		}
        
        if (advocatePaymentSearchCriteria.getFromDate() != null ) {
			
			selectQuery.append(" AND demanddate >= ?");
			preparedStatementValues.add(advocatePaymentSearchCriteria.getFromDate());
		}

		if (advocatePaymentSearchCriteria.getToDate() != null ) {
			
			
			selectQuery.append(" AND demanddate <= ?");
			preparedStatementValues.add(advocatePaymentSearchCriteria.getToDate());
		}


        if (advocatePaymentSearchCriteria.getAdvocateName() != null && !advocatePaymentSearchCriteria.getAdvocateName().isEmpty()) {
            selectQuery.append(" AND advocate->>'code'= ?");
            preparedStatementValues.add(advocatePaymentSearchCriteria.getAdvocateName());
        }

    }

    /**
     * This method is to append offset, and limit to SELECT query
     * 
     * @param selectQuery
     * @param preparedStatementValues
     * @param advocatePaymentSearchCriteria
     */
    private void addPagingClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria) {
    	
    	if(advocatePaymentSearchCriteria.getPageNumber() != null && advocatePaymentSearchCriteria.getPageSize() != null){
    	int offset = 0;
    	int limit = advocatePaymentSearchCriteria.getPageNumber() * advocatePaymentSearchCriteria.getPageSize();

    	if (advocatePaymentSearchCriteria.getPageNumber() <= 1)
    	offset = (limit - advocatePaymentSearchCriteria.getPageSize());
    	else
    	offset = (limit - advocatePaymentSearchCriteria.getPageSize()) + 1;

    	selectQuery.append(" offset ?  limit ?");
    	preparedStatementValues.add(offset);
    	preparedStatementValues.add(limit);
    	}
        
    	
    }
    
    /**
     * This method is to append ORDER BY clause to SELECT query
     * 
     * @param selectQuery
     * @param advocatePaymentSearchCriteria
     */
    private void addOrderByClause(final StringBuilder selectQuery, final AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria) {
    	if(advocatePaymentSearchCriteria.getSort() != null && !advocatePaymentSearchCriteria.getSort().isEmpty()){
    		selectQuery.append(" ORDER BY "+advocatePaymentSearchCriteria.getSort());	
    	}
    	else
    	{
    		selectQuery.append(" ORDER BY lastmodifiedtime DESC");
    	}
    }
}
