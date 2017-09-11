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

import org.egov.wcms.web.contract.PropertyTypePipeSizeGetRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertyPipeSizeQueryBuilder {

    private static final String BASE_QUERY = "SELECT propertypipesize.id as propertypipesize_id,propertypipesize.code as propertypipesize_code, propertypipesize.propertytypeid "
            + "as propertypipesize_propertytypeId, pipesize.sizeinmilimeter as pipesize_sizeinmm,pipesize.sizeininch as pipesize_sizeininch ,"
            + "propertypipesize.pipesizeid as propertypipesize_pipesizeId,propertypipesize.active as propertypipesize_active, "
            + "propertypipesize.tenantId as propertypipesize_tenantId "
            + " FROM egwtr_property_pipe_size propertypipesize LEFT JOIN egwtr_pipesize pipesize ON propertypipesize.pipesizeid = pipesize.id";

    
    public String getQuery(final PropertyTypePipeSizeGetRequest propertyPipeSizeGetRequest,
            final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, propertyPipeSizeGetRequest);
        addOrderByClause(selectQuery, propertyPipeSizeGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("unchecked")
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final PropertyTypePipeSizeGetRequest propertyPipeSizeGetRequest) {

        if (propertyPipeSizeGetRequest.getId() == null && propertyPipeSizeGetRequest.getPropertyTypeName() == null &&
                propertyPipeSizeGetRequest.getPipeSize() == null && propertyPipeSizeGetRequest.getActive() == null
                && propertyPipeSizeGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (propertyPipeSizeGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" propertypipesize.tenantId = ?");
            preparedStatementValues.add(propertyPipeSizeGetRequest.getTenantId());
        }

        if (propertyPipeSizeGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" propertypipesize.id IN " + getIdQuery(propertyPipeSizeGetRequest.getId()));
        }

        if (propertyPipeSizeGetRequest.getPipeSize() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" pipesize.sizeinmilimeter = ?");
            preparedStatementValues.add(propertyPipeSizeGetRequest.getPipeSize());
        }

        if (propertyPipeSizeGetRequest.getPropertyTypeName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" propertypipesize.propertytypeid = ?");
            preparedStatementValues.add(propertyPipeSizeGetRequest.getPropertyTypeId());
        }

        if (propertyPipeSizeGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" propertypipesize.active = ?");
            preparedStatementValues.add(propertyPipeSizeGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final PropertyTypePipeSizeGetRequest propertyPipeSizeGetRequest) {
        final String sortBy = propertyPipeSizeGetRequest.getSortBy() == null ? "propertypipesize.id"
                : "propertypipesize." + propertyPipeSizeGetRequest.getSortBy();
        final String sortOrder = propertyPipeSizeGetRequest.getSortOrder() == null ? "DESC"
                : propertyPipeSizeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
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

    public static String insertPropertyPipeSizeQuery() {
        return "INSERT INTO egwtr_property_pipe_size(id,code,pipesizeid,propertytypeid,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:pipesizeid,:propertytypeid,:active,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updatePropertyPipeSizeQuery() {
        return "UPDATE egwtr_property_pipe_size SET pipesizeid = :pipesizeid,propertytypeid = :propertytypeid,"
                + "active = :active,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code ";
    }

    public static String selectPropertyByPipeSizeQuery() {
        return " select code FROM egwtr_property_pipe_size where propertytypeid = ? and pipesizeid = ? and tenantId = ?";
    }

    public static String selectPropertyByPipeSizeNotInQuery() {
        return " select code from egwtr_property_pipe_size where propertytypeid = ? and pipesizeid = ? and tenantId = ? and code != ? ";
    }

    public static String getPipeSizeIdQuery() {
        return " select id FROM egwtr_pipesize where sizeinmilimeter= ? and tenantId = ?";
    }

    public static String getPipeSizeInmm() {
        return "SELECT sizeinmilimeter FROM egwtr_pipesize WHERE id = ? and tenantId = ? ";
    }

}
