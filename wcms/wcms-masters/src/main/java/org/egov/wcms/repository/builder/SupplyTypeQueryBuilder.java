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
import org.egov.wcms.web.contract.SupplyTypeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SupplyTypeQueryBuilder {

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT supplytype.id as supplytype_id, supplytype.code as supplytype_code,"
            + " supplytype.name as supplytype_name, supplytype.description as supplytype_description,supplytype.active as supplytype_active,"
            + " supplytype.tenantId as supplytype_tenantId "
            + " FROM egwtr_supply_type supplytype ";

    @SuppressWarnings("rawtypes")
    public String getQuery(final SupplyTypeGetRequest supplyGetRequest, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, supplyGetRequest);
        addOrderByClause(selectQuery, supplyGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, supplyGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final SupplyTypeGetRequest supplyGetRequest) {

        if (supplyGetRequest.getIds() == null && supplyGetRequest.getName() == null && supplyGetRequest.getActive() == null
                && supplyGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");

        if (supplyGetRequest.getTenantId() != null) {
            selectQuery.append(" supplytype.tenantId = ?");
            preparedStatementValues.add(supplyGetRequest.getTenantId());
        }

        if (supplyGetRequest.getIds() != null) {
            selectQuery.append(" AND supplytype.id IN " + getIdQuery(supplyGetRequest.getIds()));
        }

        if (supplyGetRequest.getName() != null) {
            selectQuery.append(" AND supplytype.name = ?");
            preparedStatementValues.add(supplyGetRequest.getName());
        }

        if (supplyGetRequest.getCode() != null) {
            selectQuery.append(" AND supplytype.code = ?");
            preparedStatementValues.add(supplyGetRequest.getCode());
        }

        if (supplyGetRequest.getActive() != null) {
            selectQuery.append(" AND supplytype.active = ?");
            preparedStatementValues.add(supplyGetRequest.getActive());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery, final SupplyTypeGetRequest supplyGetRequest) {
        final String sortBy = supplyGetRequest.getSortBy() == null ? "supplytype.code"
                : "supplytype." + supplyGetRequest.getSortBy();
        final String sortOrder = supplyGetRequest.getSortOrder() == null ? "DESC" : supplyGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final SupplyTypeGetRequest supplyGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.wcmsSearchPageSizeDefault());
        if (supplyGetRequest.getPageSize() != null)
            pageSize = supplyGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (supplyGetRequest.getPageNumber() != null)
            pageNumber = supplyGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
        // pageNo * pageSize
    }

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (!idList.isEmpty()) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

    public static String insertSupplyTypeQuery() {
        return "INSERT INTO egwtr_supply_type(id,code,name,description,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:name,:description,:active,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updateSupplyTypeQuery() {
        return "UPDATE egwtr_supply_type SET name = :name , description = :description ,"
                + "active = :active ,lastmodifiedby = :lastmodifiedby ,lastmodifieddate = :lastmodifieddate "
                + "where code = :code and tenantid = :tenantid ";
    }

    public static String selectSupplyTypeByNameAndCodeQuery() {
        return " select code FROM egwtr_supply_type where name = ?"
                + " and tenantId = ?";
    }

    public static String selectSupplyTypeByNameAndCodeNotInQuery() {
        return " select code from egwtr_supply_type where name = ? and"
                + " tenantId = ? and code != ? ";
    }

    public static String selectSupplytypeByNameQuery() {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        selectQuery.append(" Where supplytype.name = ?");
        selectQuery.append(" AND supplytype.tenantId = ?");
        return selectQuery.toString();
    }

}
