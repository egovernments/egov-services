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

import java.util.Map;

import org.egov.wcms.web.contract.StorageReservoirGetRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StorageReservoirQueryBuilder {

    private static final String BASE_QUERY = "SELECT storagereservoir.id as storagereservoir_id,storagereservoir.code as storagereservoir_code, storagereservoir.name as storagereservoir_name,"
            + "storagereservoir.reservoirtype as storagereservoir_reservoirtype,storagereservoir.location as storagereservoir_location, "
            + " storagereservoir.capacity as storagereservoir_capacity ,"
            + "storagereservoir.noofsublines as storagereservoir_noofsublines,storagereservoir.noofmaindistributionlines as storagereservoir_noofmaindistributionlines ,"
            + "storagereservoir.noofconnection as storagereservoir_noofconnection ,"
            + "storagereservoir.tenantId as storagereservoir_tenantId "
            + "FROM egwtr_storage_reservoir storagereservoir ";

    public String getQuery(final StorageReservoirGetRequest storageReservoirGetRequest,
            @SuppressWarnings("rawtypes") final Map<String, Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        addWhereClause(selectQuery, preparedStatementValues, storageReservoirGetRequest);
        addOrderByClause(selectQuery, storageReservoirGetRequest);
        log.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings("unchecked")
    private void addWhereClause(final StringBuilder selectQuery, final Map<String, Object> preparedStatementValues,
            final StorageReservoirGetRequest storageReservoirGetRequest) {

        if (storageReservoirGetRequest.getIds() == null && storageReservoirGetRequest.getName() == null
                && storageReservoirGetRequest.getReservoirType() == null
                && storageReservoirGetRequest.getLocation() == null
                && storageReservoirGetRequest.getCapacity() == 0 && storageReservoirGetRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (storageReservoirGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" storagereservoir.tenantId = :tenantId");
            preparedStatementValues.put("tenantId", storageReservoirGetRequest.getTenantId());
        }

        if (storageReservoirGetRequest.getIds() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" storagereservoir.id IN (:ids)");
            preparedStatementValues.put("ids", storageReservoirGetRequest.getIds());
        }

        if (storageReservoirGetRequest.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" storagereservoir.name = :name");
            preparedStatementValues.put("name", storageReservoirGetRequest.getName());
        }

        if (storageReservoirGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" storagereservoir.code = :code");
            preparedStatementValues.put("code", storageReservoirGetRequest.getCode());
        }

        if (storageReservoirGetRequest.getReservoirType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" storagereservoir.reservoirtype = :reservoirtype");
            preparedStatementValues.put("reservoirtype", storageReservoirGetRequest.getReservoirType());
        }

        if (storageReservoirGetRequest.getLocation() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" storagereservoir.location = :location");
            preparedStatementValues.put("location", storageReservoirGetRequest.getLocation());
        }

    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final StorageReservoirGetRequest storageReservoirGetRequest) {
        final String sortBy = storageReservoirGetRequest.getSortBy() == null ? "storagereservoir.id"
                : "storagereservoir." + storageReservoirGetRequest.getSortBy();
        final String sortOrder = storageReservoirGetRequest.getSortOrder() == null ? "DESC"
                : storageReservoirGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    public static String insertStorageReserviorQuery() {
        return "INSERT INTO egwtr_storage_reservoir(id,code,name,reservoirtype,location,capacity,noofsublines,noofmaindistributionlines,"
                + "noofconnection,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
                + "(:id,:code,:name,:reservoirtype,:location,:capacity,:noofsublines,"
                + ":noofmaindistributionlines,:noofconnection,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
    }

    public static String updateStorageReserviorQuery() {
        return "UPDATE egwtr_storage_reservoir SET name = :name,reservoirtype = :reservoirtype,location = :location "
                + " , capacity = :capacity,noofsublines = :noofsublines,noofmaindistributionlines = :noofmaindistributionlines,"
                + "noofconnection = :noofconnection,lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code  and tenantid = :tenantid";
    }

    public static String selectStorageResrvoirByNameByCodeQuery() {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        selectQuery.append("where storagereservoir.name= :name and storagereservoir.tenantId = :tenantId");
        return selectQuery.toString();
    }

    public static String selectStorageReservoirByNameByCodeNotInQuery() {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        selectQuery.append(
                "where storagereservoir.name = :name and storagereservoir.tenantId = :tenantId and storagereservoir.code != :code");
        return selectQuery.toString();
    }
}
