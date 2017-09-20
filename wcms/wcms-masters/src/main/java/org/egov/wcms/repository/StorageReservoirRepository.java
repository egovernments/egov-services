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
package org.egov.wcms.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.StorageReservoir;
import org.egov.wcms.repository.builder.StorageReservoirQueryBuilder;
import org.egov.wcms.repository.rowmapper.StorageReservoirRowMapper;
import org.egov.wcms.web.contract.StorageReservoirGetRequest;
import org.egov.wcms.web.contract.StorageReservoirRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class StorageReservoirRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private StorageReservoirRowMapper storageReservoirRowMapper;

    @Autowired
    private StorageReservoirQueryBuilder storageReservoirQueryBuilder;

    public StorageReservoirRequest create(
            final StorageReservoirRequest storageReservoirRequest) {
        log.info("storageReservoirRequest::" + storageReservoirRequest);
        final String storageReservoirInsert = StorageReservoirQueryBuilder.insertStorageReserviorQuery();
        final List<Map<String, Object>> batchValues = new ArrayList<>(storageReservoirRequest.getStorageReservoirs().size());
        for (final StorageReservoir storageReservoir : storageReservoirRequest.getStorageReservoirs())
            batchValues.add(new MapSqlParameterSource("id", Long.valueOf(storageReservoir.getCode()))
                    .addValue("code", storageReservoir.getCode()).addValue("name", storageReservoir.getName())
                    .addValue("reservoirtype", storageReservoir.getReservoirType())
                    .addValue("location", storageReservoir.getLocation())
                    .addValue("capacity", storageReservoir.getCapacity())
                    .addValue("noofsublines", storageReservoir.getNoOfSubLines())
                    .addValue("noofmaindistributionlines", storageReservoir.getNoOfMainDistributionLines())
                    .addValue("noofconnection", storageReservoir.getNoOfConnection())
                    .addValue("createdby", Long.valueOf(storageReservoirRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifiedby",
                            Long.valueOf(storageReservoirRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("createddate", new Date(new java.util.Date().getTime()))
                    .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                    .addValue("tenantid", storageReservoir.getTenantId()).getValues());

        namedParameterJdbcTemplate.batchUpdate(storageReservoirInsert,
                batchValues.toArray(new Map[storageReservoirRequest.getStorageReservoirs().size()]));
        return storageReservoirRequest;
    }

    public StorageReservoirRequest update(
            final StorageReservoirRequest storageReservoirRequest) {
        log.info("StorageReservoir Request::" + storageReservoirRequest);
        final String storageReservoirUpdate = StorageReservoirQueryBuilder.updateStorageReserviorQuery();
        final List<Map<String, Object>> batchValues = new ArrayList<>(storageReservoirRequest.getStorageReservoirs().size());
        for (final StorageReservoir storageReservoir : storageReservoirRequest.getStorageReservoirs())
            batchValues.add(new MapSqlParameterSource("name", storageReservoir.getName())
                    .addValue("reservoirtype", storageReservoir.getReservoirType())
                    .addValue("location", storageReservoir.getLocation())
                    .addValue("capacity", storageReservoir.getCapacity())
                    .addValue("noofsublines", storageReservoir.getNoOfSubLines())
                    .addValue("noofmaindistributionlines", storageReservoir.getNoOfMainDistributionLines())
                    .addValue("noofconnection", storageReservoir.getNoOfConnection())
                    .addValue("lastmodifiedby",
                            Long.valueOf(storageReservoirRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                    .addValue("code", storageReservoir.getCode()).addValue("tenantid", storageReservoir.getTenantId())
                    .getValues());

        namedParameterJdbcTemplate.batchUpdate(storageReservoirUpdate,
                batchValues.toArray(new Map[storageReservoirRequest.getStorageReservoirs().size()]));

        return storageReservoirRequest;
    }

    public List<StorageReservoir> findForCriteria(final StorageReservoirGetRequest storageReservoirGetRequest) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        /*
         * final List<String> boundaryWardNumsList = new ArrayList<>(); final List<String> boundaryZoneNumsList = new
         * ArrayList<>(); final List<String> boundaryLocationNumsList = new ArrayList<>();
         */
        final String queryStr = storageReservoirQueryBuilder.getQuery(storageReservoirGetRequest,
                preparedStatementValues);
        final List<StorageReservoir> storageReservoirList = namedParameterJdbcTemplate.query(queryStr,
                preparedStatementValues, storageReservoirRowMapper);

        /*
         * // fetch boundary Ward Nums and set the boundary name here for (final StorageReservoir storageReservoir :
         * storageReservoirList) boundaryWardNumsList.add(storageReservoir.getWardNum()); final String[] boundaryWardNum =
         * boundaryWardNumsList.toArray(new String[boundaryWardNumsList.size()]); final BoundaryResponse boundaryResponse =
         * restExternalMasterService.getBoundaryName(WcmsConstants.WARD, boundaryWardNum,
         * storageReservoirGetRequest.getTenantId()); for (final StorageReservoir storageReservoir : storageReservoirList) for
         * (final Boundary boundary : boundaryResponse.getBoundarys()) if
         * (boundary.getBoundaryNum().equals(storageReservoir.getWardNum())) storageReservoir.setWardName(boundary.getName()); //
         * fetch boundary Zone Nums and set the boundary name here for (final StorageReservoir storageReservoir :
         * storageReservoirList) boundaryZoneNumsList.add(storageReservoir.getZoneNum()); final String[] boundaryZoneNum =
         * boundaryZoneNumsList.toArray(new String[boundaryZoneNumsList.size()]); final BoundaryResponse boundaryZone =
         * restExternalMasterService.getBoundaryName(WcmsConstants.ZONE, boundaryZoneNum,
         * storageReservoirGetRequest.getTenantId()); for (final StorageReservoir storageReservoir : storageReservoirList) for
         * (final Boundary boundary : boundaryZone.getBoundarys()) if
         * (boundary.getBoundaryNum().equals(storageReservoir.getZoneNum())) storageReservoir.setZoneName(boundary.getName()); //
         * fetch boundary Location Nums and set the boundary name here for (final StorageReservoir storageReservoir :
         * storageReservoirList) boundaryLocationNumsList.add(storageReservoir.getLocationNum()); final String[]
         * boundaryLocationNum = boundaryLocationNumsList .toArray(new String[boundaryLocationNumsList.size()]); final
         * BoundaryResponse boundaryLocation = restExternalMasterService.getBoundaryName(WcmsConstants.LOCALITY,
         * boundaryLocationNum, storageReservoirGetRequest.getTenantId()); for (final StorageReservoir storageReservoir :
         * storageReservoirList) for (final Boundary boundary : boundaryLocation.getBoundarys()) if
         * (boundary.getBoundaryNum().equals(storageReservoir.getLocationNum()))
         * storageReservoir.setLocationName(boundary.getName());
         */

        return storageReservoirList;
    }

    public boolean checkStorageReservoirByNameAndCode(final String code, final String name, final String tenantId) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        preparedStatementValues.put("name", name);
        preparedStatementValues.put("tenantId", tenantId);
        final String query;
        if (code == null)
            query = StorageReservoirQueryBuilder.selectStorageResrvoirByNameByCodeQuery();
        else {
            preparedStatementValues.put("code", code);
            query = StorageReservoirQueryBuilder.selectStorageReservoirByNameByCodeNotInQuery();
        }
        final List<StorageReservoir> storageReservoir = namedParameterJdbcTemplate.query(query, preparedStatementValues,
                storageReservoirRowMapper);
        if (!storageReservoir.isEmpty())
            return false;

        return true;
    }

}
