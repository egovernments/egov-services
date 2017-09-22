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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.MeterStatus;
import org.egov.wcms.repository.builder.MeterStatusQueryBuilder;
import org.egov.wcms.repository.rowmapper.MeterStatusRowMapper;
import org.egov.wcms.service.CodeGeneratorService;
import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MeterStatusRepository {
    public static final Logger logger = LoggerFactory.getLogger(MeterStatusRepository.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MeterStatusQueryBuilder meterStatusQueryBuilder;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private MeterStatusRowMapper meterStatusRowMapper;

    public List<MeterStatus> pushCreateToQueue(final MeterStatusReq meterStatusReq) {
        logger.info("Sending MeterStatusRequest to queue");
        final List<MeterStatus> meterStatuses = meterStatusReq.getMeterStatus();
        for (final MeterStatus meterStatus : meterStatuses)
            meterStatus.setCode(codeGeneratorService.generate(MeterStatus.SEQ_METER_STATUS));
        try {
            kafkaTemplate.send(applicationProperties.getCreateMeterStatusTopicName(), meterStatusReq);
        } catch (final Exception e) {
            logger.error("Exception encountered:" + e);
        }
        return meterStatusReq.getMeterStatus();
    }

    public MeterStatusReq create(final MeterStatusReq meterStatusRequest) {
        final List<MeterStatus> meterStatuses = meterStatusRequest.getMeterStatus();
        final List<Map<String, Object>> batchValues = new ArrayList<>(meterStatuses.size());
        for (final MeterStatus meterStatus : meterStatuses)
            batchValues.add(new MapSqlParameterSource("id", Long.valueOf(meterStatus.getCode()))
                    .addValue("code", meterStatus.getCode()).addValue("status", meterStatus.getMeterStatus())
                    .addValue("active", meterStatus.getActive())
                    .addValue("description", meterStatus.getDescription())
                    .addValue("createdby", meterStatusRequest.getRequestInfo().getUserInfo().getId())
                    .addValue("createddate", new Date().getTime())
                    .addValue("lastmodifiedby", meterStatusRequest.getRequestInfo().getUserInfo().getId())
                    .addValue("lastmodifieddate", new Date().getTime()).addValue("tenantId", meterStatus.getTenantId())
                    .getValues());
        namedParameterJdbcTemplate.batchUpdate(meterStatusQueryBuilder.getCreateMeterStatusQuery(),
                batchValues.toArray(new Map[meterStatuses.size()]));
        return meterStatusRequest;

    }

    public List<MeterStatus> pushUpdateToQueue(final MeterStatusReq meterStatusRequest) {
        logger.info("Sending MeterStatusRequest to queue");
        try {
            kafkaTemplate.send(applicationProperties.getUpdateMeterStatusTopicName(), meterStatusRequest);
        } catch (final Exception e) {
            logger.error("Exception encountered:" + e);
        }
        return meterStatusRequest.getMeterStatus();
    }

    public MeterStatusReq update(final MeterStatusReq meterStatusRequest) {
        final List<MeterStatus> meterStatuses = meterStatusRequest.getMeterStatus();
        final List<Map<String, Object>> batchValues = new ArrayList<>(meterStatuses.size());
        for (final MeterStatus meterStatus : meterStatuses)
            batchValues.add(new MapSqlParameterSource("status", meterStatus.getMeterStatus())
                    .addValue("active", meterStatus.getActive())
                    .addValue("description", meterStatus.getDescription())
                    .addValue("lastmodifiedby", meterStatusRequest.getRequestInfo().getUserInfo().getId())
                    .addValue("lastmodifieddate", new Date().getTime()).addValue("code", meterStatus.getCode())
                    .addValue("tenantId", meterStatus.getTenantId()).getValues());
        namedParameterJdbcTemplate.batchUpdate(meterStatusQueryBuilder.getUpdateMeterStatusQuery(),
                batchValues.toArray(new Map[meterStatuses.size()]));
        return meterStatusRequest;
    }

    public List<MeterStatus> getMeterStatusByCriteria(final MeterStatusGetRequest meterStatusGetRequest) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final String queryString = meterStatusQueryBuilder.getQuery(meterStatusGetRequest, preparedStatementValues);
        return namedParameterJdbcTemplate.query(queryString, preparedStatementValues, meterStatusRowMapper);
    }

}
