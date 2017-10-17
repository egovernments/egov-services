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
package org.egov.wcms.transaction.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.MeterReading;
import org.egov.wcms.transaction.model.enums.BillingType;
import org.egov.wcms.transaction.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MeterRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(MeterRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void persistMeterReading(final WaterConnectionReq waterConnectionRequest, final Long meterId) {
        if (!waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings().isEmpty()) {
            final String insertMeterReadingQuery = WaterConnectionQueryBuilder.insertMeterReadingQuery();
            final List<Object[]> values = new ArrayList<>();
            for (final MeterReading meterReading : waterConnectionRequest.getConnection().getMeter().get(0)
                    .getMeterReadings()) {

                final Object[] obj = { meterId,
                        meterReading.getReading(),
                        meterReading.getReadingDate(), waterConnectionRequest.getConnection().getTenantId(),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(),
                        new Date(new java.util.Date().getTime()),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(),
                        new Date(new java.util.Date().getTime()),
                        meterReading.getGapCode() != null ? meterReading.getGapCode() : "",
                        meterReading.getConsumption() != null ? meterReading.getConsumption() : "",
                        meterReading.getConsumptionAdjusted() != null ? meterReading.getConsumptionAdjusted() : "",
                        meterReading.getNumberOfDays() != null ? meterReading.getNumberOfDays() : "",
                        meterReading.getResetFlag() != null ? meterReading.getResetFlag() : false
                };

                values.add(obj);
            }
            try {
                jdbcTemplate.batchUpdate(insertMeterReadingQuery, values);
            } catch (final Exception e) {
                LOGGER.error("Inserting MetereReading failed!", e);
            }
        }
    }
    
    public void removeMeterReading(final WaterConnectionReq waterConnectionRequest, final Long meterId) { 
    	if (!waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings().isEmpty()) {
    		String removeMeterReadingQuery = WaterConnectionQueryBuilder.removeMeterReadingQuery();
    		final HashMap<String, Object> parametersMap = new HashMap<>();
            parametersMap.put("meterId", meterId);
            namedParameterJdbcTemplate.update(removeMeterReadingQuery, parametersMap);
    	}
    }

    public void updateMeter(final WaterConnectionReq waterConnectionRequest, Long connectionId) { 
    	if (connectionId > 0 && null != waterConnectionRequest.getConnection().getBillingType() &&
                waterConnectionRequest.getConnection().getBillingType().equals(BillingType.METERED.toString()) &&
                null != waterConnectionRequest.getConnection().getMeter()) {
    		final String updateMeterQuery = WaterConnectionQueryBuilder.updateMeterQuery();
    		namedParameterJdbcTemplate.update(updateMeterQuery, getMeterParametersMap(waterConnectionRequest, connectionId));
    		removeMeterReading(waterConnectionRequest, waterConnectionRequest.getConnection().getMeter().get(0).getId());
    		persistMeterReading(waterConnectionRequest, waterConnectionRequest.getConnection().getMeter().get(0).getId());
    	}
    }
    
    public HashMap<String, Object> getMeterParametersMap(WaterConnectionReq waterConnectionRequest, Long connectionId) {
    	Connection conn = waterConnectionRequest.getConnection();
		final HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("connectionId", connectionId);
        parametersMap.put("tenantId", conn.getTenantId());
        parametersMap.put("meterId", conn.getMeter().get(0).getId());
        parametersMap.put("metermake", conn.getMeter().get(0).getMeterMake());
        parametersMap.put("initialmeterreading", conn.getMeter().get(0).getInitialMeterReading());
        parametersMap.put("meterSlNo", conn.getMeter().get(0).getMeterSlNo()); 
        parametersMap.put("meterCost" , conn.getMeter().get(0).getMeterCost()); 
        parametersMap.put("lastmodifiedby", waterConnectionRequest.getRequestInfo().getUserInfo().getId()); 
        parametersMap.put("lastmodifiedtime", new Date(new java.util.Date().getTime()));
        parametersMap.put("meterowner", conn.getMeter().get(0).getMeterOwner());
        parametersMap.put("metermodel", conn.getMeter().get(0).getMeterModel());
        parametersMap.put("maximumMeterReading", conn.getMeter().get(0).getMaximumMeterReading());
        parametersMap.put("meterStatus", conn.getMeter().get(0).getMeterStatus());
        return parametersMap; 
    }
    
    
    public WaterConnectionReq persistMeter(final WaterConnectionReq waterConnectionRequest,Long connectionId) {
        if (connectionId > 0 && null != waterConnectionRequest.getConnection().getBillingType() &&
                waterConnectionRequest.getConnection().getBillingType().equals(BillingType.METERED.toString()) &&
                null != waterConnectionRequest.getConnection().getMeter()) {
        final String insertestQuery = WaterConnectionQueryBuilder.insertMeterQuery();
        try {

            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update((PreparedStatementCreator) connectiontemp -> {
                final String[] returnValColumn = new String[] { "id" };
                final PreparedStatement statement = connectiontemp.prepareStatement(insertestQuery,
                        returnValColumn);
                statement.setLong(1, waterConnectionRequest.getConnection().getId());
                statement.setString(2, waterConnectionRequest.getConnection().getMeter().get(0).getMeterMake());
                statement.setString(3, waterConnectionRequest.getConnection().getMeter().get(0).getInitialMeterReading());

                statement.setString(4, waterConnectionRequest.getConnection().getMeter().get(0).getMeterSlNo());
                statement.setString(5, waterConnectionRequest.getConnection().getMeter().get(0).getMeterCost());
                statement.setString(6, waterConnectionRequest.getConnection().getTenantId());
                statement.setLong(7, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setDate(8, new Date(new java.util.Date().getTime()));

                statement.setLong(9, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setDate(10, new Date(new java.util.Date().getTime()));
                statement.setString(11, waterConnectionRequest.getConnection().getMeter().get(0).getMeterOwner() != null
                        ? waterConnectionRequest.getConnection().getMeter().get(0).getMeterOwner() : "");
                statement.setString(12, waterConnectionRequest.getConnection().getMeter().get(0).getMeterModel() != null
                        ? waterConnectionRequest.getConnection().getMeter().get(0).getMeterModel() : "");
                statement.setString(13,
                        waterConnectionRequest.getConnection().getMeter().get(0).getMaximumMeterReading() != null
                                ? waterConnectionRequest.getConnection().getMeter().get(0).getMaximumMeterReading() : "");
                statement.setString(14, waterConnectionRequest.getConnection().getMeter().get(0).getMeterStatus() != null
                        ? waterConnectionRequest.getConnection().getMeter().get(0).getMeterStatus() : "");

                return statement;
            }, keyHolder);

         Long meterId=keyHolder.getKey().longValue();
          System.out.println("meterId" + meterId);
            persistMeterReading(waterConnectionRequest,meterId);
        } catch (final Exception e) {
            LOGGER.error("Inserting meter failed!", e);

        }
        
    }
        return waterConnectionRequest;  
    }

}
