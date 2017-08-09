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
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.MeterCostCriteria;
import org.egov.wcms.model.MeterCost;
import org.egov.wcms.repository.builder.MeterCostQueryBuilder;
import org.egov.wcms.repository.rowmapper.MeterCostRowMapper;
import org.egov.wcms.web.contract.MeterCostReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository

public class MeterCostRepository {
	public static final Logger logger = LoggerFactory.getLogger(MeterCostRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private MeterCostQueryBuilder meterCostQueryBuilder;

	@Autowired
	private MeterCostRowMapper meterCostRowMapper;

	public MeterCostReq persistCreateMeterCost(final MeterCostReq meterCostRequest) {
		logger.info("MeterCostRequest::" + meterCostRequest);
		List<MeterCost> meterCosts = meterCostRequest.getMeterCost();
		List<Object[]> batchArguments = new ArrayList<>();
		String insertQuery = meterCostQueryBuilder.insertMeterCostQuery();
		for (MeterCost meterCost : meterCosts) {
			final Object[] obj = { getNextSequenceForMeterCost("seq_egwtr_meter_cost"), meterCost.getCode(),
					meterCost.getPipeSizeId(), meterCost.getMeterMake(), meterCost.getAmount(), meterCost.getActive(),
					Long.valueOf(meterCostRequest.getRequestInfo().getUserInfo().getId()),
					Long.valueOf(meterCostRequest.getRequestInfo().getUserInfo().getId()),
					new java.util.Date().getTime(), new java.util.Date().getTime(),
					meterCost.getTenantId() };
			batchArguments.add(obj);
		}
		jdbcTemplate.batchUpdate(insertQuery, batchArguments);
		return meterCostRequest;
	}

	public MeterCostReq persistUpdateMeterCost(MeterCostReq meterCostRequest) {
		logger.info("MeterCostRequest::" + meterCostRequest);
		List<MeterCost> meterCosts = meterCostRequest.getMeterCost();
		List<Object[]> batchArguments = new ArrayList<>();
		String updateMeterCostQuery = meterCostQueryBuilder.updateMeterCostQuery();
		for (MeterCost meterCost : meterCosts) {
			Object[] obj = { meterCost.getPipeSizeId(), meterCost.getMeterMake(), meterCost.getAmount(),
					meterCost.getActive(), meterCostRequest.getRequestInfo().getUserInfo().getId(),
					new java.util.Date().getTime(), meterCost.getCode(), meterCost.getTenantId() };
			batchArguments.add(obj);
		}
		jdbcTemplate.batchUpdate(updateMeterCostQuery, batchArguments);
		return meterCostRequest;
	}

	public List<MeterCost> pushCreateMeterCostReqToQueue(MeterCostReq meterCostRequest) {
		try {
			kafkaTemplate.send(applicationProperties.getCreateMeterCostTopicName(), meterCostRequest);
		} catch (final Exception ex) {
			logger.error("Exception Encountered : " + ex);
		}
		return meterCostRequest.getMeterCost();
	}

	public List<MeterCost> pushUpdateMeterCostReqToQueue(MeterCostReq meterCostRequest) {
		try {
			kafkaTemplate.send(applicationProperties.getUpdateMeterCostTopicName(), meterCostRequest);
		} catch (Exception ex) {
			logger.error("Exception Encountered : " + ex);
		}
		return meterCostRequest.getMeterCost();
	}

	private Long getNextSequenceForMeterCost(String sequenceName) {
		return jdbcTemplate.queryForObject("SELECT nextval('" + sequenceName + "')", Long.class);
	}

	public List<MeterCost> searchMeterCostByCriteria(MeterCostCriteria meterCostCriteria) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String searchQuery = meterCostQueryBuilder.getQuery(meterCostCriteria, preparedStatementValues);
		return jdbcTemplate.query(searchQuery, preparedStatementValues.toArray(), meterCostRowMapper);

	}

	public Boolean checkMeterMakeAlreadyExistsInDB(MeterCost meterCost) {
		String code=meterCost.getCode();
		String name=meterCost.getMeterMake();
		String tenantId=meterCost.getTenantId();
	
		        final List<Object> preparedStatementValues = new ArrayList<>();
		        preparedStatementValues.add(name);
		        preparedStatementValues.add(tenantId);
		        final String query;
		        if (code == null)
		            query = meterCostQueryBuilder.selectMeterCostByNameAndTenantIdQuery();
		        else {
		            preparedStatementValues.add(code);
		            query = meterCostQueryBuilder.selectMeterCostByNameTenantIdAndCodeNotInQuery();
		        }
		        final List<Map<String, Object>> meterMake = jdbcTemplate.queryForList(query,
		                preparedStatementValues.toArray());
		        if (!meterMake.isEmpty())
		            return false;

		        return true;
		    }
		
	}

