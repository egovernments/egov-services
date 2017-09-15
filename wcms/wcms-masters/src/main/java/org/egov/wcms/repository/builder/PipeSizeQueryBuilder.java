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
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PipeSizeQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT pipesize.id as pipesize_id, pipesize.code as pipesize_code,"
            + " pipesize.sizeinmilimeter as pipesize_sizeinmilimeter, pipesize.sizeininch as pipesize_sizeininch,pipesize.active as pipesize_active, "
            + " pipesize.description as pipesize_description,pipesize.tenantId as pipesize_tenantId "
            + " FROM egwtr_pipesize pipesize ";

    @SuppressWarnings("rawtypes")
    public String getQuery(final PipeSizeGetRequest pipeSizeGetRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, pipeSizeGetRequest);
        addOrderByClause(selectQuery, pipeSizeGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, pipeSizeGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final PipeSizeGetRequest pipeSizeGetRequest) {

        if (pipeSizeGetRequest.getIds() == null && pipeSizeGetRequest.getSizeInMilimeter() == 0
                && pipeSizeGetRequest.getActive() == null && pipeSizeGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (pipeSizeGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" pipesize.tenantId = ?");
            preparedStatementValues.add(pipeSizeGetRequest.getTenantId());
        }

        if (pipeSizeGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" pipesize.id IN " + getIdQuery(pipeSizeGetRequest.getIds()));
        }

        if (pipeSizeGetRequest.getSizeInMilimeter() != 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" pipesize.sizeinmilimeter = ?");
            preparedStatementValues.add(pipeSizeGetRequest.getSizeInMilimeter());
        }

        if (pipeSizeGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" pipesize.code = ?");
            preparedStatementValues.add(pipeSizeGetRequest.getCode());
        }

        if (pipeSizeGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" pipesize.active = ?");
            preparedStatementValues.add(pipeSizeGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery, final PipeSizeGetRequest pipeSizeGetRequest) {
        final String sortBy = pipeSizeGetRequest.getSortBy() == null ? "pipesize.code"
                : "pipesize." + pipeSizeGetRequest.getSortBy();
        final String sortOrder = pipeSizeGetRequest.getSortOrder() == null ? "DESC" : pipeSizeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final PipeSizeGetRequest pipeSizeGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
        if (pipeSizeGetRequest.getPageSize() != null)
            pageSize = pipeSizeGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (pipeSizeGetRequest.getPageNumber() != null)
            pageNumber = pipeSizeGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }

    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
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

    public static String insertPipeSizeQuery() {
        return "INSERT INTO egwtr_pipesize(id,code,sizeinmilimeter,sizeininch,description,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:sizeinmilimeter,:sizeininch,:description,:active,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updatePipeSizeQuery() {
        return "UPDATE egwtr_pipesize SET sizeinmilimeter = :sizeinmilimeter,sizeininch = :sizeininch,description = :description ,"
                + "active = :active,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code and tenantid = :tenantid ";
    }

    public static String selectPipeSizeInmmAndCodeQuery() {
        return " select code FROM egwtr_pipesize where sizeinmilimeter = ? and tenantId = ?";
    }

    public static String selectPipeSizeInmmAndCodeNotInQuery() {
        return " select code from egwtr_pipesize where sizeinmilimeter = ? and tenantId = ? and code != ? ";
    }

    public static String selectPipesizeByCodeQuery() {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        selectQuery.append(" Where pipesize.code = ?");
        selectQuery.append(" AND pipesize.tenantId = ?");
        return selectQuery.toString();
    }

    public static String selectPipeSizeIdQuery() {
        return " select * FROM egwtr_pipesize where id = ?";
    }

}
