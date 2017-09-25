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

import org.egov.wcms.model.MeterWaterRates;
import org.egov.wcms.model.Slab;
import org.egov.wcms.repository.builder.MeterWaterRatesQueryBuilder;
import org.egov.wcms.repository.rowmapper.MeterWaterRatesRowMapper;
import org.egov.wcms.repository.rowmapper.SlabRowMapper;
import org.egov.wcms.web.contract.MeterWaterRatesGetRequest;
import org.egov.wcms.web.contract.MeterWaterRatesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MeterWaterRatesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MeterWaterRatesQueryBuilder meterWaterRatesQueryBuilder;

    @Autowired
    private MeterWaterRatesRowMapper meterWaterRatesRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private SlabRowMapper slabRowMapper;

    public MeterWaterRatesRequest create(final MeterWaterRatesRequest meterWaterRatesRequest) {
        log.info("MeterWaterRatesRequest::" + meterWaterRatesRequest);
        final String meterWaterRatesInsertQuery = MeterWaterRatesQueryBuilder.insertMeterWaterRatesQuery();
        final String pipesizeQuery = MeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
        final String sourcetypeQuery = MeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
        final List<MeterWaterRates> meterWaterRatesList = meterWaterRatesRequest.getMeterWaterRates();
        final List<Slab> slabs = new ArrayList<>();
        for (final MeterWaterRates meterWaterRate : meterWaterRatesList)
            slabs.addAll(meterWaterRate.getSlab());
        final String slabInsertQuery = MeterWaterRatesQueryBuilder.insertSlabQuery();
        final List<Map<String, Object>> batchValuesSlab = new ArrayList<>(slabs.size());
        final List<Map<String, Object>> batchValues = new ArrayList<>(meterWaterRatesList.size());
        for (final MeterWaterRates meterWaterRates : meterWaterRatesList) {
            Long pipesizeId = 0L;
            try {
                pipesizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                        new Object[] { meterWaterRates.getPipeSize(), meterWaterRates.getTenantId() }, Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (pipesizeId == null)
                log.info("Invalid input.");
            Long sourcetypeId = 0L;
            try {
                sourcetypeId = jdbcTemplate.queryForObject(sourcetypeQuery,
                        new Object[] { meterWaterRates.getSourceTypeName(), meterWaterRates.getTenantId() },
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (sourcetypeId == null)
                log.info("Invalid input.");
            batchValues.add(new MapSqlParameterSource("id", Long.valueOf(meterWaterRates.getCode()))
                    .addValue("code", meterWaterRates.getCode())
                    .addValue("billingtype", meterWaterRates.getBillingType())
                    .addValue("usagetypeid", meterWaterRates.getUsageTypeId())
                    .addValue("subusagetypeid", meterWaterRates.getSubUsageTypeId())
                    .addValue("outsideulb", meterWaterRates.getOutsideUlb()).addValue("sourcetypeid", sourcetypeId)
                    .addValue("pipesizeid", pipesizeId).addValue("fromdate", meterWaterRates.getFromDate())
                    .addValue("todate", meterWaterRates.getToDate()).addValue("active", meterWaterRates.getActive())
                    .addValue("createdby", Long.valueOf(meterWaterRatesRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifiedby",
                            Long.valueOf(meterWaterRatesRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("createddate", new Date(new java.util.Date().getTime()))
                    .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                    .addValue("tenantid", meterWaterRates.getTenantId()).getValues());

            final List<Slab> slabList = meterWaterRates.getSlab();

            if (slabList != null && !slabList.isEmpty()) {

                log.info("the insert query for slab : " + slabInsertQuery);

                for (final Slab slab : slabList)
                    batchValuesSlab
                            .add(new MapSqlParameterSource("meterwaterratesid", Long.valueOf(meterWaterRates.getCode()))
                                    .addValue("fromunit", slab.getFromUnit()).addValue("tounit", slab.getToUnit())
                                    .addValue("unitrate", slab.getUnitRate())
                                    .addValue("tenantid", meterWaterRates.getTenantId()).getValues());

            }
        }
        namedParameterJdbcTemplate.batchUpdate(meterWaterRatesInsertQuery,
                batchValues.toArray(new Map[meterWaterRatesList.size()]));
        namedParameterJdbcTemplate.batchUpdate(slabInsertQuery,
                batchValuesSlab.toArray(new Map[slabs.size()]));
        return meterWaterRatesRequest;
    }

    public MeterWaterRatesRequest update(final MeterWaterRatesRequest meterWaterRatesRequest) {
        log.info("MeterWaterRatesRequest::" + meterWaterRatesRequest);
        final String meterWaterRatesUpdateQuery = MeterWaterRatesQueryBuilder.updateMeterWaterRatesQuery();
        final String pipesizeQuery = MeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
        final String sourcetypeQuery = MeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
        final String slabDeleteQuery = meterWaterRatesQueryBuilder.deleteSlabValuesQuery();

        final List<MeterWaterRates> meterWaterRatesList = meterWaterRatesRequest.getMeterWaterRates();
        final List<Slab> slabs = new ArrayList<>();
        for (final MeterWaterRates meterWaterRate : meterWaterRatesList)
            slabs.addAll(meterWaterRate.getSlab());
        final List<Map<String, Object>> batchValuesSlab = new ArrayList<>(slabs.size());
        final List<Map<String, Object>> batchValuesForDeletion = new ArrayList<>();

        final String slabInsertQuery = MeterWaterRatesQueryBuilder.insertSlabQuery();

        final List<Map<String, Object>> batchValues = new ArrayList<>(meterWaterRatesList.size());
        for (final MeterWaterRates meterWaterRates : meterWaterRatesList) {
            Long pipesizeId = 0L;
            try {
                pipesizeId = jdbcTemplate.queryForObject(pipesizeQuery,
                        new Object[] { meterWaterRates.getPipeSize(), meterWaterRates.getTenantId() }, Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (pipesizeId == null)
                log.info("Invalid input.");
            Long sourcetypeId = 0L;
            try {
                sourcetypeId = jdbcTemplate.queryForObject(sourcetypeQuery,
                        new Object[] { meterWaterRates.getSourceTypeName(), meterWaterRates.getTenantId() },
                        Long.class);
            } catch (final EmptyResultDataAccessException e) {
                log.info("EmptyResultDataAccessException: Query returned empty result set");
            }
            if (sourcetypeId == null)
                log.info("Invalid input.");
            batchValues.add(new MapSqlParameterSource("billingtype", meterWaterRates.getBillingType())
                    .addValue("usagetypeid", meterWaterRates.getUsageTypeId())
                    .addValue("subusagetypeid", meterWaterRates.getSubUsageTypeId())
                    .addValue("outsideulb", meterWaterRates.getOutsideUlb()).addValue("sourcetypeid", sourcetypeId)
                    .addValue("pipesizeid", pipesizeId).addValue("fromdate", meterWaterRates.getFromDate())
                    .addValue("todate", meterWaterRates.getToDate()).addValue("active", meterWaterRates.getActive())
                    .addValue("lastmodifiedby",
                            Long.valueOf(meterWaterRatesRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifieddate", new Date(new java.util.Date().getTime()))
                    .addValue("code", meterWaterRates.getCode()).addValue("tenantid", meterWaterRates.getTenantId())
                    .getValues());
            batchValuesForDeletion.add(new MapSqlParameterSource("meterwaterratesid", Long.valueOf(meterWaterRates.getCode()))
                    .addValue("tenantId", meterWaterRates.getTenantId()).getValues());

            final List<Slab> slabList = meterWaterRates.getSlab();
            if (slabList != null && !slabList.isEmpty()) {

                log.info("the insert query for slab : " + slabInsertQuery);

                for (final Slab slab : slabList)
                    batchValuesSlab
                            .add(new MapSqlParameterSource("meterwaterratesid", Long.valueOf(meterWaterRates.getCode()))
                                    .addValue("fromunit", slab.getFromUnit()).addValue("tounit", slab.getToUnit())
                                    .addValue("unitrate", slab.getUnitRate())
                                    .addValue("tenantid", meterWaterRates.getTenantId()).getValues());

            }
        }
        namedParameterJdbcTemplate.batchUpdate(slabDeleteQuery,
                batchValuesForDeletion.toArray(new Map[meterWaterRatesList.size()]));
        namedParameterJdbcTemplate.batchUpdate(meterWaterRatesUpdateQuery,
                batchValues.toArray(new Map[meterWaterRatesList.size()]));
        namedParameterJdbcTemplate.batchUpdate(slabInsertQuery,
                batchValuesSlab.toArray(new Map[slabs.size()]));
        return meterWaterRatesRequest;
    }

    public List<MeterWaterRates> findForCriteria(final MeterWaterRatesGetRequest meterWaterRatesGetRequest) {
        if (meterWaterRatesGetRequest.getSourceTypeName() != null) {
            final Map<String, Object> paramMapForSourceType = new HashMap<>();
            paramMapForSourceType.put("name", meterWaterRatesGetRequest.getSourceTypeName());
            paramMapForSourceType.put("tenantId", meterWaterRatesGetRequest.getTenantId());
            final Long sourceTypeId = namedParameterJdbcTemplate.queryForObject(
                    MeterWaterRatesQueryBuilder.getSourceTypeIdQueryForSearch(), paramMapForSourceType, Long.class);
            meterWaterRatesGetRequest.setSourceTypeId(sourceTypeId);
        }
        if (meterWaterRatesGetRequest.getUsageTypeCode() != null) {
            final Map<String, Object> paramMapForUsageType = new HashMap<>();
            paramMapForUsageType.put("code", meterWaterRatesGetRequest.getUsageTypeCode());
            paramMapForUsageType.put("tenantId", meterWaterRatesGetRequest.getTenantId());
            final Long usageTypeId = namedParameterJdbcTemplate
                    .queryForObject(MeterWaterRatesQueryBuilder.getUsageTypeIdQueryForSearch(), paramMapForUsageType, Long.class);
            meterWaterRatesGetRequest.setUsageTypeId(String.valueOf(usageTypeId));
        }
        if (meterWaterRatesGetRequest.getSubUsageTypeCode() != null) {
            final Map<String, Object> paramMapForSubUsageType = new HashMap<>();
            paramMapForSubUsageType.put("code", meterWaterRatesGetRequest.getSubUsageTypeCode());
            paramMapForSubUsageType.put("tenantId", meterWaterRatesGetRequest.getTenantId());
            final Long subUsageTypeId = namedParameterJdbcTemplate.queryForObject(
                    MeterWaterRatesQueryBuilder.getUsageTypeIdQueryForSearch(), paramMapForSubUsageType, Long.class);
            meterWaterRatesGetRequest.setSubUsageTypeId(String.valueOf(subUsageTypeId));
        }
        if (meterWaterRatesGetRequest.getPipeSize() != null) {
            final Map<String, Object> paramMapForPipeSize = new HashMap<>();
            paramMapForPipeSize.put("sizeinmilimeter", meterWaterRatesGetRequest.getPipeSize());
            paramMapForPipeSize.put("tenantId", meterWaterRatesGetRequest.getTenantId());
            final Long pipeSizeId = namedParameterJdbcTemplate.queryForObject(
                    MeterWaterRatesQueryBuilder.getPipeSizeIdQueryForSearch(),
                    paramMapForPipeSize, Long.class);
            meterWaterRatesGetRequest.setPipeSizeId(pipeSizeId);
        }
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final String queryStr = meterWaterRatesQueryBuilder.getQuery(meterWaterRatesGetRequest, preparedStatementValues);
        final List<MeterWaterRates> meterWaterRatesList = namedParameterJdbcTemplate.query(queryStr,
                preparedStatementValues, meterWaterRatesRowMapper);
        final String slabQuery = meterWaterRatesQueryBuilder.getSlabDetailsQuery();
        final Map<String, Object> paramsForSlab = new HashMap<>(meterWaterRatesList.size());
        for (final MeterWaterRates meterWaterRate : meterWaterRatesList) {
            paramsForSlab.put("meterwaterratesid", Long.valueOf(meterWaterRate.getCode()));
            paramsForSlab.put("tenantid", meterWaterRate.getTenantId());
            final List<Slab> slabs = namedParameterJdbcTemplate.query(slabQuery, paramsForSlab, slabRowMapper);
            meterWaterRate.setSlab(slabs);
        }
        return meterWaterRatesList;
    }

    public boolean checkMeterWaterRatesExists(final String code, final Long usageTypeId, final Long subUsageTypeId,
            final String sourceTypeName, final Double pipeSize, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String pipesizeQuery = MeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
        Long pipesizeId = 0L;
        try {
            pipesizeId = jdbcTemplate.queryForObject(pipesizeQuery, new Object[] { pipeSize, tenantId }, Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set while update");
        }
        if (pipesizeId == null)
            log.info("Invalid input.");
        final String sourcetypeQuery = MeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
        Long sourcetypeId = 0L;
        try {
            sourcetypeId = jdbcTemplate.queryForObject(sourcetypeQuery, new Object[] { sourceTypeName, tenantId },
                    Long.class);
        } catch (final EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException: Query returned empty result set while update");
        }
        if (pipesizeId == null)
            log.info("Invalid input.");
        preparedStatementValues.add(usageTypeId);
        preparedStatementValues.add(subUsageTypeId);
        preparedStatementValues.add(sourcetypeId);
        preparedStatementValues.add(pipesizeId);
        preparedStatementValues.add(tenantId);
        final String query;
        if (code == null)
            query = MeterWaterRatesQueryBuilder.selectMeterWaterRatesByCodeQuery();
        else {
            preparedStatementValues.add(code);
            query = MeterWaterRatesQueryBuilder.selectMeterWaterRatesByCodeNotInQuery();
        }
        final List<Map<String, Object>> meterWaterRates = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!meterWaterRates.isEmpty())
            return false;

        return true;
    }

    public boolean checkPipeSizeExists(final Double pipeSize, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(pipeSize);
        preparedStatementValues.add(tenantId);
        final String query = MeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
        final List<Map<String, Object>> pipeSizes = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
        if (!pipeSizes.isEmpty())
            return false;

        return true;
    }

    public boolean checkSourceTypeExists(final String sourceTypeName, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(sourceTypeName);
        preparedStatementValues.add(tenantId);
        final String query = MeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
        final List<Map<String, Object>> sourceTypes = jdbcTemplate.queryForList(query,
                preparedStatementValues.toArray());
        if (!sourceTypes.isEmpty())
            return false;

        return true;
    }

    public Map<String, Object> checkUsageAndSubUsageTypeExists(final String usageType, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        preparedStatementValues.add(usageType);
        preparedStatementValues.add(tenantId);
        final String query = MeterWaterRatesQueryBuilder.getUsageTypeIdQuery();

        try {
            return jdbcTemplate.queryForMap(query, preparedStatementValues.toArray());
        } catch (final EmptyResultDataAccessException exception) {
            return new HashMap<String, Object>();
        }

    }

}
