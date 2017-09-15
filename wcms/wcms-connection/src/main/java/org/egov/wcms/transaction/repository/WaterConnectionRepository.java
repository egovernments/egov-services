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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.ConnectionOwner;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.EstimationCharge;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.Material;
import org.egov.wcms.transaction.model.Meter;
import org.egov.wcms.transaction.model.MeterReading;
import org.egov.wcms.transaction.model.Property;
import org.egov.wcms.transaction.model.User;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.model.enums.BillingType;
import org.egov.wcms.transaction.model.enums.NewConnectionStatus;
import org.egov.wcms.transaction.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.transaction.repository.rowmapper.ConnectionDocumentRowMapper;
import org.egov.wcms.transaction.repository.rowmapper.WaterConnectionRowMapper;
import org.egov.wcms.transaction.repository.rowmapper.WaterConnectionRowMapper.ConnectionMeterRowMapper;
import org.egov.wcms.transaction.util.ConnectionMasterAdapter;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.PropertyInfo;
import org.egov.wcms.transaction.web.contract.PropertyOwnerInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.UserResponseInfo;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class WaterConnectionRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionRepository.class);
    public static final String roleCode = "CITIZEN"; 

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private WaterConnectionQueryBuilder waterConnectionQueryBuilder;
    
    @Autowired
    private RestConnectionService restConnectionService; 

    public WaterConnectionReq persistConnection(final WaterConnectionReq waterConnectionRequest) {

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
                statement.setString(18, waterConnectionRequest.getConnection().getUsageTypeId());
                statement.setDouble(19, waterConnectionRequest.getConnection().getDonationCharge());
                statement.setString(20, waterConnectionRequest.getConnection().getWaterTreatmentId());
                statement.setBoolean(21, waterConnectionRequest.getConnection().getIsLegacy());
                if (!waterConnectionRequest.getConnection().getIsLegacy()
                        && waterConnectionRequest.getConnection().getId() == 0) {
                    waterConnectionRequest.getConnection().setStatus(NewConnectionStatus.CREATED.name());
                    statement.setString(22, NewConnectionStatus.CREATED.name());
                } else if (waterConnectionRequest.getConnection().getIsLegacy()) {
                    waterConnectionRequest.getConnection().setStatus(NewConnectionStatus.SANCTIONED.name());
                    statement.setString(22, NewConnectionStatus.SANCTIONED.name());
                } else {
                    statement.setString(22, NewConnectionStatus.VERIFIED.name());
                    waterConnectionRequest.getConnection().setStatus(NewConnectionStatus.VERIFIED.name());
                }
                statement.setDouble(23, waterConnectionRequest.getConnection().getNumberOfFamily());
                statement.setString(24, waterConnectionRequest.getConnection().getSubUsageTypeId());
                statement.setString(25, waterConnectionRequest.getConnection().getPlumberName());
                statement.setDouble(26, waterConnectionRequest.getConnection().getBillSequenceNumber() != null
                        ? waterConnectionRequest.getConnection().getBillSequenceNumber() : 0l);

                statement.setBoolean(27, waterConnectionRequest.getConnection().getOutsideULB());
                statement.setString(28, waterConnectionRequest.getConnection().getStorageReservoirId());

                if (waterConnectionRequest.getConnection().getIsLegacy()) {
                    statement.setString(29, waterConnectionRequest.getConnection().getLegacyConsumerNumber());
                    statement.setString(30, waterConnectionRequest.getConnection().getConsumerNumber());
                    statement.setLong(31, waterConnectionRequest.getConnection().getExecutionDate());
                    statement.setInt(32, waterConnectionRequest.getConnection().getNoOfFlats());
                    statement.setString(33, waterConnectionRequest.getConnection().getManualConsumerNumber());
                    statement.setString(34, waterConnectionRequest.getConnection().getHouseNumber());
                    statement.setString(35, waterConnectionRequest.getConnection().getManualReceiptNumber());
                    statement.setLong(36, waterConnectionRequest.getConnection().getManualReceiptDate());

                }

                if (waterConnectionRequest.getConnection().getParentConnectionId() != 0)
                    statement.setLong(32, waterConnectionRequest.getConnection().getParentConnectionId());

                // Please verify if there's proper validation on all these fields to avoid NPE.

                return statement;
            }, keyHolder);

            connectionId = keyHolder.getKey().longValue();
            waterConnectionRequest.getConnection().setId(connectionId);
        } catch (final Exception e) {
            LOGGER.error("Inserting Connection Object failed!", e);
        }

        if (connectionId > 0) {
            final List<Object[]> values = new ArrayList<>();
            if (waterConnectionRequest.getConnection().getDocuments() != null
                    && !waterConnectionRequest.getConnection().getDocuments().isEmpty()) {
                for (final DocumentOwner document : waterConnectionRequest.getConnection().getDocuments()) {
                    document.setDocumentId(Integer.parseInt(document.getDocument()));
                    final Object[] obj = { document.getDocumentId(),
                            document.getName(),
                            document.getFileStoreId(),
                            waterConnectionRequest.getConnection().getId(),
                            waterConnectionRequest.getConnection().getTenantId() };

                    values.add(obj);
                }
                final String insertDocsQuery = WaterConnectionQueryBuilder.insertDocumentQuery();
                try {
                    jdbcTemplate.batchUpdate(insertDocsQuery, values);
                } catch (final Exception e) {
                    LOGGER.error("Inserting documents failed!", e);
                }
            }
        }
        if (connectionId > 0 && null != waterConnectionRequest.getConnection().getBillingType() &&
                waterConnectionRequest.getConnection().getBillingType().equals(BillingType.METERED.toString()) &&
                null != waterConnectionRequest.getConnection().getMeter()) {

            Long meterId = null;
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

                meterId = keyHolder.getKey().longValue();

            } catch (final Exception e) {
                LOGGER.error("Inserting meter failed!", e);

            }

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
                    LOGGER.error("Inserting documents failed!", e);
                }
            }
        }

        return waterConnectionRequest;

    }
    
    public long insertConnectionLocation(final WaterConnectionReq waterConnectionReq) {
        final String persistConnectionLocationQuery = WaterConnectionQueryBuilder.getWaterConnectionLocationQueryForInsert();
        LOGGER.info("Persist Connection Location Query : " + persistConnectionLocationQuery);
        final Connection conn = waterConnectionReq.getConnection();
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update((PreparedStatementCreator) connection -> {
                final String[] returnValColumn = new String[] { "id" };
                final PreparedStatement statement = connection.prepareStatement(persistConnectionLocationQuery,
                        returnValColumn);
                statement.setLong(1, conn.getConnectionLocation().getRevenueBoundary().getId());
                statement.setLong(2, conn.getConnectionLocation().getLocationBoundary() != null
                        && null != conn.getConnectionLocation().getLocationBoundary().getId()
                                ? conn.getConnectionLocation().getLocationBoundary().getId() : 0l);
                statement.setLong(3, conn.getConnectionLocation().getAdminBoundary() != null
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
                new Date(new java.util.Date().getTime()), NewConnectionStatus.ESTIMATIONAMOUNTCOLLECTED,
                consumerCode };
        jdbcTemplate.update(insertquery, obj);

    }

    public void updateValuesForNoPropertyConnections(final WaterConnectionReq waterConnectionReq, final long addressId,
            final long locationId) {
        final String updateQuery = WaterConnectionQueryBuilder.updateValuesForNoPropertyConnections();
        final Object[] obj = new Object[] { waterConnectionReq.getConnection().getConnectionOwner().getId(), addressId,
                locationId, waterConnectionReq.getConnection().getConnectionOwner().getIsPrimaryOwner(),
                waterConnectionReq.getConnection().getAcknowledgementNumber(),
                waterConnectionReq.getConnection().getTenantId() };
        jdbcTemplate.update(updateQuery, obj);
    }

    public WaterConnectionReq updateConnectionWorkflow(final WaterConnectionReq waterConnectionReq,
            final Connection connectiondemand) {
        String insertQuery = "";
        Object[] obj = null;
        if (waterConnectionReq != null) {
            final Connection connection = waterConnectionReq.getConnection();
            insertQuery = WaterConnectionQueryBuilder.updateConnectionQuery();
            obj = new Object[] { connection.getStateId(),
                    connection.getAcknowledgementNumber() };

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
            insertQuery = WaterConnectionQueryBuilder.updateConnectionQuery();
        long estmId = 0;
        if (waterConnectionReq.getConnection().getEstimationCharge() != null
                && !waterConnectionReq.getConnection().getEstimationCharge().isEmpty())
            for (final EstimationCharge estmaCharge : waterConnectionReq.getConnection().getEstimationCharge()) {
                final String insertestQuery = WaterConnectionQueryBuilder.insertEstimationCharge();
                try {

                    final KeyHolder keyHolder = new GeneratedKeyHolder();
                    jdbcTemplate.update((PreparedStatementCreator) connectiontemp -> {
                        final String[] returnValColumn = new String[] { "id" };
                        final PreparedStatement statement = connectiontemp.prepareStatement(insertestQuery,
                                returnValColumn);
                        statement.setLong(1, connection.getId());
                        statement.setString(2, estmaCharge.getExistingDistributionPipeline());
                        statement.setDouble(3, estmaCharge.getPipelineToHomeDistance());

                        statement.setDouble(4, estmaCharge.getEstimationCharges());
                        statement.setDouble(5, estmaCharge.getSupervisionCharges());
                        statement.setDouble(6, estmaCharge.getMaterialCharges());
                        statement.setString(7, connection.getTenantId());
                        statement.setLong(8, waterConnectionReq.getRequestInfo().getUserInfo().getId());
                        statement.setLong(9, waterConnectionReq.getRequestInfo().getUserInfo().getId());
                        statement.setDate(10, new Date(new java.util.Date().getTime()));
                        statement.setDate(11, new Date(new java.util.Date().getTime()));

                        return statement;
                    }, keyHolder);

                    estmId = keyHolder.getKey().longValue();

                } catch (final Exception e) {
                    LOGGER.error("Inserting estimation Charge failed!", e);
                }

                final List<Object[]> values = new ArrayList<>();
                final String insertMaterialQuery = WaterConnectionQueryBuilder.insertMaterial();
                if (null != estmaCharge.getMaterials())
                    for (final Material matObj : estmaCharge.getMaterials()) {
                        final Object[] objct = new Object[] { estmId, matObj.getName(), matObj.getQuantity(),
                                matObj.getSize(), matObj.getAmountDetails(),
                                waterConnectionReq.getConnection().getTenantId(),
                                waterConnectionReq.getRequestInfo().getUserInfo().getId(),
                                new Date(new java.util.Date().getTime()),
                                waterConnectionReq.getRequestInfo().getUserInfo().getId(),
                                new Date(new java.util.Date().getTime()) };
                        values.add(objct);
                        try {
                            jdbcTemplate.batchUpdate(insertMaterialQuery, values);
                        } catch (final Exception e) {
                            LOGGER.error("Inserting material failed!", e);
                        }
                    }
            }

        if (waterConnectionReq.getConnection().getDemandid() != null) {
            final String insertDemandConnectionQuery = WaterConnectionQueryBuilder.insertDemandConnection();

            final Object[] objValue = new Object[] { waterConnectionReq.getConnection().getId(),
                    waterConnectionReq.getConnection().getDemandid(), waterConnectionReq.getConnection().getTenantId(),
                    Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()),
                    Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                    new Date(new java.util.Date().getTime()) };
            jdbcTemplate.update(insertDemandConnectionQuery, objValue);
        }
        final Object[] obj = new Object[] { connection.getConnectionType(), connection.getApplicationType(),
                connection.getBillingType(),
                connection.getHscPipeSizeType(), connection.getSourceType(), connection.getConnectionStatus(),
                connection.getSumpCapacity(), connection.getNumberOfTaps(),
                connection.getNumberOfPersons(), Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), connection.getStateId(), connection.getNumberOfFamily(),
                connection.getStatus(), connection.getEstimationNumber(), connection.getWorkOrderNumber(),
                connection.getConsumerNumber(),
                connection.getAcknowledgementNumber() };
        jdbcTemplate.update(insertQuery, obj);

        return waterConnectionReq;
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
    

	public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq, RequestInfo requestInfo, List<PropertyInfo> propertyInfoList) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		final String fetchQuery = waterConnectionQueryBuilder.getQuery(waterConnectionGetReq, preparedStatementValues);
		LOGGER.info("Get Connection Details Query : " + fetchQuery);
		final List<Connection> connectionList = jdbcTemplate.query(fetchQuery, preparedStatementValues.toArray(),
				new WaterConnectionRowMapper().new WaterConnectionPropertyRowMapper());
		LOGGER.info(connectionList.size() + " Connection Objects fetched from DB");
		addPropertyDetails(propertyInfoList, connectionList);
		if (connectionList.size() == 1 && propertyInfoList.size() == 0) {
			resolvePropertyIdentifierDetails(connectionList, requestInfo);
		}
		final List<Object> secondPreparedStatementValues = new ArrayList<>();
		final String secondFetchQuery = waterConnectionQueryBuilder.getSecondQuery(waterConnectionGetReq,
				secondPreparedStatementValues);
		
		LOGGER.info("Get Connection Details Query for Without Property Cases : " + secondFetchQuery);
		try {
			final List<Connection> secondConnectionList = jdbcTemplate.query(secondFetchQuery,
					secondPreparedStatementValues.toArray(),
					new WaterConnectionRowMapper().new WaterConnectionWithoutPropertyRowMapper());
			LOGGER.info(secondConnectionList.size() + " Connection Objects fetched from DB");
			if (secondConnectionList.size() > 0) {
				if(secondConnectionList.size() == 1)  { 
					resolveUserDetails(secondConnectionList, requestInfo);
				}
				connectionList.addAll(secondConnectionList);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered while fetching the Connection list without Property : " + ex);
		}
		resolveMasterDetails(connectionList, requestInfo);
		// This condition is added to fetch the Meter Details only in single view case. Not in fetch all case 
		if(connectionList.size() == 1) { 
			getConnectionMeterDetails(connectionList);
		}
		return connectionList;
	}
	
	private void resolveUserDetails(List<Connection> secondConnectionList, RequestInfo rInfo) {
		String searchUrl = restConnectionService.getUserServiceSearchPath();
		UserResponseInfo userResponse = null;
		Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
		List<Long> userIds = new ArrayList<>();
		for (Connection conn : secondConnectionList) {
			userIds.add(conn.getConnectionOwner().getId());
			userSearchRequestInfo.put("tenantId", conn.getTenantId());
			userSearchRequestInfo.put("type", roleCode);
			userSearchRequestInfo.put("id", userIds);

			userSearchRequestInfo.put("RequestInfo", rInfo);
			LOGGER.info("User Service Search URL :: " + searchUrl.toString() + " \n userSearchRequestInfo  :: "
					+ userSearchRequestInfo);
			userResponse = new RestTemplate().postForObject(searchUrl.toString(), userSearchRequestInfo,
					UserResponseInfo.class);
			LOGGER.info("User Service Search Response :: " + userResponse);
			ConnectionOwner connOwner = null;
			if (null != userResponse && null != userResponse.getUser() && userResponse.getUser().size() > 0) {
				List<User> userList = userResponse.getUser();
				for (User eachUser : userList) {
					connOwner = new ConnectionOwner();
					connOwner.setName(eachUser.getName());
					connOwner.setPermanentAddress(eachUser.getPermanentAddress());
					connOwner.setUserName(eachUser.getUserName());
					connOwner.setEmailId(eachUser.getEmailId());
					connOwner.setMobileNumber(eachUser.getMobileNumber());
					connOwner.setAadhaarNumber(eachUser.getAadhaarNumber());
					connOwner.setGender(eachUser.getGender());
				}
			}
			if (null != connOwner) {
				conn.setConnectionOwner(connOwner);
			}

		}

	}
	
	private void resolvePropertyIdentifierDetails(List<Connection> connectionList, RequestInfo rInfo) {
		WaterConnectionReq wR = new WaterConnectionReq();
		wR.setRequestInfo(rInfo);
		PropertyResponse propResp = new PropertyResponse(); 
		for(Connection conn : connectionList) { 
			wR.setConnection(conn);
			propResp = restConnectionService.getPropertyDetailsByUpicNo(wR);
		}
		if(null != propResp.getProperties() && propResp.getProperties().size() > 0) { 
			addPropertyDetails(propResp.getProperties(), connectionList) ; 
		}
	}
	
	private void resolveMasterDetails(List<Connection> connectionList, RequestInfo requestInfo) {
		LOGGER.info("Resolving the names for the IDs");
		for(Connection conn : connectionList) { 
			if(StringUtils.isNotBlank(conn.getPipesizeId())) { 
				conn.setHscPipeSizeType(ConnectionMasterAdapter.getPipeSizeById(conn.getPipesizeId(), conn.getTenantId(), requestInfo));
			} 
			if(StringUtils.isNotBlank(conn.getSupplyTypeId())) { 
				conn.setSupplyType(ConnectionMasterAdapter.getSupplyTypeById(conn.getSupplyTypeId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getSourceTypeId())) { 
				conn.setSourceType(ConnectionMasterAdapter.getSourceTypeById(conn.getSourceTypeId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getWaterTreatmentId())) { 
				conn.setWaterTreatment(ConnectionMasterAdapter.getTreatmentPlantById(conn.getWaterTreatmentId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getStorageReservoirId())) { 
				conn.setStorageReservoir(ConnectionMasterAdapter.getStorageReservoiById(conn.getStorageReservoirId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getUsageTypeId())) { 
				conn.setUsageType(ConnectionMasterAdapter.getUsageTypeById(conn.getUsageTypeId(), conn.getTenantId(), requestInfo));
			}
			if(StringUtils.isNotBlank(conn.getSubUsageTypeId())) { 
				conn.setSubUsageType(ConnectionMasterAdapter.getSubUsageTypeById(conn.getSubUsageTypeId(), conn.getTenantId(), requestInfo));
			}
		}
	}
	
	private void addPropertyDetails(List<PropertyInfo> propertyInfoList, List<Connection> connectionWithProperty) {
		LOGGER.info("Adding Property Details Connection List");
		for(Connection conn : connectionWithProperty) { 
			for(PropertyInfo pInfo : propertyInfoList) { 
				if(StringUtils.isNotBlank(conn.getPropertyIdentifier()) && StringUtils.isNotBlank(pInfo.getUpicNumber()) && 
						conn.getPropertyIdentifier().equals(pInfo.getUpicNumber())) {
					Property prop = new Property(); 
					prop.setPropertyidentifier(pInfo.getUpicNumber()); 
					for(PropertyOwnerInfo owner : pInfo.getOwners()) { 
						prop.setNameOfApplicant(owner.getName());
						prop.setAddress(owner.getPermanentAddress());
						prop.setAdharNumber(owner.getAadhaarNumber());
						prop.setEmail(owner.getEmailId());
						prop.setMobileNumber(owner.getMobileNumber());
						prop.setLocality(owner.getLocale());
					}
					conn.setProperty(prop);
				}
			}
		}
	}
	
	private void getConnectionMeterDetails(List<Connection> connectionList) {
		String meterDetailsQuery = WaterConnectionQueryBuilder.getConnectionMeterQueryForSearch();
		for(Connection conn : connectionList) { 
			final List<Object> preparedStatementValues = new ArrayList<>();
			preparedStatementValues.add(conn.getId());
			LOGGER.info("Get Meter Details Query : " + meterDetailsQuery);
			ConnectionMeterRowMapper mapper = new WaterConnectionRowMapper().new ConnectionMeterRowMapper(); 
			jdbcTemplate.query(meterDetailsQuery, preparedStatementValues.toArray(),
					mapper);
			sortMeterDetailsToConnection(conn, mapper);
		}
	}
	
	private void sortMeterDetailsToConnection(Connection conn, ConnectionMeterRowMapper mapper) { 
		Map<Long, Map<Long, Meter>> meterReadingMap = mapper.meterReadingMap; 
		Iterator<Entry<Long, Map<Long, Meter>>> itr = meterReadingMap.entrySet().iterator(); 
		while(itr.hasNext()) { 
			Entry<Long, Map<Long, Meter>> entry = itr.next();
			long connectionId = entry.getKey();
			if(conn.getId() == connectionId) { 
				Map<Long, Meter> innerMap = entry.getValue();
				Iterator<Entry<Long, Meter>> innerItr = innerMap.entrySet().iterator();
				List<Meter> meterList = new ArrayList<>(); 
				while(innerItr.hasNext()) { 
					Entry<Long, Meter> innerEntry = innerItr.next();
					Meter meter = innerEntry.getValue();
					meterList.add(meter); 
				}
				conn.setMeter(meterList);
			}
		}
	}
	
	
/*	private void resolvePropertyUsageTypeNames(String tenantId, List<Connection> connectionList, RequestInfo requestInfo) {
		CommonResponseInfo usage = new CommonResponseInfo();  
		try {
			Map<String, CommonResponseInfo> usagTypeMap = getUsageNameFromPTModule(tenantId, requestInfo);
			for (Connection conn : connectionList) {
				usage = usagTypeMap.get(conn.getUsageTypeId());
				if (null != usage) {
					conn.setUsageType(usage.getName());
				}
				usage = usagTypeMap.get(String.valueOf(conn.getSubUsageTypeId()));
				if (null != usage) {
					conn.setSubUsageType(usage.getName());
				}
			}
		} catch(Exception e) {
			LOGGER.error("Exception Encountered while fetching the name for Property or Usage Type ID" + e);
		}
	}
	
	public Map<String, CommonResponseInfo> getUsageNameFromPTModule(final String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder(configurationManager.getWaterMasterServiceBasePathTopic()).append(configurationManager.getSubUsageTypeSearchPathTopic()); 
		Map<String, CommonResponseInfo> usagTypeMap = new HashMap<>();
		final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		final UsageTypeResponse usageTypes = new RestTemplate().postForObject(url.toString(), request,
				UsageTypeResponse.class, tenantId);
		if(null != usageTypes) { 
			LOGGER.info("Usage Types fetched from the Property Module : " + usageTypes);
			if(null != usageTypes.getUsageTypes()) { 
				for(CommonResponseInfo usage : usageTypes.getUsageTypes()) { 
					usagTypeMap.put(usage.getId(), usage);
				}
			}
		}
		return usagTypeMap;
	}*/
    
    public boolean persistEstimationNoticeLog(EstimationNotice estimationNotice, long connectionId, String tenantId) { 
    	String persistsEstimationNoticeQuery = WaterConnectionQueryBuilder.persistEstimationNoticeQuery();
        int insertStatus = jdbcTemplate.update(persistsEstimationNoticeQuery, getObjectForInsertEstimationNotice(estimationNotice, connectionId, tenantId));
        if(insertStatus > 0) { 
        	return true;
        }
        return false; 
    }

    public boolean persistWorkOrderLog(final WorkOrderFormat workOrder) {
        final String persistsWorkOrderLogQuery = WaterConnectionQueryBuilder.persistWorkOrderQuery();
        LOGGER.info("Persist Work Order Query : " + persistsWorkOrderLogQuery);
        if (namedParameterJdbcTemplate.update(persistsWorkOrderLogQuery, getObjectForInsertWorkOrder(workOrder)) > 0)
            return true;
        return false;
    }

    public Long generateNextConsumerNumber() {
        return jdbcTemplate.queryForObject(WaterConnectionQueryBuilder.getNextConsumerNumberFromSequence(), Long.class);
    }

    public Map<String, Object> getObjectForInsertEstimationNotice(final EstimationNotice estimationNotice,
            final long connectionId,
            final String tenantId) {
        final Long createdBy = 1L;
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("waterConnectionId", connectionId);
        parameters.put("tenantId", tenantId);
        parameters.put("dateOfLetter", estimationNotice.getDateOfLetter());
        parameters.put("letterNumber", estimationNotice.getLetterNumber());
        parameters.put("letterTo", estimationNotice.getLetterTo());
        parameters.put("letterIntimationSubject", estimationNotice.getLetterIntimationSubject());
        parameters.put("applicationNumber", estimationNotice.getApplicationNumber());
        parameters.put("applicationDate", estimationNotice.getApplicationDate());
        parameters.put("applicantName", estimationNotice.getApplicantName());
        parameters.put("serviceName", "New water tap connection");
        parameters.put("waterNumber", estimationNotice.getApplicationNumber());
        parameters.put("slaDays", estimationNotice.getSlaDays());
        parameters.put("chargeDescription1", estimationNotice.getChargeDescription().get(0));
        parameters.put("chargeDescription2", estimationNotice.getChargeDescription().get(1));
        parameters.put("createdBy", createdBy);
        parameters.put("createdDate", new Date(new java.util.Date().getTime()).getTime());
        return parameters;
    }

    public Map<String, Object> getObjectForInsertWorkOrder(final WorkOrderFormat workOrder) {
        final Long createdBy = 1L;
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("waterConnectionId", workOrder.getConnectionId());
        parameters.put("tenantId", workOrder.getTenantId());
        parameters.put("workOrderNumber", workOrder.getWorkOrderNumber());
        parameters.put("workOrderDate", workOrder.getWorkOrderDate());
        parameters.put("waterTapOwnerName", workOrder.getWaterTapOwnerName());
        parameters.put("ackNumber", workOrder.getAckNumber());
        parameters.put("ackNumberDate", workOrder.getAckNumberDate());
        parameters.put("hscNumber", workOrder.getHscNumber());
        parameters.put("hscNumberDate", workOrder.getHscNumberDate());
        parameters.put("serviceName", "New water tap connection");
        // TODO
        parameters.put("plumberName", "To be added");
        //
        parameters.put("createdBy", createdBy);
        parameters.put("createdDate", new Date(new java.util.Date().getTime()).getTime());
        return parameters;
    }

    public Map<String, Object> getObjectForConnectionAddress(final WaterConnectionReq waterConnectionRequest) {
        final Connection conn = waterConnectionRequest.getConnection();
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tenantid", conn.getId());
        parameters.put("latitude", conn.getAddress().getLatitude());
        parameters.put("longitude", conn.getAddress().getLongitude());
        parameters.put("addressId", conn.getAddress().getAddressId());
        parameters.put("addressNumber", conn.getAddress().getAddressNumber());
        parameters.put("addressLine1", conn.getAddress().getAddressLine1());
        parameters.put("addressLine2", conn.getAddress().getAddressLine2());
        parameters.put("landmark", conn.getAddress().getLandMark());
        parameters.put("doorno", conn.getAddress().getDoorNo());
        parameters.put("city", conn.getAddress().getCity());
        parameters.put("pincode", conn.getAddress().getPinCode());
        parameters.put("detail", conn.getAddress().getDetail());
        parameters.put("route", conn.getAddress().getRoute());
        parameters.put("street", conn.getAddress().getStreet());
        parameters.put("area", conn.getAddress().getArea());
        parameters.put("roadname", conn.getAddress().getRoadName());
        parameters.put("createdby", waterConnectionRequest.getRequestInfo().getUserInfo().getId());
        parameters.put("createdtime", new Date(new java.util.Date().getTime()).getTime());
        return parameters;
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
}
