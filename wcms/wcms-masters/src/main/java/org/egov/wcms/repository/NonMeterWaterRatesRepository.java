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
import org.egov.wcms.service.RestWaterExternalMasterService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.NonMeterWaterRatesGetReq;
import org.egov.wcms.web.contract.NonMeterWaterRatesReq;
import org.egov.wcms.web.contract.PropertyTaxResponseInfo;
import org.egov.wcms.web.contract.UsageTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private RestWaterExternalMasterService restExternalMasterService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public NonMeterWaterRatesReq persistCreateNonMeterWaterRates(final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        log.info("NonMeterWaterRatesReq::" + nonMeterWaterRatesReq);
        final String nonMeterWaterRatesInsertQuery = NonMeterWaterRatesQueryBuilder.insertNonMeterWaterRatesQuery();
        final String pipesizeQuery = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
        final String sourcetypeQuery = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
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

    public NonMeterWaterRatesReq persistUpdateNonMeterWaterRates(final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        log.info("NonMeterWaterRatesReq::" + nonMeterWaterRatesReq);
        final String nonMeterWaterRatesUpdateQuery = NonMeterWaterRatesQueryBuilder.updateNonMeterWaterRatesQuery();
        final String pipesizeQuery = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
        final String sourcetypeQuery = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
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

        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final List<Integer> usageTypeIdsList = new ArrayList<>();
        final List<Integer> subUsageTypeIdsList = new ArrayList<>();
        final String queryStr = nonMeterWaterRatesQueryBuilder.getQuery(nonMeterWaterRatesGetRequest, preparedStatementValues);
        final List<NonMeterWaterRates> nonMeterWaterRatesList = namedParameterJdbcTemplate.query(queryStr,
                preparedStatementValues, nonMeterWaterRatesRowMapper);
        log.info("NonMeterWaterRatesListFromDB"+nonMeterWaterRatesList);
        // fetch usage type Id and set the usage type name here
        for (final NonMeterWaterRates nonMeterWaterRates : nonMeterWaterRatesList)
            usageTypeIdsList.add(Integer.valueOf(nonMeterWaterRates.getUsageTypeId()));
        final Integer[] usageTypeIds = usageTypeIdsList.toArray(new Integer[usageTypeIdsList.size()]);
        final UsageTypeResponse usageResponse = restExternalMasterService.getUsageNameFromPTModule(
                usageTypeIds, WcmsConstants.WC, nonMeterWaterRatesGetRequest.getTenantId());
        for (final NonMeterWaterRates nonMeterWaterRatesObj : nonMeterWaterRatesList)
            for (final PropertyTaxResponseInfo propertyResponse : usageResponse.getUsageMasters())
                if (propertyResponse.getId().equals(nonMeterWaterRatesObj.getUsageTypeId()))
                    nonMeterWaterRatesObj.setUsageTypeName(propertyResponse.getName());
        // fetch sub usage type Id and set the usage type name here
        for (final NonMeterWaterRates nonMeterWaterRates : nonMeterWaterRatesList)
            subUsageTypeIdsList.add(Integer.valueOf(nonMeterWaterRates.getSubUsageTypeId()));
        final Integer[] subUsageTypeIds = subUsageTypeIdsList.toArray(new Integer[subUsageTypeIdsList.size()]);
        final UsageTypeResponse subUsageResponse = restExternalMasterService.getSubUsageNameFromPTModule(
                subUsageTypeIds, WcmsConstants.WC, nonMeterWaterRatesGetRequest.getTenantId());
        for (final NonMeterWaterRates nonMeterWaterRatesObj : nonMeterWaterRatesList)
            for (final PropertyTaxResponseInfo propertyResponse : subUsageResponse.getUsageMasters())
                if (propertyResponse.getId().equals(nonMeterWaterRatesObj.getSubUsageTypeId()))
                    nonMeterWaterRatesObj.setSubUsageType(propertyResponse.getName());
        return nonMeterWaterRatesList;
    }

    public boolean checkNonMeterWaterRatesExists(final String code, final String connectionType, final String usageTypeId,
            final String sourceTypeName, final Double pipeSize, final Long fromDate, final String tenantId) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final Map<String, Object> batchArguments = new HashMap<>();
        final String pipesizeQuery = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
        final String sourcetypeQuery = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
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
        final String query = NonMeterWaterRatesQueryBuilder.getPipeSizeIdQuery();
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
        final String query = NonMeterWaterRatesQueryBuilder.getSourceTypeIdQuery();
        final List<Long> sourceTypes = namedParameterJdbcTemplate.queryForList(query,
                preparedStatementValues, Long.class);
        if (!sourceTypes.isEmpty())
            return false;

        return true;
    }
}
