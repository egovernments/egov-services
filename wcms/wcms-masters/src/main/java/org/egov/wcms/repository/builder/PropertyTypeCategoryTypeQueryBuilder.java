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
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertyTypeCategoryTypeQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT propcategory.id AS propcategory_id ,category.name as categoryName, propcategory.categorytypeid as categoryTypeId, propcategory.propertytypeid as propertyTypeId,"
            + "propcategory.active as propcategory_active,"
            + " propcategory.tenantid as propcategory_tenantId from egwtr_property_category_type  propcategory"
            + " LEFT JOIN egwtr_category category ON propcategory.categorytypeid = category.id";

    @SuppressWarnings("rawtypes")
    public String getQuery(final PropertyCategoryGetRequest propertyCategoryGetRequest,
            final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, propertyCategoryGetRequest);
        if (null != propertyCategoryGetRequest.getSortBy() && null != propertyCategoryGetRequest.getSortOrder())
            addOrderByClause(selectQuery, propertyCategoryGetRequest);
        if (null != propertyCategoryGetRequest.getPageSize() && null != propertyCategoryGetRequest.getPageNumber())
            addPagingClause(selectQuery, preparedStatementValues, propertyCategoryGetRequest);
        log.info("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final PropertyCategoryGetRequest propertyCategoryGetRequest) {

        if (propertyCategoryGetRequest.getId() == null && propertyCategoryGetRequest.getPropertyTypeName() == null
                && propertyCategoryGetRequest.getActive() == null && propertyCategoryGetRequest.getTenantId() == null
                && propertyCategoryGetRequest.getCategoryTypeName() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (propertyCategoryGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" propcategory.tenantid = ?");
            preparedStatementValues.add(propertyCategoryGetRequest.getTenantId());
        }

        if (propertyCategoryGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" propcategory.id IN " + getIdQuery(propertyCategoryGetRequest.getId()));
        }

        if (propertyCategoryGetRequest.getPropertyTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" propcategory.propertytypeid = ?");
            preparedStatementValues.add(propertyCategoryGetRequest.getPropertyTypeId());
        }

        if (propertyCategoryGetRequest.getCategoryTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" category.name = ?");
            preparedStatementValues.add(propertyCategoryGetRequest.getCategoryTypeName());
        }

        if (propertyCategoryGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" propcategory.active = ?");
            preparedStatementValues.add(propertyCategoryGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final PropertyCategoryGetRequest propertyCategoryGetRequest) {
        final String sortBy = propertyCategoryGetRequest.getSortBy() == null ? "id"
                : "" + propertyCategoryGetRequest.getSortBy();
        final String sortOrder = propertyCategoryGetRequest.getSortOrder() == null ? "DESC"
                : propertyCategoryGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final PropertyCategoryGetRequest propertyCategoryGetRequest) {
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

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

    public static String insertPropertyCategoryQuery() {
        return "INSERT INTO egwtr_property_category_type(id,propertytypeid, categorytypeid, active, tenantid, "
                + "createddate, createdby, lastmodifieddate, lastmodifiedby) "
                + "values (nextval('seq_egwtr_property_category_type'),?,?,?,?,?,?,?,?)";
    }

    public static String updatePropertyCategoryQuery() {
        return "UPDATE egwtr_property_category_type SET propertytypeid = ?,categorytypeid = ?,"
                + "active = ?,lastmodifiedby = ?,lastmodifieddate = ? where id = ?";
    }

    public static String getCategoryId() {
        return "SELECT id FROM egwtr_category WHERE name = ?  and tenantId = ?";
    }

    public static String getCategoryTypeName() {
        return "SELECT name FROM egwtr_category WHERE id = ? and tenantId = ?";
    }
    

    public static String selectPropertyByCategoryQuery() {
        return " select id FROM egwtr_property_category_type where propertytypeid = ? and categorytypeid = ? and tenantId = ?";
    }

    public static String selectPropertyByCategoryNotInQuery() {
        return " select id from egwtr_property_category_type where propertytypeid = ? and categorytypeid = ? and tenantId = ? and id != ? ";
    }

}
