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

import org.egov.wcms.model.SourceType;
import org.egov.wcms.repository.builder.SourceTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.SourceTypeRowMapper;
import org.egov.wcms.web.contract.SourceTypeGetRequest;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SourceTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SourceTypeQueryBuilder sourceTypeQueryBuilder;

    @Autowired
    private SourceTypeRowMapper sourceTypeRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SourceTypeRequest create(final SourceTypeRequest sourceTypeRequest) {
        log.info("SourceTypeRequest::" + sourceTypeRequest);
        final String waterSourceInsert = SourceTypeQueryBuilder.insertWaterSourceTypeQuery();
        final List<SourceType> waterSourceList = sourceTypeRequest.getSourceTypes();
        final List<Map<String, Object>> batchValues = new ArrayList<>(waterSourceList.size());
        for (final SourceType waterSource : waterSourceList)
            batchValues.add(
                    new MapSqlParameterSource("id", Long.valueOf(waterSource.getCode())).addValue("code", waterSource.getCode())
                            .addValue("name", waterSource.getName()).addValue("description", waterSource.getDescription())
                            .addValue("active", waterSource.getActive())
                            .addValue("sourcecapacity", waterSource.getSourceCapacity())
                            .addValue("ulbreserved", waterSource.getUlbReserved())
                            .addValue("createdby", Long.valueOf(sourceTypeRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifiedby", Long.valueOf(sourceTypeRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("createddate", new Date(new java.util.Date().getTime()))
                            .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                            .addValue("tenantid", waterSource.getTenantId())
                            .getValues());
        namedParameterJdbcTemplate.batchUpdate(waterSourceInsert, batchValues.toArray(new Map[waterSourceList.size()]));
        return sourceTypeRequest;
    }

    public SourceTypeRequest update(final SourceTypeRequest sourceTypeRequest) {
        log.info("SourceTypeRequest::" + sourceTypeRequest);
        final String waterSourceUpdate = SourceTypeQueryBuilder.updateWaterSourceTypeQuery();
        final List<SourceType> waterSourceList = sourceTypeRequest.getSourceTypes();
        final List<Map<String, Object>> batchValues = new ArrayList<>(waterSourceList.size());
        for (final SourceType waterSource : waterSourceList)
            batchValues.add(
                    new MapSqlParameterSource("name", waterSource.getName())
                            .addValue("description", waterSource.getDescription())
                            .addValue("active", waterSource.getActive())
                            .addValue("sourcecapacity", waterSource.getSourceCapacity())
                            .addValue("ulbreserved", waterSource.getUlbReserved())
                            .addValue("lastmodifiedby", Long.valueOf(sourceTypeRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                            .addValue("code", waterSource.getCode())
                            .addValue("tenantid", waterSource.getTenantId())
                            .getValues());
        namedParameterJdbcTemplate.batchUpdate(waterSourceUpdate, batchValues.toArray(new Map[waterSourceList.size()]));
        return sourceTypeRequest;

    }

    public boolean checkWaterSourceTypeByNameAndCode(final String code, final String name, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(name);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = SourceTypeQueryBuilder.selectWaterSourceByNameAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = SourceTypeQueryBuilder.selectWaterSourceByNameAndCodeNotInQuery();
        }
        final List<Map<String, Object>> waterSourceTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!waterSourceTypes.isEmpty())
            return false;

        return true;
    }

    public List<SourceType> findForCriteria(final SourceTypeGetRequest sourceTypeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = sourceTypeQueryBuilder.getQuery(sourceTypeGetRequest, preparedStatementValues);
        final List<SourceType> waterSourceTypes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                sourceTypeRowMapper);
        return waterSourceTypes;
    }
}
