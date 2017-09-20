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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.TreatmentPlant;
import org.egov.wcms.repository.builder.TreatmentPlantQueryBuilder;
import org.egov.wcms.repository.rowmapper.TreatmentPlantRowMapper;
import org.egov.wcms.web.contract.TreatmentPlantGetRequest;
import org.egov.wcms.web.contract.TreatmentPlantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TreatmentPlantRepository {
    @Autowired
    private TreatmentPlantRowMapper treatmentPlantRowMapper;

    @Autowired
    private TreatmentPlantQueryBuilder treatmentPlantQueryBuilder;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TreatmentPlantRequest create(final TreatmentPlantRequest treatmentPlantRequest) {
        log.info("treatmentPlantRequest::" + treatmentPlantRequest);
        final List<Map<String, Object>> batchArgs = new ArrayList<>();
        final Map<String, Object> batchArguments = new HashMap<>();
        final String treatmentPlantInsert = TreatmentPlantQueryBuilder.insertTreatmentPlantQuery();
        final String storageReservoirQuery = TreatmentPlantQueryBuilder.getStorageReservoirIdQuery();
        for (final TreatmentPlant treatmentPlant : treatmentPlantRequest.getTreatmentPlants()) {
            Long storageReservoirId = null;
            try {
                batchArguments.put("name", treatmentPlant.getStorageReservoirName());
                batchArguments.put("tenantId", treatmentPlant.getTenantId());
                storageReservoirId = namedParameterJdbcTemplate.queryForObject(storageReservoirQuery, batchArguments,
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            batchArgs.add(new MapSqlParameterSource("id", Long.valueOf(treatmentPlant.getCode()))
                    .addValue("code", treatmentPlant.getCode()).addValue("name", treatmentPlant.getName())
                    .addValue("planttype", treatmentPlant.getPlantType())
                    .addValue("location", treatmentPlant.getLocation())
                    .addValue("capacity", treatmentPlant.getCapacity())
                    .addValue("storagereservoirid", storageReservoirId)
                    .addValue("description", treatmentPlant.getDescription())
                    .addValue("createdby", Long.valueOf(treatmentPlantRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifiedby",
                            Long.valueOf(treatmentPlantRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("createddate", new Date(new java.util.Date().getTime()))
                    .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                    .addValue("tenantid", treatmentPlant.getTenantId()).getValues());
        }
        namedParameterJdbcTemplate.batchUpdate(treatmentPlantInsert,
                batchArgs.toArray(new Map[treatmentPlantRequest.getTreatmentPlants().size()]));
        return treatmentPlantRequest;
    }

    public TreatmentPlantRequest update(final TreatmentPlantRequest treatmentPlantRequest) {
        log.info("TreatmentPlantRequest::" + treatmentPlantRequest);
        final Map<String, Object> batchArguments = new HashMap<>();
        final String treatmentPlantUpdate = TreatmentPlantQueryBuilder.updateTreatmentPlantQuery();
        final String storageReservoirQuery = TreatmentPlantQueryBuilder.getStorageReservoirIdQuery();
        final List<Map<String, Object>> batchArgs = new ArrayList<>();
        for (final TreatmentPlant treatmentPlant : treatmentPlantRequest.getTreatmentPlants()) {
            Long storageReservoirId = null;
            try {
                batchArguments.put("name", treatmentPlant.getStorageReservoirName());
                batchArguments.put("tenantId", treatmentPlant.getTenantId());
                storageReservoirId = namedParameterJdbcTemplate.queryForObject(storageReservoirQuery, batchArguments,
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            batchArgs.add(new MapSqlParameterSource("name", treatmentPlant.getName())
                    .addValue("planttype", treatmentPlant.getPlantType())
                    .addValue("location", treatmentPlant.getLocation())
                    .addValue("capacity", treatmentPlant.getCapacity())
                    .addValue("storagereservoirid", storageReservoirId)
                    .addValue("description", treatmentPlant.getDescription())
                    .addValue("lastmodifiedby",
                            Long.valueOf(treatmentPlantRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                    .addValue("code", treatmentPlant.getCode()).addValue("tenantid", treatmentPlant.getTenantId())
                    .getValues());
        }
        namedParameterJdbcTemplate.batchUpdate(treatmentPlantUpdate,
                batchArgs.toArray(new Map[treatmentPlantRequest.getTreatmentPlants().size()]));
        return treatmentPlantRequest;
    }

    public List<TreatmentPlant> findForCriteria(final TreatmentPlantGetRequest treatmentPlantGetRequest) {
        /*
         * final List<String> boundaryWardNumsList = new ArrayList<>(); final List<String> boundaryZoneNumsList = new
         * ArrayList<>(); final List<String> boundaryLocationNumsList = new ArrayList<>();
         */
        final Map<String, Object> batchArguments = new HashMap<>();
        final Map<String, Object> batchArgs = new HashMap<>();
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        try {
            if (treatmentPlantGetRequest.getStorageReservoirName() != null) {
                batchArguments.put("name", treatmentPlantGetRequest.getStorageReservoirName());
                batchArguments.put("tenantId", treatmentPlantGetRequest.getTenantId());

                final Long storageReservoirId = namedParameterJdbcTemplate.queryForObject(
                        TreatmentPlantQueryBuilder.getStorageReservoirIdQuery(), batchArguments, Long.class);
                treatmentPlantGetRequest.setStorageReservoirId(storageReservoirId);
            }
        } catch (final EmptyResultDataAccessException e) {
            log.error("EmptyResultDataAccessException: Query returned empty RS.");
        }
        final String queryStr = treatmentPlantQueryBuilder.getQuery(treatmentPlantGetRequest, preparedStatementValues);
        final List<TreatmentPlant> treatmentPlantList = namedParameterJdbcTemplate.query(queryStr,
                preparedStatementValues, treatmentPlantRowMapper);
        final String storageReservoirNameOuery = TreatmentPlantQueryBuilder.getStorageReservoirName();
        for (final TreatmentPlant treatmentPlant : treatmentPlantList)
            if (treatmentPlant.getStorageReservoirId() != null) {
                batchArgs.put("id", treatmentPlant.getStorageReservoirId());
                batchArgs.put("tenantId", treatmentPlant.getTenantId());
                treatmentPlant.setStorageReservoirName(
                        namedParameterJdbcTemplate.queryForObject(storageReservoirNameOuery, batchArgs, String.class));
            }
        /*
         * // fetch boundary Ward Nums and set the boundary name here for (final TreatmentPlant treatmentPlant :
         * treatmentPlantList) boundaryWardNumsList.add(treatmentPlant.getWardNum()); final String[] boundaryWardNum =
         * boundaryWardNumsList.toArray(new String[boundaryWardNumsList.size()]); final BoundaryResponse boundaryResponse =
         * restExternalMasterService.getBoundaryName(WcmsConstants.WARD, boundaryWardNum, treatmentPlantGetRequest.getTenantId());
         * for (final TreatmentPlant treatmentPlant : treatmentPlantList) for (final Boundary boundary :
         * boundaryResponse.getBoundarys()) if (boundary.getBoundaryNum().equals(treatmentPlant.getWardNum()))
         * treatmentPlant.setWardName(boundary.getName()); // fetch boundary Zone Nums and set the boundary name here for (final
         * TreatmentPlant treatmentPlant : treatmentPlantList) boundaryZoneNumsList.add(treatmentPlant.getZoneNum()); final
         * String[] boundaryZoneNum = boundaryZoneNumsList.toArray(new String[boundaryZoneNumsList.size()]); final
         * BoundaryResponse boundaryZone = restExternalMasterService.getBoundaryName(WcmsConstants.ZONE, boundaryZoneNum,
         * treatmentPlantGetRequest.getTenantId()); for (final TreatmentPlant treatmentPlant : treatmentPlantList) for (final
         * Boundary boundary : boundaryZone.getBoundarys()) if (boundary.getBoundaryNum().equals(treatmentPlant.getZoneNum()))
         * treatmentPlant.setZoneName(boundary.getName()); // fetch boundary Location Nums and set the boundary name here for
         * (final TreatmentPlant treatmentPlant : treatmentPlantList)
         * boundaryLocationNumsList.add(treatmentPlant.getLocationNum()); final String[] boundaryLocationNum =
         * boundaryLocationNumsList .toArray(new String[boundaryLocationNumsList.size()]); final BoundaryResponse boundaryLocation
         * = restExternalMasterService.getBoundaryName(WcmsConstants.LOCALITY, boundaryLocationNum,
         * treatmentPlantGetRequest.getTenantId()); for (final TreatmentPlant treatmentPlant : treatmentPlantList) for (final
         * Boundary boundary : boundaryLocation.getBoundarys()) if
         * (boundary.getBoundaryNum().equals(treatmentPlant.getLocationNum())) treatmentPlant.setLocationName(boundary.getName());
         */
        return treatmentPlantList;
    }

    public boolean checkTreatmentPlantByNameAndCode(final String code, final String name, final String tenantId) {
        final Map<String, Object> batchValues = new HashMap<>();
        batchValues.put("name", name);
        batchValues.put("tenantId", tenantId);
        final String query;
        if (code == null)
            query = TreatmentPlantQueryBuilder.selectTreatmentPlantByNameByCodeQuery();
        else {
            batchValues.put("code", code);
            query = TreatmentPlantQueryBuilder.selectTreatmentPlantByNameByCodeNotInQuery();
        }
        final List<String> treatmentPlantCode = namedParameterJdbcTemplate.queryForList(query, batchValues,
                String.class);
        if (!treatmentPlantCode.isEmpty())
            return false;

        return true;
    }

    public boolean checkStorageReservoirExists(final String storageReservoirName, final String tenantId) {
        final Map<String, Object> batchValues = new HashMap<>();
        batchValues.put("name", storageReservoirName);
        batchValues.put("tenantId", tenantId);
        final String query = TreatmentPlantQueryBuilder.getStorageReservoirIdQuery();
        final List<Long> storageReserviorId = namedParameterJdbcTemplate.queryForList(query, batchValues, Long.class);
        if (!storageReserviorId.isEmpty())
            return false;

        return true;
    }

}
