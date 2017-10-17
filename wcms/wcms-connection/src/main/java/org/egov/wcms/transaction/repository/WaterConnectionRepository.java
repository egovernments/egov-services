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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.ConnectionOwner;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.EnumData;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.model.enums.NewConnectionStatus;
import org.egov.wcms.transaction.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.transaction.repository.rowmapper.ConnectionDocumentRowMapper;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WaterConnectionRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionRepository.class);
    public static final String roleCode = "CITIZEN"; 

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    

    public Long createConnection(final WaterConnectionReq waterConnectionRequest) {

        return persistConnection(waterConnectionRequest);

    }

    public Long persistConnection(final WaterConnectionReq waterConnectionRequest) {
        String insertQuery = "";
        if (waterConnectionRequest.getConnection().getIsLegacy())
            insertQuery = WaterConnectionQueryBuilder.insertLegacyConnectionQuery();
        else if (waterConnectionRequest.getConnection().getParentConnectionId() != 0)
            insertQuery = WaterConnectionQueryBuilder.insertAdditionalConnectionQuery();
        else
            insertQuery = WaterConnectionQueryBuilder.insertConnectionQuery();

        final String query = insertQuery;
        LOGGER.info("Insert Query is : " + insertQuery);
        LOGGER.info("Created By and Updated By : " + waterConnectionRequest.getRequestInfo());

        Long connectionId = 0L;
        try {
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update((PreparedStatementCreator) connection -> {
                final String[] returnValColumn = new String[] { "id" };
                final PreparedStatement statement = connection.prepareStatement(query,
                        returnValColumn);
                statement.setString(1, waterConnectionRequest.getConnection().getTenantId());
                statement.setString(2, waterConnectionRequest.getConnection().getConnectionType());
                statement.setString(3, waterConnectionRequest.getConnection().getApplicationType());
                statement.setString(4, waterConnectionRequest.getConnection().getBillingType());
                statement.setString(5, waterConnectionRequest.getConnection().getPipesizeId());
                statement.setString(6, waterConnectionRequest.getConnection().getSupplyTypeId());
                statement.setString(7, waterConnectionRequest.getConnection().getSourceTypeId());
                statement.setString(8, waterConnectionRequest.getConnection().getConnectionStatus());
                statement.setDouble(9, waterConnectionRequest.getConnection().getSumpCapacity());
                statement.setInt(10, waterConnectionRequest.getConnection().getNumberOfTaps());
                statement.setInt(11, waterConnectionRequest.getConnection().getNumberOfPersons());
                statement.setString(12, waterConnectionRequest.getConnection().getAcknowledgementNumber());
                statement.setLong(13, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setLong(14, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setDate(15, new Date(new java.util.Date().getTime()));
                statement.setDate(16, new Date(new java.util.Date().getTime()));
                statement.setString(17, waterConnectionRequest.getConnection().getPropertyIdentifier());
                statement.setString(18,  StringUtils.isNotBlank(waterConnectionRequest.getConnection().getOldPropertyIdentifier()) ? waterConnectionRequest.getConnection().getOldPropertyIdentifier() :  "");
                statement.setString(19, waterConnectionRequest.getConnection().getUsageTypeId());
                statement.setDouble(20, waterConnectionRequest.getConnection().getDonationCharge());
                statement.setString(21, waterConnectionRequest.getConnection().getWaterTreatmentId());
                statement.setBoolean(22, waterConnectionRequest.getConnection().getIsLegacy());
                if (!waterConnectionRequest.getConnection().getIsLegacy()
                        && waterConnectionRequest.getConnection().getId() == 0) {
                    waterConnectionRequest.getConnection().setStatus(NewConnectionStatus.CREATED.name());
                    statement.setString(23, NewConnectionStatus.CREATED.name());
                } else if (waterConnectionRequest.getConnection().getIsLegacy()) {
                    waterConnectionRequest.getConnection().setStatus(NewConnectionStatus.SANCTIONED.name());
                    statement.setString(23, NewConnectionStatus.SANCTIONED.name());
                } else {
                    statement.setString(23, NewConnectionStatus.VERIFIED.name());
                    waterConnectionRequest.getConnection().setStatus(NewConnectionStatus.VERIFIED.name());
                }
                statement.setDouble(24, waterConnectionRequest.getConnection().getNumberOfFamily());
                statement.setString(25, waterConnectionRequest.getConnection().getSubUsageTypeId());
                statement.setString(26, waterConnectionRequest.getConnection().getPlumberName());
                statement.setDouble(27, waterConnectionRequest.getConnection().getBillSequenceNumber() != null
                        ? waterConnectionRequest.getConnection().getBillSequenceNumber() : 0l);

                statement.setBoolean(28, waterConnectionRequest.getConnection().getOutsideULB());
                statement.setString(29, waterConnectionRequest.getConnection().getStorageReservoirId());

                if (waterConnectionRequest.getConnection().getIsLegacy()) {
                    statement.setString(30, waterConnectionRequest.getConnection().getLegacyConsumerNumber());
                    statement.setString(31, waterConnectionRequest.getConnection().getConsumerNumber());
                    statement.setLong(32, waterConnectionRequest.getConnection().getExecutionDate());
                    statement.setInt(33, waterConnectionRequest.getConnection().getNoOfFlats());
                    statement.setString(34, waterConnectionRequest.getConnection().getManualConsumerNumber());
                    statement.setString(35, waterConnectionRequest.getConnection().getHouseNumber());
                    statement.setString(36, waterConnectionRequest.getConnection().getManualReceiptNumber());
                    statement.setLong(37, waterConnectionRequest.getConnection().getManualReceiptDate());

                }

                if (waterConnectionRequest.getConnection().getParentConnectionId() != 0)
                    statement.setLong(33, waterConnectionRequest.getConnection().getParentConnectionId());

                // Please verify if there's proper validation on all these fields to avoid NPE.

                return statement;
            }, keyHolder);

            connectionId = keyHolder.getKey().longValue();
            waterConnectionRequest.getConnection().setId(connectionId);
        } catch (final Exception e) {
            LOGGER.error("Inserting Connection Object failed!", e);
        }
        return connectionId;
    }

    
    
    public long persistConnectionLocation(final WaterConnectionReq waterConnectionReq) {
        final String persistConnectionLocationQuery = WaterConnectionQueryBuilder.getWaterConnectionLocationQueryForInsert();
        LOGGER.info("Persist Connection Location Query : " + persistConnectionLocationQuery);
        final Connection conn = waterConnectionReq.getConnection();
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update((PreparedStatementCreator) connection -> {
                final String[] returnValColumn = new String[] { "id" };
                final PreparedStatement statement = connection.prepareStatement(persistConnectionLocationQuery,
                        returnValColumn);
                statement.setLong(1, null != conn.getConnectionLocation().getRevenueBoundary() 
                		&& null != conn.getConnectionLocation().getRevenueBoundary().getId()
                				? conn.getConnectionLocation().getRevenueBoundary().getId() : 0l);
                statement.setLong(2, null != conn.getConnectionLocation().getLocationBoundary()
                        && null != conn.getConnectionLocation().getLocationBoundary().getId()
                                ? conn.getConnectionLocation().getLocationBoundary().getId() : 0l);
                statement.setLong(3, null != conn.getConnectionLocation().getAdminBoundary()
                		&& null != conn.getConnectionLocation().getAdminBoundary().getId() 
                        		? conn.getConnectionLocation().getAdminBoundary().getId() : 0l);
                statement.setString(4, conn.getConnectionLocation().getBillingAddress());
                statement.setString(5, conn.getConnectionLocation().getBuildingName());
                statement.setString(6, conn.getConnectionLocation().getGisNumber());
                statement.setString(7, conn.getConnectionLocation().getRoadName());
                statement.setLong(8, waterConnectionReq.getRequestInfo().getUserInfo().getId());
                statement.setDate(9, new Date(new java.util.Date().getTime()));
                return statement;
            }, keyHolder);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
        }
        return keyHolder.getKey().longValue();
    }

    public void updateConnectionAfterWorkFlowQuery(final String consumerCode) {
        final String insertquery = WaterConnectionQueryBuilder.updateConnectionAfterWorkFlowQuery();
        final Object[] obj = new Object[] {
                new Date(new java.util.Date().getTime()), NewConnectionStatus.APPLICATIONFEESPAID,
                consumerCode };
        jdbcTemplate.update(insertquery, obj);

    }

    public void updateValuesForNoPropertyConnections(final WaterConnectionReq waterConnectionReq, final long addressId,
            final long locationId) {
        final String updateQuery = WaterConnectionQueryBuilder.updateValuesForNoPropertyConnections();
        final Object[] obj = new Object[] {  addressId,locationId,
                waterConnectionReq.getConnection().getAcknowledgementNumber(),
                waterConnectionReq.getConnection().getTenantId() };
        jdbcTemplate.update(updateQuery, obj);
    }
    public Long getObjectByParam(String query, Map<String, Object> paramMap)
    {
     return  namedParameterJdbcTemplate.queryForObject(query, paramMap, Long.class);   
    }
    
    public void updateValuesForWithPropertyConnections(final WaterConnectionReq waterConnectionReq, 
            final long locationId) {
        final String updateQuery = WaterConnectionQueryBuilder.updateValuesForWithPropertyConnections();
        final Object[] obj = new Object[] { locationId , 
                waterConnectionReq.getConnection().getAcknowledgementNumber(),
                waterConnectionReq.getConnection().getTenantId() };
        jdbcTemplate.update(updateQuery, obj);
    }

    public WaterConnectionReq updateConnectionAfterWorkflow(final WaterConnectionReq waterConnectionReq,
            final Connection connectiondemand) {
        String insertQuery = "";
        Object[] obj = null;
        if (waterConnectionReq != null) {
            final Connection connection = waterConnectionReq.getConnection();
            insertQuery = WaterConnectionQueryBuilder.updateConnectionQuery();
            obj = new Object[] { connection.getStateId(),
                    connection.getAcknowledgementNumber() ,connection.getTenantId()};

        } else if (connectiondemand.getDemandid() != null) {
            final String insertDemandConnectionQuery = WaterConnectionQueryBuilder.insertDemandConnection();

            final Object[] objValue = new Object[] { connectiondemand.getId(),
                    connectiondemand.getDemandid(), connectiondemand.getTenantId(),
                    Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()),
                    Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()) };
            jdbcTemplate.update(insertDemandConnectionQuery, objValue);
        }

        jdbcTemplate.update(insertQuery, obj);

        return waterConnectionReq;
    }

    public void updateConnectionOnChangeOfDemand(final String demandId, final Connection waterConn,
            final RequestInfo requestInfo) {
        if (demandId != null) {
            final String insertDemandConnectionQuery = WaterConnectionQueryBuilder.insertDemandConnection();

            final Object[] objValue = new Object[] { waterConn.getId(),
                    demandId, waterConn.getTenantId(), Long.valueOf(requestInfo.getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()),
                    Long.valueOf(requestInfo.getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()) };
            jdbcTemplate.update(insertDemandConnectionQuery, objValue);
        }
    }

    public WaterConnectionReq updateWaterConnection(final WaterConnectionReq waterConnectionReq) {
        String insertQuery = "";
        final Connection connection = waterConnectionReq.getConnection();
        if (waterConnectionReq.getConnection().getId() != 0)
            insertQuery = WaterConnectionQueryBuilder.updateConnection();

        if (waterConnectionReq.getConnection().getDemandid() != null) {
            final String insertDemandConnectionQuery = WaterConnectionQueryBuilder.insertDemandConnection();

            final Object[] objValue = new Object[] { waterConnectionReq .getConnection().getId(),
                    waterConnectionReq.getConnection().getDemandid(), waterConnectionReq.getConnection().getTenantId(),
                    Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()),
                    Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()) };
            jdbcTemplate.update(insertDemandConnectionQuery, objValue);
        }
        final Object[] obj = new Object[] {connection.getTenantId(),connection.getPipesizeId()
                ,connection.getSupplyTypeId(), connection.getSourceTypeId(), connection.getConnectionStatus(),
                connection.getSumpCapacity(), connection.getNumberOfTaps(),
                connection.getNumberOfPersons(), Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()),connection.getUsageTypeId(),connection.getWaterTreatmentId(),
                connection.getStatus(), connection.getNumberOfFamily(),connection.getSubUsageTypeId(),
                connection.getPlumberName(),connection.getBillSequenceNumber(),
                connection.getOutsideULB(),connection.getStorageReservoirId(),connection.getStateId(),
                connection.getEstimationNumber(), connection.getWorkOrderNumber(),
                connection.getConsumerNumber(),
                connection.getAcknowledgementNumber() };
        jdbcTemplate.update(insertQuery, obj);

        return waterConnectionReq;
    }
    
    public Long updateLegacyWaterConnection(final WaterConnectionReq waterConnectionReq) {
    	String updateLegacyConnectionQuery = "" ; 
    	final Connection connection = waterConnectionReq.getConnection();
    	if(connection.getId() != 0) { 
    		updateLegacyConnectionQuery = WaterConnectionQueryBuilder.updateLegacyConnectionQuery();
    		final Object[] obj = new Object[] {connection.getPipesizeId()
                    ,connection.getSupplyTypeId(), connection.getSourceTypeId(), connection.getConnectionStatus(),
                    connection.getSumpCapacity(), connection.getNumberOfTaps(),
                    connection.getNumberOfPersons(), Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()),connection.getUsageTypeId(),connection.getWaterTreatmentId(),
                    connection.getStatus(), connection.getNumberOfFamily(),connection.getSubUsageTypeId(),
                    connection.getPlumberName(),connection.getBillSequenceNumber(),
                    connection.getOutsideULB(),connection.getStorageReservoirId(),connection.getStateId(),
                    connection.getEstimationNumber(), connection.getWorkOrderNumber(),
                    connection.getConsumerNumber(), connection.getLegacyConsumerNumber(), connection.getManualConsumerNumber(),
                    connection.getPropertyIdentifier(), connection.getConsumerNumber(), connection.getTenantId()};
            int updateStatus = jdbcTemplate.update(updateLegacyConnectionQuery, obj);
           LOGGER.info("Update Status has been : " + updateStatus ) ;  
    	}
    	return waterConnectionReq.getConnection().getId(); 
    }

    public List<Connection> findByApplicationNmber(final String acknowledgeNumber, final String tenantid) {
        final HashMap<String, Object> parametersMap = new HashMap<>();
        if (StringUtils.isNotEmpty(acknowledgeNumber)) {
            parametersMap.put("acknowledgeNumber", acknowledgeNumber);
            parametersMap.put("tenantid", tenantid);
        }
        return namedParameterJdbcTemplate.query(WaterConnectionQueryBuilder.getWaterConnectionByacknowledgenumber(),
                parametersMap, new BeanPropertyRowMapper<>(Connection.class));
    }

    public List<Connection> getWaterConnectionByConsumerNumber(final String consumerNumber, final String legacyConsumerNumber,
            final String tenantid) {
        if (StringUtils.isNotBlank(consumerNumber)) {
            final HashMap<String, Object> parametersMap = new HashMap<>();
            parametersMap.put("consumerNumber", consumerNumber);
            parametersMap.put("tenantid", tenantid);
            return namedParameterJdbcTemplate.query(WaterConnectionQueryBuilder.getWaterConnectionByConsumerNumber(),
                    parametersMap, new BeanPropertyRowMapper<>(Connection.class));
        } else {
            final HashMap<String, Object> parametersMap = new HashMap<>();
            parametersMap.put("legacyConsumerNumber", legacyConsumerNumber);
            parametersMap.put("tenantid", tenantid);
            return namedParameterJdbcTemplate.query(WaterConnectionQueryBuilder.getWaterConnectionByLegacyConsumernumber(),
                    parametersMap, new BeanPropertyRowMapper<>(Connection.class));
        }
    }
    public List<ConnectionOwner> getConnectionOwner( final Long connId,
            final String tenantid){
        final HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("waterconnectionid", connId);
        parametersMap.put("tenantid", tenantid);
        return namedParameterJdbcTemplate.query(WaterConnectionQueryBuilder.getConnectionOwnersByConnectionId(),
                parametersMap, new BeanPropertyRowMapper<>(ConnectionOwner.class));
    }
    
    public List<EnumData> getExecutionDatePeriodCycle(String ackNumber, String tenantId) { 
    	return jdbcTemplate.query(WaterConnectionQueryBuilder.getExecutionDateForAckNumber(), new Object[] { ackNumber, tenantId } , new BeanPropertyRowMapper<>(EnumData.class));
    }

    public boolean persistEstimationNoticeLog(EstimationNotice estimationNotice, long connectionId, String tenantId,Map<String, Object> estimationNoticeMap) { 
        String persistsEstimationNoticeQuery = WaterConnectionQueryBuilder.persistEstimationNoticeQuery();
        LOGGER.info("Persist Estimation Notice Query : " + persistsEstimationNoticeQuery);
        if(namedParameterJdbcTemplate.update(persistsEstimationNoticeQuery, estimationNoticeMap) > 0) { 
                return true;
        }
        return false; 
    }

    public boolean persistWorkOrderLog(final WorkOrderFormat workOrder,Map<String, Object> workOrderMap) {
        final String persistsWorkOrderLogQuery = WaterConnectionQueryBuilder.persistWorkOrderQuery();
        LOGGER.info("Persist Work Order Query : " + persistsWorkOrderLogQuery);
        if (namedParameterJdbcTemplate.update(persistsWorkOrderLogQuery, workOrderMap) > 0)
            return true;
        return false;
    }

    public Long generateNextConsumerNumber() {
        return jdbcTemplate.queryForObject(WaterConnectionQueryBuilder.getNextConsumerNumberFromSequence(), Long.class);
    }

    


    public List<DocumentOwner> getDocumentForConnection(final Connection connection) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String fetchQuery = WaterConnectionQueryBuilder.getDocumentForConnection();
        LOGGER.info("Get Document for Connection Query : " + fetchQuery);
        List<DocumentOwner> documentList = new ArrayList<>();
        preparedStatementValues.add(connection.getId());
        preparedStatementValues.add(connection.getTenantId());
        try { 
                documentList = jdbcTemplate.query(fetchQuery, preparedStatementValues.toArray(),new ConnectionDocumentRowMapper());
        } catch(Exception ex) { 
                LOGGER.error("Exception encountered while fetching documents : " +ex);
        }
                
        return documentList;
    }
    
    public void removeConnectionOwnerDetails(WaterConnectionReq waterConnectionRequest, Long connectionId) { 
    	Connection connection = waterConnectionRequest.getConnection();
    	final HashMap<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("waterconnectionid", connectionId);
        parametersMap.put("tenantid", connection.getTenantId());
        namedParameterJdbcTemplate.update(WaterConnectionQueryBuilder.removeConnectionUserQuery(),
                parametersMap); 
    	
    }

	public void pushUserDetails(WaterConnectionReq waterConnectionRequest,Long connectionId) {
    Connection connection = waterConnectionRequest.getConnection();
		String insertUserQuery = WaterConnectionQueryBuilder.insertConnectionUserQuery();
		List<Map<String,Object>> batchValues = new ArrayList<>(connection.getConnectionOwners().size());
		Double i = 1d;
		for(ConnectionOwner connectionOwner : connection.getConnectionOwners()){
			batchValues.add( new MapSqlParameterSource("waterconnectionid", connectionId)
					.addValue("ownerid", connectionOwner.getOwnerid())
					.addValue("primaryowner", connectionOwner.getPrimaryOwner()!=null ? connectionOwner.getPrimaryOwner() : false)
					.addValue("ordernumber", i++)
			        .addValue("tenantid", connection.getTenantId())
			        .addValue("createdby", waterConnectionRequest.getRequestInfo().getUserInfo().getId())
			        .addValue("lastmodifiedby", waterConnectionRequest.getRequestInfo().getUserInfo().getId())
			        .addValue("createdtime", new java.util.Date().getTime())
			        .addValue("lastmodifiedtime", new java.util.Date().getTime())
			        .getValues());
		}
		namedParameterJdbcTemplate.batchUpdate(insertUserQuery, batchValues.toArray(new Map
				[connection.getConnectionOwners().size()]));
	}
}
