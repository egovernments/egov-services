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
import java.util.List;

import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.EstimationCharge;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.Material;
import org.egov.wcms.transaction.model.MeterReading;
import org.egov.wcms.transaction.model.enums.BillingType;
import org.egov.wcms.transaction.model.enums.NewConnectionStatus;
import org.egov.wcms.transaction.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.transaction.repository.rowmapper.UpdateWaterConnectionRowMapper;
import org.egov.wcms.transaction.repository.rowmapper.WaterConnectionRowMapper;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WaterConnectionRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterConnectionRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WaterConnectionQueryBuilder waterConnectionQueryBuilder;

    public WaterConnectionReq persistConnection(final WaterConnectionReq waterConnectionRequest) {

        String insertQuery = "";
        if (waterConnectionRequest.getConnection().getIsLegacy())
            insertQuery = WaterConnectionQueryBuilder.insertLegacyConnectionQuery();
        else if (waterConnectionRequest.getConnection().getParentConnectionId() != 0)
            insertQuery = WaterConnectionQueryBuilder.insertAdditionalConnectionQuery();
        else
            insertQuery = WaterConnectionQueryBuilder.insertConnectionQuery();

        final String query = insertQuery;

        long connectionId = 0L;
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
                statement.setString(5, waterConnectionRequest.getConnection().getCategoryId());
                statement.setString(6, waterConnectionRequest.getConnection().getPipesizeId());
                statement.setString(7, waterConnectionRequest.getConnection().getSupplyTypeId());
                statement.setString(8, waterConnectionRequest.getConnection().getSourceTypeId());
                statement.setString(9, waterConnectionRequest.getConnection().getConnectionStatus());
                statement.setDouble(10, waterConnectionRequest.getConnection().getSumpCapacity());
                statement.setInt(11, waterConnectionRequest.getConnection().getNumberOfTaps());
                statement.setInt(12, waterConnectionRequest.getConnection().getNumberOfPersons());
                statement.setString(13, waterConnectionRequest.getConnection().getAcknowledgementNumber());
                statement.setLong(14, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setLong(15, waterConnectionRequest.getRequestInfo().getUserInfo().getId());
                statement.setDate(16, new Date(new java.util.Date().getTime()));
                statement.setDate(17, new Date(new java.util.Date().getTime()));
                statement.setString(18, waterConnectionRequest.getConnection().getPropertyIdentifier());
                statement.setString(19, waterConnectionRequest.getConnection().getProperty().getUsageTypeId());
                statement.setString(20, waterConnectionRequest.getConnection().getProperty().getPropertyTypeId());
                statement.setString(21, "AddressTest"); // waterConnectionRequest.getConnection().getProperty().getAddress());
                statement.setDouble(22, waterConnectionRequest.getConnection().getDonationCharge());

                statement.setString(23, waterConnectionRequest.getConnection().getAssetIdentifier());
                statement.setString(24, waterConnectionRequest.getConnection().getWaterTreatmentId());
                statement.setBoolean(25, waterConnectionRequest.getConnection().getIsLegacy());
                if (!waterConnectionRequest.getConnection().getIsLegacy() && waterConnectionRequest.getConnection().getId() == 0)
                    statement.setString(26, NewConnectionStatus.CREATED.name());
                else if (waterConnectionRequest.getConnection().getIsLegacy())
                    statement.setString(26, NewConnectionStatus.SANCTIONED.name());
                else
                    statement.setString(26, NewConnectionStatus.VERIFIED.name());
                if (!waterConnectionRequest.getConnection().getIsLegacy()) {
                    statement.setLong(27, waterConnectionRequest.getConnection().getStateId() != null
                            ? waterConnectionRequest.getConnection().getStateId() : 1l);
                    statement.setString(28, waterConnectionRequest.getConnection().getDemandid());
                }
                if (waterConnectionRequest.getConnection().getIsLegacy()
                        || waterConnectionRequest.getConnection().getParentConnectionId() != 0) {
                    statement.setString(27, waterConnectionRequest.getConnection().getLegacyConsumerNumber());
                    statement.setString(28, waterConnectionRequest.getConnection().getConsumerNumber());
                }

                if (waterConnectionRequest.getConnection().getParentConnectionId() != 0)
                    statement.setLong(29, waterConnectionRequest.getConnection().getParentConnectionId());

                // Please verify if there's proper validation on all these fields to avoid NPE.

                return statement;
            }, keyHolder);

            connectionId = keyHolder.getKey().longValue();
            waterConnectionRequest.getConnection().setId(connectionId);
        } catch (final Exception e) {
            LOGGER.error("Inserting Connection Object failed!", e);
        }

        if (connectionId > 0 && !waterConnectionRequest.getConnection().getIsLegacy()) {
            final List<Object[]> values = new ArrayList<>();
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
        } else if (connectionId > 0 && waterConnectionRequest.getConnection().getBillingType() != null &&
                waterConnectionRequest.getConnection().getBillingType().equals(BillingType.METERED) &&
                !waterConnectionRequest.getConnection().getMeter().isEmpty()) {

            final String insertMeterQuery = WaterConnectionQueryBuilder.insertMeterQuery();
            try {
                final Object[] obj = new Object[] { waterConnectionRequest.getConnection().getMeter().get(0).getMeterMake(),
                        connectionId, waterConnectionRequest.getConnection().getMeter().get(0).getInitialMeterReading(),
                        waterConnectionRequest.getConnection().getTenantId(),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()),
                        waterConnectionRequest.getRequestInfo().getUserInfo().getId(), new Date(new java.util.Date().getTime()) };

                jdbcTemplate.update(insertMeterQuery, obj);
            } catch (final Exception e) {
                LOGGER.error("Inserting Meter failed!", e);
            }
            if (!waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings().isEmpty()) {
                final String insertMeterReadingQuery = WaterConnectionQueryBuilder.insertMeterReadingQuery();
                final List<Object[]> values = new ArrayList<>();
                for (final MeterReading meterReading : waterConnectionRequest.getConnection().getMeter().get(0)
                        .getMeterReadings()) {

                    final Object[] obj = { connectionId,
                            meterReading.getReading(), waterConnectionRequest.getConnection().getTenantId(),
                            waterConnectionRequest.getRequestInfo().getUserInfo().getId(),
                            meterReading.getAuditDetails().getCreatedDate(),
                            waterConnectionRequest.getRequestInfo().getUserInfo().getId(),
                            meterReading.getAuditDetails().getCreatedDate() };

                    values.add(obj);
                }
                try {
                    jdbcTemplate.batchUpdate(insertMeterReadingQuery, values);
                } catch (final Exception e) {
                    LOGGER.error("Inserting documents failed!", e);
                }
            }
        }

        LOGGER.info("Insertion to document owner table left unattempted upon failure of connection object insertion.");
        return waterConnectionRequest;

    }

    public WaterConnectionReq updateConnectionWorkflow(final WaterConnectionReq waterConnectionReq,Connection connectiondemand)
    {
        String insertQuery = "";
         Object[] obj =null;
        if(waterConnectionReq!=null){
            Connection connection=waterConnectionReq.getConnection();
        insertQuery = WaterConnectionQueryBuilder.updateConnectionQuery();
       obj = new Object[] { connection.getConnectionType(), connection.getApplicationType(),
                connection.getBillingType(), connection.getCategoryId(),
                connection.getPipesizeId(), connection.getSourceTypeId(), connection.getConnectionStatus(),
                connection.getSumpCapacity(), connection.getNumberOfTaps(),
                connection.getNumberOfPersons(), Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), connection.getStateId(),
                connection.getDemandid(), connection.getAcknowledgementNumber() };
        }
        else{
         
            insertQuery = WaterConnectionQueryBuilder.updateConnectionByConsumerNumberQuery();
           obj= new Object[] {connectiondemand.getDemandid(),connectiondemand.getConsumerNumber() };
        }
       
        jdbcTemplate.update(insertQuery, obj);
        
        return waterConnectionReq;
    }
    public void updateConnectionOnChangeOfDemand(final String demandId ,String consumerNumber)
    {
        String insertQuery = "";
        Object[] obj =null;
        insertQuery = WaterConnectionQueryBuilder.updateConnectionByConsumerNumberQuery();
        obj= new Object[] {demandId, consumerNumber};
        jdbcTemplate.update(insertQuery, obj);
    }
    public WaterConnectionReq updateWaterConnection(final WaterConnectionReq waterConnectionReq) {
        String insertQuery = "";
        final Connection connection = waterConnectionReq.getConnection();
        if (waterConnectionReq.getConnection().getId() != 0)
            insertQuery = WaterConnectionQueryBuilder.updateConnectionQuery();
        long estmId = 0;
        if(waterConnectionReq.getConnection().getEstimationCharge()!=null && !waterConnectionReq.getConnection().getEstimationCharge().isEmpty())
        {
        for(EstimationCharge estmaCharge:waterConnectionReq.getConnection().getEstimationCharge())
        {
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
                    
                    statement.setDouble(4,estmaCharge.getEstimationCharges());
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
				if (null != estmaCharge.getMaterials()) {
					for (Material matObj : estmaCharge.getMaterials()) {
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
        }
        }
       
        final Object[] obj = new Object[] { connection.getConnectionType(), connection.getApplicationType(),
                connection.getCategoryType(), connection.getBillingType(),
                connection.getHscPipeSizeType(), connection.getSourceType(), connection.getConnectionStatus(),
                connection.getSumpCapacity(), connection.getNumberOfTaps(),
                connection.getNumberOfPersons(), Long.valueOf(waterConnectionReq.getRequestInfo().getUserInfo().getId()),
                new Date(new java.util.Date().getTime()), connection.getStatus(), connection.getStateId(),
                connection.getDemandid(), connection.getAcknowledgementNumber() };
        jdbcTemplate.update(insertQuery, obj);
        
        return waterConnectionReq;
    }

    public Connection findByApplicationNmber(final String acknowledgeNumber) {

        final Connection connection = jdbcTemplate.queryForObject(
                WaterConnectionQueryBuilder.getWaterConnectionByacknowledgenumber(), new UpdateWaterConnectionRowMapper(),
                acknowledgeNumber);
        return connection;
    }
    public Connection getWaterConnectionByConsumerNumber(final String acknowledgeNumber) {

        final Connection connection = jdbcTemplate.queryForObject(
                WaterConnectionQueryBuilder.getWaterConnectionByConsumerNumber(), new UpdateWaterConnectionRowMapper(),
                acknowledgeNumber);
        return connection;
    }


    public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String fetchQuery = waterConnectionQueryBuilder.getQuery(waterConnectionGetReq, preparedStatementValues);
        final List<Connection> connectionList = jdbcTemplate.query(fetchQuery, preparedStatementValues.toArray(),
                new WaterConnectionRowMapper());
        return connectionList;
    }
    
    public boolean persistEstimationNoticeLog(EstimationNotice estimationNotice, long connectionId, String tenantId) { 
    	String persistsEstimationNoticeQuery = WaterConnectionQueryBuilder.persistEstimationNoticeQuery();
        int insertStatus = jdbcTemplate.update(persistsEstimationNoticeQuery, getObjectForInsertEstimationNotice(estimationNotice, connectionId, tenantId));
        if(insertStatus > 0) { 
        	return true;
        }
        return false;
    }
    
    public Object[] getObjectForInsertEstimationNotice(EstimationNotice estimationNotice, long connectionId, String tenantId) {
    	Long createdBy = 1L; 
        final Object[] obj = new Object[] { connectionId, tenantId, estimationNotice.getDateOfLetter(), estimationNotice.getLetterNumber(),
        		estimationNotice.getLetterTo(), estimationNotice.getLetterIntimationSubject(), estimationNotice.getApplicationNumber(), estimationNotice.getApplicationDate(),
        		estimationNotice.getApplicantName(), estimationNotice.getServiceName(), estimationNotice.getWaterNo(), estimationNotice.getSlaDays(), estimationNotice.getChargeDescription1(),
        		estimationNotice.getChargeDescription2(), createdBy, new Date(new java.util.Date().getTime()) };
        return obj;
    	
    	
    }
}
