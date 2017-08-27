package org.egov.wcms.repository.builder;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.web.contract.CategoryTypeGetRequest;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GapcodeQueryBuilder {
	
	    public static String insertQuery() {
	        return "INSERT INTO egwtr_gapcode(id,code,name,outSideUlb,noOfLastMonths,logic,active,description,createdBy,lastUpdatedBy,createdDate,lastUpdatedDate,tenantId) values "
	                + "(:id,:code,:name,:outSideUlb,:noOfLastMonths,:logic,:active,:description,:createdBy,:lastUpdatedBy,:createdDate,:lastUpdatedDate,:tenantId)";
	    }
	    
	    public static String updateQuery() {
	        return "UPDATE egwtr_gapcode SET name = :name ,outSideUlb = :outSideUlb ,"
	                + "noOfLastMonths = :noOfLastMonths,logic = :logic,description = :description,active = :active,lastUpdatedBy = :lastUpdatedBy,lastupdatedDate = :lastUpdatedDate where code = :code ";
	    }
	    
	    public static String getFormulaQuery() {
	        return "SELECT code,name FROM egwtr_gapcode";
	    }
	    
	    @Autowired
	    private ApplicationProperties applicationProperties;

	    private static final String BASE_QUERY = "SELECT id, code, name,"
	            + " outSideUlb, noOfLastMonths,logic,description,active,createdDate,lastUpdatedDate,createdBy,lastUpdatedBy,tenantId "
	            + " FROM egwtr_gapcode";

	    @SuppressWarnings("rawtypes")
	    public String getQuery(final GapcodeGetRequest gapcodeGetRequest, final List preparedStatementValues) {
	        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
	        addWhereClause(selectQuery, preparedStatementValues, gapcodeGetRequest);
	        addOrderByClause(selectQuery, gapcodeGetRequest);
	        addPagingClause(selectQuery, preparedStatementValues, gapcodeGetRequest);
	        log.debug("Query : " + selectQuery);
	        return selectQuery.toString();
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
	            final GapcodeGetRequest gapcodeGetRequest) {

	        if (gapcodeGetRequest.getCode() == null && gapcodeGetRequest.getTenantId() == null)
	            return;

	        selectQuery.append(" WHERE");
	        boolean isAppendAndClause = false;

	        if (gapcodeGetRequest.getTenantId() != null) {
	            isAppendAndClause = true;
	            selectQuery.append(" tenantId = ?");
	            preparedStatementValues.add(gapcodeGetRequest.getTenantId());
	        }

	        if (gapcodeGetRequest.getId() != null) {
	            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
	            selectQuery.append(" id = ?");
	            preparedStatementValues.add(gapcodeGetRequest.getId());
	        }
	        
	        if (gapcodeGetRequest.getCode() != null) {
	            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
	            selectQuery.append(" code = ?");
	            preparedStatementValues.add(gapcodeGetRequest.getCode());
	        }
	        
	        if (gapcodeGetRequest.getName() != null) {
	            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
	            selectQuery.append(" name = ?");
	            preparedStatementValues.add(gapcodeGetRequest.getName());
	        }
	        
	    }

	    private void addOrderByClause(final StringBuilder selectQuery, final GapcodeGetRequest gapcodeGetRequest) {
	        final String sortBy = gapcodeGetRequest.getSortBy() == null ? "code"
	                : gapcodeGetRequest.getSortBy();
	        final String sortOrder = gapcodeGetRequest.getSortOrder() == null ? "DESC" : gapcodeGetRequest.getSortOrder();
	        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
	            final GapcodeGetRequest gapcodeGetRequest) {
	        // handle limit(also called pageSize) here
	        selectQuery.append(" LIMIT ?");
	        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
	        if (gapcodeGetRequest.getPageSize() != null)
	            pageSize = gapcodeGetRequest.getPageSize();
	        preparedStatementValues.add(pageSize); // Set limit to pageSize

	        // handle offset here
	        selectQuery.append(" OFFSET ?");
	        int pageNumber = 0; // Default pageNo is zero meaning first page
	        if (gapcodeGetRequest.getPageNumber() != null)
	            pageNumber = gapcodeGetRequest.getPageNumber() - 1;
	        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
	        // pageNo * pageSize
	    }

	    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
	        if (appendAndClauseFlag)
	            queryString.append(" AND");

	        return true;
	    }
}
