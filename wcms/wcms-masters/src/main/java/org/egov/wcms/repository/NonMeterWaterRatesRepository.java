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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.wcms.model.NonMeterWaterRates;
import org.egov.wcms.repository.builder.NonMeterWaterRatesQueryBuilder;
import org.egov.wcms.repository.rowmapper.NonMeterWaterRatesRowMapper;
import org.egov.wcms.web.contract.NonMeterWaterRatesGetReq;
import org.egov.wcms.web.contract.NonMeterWaterRatesReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class NonMeterWaterRatesRepository {

    @Autowired
    private NonMeterWaterRatesQueryBuilder nonMeterWaterRatesQueryBuilder;

    @Autowired
    private NonMeterWaterRatesRowMapper nonMeterWaterRatesRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public NonMeterWaterRatesReq create(final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        log.info("NonMeterWaterRatesReq::" + nonMeterWaterRatesReq);
        final String nonMeterWaterRatesInsertQuery = NonMeterWaterRatesQueryBuilder.insertNonMeterWaterRatesQuery();
        final String pipesizeQuery = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQueryForSearch();
        final String sourcetypeQuery = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQueryForSearch();
        final List<NonMeterWaterRates> nonMeterWaterRatesList = nonMeterWaterRatesReq.getNonMeterWaterRates();
        final List<Map<String, Object>> batchValues = new ArrayList<>(nonMeterWaterRatesList.size());
        final Map<String, Object> batchArguments = new HashMap<>();
        for (final NonMeterWaterRates nonMeterWaterRates : nonMeterWaterRatesList) {
            Long pipesizeId = 0L;
            try {
                batchArguments.put("sizeinmilimeter", nonMeterWaterRates.getPipeSize());
                batchArguments.put("tenantId", nonMeterWaterRates.getTenantId());
                pipesizeId = namedParameterJdbcTemplate.queryForObject(pipesizeQuery, batchArguments,
                        Long.class);

            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (pipesizeId == null)
                log.info("Invalid input.");
            Long sourcetypeId = 0L;
            try {
                batchArguments.put("name", nonMeterWaterRates.getSourceTypeName());
                batchArguments.put("tenantId", nonMeterWaterRates.getTenantId());
                sourcetypeId = namedParameterJdbcTemplate.queryForObject(sourcetypeQuery, batchArguments,
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (sourcetypeId == null)
                log.info("Invalid input.");
            batchValues.add(
                    new MapSqlParameterSource("id", Long.valueOf(nonMeterWaterRates.getCode()))
                            .addValue("code", nonMeterWaterRates.getCode())
                            .addValue("billingtype", nonMeterWaterRates.getBillingType())
                            .addValue("connectiontype", nonMeterWaterRates.getConnectionType())
                            .addValue("usagetypeid", nonMeterWaterRates.getUsageTypeId())
                            .addValue("subusagetypeid", nonMeterWaterRates.getSubUsageTypeId())
                            .addValue("outsideulb", nonMeterWaterRates.getOutsideUlb())
                            .addValue("sourcetypeid", sourcetypeId).addValue("pipesizeid", pipesizeId)
                            .addValue("fromdate", nonMeterWaterRates.getFromDate())
                            .addValue("amount", nonMeterWaterRates.getAmount()).addValue("active", nonMeterWaterRates.getActive())
                            .addValue("nooftaps", nonMeterWaterRates.getNoOfTaps())
                            .addValue("createdby", Long.valueOf(nonMeterWaterRatesReq.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifiedby",
                                    Long.valueOf(nonMeterWaterRatesReq.getRequestInfo().getUserInfo().getId()))
                            .addValue("createddate", new java.util.Date().getTime())
                            .addValue("lastmodifieddate", new java.util.Date().getTime())
                            .addValue("tenantid", nonMeterWaterRates.getTenantId())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(nonMeterWaterRatesInsertQuery,
                batchValues.toArray(new Map[nonMeterWaterRatesList.size()]));

        return nonMeterWaterRatesReq;
    }

    public NonMeterWaterRatesReq update(final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        log.info("NonMeterWaterRatesReq::" + nonMeterWaterRatesReq);
        final String nonMeterWaterRatesUpdateQuery = NonMeterWaterRatesQueryBuilder.updateNonMeterWaterRatesQuery();
        final String pipesizeQuery = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQueryForSearch();
        final String sourcetypeQuery = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQueryForSearch();
        final List<NonMeterWaterRates> nonMeterWaterRatesList = nonMeterWaterRatesReq.getNonMeterWaterRates();
        final List<Map<String, Object>> batchValues = new ArrayList<>(nonMeterWaterRatesList.size());
        final Map<String, Object> batchArguments = new HashMap<>();
        for (final NonMeterWaterRates nonMeterWaterRates : nonMeterWaterRatesList) {
            Long pipesizeId = 0L;
            try {
                batchArguments.put("sizeinmilimeter", nonMeterWaterRates.getPipeSize());
                batchArguments.put("tenantId", nonMeterWaterRates.getTenantId());
                pipesizeId = namedParameterJdbcTemplate.queryForObject(pipesizeQuery, batchArguments,
                        Long.class);

            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (pipesizeId == null)
                log.info("Invalid input.");
            Long sourcetypeId = 0L;
            try {
                batchArguments.put("name", nonMeterWaterRates.getSourceTypeName());
                batchArguments.put("tenantId", nonMeterWaterRates.getTenantId());
                sourcetypeId = namedParameterJdbcTemplate.queryForObject(sourcetypeQuery, batchArguments,
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (sourcetypeId == null)
                log.info("Invalid input.");
            batchValues.add(
                    new MapSqlParameterSource("billingtype", nonMeterWaterRates.getBillingType())
                            .addValue("connectiontype", nonMeterWaterRates.getConnectionType())
                            .addValue("usagetypeid", nonMeterWaterRates.getUsageTypeId())
                            .addValue("subusagetypeid", nonMeterWaterRates.getSubUsageTypeId())
                            .addValue("outsideulb", nonMeterWaterRates.getOutsideUlb())
                            .addValue("sourcetypeid", sourcetypeId).addValue("pipesizeid", pipesizeId)
                            .addValue("fromdate", nonMeterWaterRates.getFromDate())
                            .addValue("amount", nonMeterWaterRates.getAmount()).addValue("active", nonMeterWaterRates.getActive())
                            .addValue("nooftaps", nonMeterWaterRates.getNoOfTaps())
                            .addValue("lastmodifiedby",
                                    Long.valueOf(nonMeterWaterRatesReq.getRequestInfo().getUserInfo().getId()))

                            .addValue("lastmodifieddate", new java.util.Date().getTime())
                            .addValue("tenantid", nonMeterWaterRates.getTenantId())
                            .addValue("code", nonMeterWaterRates.getCode())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(nonMeterWaterRatesUpdateQuery,
                batchValues.toArray(new Map[nonMeterWaterRatesList.size()]));

        return nonMeterWaterRatesReq;
    }

    public List<NonMeterWaterRates> findForCriteria(final NonMeterWaterRatesGetReq nonMeterWaterRatesGetRequest) {
        if (nonMeterWaterRatesGetRequest.getSourceTypeName() != null) {
            final Map<String, Object> paramMapForSourceType = new HashMap<>();
            paramMapForSourceType.put("name", nonMeterWaterRatesGetRequest.getSourceTypeName());
            paramMapForSourceType.put("tenantId", nonMeterWaterRatesGetRequest.getTenantId());
            final Long sourceTypeId = namedParameterJdbcTemplate.queryForObject(
                    NonMeterWaterRatesQueryBuilder.getSourceTypeIdQueryForSearch(), paramMapForSourceType, Long.class);
            nonMeterWaterRatesGetRequest.setSourceTypeId(sourceTypeId);
        }
        if (nonMeterWaterRatesGetRequest.getUsageTypeCode() != null) {
            final Map<String, Object> paramMapForUsageType = new HashMap<>();
            paramMapForUsageType.put("code", nonMeterWaterRatesGetRequest.getUsageTypeCode());
            paramMapForUsageType.put("tenantId", nonMeterWaterRatesGetRequest.getTenantId());
            final Long usageTypeId = namedParameterJdbcTemplate.queryForObject(
                    NonMeterWaterRatesQueryBuilder.getUsageTypeIdQueryForSearch(), paramMapForUsageType, Long.class);
            nonMeterWaterRatesGetRequest.setUsageTypeId(String.valueOf(usageTypeId));
        }
        if (nonMeterWaterRatesGetRequest.getSubUsageTypeCode() != null) {
            final Map<String, Object> paramMapForSubUsageType = new HashMap<>();
            paramMapForSubUsageType.put("code", nonMeterWaterRatesGetRequest.getSubUsageTypeCode());
            paramMapForSubUsageType.put("tenantId", nonMeterWaterRatesGetRequest.getTenantId());
            final Long subUsageTypeId = namedParameterJdbcTemplate.queryForObject(
                    NonMeterWaterRatesQueryBuilder.getUsageTypeIdQueryForSearch(), paramMapForSubUsageType, Long.class);
            nonMeterWaterRatesGetRequest.setSubUsageTypeId(String.valueOf(subUsageTypeId));
        }
        if (nonMeterWaterRatesGetRequest.getPipeSize() != null) {
            final Map<String, Object> paramMapForPipeSize = new HashMap<>();
            paramMapForPipeSize.put("sizeinmilimeter", nonMeterWaterRatesGetRequest.getPipeSize());
            paramMapForPipeSize.put("tenantId", nonMeterWaterRatesGetRequest.getTenantId());
            final Long pipeSizeId = namedParameterJdbcTemplate.queryForObject(
                    NonMeterWaterRatesQueryBuilder.getPipeSizeIdQueryForSearch(), paramMapForPipeSize, Long.class);
            nonMeterWaterRatesGetRequest.setPipeSizeId(pipeSizeId);
        }
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final String queryStr = nonMeterWaterRatesQueryBuilder.getQuery(nonMeterWaterRatesGetRequest, preparedStatementValues);
        final List<NonMeterWaterRates> nonMeterWaterRatesList = namedParameterJdbcTemplate.query(queryStr,
                preparedStatementValues, nonMeterWaterRatesRowMapper);
        return nonMeterWaterRatesList;
    }

    public boolean checkNonMeterWaterRatesExists(final String code, final String connectionType, final Long usageTypeId,
            final Long subUsageTypeId, final String sourceTypeName, final Double pipeSize, final Long fromDate,
            final String tenantId) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final Map<String, Object> batchArguments = new HashMap<>();
        final String pipesizeQuery = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQueryForSearch();
        final String sourcetypeQuery = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQueryForSearch();
        Long pipesizeId = 0L;
        try {
            batchArguments.put("sizeinmilimeter", pipeSize);
            batchArguments.put("tenantId", tenantId);
            pipesizeId = namedParameterJdbcTemplate.queryForObject(pipesizeQuery, batchArguments,
                    Long.class);

        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        if (pipesizeId == null)
            log.info("Invalid input.");
        Long sourcetypeId = 0L;
        try {
            batchArguments.put("name", sourceTypeName);
            batchArguments.put("tenantId", tenantId);
            sourcetypeId = namedParameterJdbcTemplate.queryForObject(sourcetypeQuery, batchArguments,
                    Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set");
        }
        preparedStatementValues.put("usagetypeid", usageTypeId);
        preparedStatementValues.put("subusagetypeid", subUsageTypeId);
        preparedStatementValues.put("sourcetypeid", sourcetypeId);
        preparedStatementValues.put("pipesizeid", pipesizeId);
        preparedStatementValues.put("connectiontype", connectionType);
        preparedStatementValues.put("fromdate", fromDate);
        preparedStatementValues.put("tenantId", tenantId);
        final String query;
        if (code == null)
            query = NonMeterWaterRatesQueryBuilder.selectNonMeterWaterRatesByCodeQuery();
        else {
            preparedStatementValues.put("code", code);
            query = NonMeterWaterRatesQueryBuilder.selectNonMeterWaterRatesByCodeNotInQuery();
        }
        final List<NonMeterWaterRates> nonMeterWaterRates = namedParameterJdbcTemplate.query(query,
                preparedStatementValues, nonMeterWaterRatesRowMapper);
        if (!nonMeterWaterRates.isEmpty())
            return false;

        return true;
    }

    public boolean checkPipeSizeExists(final Double pipeSize, final String tenantId) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        preparedStatementValues.put("sizeinmilimeter", pipeSize);
        preparedStatementValues.put("tenantId", tenantId);
        final String query = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQueryForSearch();
        final List<Long> pipeSizes = namedParameterJdbcTemplate.queryForList(query,
                preparedStatementValues, Long.class);
        if (!pipeSizes.isEmpty())
            return false;

        return true;
    }

    public boolean checkSourceTypeExists(final String sourceTypeName, final String tenantId) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        preparedStatementValues.put("name", sourceTypeName);
        preparedStatementValues.put("tenantId", tenantId);
        final String query = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQueryForSearch();
        final List<Long> sourceTypes = namedParameterJdbcTemplate.queryForList(query,
                preparedStatementValues, Long.class);
        if (!sourceTypes.isEmpty())
            return false;

        return true;
    }

    public Map<String, Object> checkUsageAndSubUsageTypeExists(final String usageType, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(usageType);
        preparedStatementValues.add(tenantId);
        final String query = NonMeterWaterRatesQueryBuilder.getUsageTypeIdQuery();

        try {
            return jdbcTemplate.queryForMap(query, preparedStatementValues.toArray());
        } catch (final EmptyResultDataAccessException exception) {
            return new HashMap<String, Object>();
        }

    }
}
