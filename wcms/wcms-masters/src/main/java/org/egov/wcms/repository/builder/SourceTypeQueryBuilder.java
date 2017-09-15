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

import java.util.List;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.web.contract.SourceTypeGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SourceTypeQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT watersource.id as watersource_id, watersource.code as watersource_code,"
            + " watersource.name as watersource_name, watersource.description as watersource_description,watersource.active as watersource_active, "
            + " watersource.sourcecapacity as watersource_sourcecapacity,watersource.ulbreserved as watersource_ulbreserved ,watersource.tenantId as watersource_tenantId "
            + " FROM egwtr_water_source_type watersource ";

    @SuppressWarnings("rawtypes")
    public String getQuery(final SourceTypeGetRequest waterSourceTypeGetRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, waterSourceTypeGetRequest);
        addOrderByClause(selectQuery, waterSourceTypeGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, waterSourceTypeGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final SourceTypeGetRequest waterSourceTypeGetRequest) {

        if (waterSourceTypeGetRequest.getIds() == null && waterSourceTypeGetRequest.getName() == null
                && waterSourceTypeGetRequest.getActive() == null
                && waterSourceTypeGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (waterSourceTypeGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" watersource.tenantId = ?");
            preparedStatementValues.add(waterSourceTypeGetRequest.getTenantId());
        }

        if (waterSourceTypeGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" watersource.id IN " + getIdQuery(waterSourceTypeGetRequest.getIds()));
        }

        if (waterSourceTypeGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" watersource.name = ?");
            preparedStatementValues.add(waterSourceTypeGetRequest.getName());
        }

        if (waterSourceTypeGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" watersource.code = ?");
            preparedStatementValues.add(waterSourceTypeGetRequest.getCode());
        }

        if (waterSourceTypeGetRequest.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" watersource.active = ?");
            preparedStatementValues.add(waterSourceTypeGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery, final SourceTypeGetRequest waterSourceTypeGetRequest) {
        final String sortBy = waterSourceTypeGetRequest.getSortBy() == null ? "watersource.code"
                : "watersource." + waterSourceTypeGetRequest.getSortBy();
        final String sortOrder = waterSourceTypeGetRequest.getSortOrder() == null ? "DESC"
                : waterSourceTypeGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final SourceTypeGetRequest waterSourceTypeGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
        if (waterSourceTypeGetRequest.getPageSize() != null)
            pageSize = waterSourceTypeGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (waterSourceTypeGetRequest.getPageNumber() != null)
            pageNumber = waterSourceTypeGetRequest.getPageNumber() - 1;
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

    public static String insertWaterSourceTypeQuery() {
        return "INSERT INTO egwtr_water_source_type(id,code,name,description,active,sourcecapacity,ulbreserved,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:name,:description,:active,:sourcecapacity,:ulbreserved,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updateWaterSourceTypeQuery() {
        return "UPDATE egwtr_water_source_type SET name = :name,description = :description ,"
                + "active = :active ,sourcecapacity = :sourcecapacity,ulbreserved = :ulbreserved,lastmodifiedby = :lastmodifiedby ,lastmodifieddate = :lastmodifieddate where code = :code and tenantid = :tenantid  ";
    }

    public static String selectWaterSourceByNameAndCodeQuery() {
        return " select code FROM egwtr_water_source_type where name = ? and tenantId = ?";
    }

    public static String selectWaterSourceByNameAndCodeNotInQuery() {
        return " select code from egwtr_water_source_type where name = ? and tenantId = ? and code != ? ";
    }

    public static String selectSourceTypeByNameQuery() {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        selectQuery.append(" Where watersource.name = ?");
        selectQuery.append(" AND watersource.tenantId = ?");
        return selectQuery.toString();
    }
}
