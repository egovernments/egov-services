/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.repository.builder;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.web.contract.CategoryGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConnectionCategoryQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionCategoryQueryBuilder.class);

    private static final String BASE_QUERY = "SELECT category.id as category_id, category.code as category_code,"
            + " category.name as category_name, category.description as category_description,category.active as category_active, category.tenantId as category_tenantId "
            + " FROM egwtr_category category ";

    @SuppressWarnings("rawtypes")
    public String getQuery(CategoryGetRequest categoryGetRequest, List preparedStatementValues) {
        StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, categoryGetRequest);
        addOrderByClause(selectQuery, categoryGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, categoryGetRequest);
        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
                                CategoryGetRequest categoryGetRequest) {

        if (categoryGetRequest.getId() == null && categoryGetRequest.getName() == null && categoryGetRequest.getActive() == null
                && categoryGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (categoryGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" category.tenantId = ?");
            preparedStatementValues.add(categoryGetRequest.getTenantId());
        }

        if (categoryGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" category.id IN " + getIdQuery(categoryGetRequest.getId()));
        }


        if (categoryGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" category.name = ?");
            preparedStatementValues.add(categoryGetRequest.getName());
        }

        if (categoryGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" category.code = ?");
            preparedStatementValues.add(categoryGetRequest.getCode());
        }

        if (categoryGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" category.active = ?");
            preparedStatementValues.add(categoryGetRequest.getActive());
        }
    }

    private void addOrderByClause(StringBuilder selectQuery, CategoryGetRequest categoryGetRequest) {
        String sortBy = (categoryGetRequest.getSortBy() == null ? "category.code"
                : "category." + categoryGetRequest.getSortBy());
        String sortOrder = (categoryGetRequest.getSortOrder() == null ? "DESC" : categoryGetRequest.getSortOrder());
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
                                 CategoryGetRequest categoryGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
        if (categoryGetRequest.getPageSize() != null)
            pageSize = categoryGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (categoryGetRequest.getPageNumber() != null)
            pageNumber = categoryGetRequest.getPageNumber() - 1;
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



    public static String insertCategoryQuery() {
        return "INSERT INTO egwtr_category(id,code,name,description,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateCategoryQuery() {
        return "UPDATE egwtr_category SET name = ?,description = ?,"
                + "active = ?,lastmodifiedby = ?,lastmodifieddate = ? where code = ?";
    }
    public static String selectCategoryByNameAndCodeQuery() {
        return " select code FROM egwtr_category where name = ? and tenantId = ?";
    }


    public static String selectCategoryByNameAndCodeNotInQuery() {
        return " select code from egwtr_category where name = ? and tenantId = ? and code != ? ";
    }


}
