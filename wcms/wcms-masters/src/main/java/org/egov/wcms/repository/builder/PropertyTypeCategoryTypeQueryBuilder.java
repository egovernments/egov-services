/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wcms.repository.builder;

import java.util.List;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.web.contract.CategoryGetRequest;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PropertyTypeCategoryTypeQueryBuilder {
	
    private static final Logger logger = LoggerFactory.getLogger(ConnectionCategoryQueryBuilder.class);

	
	  @Autowired
	    private ApplicationProperties applicationProperties;

	   
	    private static final String BASE_QUERY = "SELECT id, property_type_Id, category_type_Id, active, tenantId"
	            + " FROM egwtr_property_category_type";

	    @SuppressWarnings("rawtypes")
	    public String getQuery(PropertyCategoryGetRequest propertyCategoryGetRequest, List preparedStatementValues) {
	    	StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
	    	addWhereClause(selectQuery, preparedStatementValues, propertyCategoryGetRequest);
	        if(null != propertyCategoryGetRequest.getSortBy() && null != propertyCategoryGetRequest.getSortOrder()){
	        addOrderByClause(selectQuery, propertyCategoryGetRequest);
	        }
	        if(null != propertyCategoryGetRequest.getPageSize() && null != propertyCategoryGetRequest.getPageNumber()){
	        addPagingClause(selectQuery, preparedStatementValues, propertyCategoryGetRequest);
	        }
	        logger.info("Query : " + selectQuery);
	        return selectQuery.toString();
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
	    		PropertyCategoryGetRequest propertyCategoryGetRequest) {

	        if (propertyCategoryGetRequest.getId() == null && propertyCategoryGetRequest.getPropertyType() == null && propertyCategoryGetRequest.getActive() == null
	                && propertyCategoryGetRequest.getTenantId() == null && propertyCategoryGetRequest.getCategoryType() == null)
	            return;

	        selectQuery.append(" WHERE");
	        boolean isAppendAndClause = false;

	        if (propertyCategoryGetRequest.getTenantId() != null) {
	            isAppendAndClause = true;
	            selectQuery.append(" tenantId = ?");
	            preparedStatementValues.add(propertyCategoryGetRequest.getTenantId());
	        }

	        if (propertyCategoryGetRequest.getId() != null) {
	            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
	            selectQuery.append(" id IN " + getIdQuery(propertyCategoryGetRequest.getId()));
	        }


	        if (propertyCategoryGetRequest.getPropertyType() != null) {
	            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
	            selectQuery.append(" property_type_Id = ?");
	            preparedStatementValues.add(propertyCategoryGetRequest.getPropertyTypeId());
	        }

	        if (propertyCategoryGetRequest.getCategoryType() != null) {
	            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
	            selectQuery.append(" category_type_Id = ?");
	            preparedStatementValues.add(propertyCategoryGetRequest.getCategoryTypeId());
	        }

	        if (propertyCategoryGetRequest.getActive() != null) {
	            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
	            selectQuery.append(" active = ?");
	            preparedStatementValues.add(propertyCategoryGetRequest.getActive());
	        }
	    }

	    private void addOrderByClause(StringBuilder selectQuery, PropertyCategoryGetRequest propertyCategoryGetRequest) {
	        String sortBy = (propertyCategoryGetRequest.getSortBy() == null ? "id"
	                : ""+propertyCategoryGetRequest.getSortBy());
	        String sortOrder = (propertyCategoryGetRequest.getSortOrder() == null ? "DESC" : propertyCategoryGetRequest.getSortOrder());
	        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
	    		PropertyCategoryGetRequest propertyCategoryGetRequest) {
	        // handle limit(also called pageSize) here
	        selectQuery.append(" LIMIT ?");
	        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
	        if (propertyCategoryGetRequest.getPageSize() != null)
	            pageSize = propertyCategoryGetRequest.getPageSize();
	        preparedStatementValues.add(pageSize); // Set limit to pageSize

	        // handle offset here
	        selectQuery.append(" OFFSET ?");
	        int pageNumber = 0; // Default pageNo is zero meaning first page
	        if (propertyCategoryGetRequest.getPageNumber() != null)
	            pageNumber = propertyCategoryGetRequest.getPageNumber() - 1;
	        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
	        // pageNo * pageSize
	    }
	    
	    /**
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
	        StringBuilder query = new StringBuilder("(");
	        if (idList.size() >= 1) {
	            query.append(idList.get(0).toString());
	            for (int i = 1; i < idList.size(); i++) {
	                query.append(", " + idList.get(i));
	            }
	        }
	        return query.append(")").toString();
	    }

	  
	  
	  
	  	public static String insertPropertyCategoryQuery() {
	        return "INSERT INTO egwtr_property_category_type(property_type_Id, category_type_Id, active, tenantid, "
	        		+ "createddate, createdby, lastmodifieddate, lastmodifiedby) "
	        		+ "values (?,?,?,?,?,?,?,?)";
	    }
	  	
	  	public static String getCategoryId(){
	  		return "SELECT id FROM egwtr_category WHERE name = ?";
	  	}
	  	
	  	public static String getCategoryTypeName(){
	  		return "SELECT name FROM egwtr_category WHERE id = ?";
	  	}
	  	
	  	public static String getCheckQuery(){
	  		return "SELECT id from egwtr_property_category_type where property_type_Id = ? AND category_type_Id = ?"
	  				+ "AND tenantId = ?";
	  	}
}
