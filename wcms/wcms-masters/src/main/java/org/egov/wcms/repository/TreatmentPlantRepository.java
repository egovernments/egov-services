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
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.TreatmentPlant;
import org.egov.wcms.repository.builder.TreatmentPlantQueryBuilder;
import org.egov.wcms.repository.rowmapper.TreatmentPlantRowMapper;
import org.egov.wcms.web.contract.TreatmentPlantGetRequest;
import org.egov.wcms.web.contract.TreatmentPlantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TreatmentPlantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TreatmentPlantRowMapper treatmentPlantRowMapper;

    @Autowired
    private TreatmentPlantQueryBuilder treatmentPlantQueryBuilder;

    public TreatmentPlantRequest persistCreateTreatmentPlant(final TreatmentPlantRequest treatmentPlantRequest) {
        log.info("treatmentPlantRequest::" + treatmentPlantRequest);
        final String treatmentPlantInsert = TreatmentPlantQueryBuilder.insertTreatmentPlantQuery();
        final String storageReservoirQuery = TreatmentPlantQueryBuilder.getStorageReservoirIdQuery();
        for (final TreatmentPlant treatmentPlant : treatmentPlantRequest.getTreatmentPlants()) {
            Long storageReservoirId = 0L;
            try {
                storageReservoirId = jdbcTemplate.queryForObject(storageReservoirQuery,
                        new Object[] { treatmentPlant.getStorageReservoirName(), treatmentPlant.getTenantId() },
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            final Object[] obj = new Object[] { Long.valueOf(treatmentPlant.getCode()), treatmentPlant.getCode(),
                    treatmentPlant.getName(), treatmentPlant.getPlantType(),
                    treatmentPlant.getLocation(), treatmentPlant.getWard(), treatmentPlant.getZone(),
                    treatmentPlant.getCapacity(),
                    storageReservoirId, treatmentPlant.getDescription(),
                    Long.valueOf(treatmentPlantRequest.getRequestInfo().getUserInfo().getId()),
                    Long.valueOf(treatmentPlantRequest.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                    treatmentPlant.getTenantId() };
            jdbcTemplate.update(treatmentPlantInsert, obj);
        }
        return treatmentPlantRequest;
    }

    public TreatmentPlantRequest persistUpdateTreatmentPlant(final TreatmentPlantRequest treatmentPlantRequest) {
        log.info("TreatmentPlantRequest::" + treatmentPlantRequest);
        final String treatmentPlantUpdate = TreatmentPlantQueryBuilder.updateTreatmentPlantQuery();
        final String storageReservoirQuery = TreatmentPlantQueryBuilder.getStorageReservoirIdQuery();
        for (final TreatmentPlant treatmentPlant : treatmentPlantRequest.getTreatmentPlants()) {
            Long storageReservoirId = 0L;
            try {
                storageReservoirId = jdbcTemplate.queryForObject(storageReservoirQuery,
                        new Object[] { treatmentPlant.getStorageReservoirName(), treatmentPlant.getTenantId() },
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            final Object[] obj = new Object[] { treatmentPlant.getName(), treatmentPlant.getPlantType(),
                    treatmentPlant.getLocation(),
                    treatmentPlant.getWard(), treatmentPlant.getZone(),
                    treatmentPlant.getCapacity(), storageReservoirId,
                    treatmentPlant.getDescription(),
                    Long.valueOf(treatmentPlantRequest.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()), treatmentPlant.getCode(), treatmentPlant.getTenantId() };
            jdbcTemplate.update(treatmentPlantUpdate, obj);
        }
        return treatmentPlantRequest;
    }

    public List<TreatmentPlant> findForCriteria(final TreatmentPlantGetRequest treatmentPlantGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        try {
            if (treatmentPlantGetRequest.getStorageReservoirName() != null)
                treatmentPlantGetRequest
                        .setStorageReservoirId(
                                jdbcTemplate.queryForObject(TreatmentPlantQueryBuilder.getStorageReservoirIdQuery(),
                                        new Object[] { treatmentPlantGetRequest.getStorageReservoirName(),
                                                treatmentPlantGetRequest.getTenantId() },
                                        Long.class));
        } catch (final EmptyResultDataAccessException e) {
            log.error("EmptyResultDataAccessException: Query returned empty RS.");

        }
        final String queryStr = treatmentPlantQueryBuilder.getQuery(treatmentPlantGetRequest, preparedStatementValues);
        final List<TreatmentPlant> treatmentPlantList = jdbcTemplate.query(queryStr,
                preparedStatementValues.toArray(), treatmentPlantRowMapper);
        final String storageReservoirNameOuery = TreatmentPlantQueryBuilder.getStorageReservoirName();
        for (final TreatmentPlant treatmentPlant : treatmentPlantList)
            treatmentPlant.setStorageReservoirName(jdbcTemplate.queryForObject(storageReservoirNameOuery,
                    new Object[] { treatmentPlant.getStorageReservoirId(), treatmentPlant.getTenantId() }, String.class));
        return treatmentPlantList;
    }

    public boolean checkTreatmentPlantByNameAndCode(final String code, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(name);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = TreatmentPlantQueryBuilder.selectTreatmentPlantByNameByCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = TreatmentPlantQueryBuilder.selectTreatmentPlantByNameByCodeNotInQuery();
        }
        final List<Map<String, Object>> treatmentPlant = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!treatmentPlant.isEmpty())
            return false;

        return true;
    }

    public boolean checkStorageReservoirExists(final String storageReservoirName, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(storageReservoirName);
        preparedStatementValues.add(tenantId);
        final String query = TreatmentPlantQueryBuilder.getStorageReservoirIdQuery();
        final List<Map<String, Object>> storageReservoirs = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!storageReservoirs.isEmpty())
            return false;

        return true;
    }

}
