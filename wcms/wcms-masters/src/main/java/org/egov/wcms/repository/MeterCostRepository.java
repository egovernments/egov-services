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

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.MeterCost;
import org.egov.wcms.repository.builder.MeterCostQueryBuilder;
import org.egov.wcms.repository.rowmapper.MeterCostRowMapper;
import org.egov.wcms.service.CodeGeneratorService;
import org.egov.wcms.web.contract.MeterCostGetRequest;
import org.egov.wcms.web.contract.MeterCostReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository

public class MeterCostRepository {
    public static final Logger logger = LoggerFactory.getLogger(MeterCostRepository.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MeterCostQueryBuilder meterCostQueryBuilder;

    @Autowired
    private MeterCostRowMapper meterCostRowMapper;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public MeterCostReq create(final MeterCostReq meterCostRequest) {
        logger.info("MeterCostRequest::" + meterCostRequest);
        final List<MeterCost> meterCosts = meterCostRequest.getMeterCost();
        final String insertQuery = meterCostQueryBuilder.insertMeterCostQuery();
        final List<Map<String, Object>> batchValues = new ArrayList<>(meterCosts.size());
        for (final MeterCost meterCost : meterCosts)
            batchValues.add(new MapSqlParameterSource("id", Long.valueOf(meterCost.getCode()))
                    .addValue("code", meterCost.getCode())
                    .addValue("metermake", meterCost.getMeterMake()).addValue("amount", meterCost.getAmount())
                    .addValue("active", meterCost.getActive())
                    .addValue("createdby", Long.valueOf(meterCostRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("lastmodifiedby", Long.valueOf(meterCostRequest.getRequestInfo().getUserInfo().getId()))
                    .addValue("createddate", new java.util.Date().getTime())
                    .addValue("lastmodifieddate", new java.util.Date().getTime())
                    .addValue("tenantid", meterCost.getTenantId()).getValues());

        namedParameterJdbcTemplate.batchUpdate(insertQuery, batchValues.toArray(new Map[meterCosts.size()]));
        return meterCostRequest;
    }

    public MeterCostReq update(final MeterCostReq meterCostRequest) {
        logger.info("MeterCostRequest::" + meterCostRequest);
        final List<MeterCost> meterCosts = meterCostRequest.getMeterCost();
        final String updateMeterCostQuery = meterCostQueryBuilder.updateMeterCostQuery();
        final List<Map<String, Object>> batchValues = new ArrayList<>(meterCosts.size());
        for (final MeterCost meterCost : meterCosts)
            batchValues.add(
                    new MapSqlParameterSource("metermake", meterCost.getMeterMake())
                            .addValue("amount", meterCost.getAmount()).addValue("active", meterCost.getActive())
                            .addValue("lastmodifiedby",
                                    Long.valueOf(meterCostRequest.getRequestInfo().getUserInfo().getId()))
                            .addValue("lastmodifieddate", new java.util.Date().getTime())
                            .addValue("code", meterCost.getCode()).addValue("tenantid", meterCost.getTenantId())
                            .getValues());
        namedParameterJdbcTemplate.batchUpdate(updateMeterCostQuery, batchValues.toArray(new Map[meterCosts.size()]));
        return meterCostRequest;
    }

    public List<MeterCost> pushCreateToQueue(final MeterCostReq meterCostRequest) {
        final List<MeterCost> listOfMeterCosts = meterCostRequest.getMeterCost();
        for (final MeterCost meterCost : listOfMeterCosts)
            meterCost.setCode(codeGeneratorService.generate(MeterCost.SEQ_METERCOST));
        try {
            kafkaTemplate.send(applicationProperties.getCreateMeterCostTopicName(), meterCostRequest);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return meterCostRequest.getMeterCost();
    }

    public List<MeterCost> pushUpdateToQueue(final MeterCostReq meterCostRequest) {
        try {
            kafkaTemplate.send(applicationProperties.getUpdateMeterCostTopicName(), meterCostRequest);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return meterCostRequest.getMeterCost();
    }

    public List<MeterCost> searchMeterCostByCriteria(final MeterCostGetRequest meterCostGetRequest) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final String searchQuery = meterCostQueryBuilder.getQuery(meterCostGetRequest, preparedStatementValues);
        final List<MeterCost> listOfMeterCosts = namedParameterJdbcTemplate.query(searchQuery, preparedStatementValues,
                meterCostRowMapper);
        return listOfMeterCosts;
    }

    public Boolean checkMeterMakeAndAmountAlreadyExists(final MeterCost meterCost) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        preparedStatementValues.put("name", meterCost.getMeterMake());
        preparedStatementValues.put("tenantId", meterCost.getTenantId());
        preparedStatementValues.put("amount", meterCost.getAmount());
        final String query;
        if (meterCost.getCode() == null)
            query = meterCostQueryBuilder.selectMeterCostByNameAndTenantIdQuery();
        else {
            preparedStatementValues.put("code", meterCost.getCode());
            query = meterCostQueryBuilder.selectMeterCostByNameTenantIdAndCodeNotInQuery();
        }

        final List<MeterCost> meterMake = namedParameterJdbcTemplate.query(query, preparedStatementValues,
                meterCostRowMapper);

        if (!meterMake.isEmpty())
            return false;

        return true;
    }

}
