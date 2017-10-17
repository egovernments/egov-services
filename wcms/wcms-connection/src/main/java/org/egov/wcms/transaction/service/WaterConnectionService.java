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

package org.egov.wcms.transaction.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.transaction.config.ApplicationProperties;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.demand.contract.PeriodCycle;
import org.egov.wcms.transaction.exception.WaterConnectionException;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.ConnectionOwner;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.EnumData;
import org.egov.wcms.transaction.model.User;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.model.WorkflowDetails;
import org.egov.wcms.transaction.model.enums.NewConnectionStatus;
import org.egov.wcms.transaction.repository.ApplicationDocumentRepository;
import org.egov.wcms.transaction.repository.MeterRepository;
import org.egov.wcms.transaction.repository.WaterConnectionRepository;
import org.egov.wcms.transaction.repository.WaterConnectionSearchRepository;
import org.egov.wcms.transaction.repository.builder.WaterConnectionQueryBuilder;
import org.egov.wcms.transaction.utils.ConnectionMasterAdapter;
import org.egov.wcms.transaction.utils.ConnectionUtils;
import org.egov.wcms.transaction.utils.WcmsConnectionConstants;
import org.egov.wcms.transaction.validator.ConnectionValidator;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.NonMeterWaterRates;
import org.egov.wcms.transaction.web.contract.PropertyInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WaterConnectionService {

    @Autowired
    private DemandConnectionService demandConnectionService;

    @Autowired
    private WaterConnectionRepository waterConnectionRepository;

    @Autowired
    private WaterConnectionSearchRepository waterConnectionSearchRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RestConnectionService restConnectionService;

    @Autowired
    private ApplicationDocumentRepository applicationDocumentRepository;

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private ConnectionValidator connectionValidator;

    @Autowired
    private ConnectionUserService connectionUserService;

    public static final String roleCode = "CITIZEN";
    public static final String roleName = "Citizen";
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private ConnectionUtils   connectionUtils;

    public Connection pushConnectionToKafka(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        return sendRequestObjToProducer(topic, key, waterConnectionRequest);
    }

    public Connection updateWaterConnection(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        return sendRequestObjToProducer(topic, key, waterConnectionRequest);
    }

    protected Connection sendRequestObjToProducer(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        try {
            kafkaTemplate.send(topic, key, waterConnectionRequest);
        } catch (final Exception e) {
            log.error("Producer failed to post request to kafka queue", e);
            return waterConnectionRequest.getConnection();
        }
        if (waterConnectionRequest.getConnection().getProperty() != null
                && waterConnectionRequest.getConnection().getPropertyIdentifier() != null)
            waterConnectionRequest.getConnection().setWithProperty(Boolean.TRUE);
        else
            waterConnectionRequest.getConnection().setWithProperty(Boolean.FALSE);
        return waterConnectionRequest.getConnection();
    }

    public Connection persistBeforeKafkaPush(final WaterConnectionReq waterConnectionRequest) {
        log.info("Service API entry for create Connection");
        Long connectionLocationId = 0L;
          if (waterConnectionRequest.getConnection() != null && waterConnectionRequest.getConnection().getIsLegacy())
                waterConnectionRequest.getConnection()
                        .setConnectionStatus(WcmsConnectionConstants.CONNECTIONSTATUSACTIVE);
            else
                waterConnectionRequest.getConnection().setConnectionStatus(WcmsConnectionConstants.CONNECTIONSTATUSCREAED);

            if (waterConnectionRequest.getConnection().getWithProperty()) {
                log.info("Persisting Connection Details :: With Property :: ");
                final Long connectionId = waterConnectionRepository.createConnection(waterConnectionRequest);
                log.info("Persisting Connection Document Details :: With Property :: ");
                applicationDocumentRepository.persistApplicationDocuments(waterConnectionRequest, connectionId);
                if (waterConnectionRequest.getConnection().getIsLegacy()) {
                    log.info("Persisting Meter Details :: With Property :: ");
                    meterRepository.persistMeter(waterConnectionRequest, connectionId);
                }
                log.info("Persisting Location Details :: With Property :: ");
               if( waterConnectionRequest.getConnection().getConnectionLocation()!=null){
                connectionLocationId = waterConnectionRepository.persistConnectionLocation(waterConnectionRequest);
               }
                log.info("Updating Water Connection :: With Property :: ");
                waterConnectionRepository.updateValuesForWithPropertyConnections(waterConnectionRequest, connectionLocationId);
            } else {
                log.info("Creating User Id :: Without Property :: ");
                connectionUserService.createUserId(waterConnectionRequest);
                log.info("Persisting Location Details :: Without Property :: ");
                connectionLocationId = waterConnectionRepository.persistConnectionLocation(waterConnectionRequest);
                log.info("Persisting Connection Details :: Without Property :: ");
                final Long connectionId = waterConnectionRepository.createConnection(waterConnectionRequest);
                log.info("Persisting Connection User :: Without Property :: ");
                waterConnectionRepository.pushUserDetails(waterConnectionRequest,connectionId);
                log.info("Persisting Connection Document Details :: Without Property :: ");
                applicationDocumentRepository.persistApplicationDocuments(waterConnectionRequest, connectionId);
                if (waterConnectionRequest.getConnection().getIsLegacy()) {
                    log.info("Persisting Meter Details :: Without Property :: ");
                    meterRepository.persistMeter(waterConnectionRequest, connectionId);
                }
                log.info("Updating Water Connection :: Without Property :: ");
                waterConnectionRepository.updateValuesForNoPropertyConnections(waterConnectionRequest,
                        0l, connectionLocationId);
                // TODO: updateValuesForNoPropertyConnections API needs to do in create connection api only
            }
        return waterConnectionRequest.getConnection();
    }

    public Connection create(final WaterConnectionReq waterConnectionRequest) {
        log.info("Service API entry for update with initiate workflow Connection");
        try {
            if (waterConnectionRequest.getConnection().getIsLegacy() != null &&
                    waterConnectionRequest.getConnection().getIsLegacy().equals(Boolean.FALSE)){
                createDemand(waterConnectionRequest);
                waterConnectionRepository.updateConnectionAfterWorkflow(waterConnectionRequest, null);
            }
        } catch (final Exception e) {
            log.error("workflow intiate and updating connection failed due to db exception", e);               
            throw new WaterConnectionException("workflow intiate and updating connection failed due to db exception", "workflow intiate and updating connection failed due to db exception", waterConnectionRequest.getRequestInfo());

            
        }
        return waterConnectionRequest.getConnection();
    }

    public Connection update(final WaterConnectionReq waterConnectionRequest) {
        log.info("Service API entry for update Connection");
            try {
                waterConnectionRepository.updateWaterConnection(waterConnectionRequest);
            } catch (final WaterConnectionException e) {
                log.error("update Connection failed due to db exception", e);
                throw new WaterConnectionException("update Connection failed due to db exception", "update Connection failed due to db exception", waterConnectionRequest.getRequestInfo());
            }
        return waterConnectionRequest.getConnection();
    }
    
	public Connection updateLegacyConnection(final WaterConnectionReq waterConnectionRequest) {
		log.info("Service API entry for Update Legacy Connection");
		try {
			connectionUserService.createUserId(waterConnectionRequest);
			final Long connectionId = waterConnectionRepository.updateLegacyWaterConnection(waterConnectionRequest);
			waterConnectionRepository.removeConnectionOwnerDetails(waterConnectionRequest, connectionId);
			waterConnectionRepository.pushUserDetails(waterConnectionRequest, connectionId);
			meterRepository.updateMeter(waterConnectionRequest, connectionId);
		} catch (final WaterConnectionException e) {
			log.error("Update Legacy Connection has failed due to an execption at DB " + e);
			throw new WaterConnectionException("Update Legacy Connection has failed due to an execption at DB",
					"Update Legacy Connection Failure", waterConnectionRequest.getRequestInfo());
		}
		return waterConnectionRequest.getConnection();
	}

    public void setApplicationStatus(final WaterConnectionReq waterConnectionRequest) {
        final Connection connection = waterConnectionRequest.getConnection();

        if (connection.getStatus() != null) {
            if (connection.getStatus().equalsIgnoreCase(NewConnectionStatus.WORKORDERGENERATED.name())) {
                connection.setStatus(NewConnectionStatus.SANCTIONED.name());
            }
            if (connection.getStatus().equalsIgnoreCase(NewConnectionStatus.APPROVED.name())) {
                restConnectionService.prepareWorkOrderNumberFormat(waterConnectionRequest);
                connection.setStatus(NewConnectionStatus.WORKORDERGENERATED.name());
            }

        }

        if (connection.getStatus().equalsIgnoreCase(NewConnectionStatus.VERIFIED.name()) ||
                connection.getStatus().equalsIgnoreCase(NewConnectionStatus.APPLICATIONFEESPAID.name())) {
            restConnectionService.generateEstimationNumber(waterConnectionRequest);
            connection.setStatus(NewConnectionStatus.APPROVED.name());
            waterConnectionRequest.getConnection()
                    .setConsumerNumber(connectionValidator.generateConsumerNumber(waterConnectionRequest));

        }
        if (connection.getStatus().equalsIgnoreCase(NewConnectionStatus.CREATED.name())) {
            connection.setStatus(NewConnectionStatus.VERIFIED.name());
        }

        waterConnectionRequest.setConnection(connection);
    }

    
    public void prepareWorkFlowDetails(final WaterConnectionReq waterConnectionRequest) {

        if (null != waterConnectionRequest.getConnection()
                && null != waterConnectionRequest.getConnection().getWorkflowDetails()) {

            final WorkflowDetails workFlowDetails = waterConnectionRequest.getConnection().getWorkflowDetails();

            if (workFlowDetails.getStateId() != null)
                waterConnectionRequest.getConnection().setStateId(Long.valueOf(workFlowDetails.getStateId()));
        }

    }

    public void prepareConnectionUpdate(final WaterConnectionReq waterConnectionRequest) {
        if (!waterConnectionRequest.getConnection().getIsLegacy()) {
            prepareWorkFlowDetails(waterConnectionRequest);
            setApplicationStatus(waterConnectionRequest);
        }
    }

    public void validateinUpdate(final WaterConnectionReq waterConnectionRequest) {
        idExistenceCheck(waterConnectionRequest.getConnection());
        validateAcknowledgementNumber(waterConnectionRequest.getConnection());
    }

    public Boolean idExistenceCheck(final Connection connection) {

        final Map<String, Object> paramMap = new HashMap<>();
        final String query = WaterConnectionQueryBuilder.getConnectionObjById();
        paramMap.put("id", connection.getId());
        final Long count = waterConnectionRepository.getObjectByParam(query, paramMap);
        return count == 1 ? true : false;
    }

    public Boolean validateAcknowledgementNumber(final Connection connection) {
        List<Connection> tempConnList;
        Connection connectionObj = null;
        tempConnList = waterConnectionRepository.findByApplicationNmber(connection.getAcknowledgementNumber(),
                connection.getTenantId());
        if (!tempConnList.isEmpty() && tempConnList.size() == 1)
            connectionObj = tempConnList.get(0);
        return connectionObj != null ? true : false;
    }
    
	public boolean validateLegacyDataForUpdate(final WaterConnectionReq waterConnectionRequest) {
		boolean idExists = idExistenceCheck(waterConnectionRequest.getConnection());
		Connection conn = getWaterConnectionByConsumerNumber(waterConnectionRequest.getConnection().getConsumerNumber(),
				waterConnectionRequest.getConnection().getLegacyConsumerNumber(),
				waterConnectionRequest.getConnection().getTenantId());
		boolean consumerNumberExists = false; 
		if(null != conn) { 
			consumerNumberExists = true; 
		}
		if(idExists && consumerNumberExists) 
			return true;
		else
			return false; 
	}

    public Connection getWaterConnectionByConsumerNumber(final String consumerCode, final String legacyConsumerNumber,
            final String tenantid) {
        List<Connection> tempConnList;
        Connection connectionObj = null;
        tempConnList = waterConnectionRepository.getWaterConnectionByConsumerNumber(consumerCode, legacyConsumerNumber, tenantid);
        if (!tempConnList.isEmpty() && tempConnList.size() == 1)
            connectionObj = tempConnList.get(0);
        return connectionObj;
    }
    
    public ConnectionOwner getConnectionOwner(final Long connId,
            final String tenantid) {
        List<ConnectionOwner> tempConnOwnerList;
        ConnectionOwner connectionOwnerObj = null;
        tempConnOwnerList = waterConnectionRepository.getConnectionOwner(connId, tenantid);
        if (!tempConnOwnerList.isEmpty() )
            connectionOwnerObj = tempConnOwnerList.get(0);
        return connectionOwnerObj;
    }

    public void updateConnectionOnChangeOfDemand(final String demandId, final Connection waterConn,
            final RequestInfo requestInfo) {
        waterConnectionRepository.updateConnectionOnChangeOfDemand(demandId, waterConn, requestInfo);
    }

    private DemandResponse createDemand(final WaterConnectionReq waterConnectionReq) {

        final List<Demand> pros = demandConnectionService
                .prepareDemand(waterConnectionReq.getConnection().getDemand(), waterConnectionReq);
        final DemandResponse demandRes = demandConnectionService.createDemand(pros, waterConnectionReq.getRequestInfo());
        if (demandRes != null && demandRes.getDemands() != null && !demandRes.getDemands().isEmpty())
            waterConnectionReq.getConnection().setDemandid(demandRes.getDemands().get(0).getId());
        System.out.println("Demand Generated for WaterConnectionDeposite with demand id="+ waterConnectionReq.getConnection().getDemandid());
        return demandRes;
    }
    
    public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq,
            final RequestInfo requestInfo, final String urlToInvoke) {
        List<PropertyInfo> propertyInfoList = new ArrayList<>();
        if (StringUtils.isNotBlank(waterConnectionGetReq.getName())
                || StringUtils.isNotBlank(waterConnectionGetReq.getMobileNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getLocality())
                || StringUtils.isNotBlank(waterConnectionGetReq.getDoorNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getRevenueWard())
                || StringUtils.isNotBlank(waterConnectionGetReq.getAadhaarNumber()))
            try {
                propertyInfoList = restConnectionService
                        .getPropertyDetailsByParams(restConnectionService.getRequestInfoWrapperWithoutAuth(), urlToInvoke);
            } catch (final Exception e) {
                log.error(
                        "Encountered an Exception while getting the property identifier from Property Module :" + e.getMessage());
            }
        if (null != propertyInfoList && propertyInfoList.size() > 0)
            waterConnectionGetReq
                    .setPropertyIdentifierList(propertyIdentifierListPreparator(waterConnectionGetReq, propertyInfoList));

        List<User> userList = new ArrayList<>();
        final List<Long> userIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(waterConnectionGetReq.getName())
                || StringUtils.isNotBlank(waterConnectionGetReq.getMobileNumber())
                || StringUtils.isNotBlank(waterConnectionGetReq.getAadhaarNumber()))
            try {
                userList = connectionUserService.searchUserServiceByParams(waterConnectionGetReq);
                if (userList.size() > 0) {
                    for (final User user : userList)
                        userIdList.add(user.getId());
                    waterConnectionGetReq.setUserIdList(userIdList);
                }
            } catch (final Exception e) {
                log.error(
                        "Encountered an Exception while getting User List from :" + e.getMessage());
            }
        final List<Connection> connectionList = waterConnectionSearchRepository.getConnectionDetails(waterConnectionGetReq,
                requestInfo,
                propertyInfoList, userList);
        if (connectionList.size() == 1)
            for (final Connection conn : connectionList) {
                final List<DocumentOwner> documentList = getDocumentForConnection(conn);
                conn.setDocuments(documentList);
            }
        return connectionList;
    }

    public List<String> propertyIdentifierListPreparator(final WaterConnectionGetReq waterConnectionGetReq,
            final List<PropertyInfo> propertyInfoList) {
        final List<String> propertyIdentifierList = new ArrayList<>();
        for (final PropertyInfo pInfo : propertyInfoList)
            propertyIdentifierList.add(pInfo.getUpicNumber());
        return propertyIdentifierList;
    }

    public boolean sendDocumentObjToProducer(final String topic, final String key, final WorkOrderFormat workOrder) {
        try {
            kafkaTemplate.send(topic, key, workOrder);
        } catch (final Exception e) {
            log.error("Producer failed to post request to kafka queue", e);
            return false;
        }
        return true;
    }

    public void updateWaterConnectionAfterCollection(final DemandResponse demandResponse) {
        Demand demand = null;
        if (demandResponse != null) {
            demand = demandResponse.getDemands().get(0);
            if (demand != null && demand.getBusinessService() != null && demand.getBusinessService().equals(WcmsConnectionConstants.BUSINESSSERVICE_COLLECTION)) {
                System.out.println("After Collection API " + demand.getBusinessService());
                List<Connection> connectionList = waterConnectionRepository.findByApplicationNmber(
                        demand.getConsumerCode(),
                        demand.getTenantId());
                Connection conn = null;
                if (!connectionList.isEmpty())
                    conn = connectionList.get(0);
                if (conn != null && conn.getStatus() != null && conn.getStatus().equals(NewConnectionStatus.APPROVED)) {
                    waterConnectionRepository.updateConnectionAfterWorkFlowQuery(demand.getConsumerCode());
                    RequestInfo requestInfo = connectionUtils
                            .prepareRequestInfoFromResponseInfo(demandResponse.getResponseInfo(),demand);
                    WaterConnectionReq waterConnectionRequest = new WaterConnectionReq();
                    waterConnectionRequest.setConnection(conn);
                    waterConnectionRequest.setRequestInfo(requestInfo);
                    try {
                        kafkaTemplate.send(applicationProperties.getUpdateconnectionAfterCollection(),
                                applicationProperties.getUpdateconnectionAfterCollection(), waterConnectionRequest);
                    } catch (final Exception e) {
                        log.error("Producer failed to post request to kafka queue", e);

                    }
                }
            }
        }
    }


    public Long generateNextConsumerNumber() {
        return waterConnectionRepository.generateNextConsumerNumber();
    }

    private List<DocumentOwner> getDocumentForConnection(final Connection connection) {
        return waterConnectionRepository.getDocumentForConnection(connection);
    }

    public void beforePersistTasks(final WaterConnectionReq waterConnectionRequest) {
        // Setting the Legacy Flag based on Consumer Number
        if (waterConnectionRequest.getConnection().getLegacyConsumerNumber() != null)
            waterConnectionRequest.getConnection().setIsLegacy(Boolean.TRUE);
        else
            waterConnectionRequest.getConnection().setIsLegacy(Boolean.FALSE);
        
		if (waterConnectionRequest.getConnection().getWithProperty()
				&& null != waterConnectionRequest.getConnection().getProperty()) {
			waterConnectionRequest.getConnection().setPropertyIdentifier(
					waterConnectionRequest.getConnection().getProperty().getPropertyIdentifier());
		}
        
        if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getPropertyIdentifier()) || 
        		StringUtils.isNotBlank(waterConnectionRequest.getConnection().getOldPropertyIdentifier())) {
            final PropertyResponse propResp = restConnectionService.getPropertyDetailsByUpicNo(waterConnectionRequest);
            if (null != propResp && null != propResp.getProperties() && propResp.getProperties().size() > 0) { 
            	for(PropertyInfo eachProp : propResp.getProperties()) { 
            		waterConnectionRequest.getConnection().setPropertyIdentifier(eachProp.getUpicNumber());  
            	}
			} else {
				if ((null != waterConnectionRequest.getConnection().getConnectionOwners()
						&& waterConnectionRequest.getConnection().getConnectionOwners().size() > 0) && 
						(null != waterConnectionRequest.getConnection().getAddress() 
						&& StringUtils.isNotBlank(waterConnectionRequest.getConnection().getAddress().getAddressLine1()))) {
					waterConnectionRequest.getConnection().setWithProperty(Boolean.FALSE); 
				}
			}
                
        }

        // Setting the Number Of Family based on the Number of Persons
        waterConnectionRequest.getConnection()
                .setNumberOfFamily(waterConnectionRequest.getConnection().getNumberOfPersons() != 0
                        ? Math.round(waterConnectionRequest.getConnection().getNumberOfPersons() / Long.valueOf(
                                WcmsConnectionConstants.NUMBEROFPERSONSDEVIDELOGIC) + Long.valueOf(
                                        WcmsConnectionConstants.NUMBEROFPERSONSADDLOGIC))
                        : null);

        // Setting Water Connection Created Date
        waterConnectionRequest.getConnection().setCreatedDate(Long.toString(new java.util.Date().getTime()));
    }

    public void generateIdsForWaterConnectionRequest(final WaterConnectionReq waterConnectionRequest) {
        if (waterConnectionRequest.getConnection().getIsLegacy()) {
            waterConnectionRequest.getConnection()
                    .setConsumerNumber(connectionValidator.generateConsumerNumber(waterConnectionRequest));
            waterConnectionRequest.getConnection()
                    .setAcknowledgementNumber(waterConnectionRequest.getConnection().getConsumerNumber());
            log.info("Consumer Number Generated is : " + waterConnectionRequest.getConnection().getConsumerNumber());

        } else {
            waterConnectionRequest.getConnection().setAcknowledgementNumber(
                    connectionValidator.generateAcknowledgementNumber(waterConnectionRequest));
            log.info(
                    "Acknowledgement Number Generated is : " + waterConnectionRequest.getConnection().getAcknowledgementNumber());
        }
    }

    public Connection afterPersistTasks(final WaterConnectionReq waterConnectionRequest, final Connection connection) {
        if (waterConnectionRequest.getConnection().getIsLegacy()) {
            connection.setConsumerNumber(waterConnectionRequest.getConnection().getConsumerNumber() != null
                    ? waterConnectionRequest.getConnection().getAcknowledgementNumber() : null);
            connection.setIsLegacy(Boolean.TRUE);
        }
        connection.setStatus(waterConnectionRequest.getConnection().getStatus());
        return connection;
    }
    
	public void calculateNonMeterWaterRates(WaterConnectionReq waterConnectionReq) {
		Connection conn = waterConnectionReq.getConnection();
		Double nonMeterRateAmount = null;

		// Fetch from cache masters
		nonMeterRateAmount = ConnectionMasterAdapter.getNonMeterWaterRatesByParams(conn.getSourceType(),
				conn.getConnectionType(), conn.getUsageType(), conn.getSubUsageType(), conn.getHscPipeSizeType(),
				conn.getNumberOfTaps(), conn.getTenantId(), waterConnectionReq.getRequestInfo());

		// If no records in cache, fetch from Masters using Rest API call
		if (null == nonMeterRateAmount) {
			List<NonMeterWaterRates> meterRatesList = restConnectionService.getNonMeterWaterRates(waterConnectionReq);
			for (NonMeterWaterRates rate : meterRatesList) {
				if (null != rate.getAmount()) {
					nonMeterRateAmount = rate.getAmount();
				}
			}

		}
		List<EnumData> datePeriodCycle = waterConnectionRepository.getExecutionDatePeriodCycle(
				waterConnectionReq.getConnection().getAcknowledgementNumber(),
				waterConnectionReq.getConnection().getTenantId());
		String periodCycle = "";
		Long execDate = 0L;
		for (EnumData data : datePeriodCycle) {
			periodCycle = data.getKey();
			execDate = ((Number) data.getObject()).longValue();
		}
		if (null != execDate && execDate > 0) {
			log.info("Calculating Non Meter Water Rates based on Period Cycle : "+periodCycle);
			Double value = nonMeterRateAmount
					* calculateBasedOnPeriodCycle(getFinMonthFromMonth(execDate), periodCycle, waterConnectionReq);
			log.info("Total Amount based on Period Cycle : " + value);
		}
	}
	
	private int getFinMonthFromMonth(long execDate) { 
		Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date(execDate));
    	log.info("Calendar Date : " + new Date(execDate));
    	int thisMonth = calendar.get(Calendar.MONTH) + 1;
    	log.info("Calendar Month : " + thisMonth);
    	int finMonth = WcmsConnectionConstants.monthFinMonthMap.get(thisMonth);
    	log.info("Financial Month : " + finMonth);
    	return finMonth; 
	}
    
    private long calculateBasedOnPeriodCycle(long finMonth, String periodCycle, WaterConnectionReq waterConnectionReq ) {
    	
    	long toBeMultipliedWith = 0; 
    	if(periodCycle.equals(PeriodCycle.ANNUAL.toString())) { 
    		if((finMonth / WcmsConnectionConstants.TOTALMONTHS) != 1) { 
        		toBeMultipliedWith = (WcmsConnectionConstants.TOTALMONTHS - finMonth) + WcmsConnectionConstants.ADDMONTH; 
        	} else {  
        		toBeMultipliedWith = finMonth / WcmsConnectionConstants.TOTALMONTHS;  
        	}
    		log.info("Annual To Be Multiplied With : " + toBeMultipliedWith);
    	} else if(periodCycle.equals(PeriodCycle.HALFYEAR.toString())) { 
    		toBeMultipliedWith = 0;
        	if(finMonth <= 6 && (finMonth / WcmsConnectionConstants.HALFYEARMONTHS) != 1) {
        		toBeMultipliedWith = (WcmsConnectionConstants.HALFYEARMONTHS - finMonth) + WcmsConnectionConstants.ADDMONTH;
        	} else if(finMonth == WcmsConnectionConstants.HALFYEARMONTHS) { 
        		toBeMultipliedWith = 1;
        	} else if(finMonth > WcmsConnectionConstants.HALFYEARMONTHS) { 
        		toBeMultipliedWith = (WcmsConnectionConstants.TOTALMONTHS - finMonth) + WcmsConnectionConstants.ADDMONTH; 
        	}
        	log.info("Half Year To Be Multiplied With : " + toBeMultipliedWith);
    	} else if(periodCycle.equals(PeriodCycle.QUARTER.toString())) {
    		toBeMultipliedWith = 0;
        	for(int i=0 ; i<WcmsConnectionConstants.quarters.length-1 ; i++) { 
        		if(finMonth > WcmsConnectionConstants.quarters[i] && finMonth <= WcmsConnectionConstants.quarters[i+1]) { 
        			toBeMultipliedWith = (WcmsConnectionConstants.quarters[i+1] - finMonth) + WcmsConnectionConstants.ADDMONTH; 
        		}
        	}
        	log.info("Quarterly To Be Multiplied With : " + toBeMultipliedWith);	
    	} else { 
    		toBeMultipliedWith = 1; 
    	}
    	return toBeMultipliedWith ; 
    }
    
}
