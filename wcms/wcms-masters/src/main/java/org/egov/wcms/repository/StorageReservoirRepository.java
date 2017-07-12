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

import org.egov.wcms.model.StorageReservoir;
import org.egov.wcms.repository.builder.StorageReservoirQueryBuilder;
import org.egov.wcms.repository.rowmapper.StorageReservoirRowMapper;
import org.egov.wcms.web.contract.StorageReservoirGetRequest;
import org.egov.wcms.web.contract.StorageReservoirRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class StorageReservoirRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StorageReservoirRowMapper storageReservoirRowMapper;

    @Autowired
    private StorageReservoirQueryBuilder storageReservoirQueryBuilder;

    public StorageReservoirRequest persistCreateStorageReservoir(final StorageReservoirRequest storageReservoirRequest) {
        log.info("storageReservoirRequest::" + storageReservoirRequest);
        final String storageReservoirInsert = StorageReservoirQueryBuilder.insertStorageReserviorQuery();
        for (final StorageReservoir storageReservoir : storageReservoirRequest.getStorageReservoir()) {
            final Object[] obj = new Object[] { storageReservoir.getName(), storageReservoir.getReservoirType(),
                    storageReservoir.getLocation(), storageReservoir.getWard(), storageReservoir.getZone(),
                    storageReservoir.getCapacity(),
                    storageReservoir.getNoOfSubLines(), storageReservoir.getNoOfMainDistributionLines(),
                    storageReservoir.getNoOfConnection(),
                    Long.valueOf(storageReservoirRequest.getRequestInfo().getUserInfo().getId()),
                    Long.valueOf(storageReservoirRequest.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                    storageReservoir.getTenantId() };
            jdbcTemplate.update(storageReservoirInsert, obj);
        }
        return storageReservoirRequest;
    }

    public StorageReservoirRequest persistUpdateStorageReservoir(final StorageReservoirRequest storageReservoirRequest) {
        log.info("StorageReservoir Request::" + storageReservoirRequest);
        final String storageReservoirUpdate = StorageReservoirQueryBuilder.updateStorageReserviorQuery();
        for (final StorageReservoir storageReservoir : storageReservoirRequest.getStorageReservoir()) {
            final Object[] obj = new Object[] { storageReservoir.getName(), storageReservoir.getReservoirType(),
                    storageReservoir.getLocation(),
                    storageReservoir.getWard(), storageReservoir.getZone(),
                    storageReservoir.getCapacity(), storageReservoir.getNoOfSubLines(),
                    storageReservoir.getNoOfMainDistributionLines(),
                    storageReservoir.getNoOfConnection(),
                    Long.valueOf(storageReservoirRequest.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()), storageReservoir.getId(), storageReservoir.getTenantId() };
            jdbcTemplate.update(storageReservoirUpdate, obj);
        }
        return storageReservoirRequest;
    }

    public List<StorageReservoir> findForCriteria(final StorageReservoirGetRequest storageReservoirGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = storageReservoirQueryBuilder.getQuery(storageReservoirGetRequest, preparedStatementValues);
        final List<StorageReservoir> storageReservoirList = jdbcTemplate.query(queryStr,
                preparedStatementValues.toArray(), storageReservoirRowMapper);
        return storageReservoirList;
    }

    public boolean checkStorageReservoirByName(final Long id, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(name);
        preparedStatementValues.add(tenantId);
        final String query;
        if (id == null)
            query = StorageReservoirQueryBuilder.selectStorageResrvoirByNameByIdQuery();
        else {
            preparedStatementValues.add(id);
            query = StorageReservoirQueryBuilder.selectStorageReservoirByNameByIdNotInQuery();
        }
        final List<Map<String, Object>> storageReservoir = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!storageReservoir.isEmpty())
            return false;

        return true;
    }

}
