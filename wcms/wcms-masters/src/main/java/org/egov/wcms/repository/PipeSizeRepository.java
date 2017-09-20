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
package org.egov.wcms.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.PipeSize;
import org.egov.wcms.repository.builder.PipeSizeQueryBuilder;
import org.egov.wcms.repository.rowmapper.PipeSizeRowMapper;
import org.egov.wcms.web.contract.PipeSizeGetRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PipeSizeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PipeSizeQueryBuilder pipeSizeQueryBuilder;

    @Autowired
    private PipeSizeRowMapper pipeSizeRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PipeSizeRequest create(final PipeSizeRequest pipeSizeRequest) {
        log.info("PipeSizeRequest::" + pipeSizeRequest);
        final String pipeSizeInsert = PipeSizeQueryBuilder.insertPipeSizeQuery();
        final List<PipeSize> pipeSizeList = pipeSizeRequest.getPipeSizes();
        final List<Map<String, Object>> batchValues = new ArrayList<>(pipeSizeList.size());
        for (final PipeSize pipeSize : pipeSizeList)
            batchValues.add(
                    new MapSqlParameterSource("id", Long.valueOf(pipeSize.getCode())).addValue("code", pipeSize.getCode())
                            .addValue("sizeinmilimeter", pipeSize.getSizeInMilimeter())
                            .addValue("sizeininch", pipeSize.getSizeInInch())
                            .addValue("description", pipeSize.getDescription()).addValue("active", pipeSize.getActive())
                            .addValue("createdby", Long.valueOf(pipeSizeRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifiedby", Long.valueOf(pipeSizeRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("createddate", new Date(new java.util.Date().getTime()))
                            .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                            .addValue("tenantid", pipeSize.getTenantId())
                            .getValues());
        namedParameterJdbcTemplate.batchUpdate(pipeSizeInsert, batchValues.toArray(new Map[pipeSizeList.size()]));
        return pipeSizeRequest;
    }

    public PipeSizeRequest update(final PipeSizeRequest pipeSizeRequest) {
        log.info("PipeSizeRequest::" + pipeSizeRequest);
        final String pipeSizeUpdate = PipeSizeQueryBuilder.updatePipeSizeQuery();
        final List<PipeSize> pipeSizeList = pipeSizeRequest.getPipeSizes();
        final List<Map<String, Object>> batchValues = new ArrayList<>(pipeSizeList.size());
        for (final PipeSize pipeSize : pipeSizeList)
            batchValues.add(
                    new MapSqlParameterSource("sizeinmilimeter", pipeSize.getSizeInMilimeter())
                            .addValue("sizeininch", pipeSize.getSizeInInch())
                            .addValue("description", pipeSize.getDescription()).addValue("active", pipeSize.getActive())
                            .addValue("lastmodifiedby", Long.valueOf(pipeSizeRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                            .addValue("code", pipeSize.getCode())
                            .addValue("tenantid", pipeSize.getTenantId())
                            .getValues());
        namedParameterJdbcTemplate.batchUpdate(pipeSizeUpdate, batchValues.toArray(new Map[pipeSizeList.size()]));
        return pipeSizeRequest;

    }

    public boolean checkPipeSizeInmmAndCode(final String code, final Double sizeInMilimeter, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(sizeInMilimeter);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = PipeSizeQueryBuilder.selectPipeSizeInmmAndCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = PipeSizeQueryBuilder.selectPipeSizeInmmAndCodeNotInQuery();
        }
        final List<Map<String, Object>> pipeSizes = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
        if (!pipeSizes.isEmpty())
            return false;

        return true;
    }

    public List<PipeSize> findForCriteria(final PipeSizeGetRequest pipeSizeGetRequest) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = pipeSizeQueryBuilder.getQuery(pipeSizeGetRequest, preparedStatementValues);
        final List<PipeSize> pipeSizes = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                pipeSizeRowMapper);
        return pipeSizes;
    }

}
