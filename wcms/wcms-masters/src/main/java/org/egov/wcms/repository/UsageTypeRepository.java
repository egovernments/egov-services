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

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.UsageType;
import org.egov.wcms.repository.builder.UsageTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.SubUsageTypeRowMapper;
import org.egov.wcms.repository.rowmapper.UsageTypeRowMapper;
import org.egov.wcms.web.contract.UsageTypeGetRequest;
import org.egov.wcms.web.contract.UsageTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsageTypeRepository {

    public static final Logger logger = LoggerFactory.getLogger(UsageTypeRepository.class);
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private UsageTypeQueryBuilder usageTypeQueryBuilder;

    @Autowired
    private UsageTypeRowMapper usageTypeRowMapper;

    @Autowired
    private SubUsageTypeRowMapper subUsageTypeRowMapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CodeSequenceNumberGenerator codeSequenceNumberGenerator;

    public List<UsageType> getUsageTypesByCriteria(final UsageTypeGetRequest usageTypeGetRequest) {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final String usageTypeQuery = usageTypeQueryBuilder.getQuery(usageTypeGetRequest, preparedStatementValues);
        if (!usageTypeGetRequest.getIsSubUsageType())
            return namedParameterJdbcTemplate.query(usageTypeQuery, preparedStatementValues, usageTypeRowMapper);
        else
            return namedParameterJdbcTemplate.query(usageTypeQuery, preparedStatementValues, subUsageTypeRowMapper);

    }

    public List<UsageType> pushCreateToQueue(final UsageTypeReq usageTypeRequest) {
        final List<UsageType> usageTypes = usageTypeRequest.getUsageTypes();
        for (final UsageType usageType : usageTypes)
            usageType.setCode(codeSequenceNumberGenerator.getNextSequence(UsageType.SEQ_USAGE_TYPE).toString());
        logger.info("Sending UsageType request to kafka Queue:" + usageTypeRequest);
        try {
            kafkaTemplate.send(applicationProperties.getCreateUsageTypeTopicName(), usageTypeRequest);
        } catch (final Exception e) {
            logger.error("Exception encountered:" + e);
        }
        return usageTypeRequest.getUsageTypes();
    }

    @SuppressWarnings("unchecked")
    public UsageTypeReq create(final UsageTypeReq usageTypeRequest) {
        final List<UsageType> usageTypes = usageTypeRequest.getUsageTypes();
        final String usageTypeInsertQuery = usageTypeQueryBuilder.getUsageTypeInsertQuery();
        final List<Map<String, Object>> usageTypeParamValues = new ArrayList<>(usageTypes.size());
        for (final UsageType usageType : usageTypes)
            usageTypeParamValues.add(new MapSqlParameterSource("id", Long.valueOf(usageType.getCode()))
                    .addValue("code", usageType.getCode()).addValue("name", usageType.getName())
                    .addValue("description", usageType.getDescription()).addValue("parent", usageType.getParent())
                    .addValue("active", usageType.getActive())
                    .addValue("createdby", usageTypeRequest.getRequestInfo().getUserInfo().getId())
                    .addValue("createddate", new Date().getTime())
                    .addValue("lastmodifiedby", usageTypeRequest.getRequestInfo().getUserInfo().getId())
                    .addValue("lastmodifieddate", new Date().getTime()).addValue("tenantid", usageType.getTenantId())
                    .getValues());
        try {
            namedParameterJdbcTemplate.batchUpdate(usageTypeInsertQuery,
                    usageTypeParamValues.toArray(new Map[usageTypes.size()]));
        } catch (final Exception e) {
            logger.error("Error occured while persisting create usageType Request to db:" + e);
        }
        return usageTypeRequest;
    }

    public List<UsageType> pushUpdateToQueue(final UsageTypeReq usageTypeRequest) {
        logger.info("Sending UsageType request to kafka Queue:" + usageTypeRequest);
        try {
            kafkaTemplate.send(applicationProperties.getUpdateUsageTypeTopicName(), usageTypeRequest);
        } catch (final Exception e) {
            logger.error("Exception encountered:" + e);
        }
        return usageTypeRequest.getUsageTypes();
    }

    @SuppressWarnings("unchecked")
    public UsageTypeReq update(final UsageTypeReq usageTypeRequest) {
        final List<UsageType> usageTypes = usageTypeRequest.getUsageTypes();
        final String usageTypeUpdateQuery = usageTypeQueryBuilder.getUpdateUsageTypeQuery();
        final List<Map<String, Object>> usageTypeQueryParams = new ArrayList<>();
        for (final UsageType usageType : usageTypes)
            usageTypeQueryParams.add(new MapSqlParameterSource("name", usageType.getName())
                    .addValue("description", usageType.getDescription()).addValue("parent", usageType.getParent())
                    .addValue("active", usageType.getActive())
                    .addValue("lastmodifiedby", usageTypeRequest.getRequestInfo().getUserInfo().getId())
                    .addValue("lastmodifieddate", new Date().getTime()).addValue("code", usageType.getCode())
                    .addValue("tenantid", usageType.getTenantId()).getValues());
        try {
            namedParameterJdbcTemplate.batchUpdate(usageTypeUpdateQuery,
                    usageTypeQueryParams.toArray(new Map[usageTypes.size()]));
        } catch (final Exception e) {
            logger.error("Error occured while updating usageType in db" + e);
        }
        return usageTypeRequest;
    }

}
