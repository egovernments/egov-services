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
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.model.enums.NewConnectionStatus;
import org.egov.wcms.transaction.repository.WaterConnectionRepository;
import org.egov.wcms.transaction.util.WcmsConnectionConstants;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.ProcessInstance;
import org.egov.wcms.transaction.web.contract.PropertyOwnerInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.Task;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.workflow.service.TransanctionWorkFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaterConnectionService {

    public static final Logger logger = LoggerFactory.getLogger(WaterConnectionService.class);
    @Autowired
    private TransanctionWorkFlowService transanctionWorkFlowService;

    @Autowired
    private DemandConnectionService demandConnectionService;

    @Autowired
    private WaterConnectionRepository waterConnectionRepository;
    
    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RestConnectionService restConnectionService;

    @Autowired
    private ConfigurationManager configurationManager;

    public Connection createWaterConnection(final String topic, final String key,
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
			logger.error("Producer failed to post request to kafka queue", e);
			return waterConnectionRequest.getConnection();
		}
		return waterConnectionRequest.getConnection();
	}
    
    public Connection persistBeforeKafkaPush(final WaterConnectionReq waterConnectionRequest) {
        logger.info("Service API entry for create Connection");
        try {
            waterConnectionRepository.persistConnection(waterConnectionRequest);
        } catch (final Exception e) {
            logger.error("Persisting failed due to db exception", e);
        }
        return waterConnectionRequest.getConnection();
    }

    public Connection create(final WaterConnectionReq waterConnectionRequest) {
        logger.info("Service API entry for update with initiate workflow Connection");
        try {
            if (waterConnectionRequest.getConnection().getIsLegacy() != null &&
                    waterConnectionRequest.getConnection().getIsLegacy().equals(Boolean.FALSE)) {
                initiateWorkFow(waterConnectionRequest);
                waterConnectionRepository.updateConnectionWorkflow(waterConnectionRequest,null);
            }
        } catch (final Exception e) {
            logger.error("workflow intiate and updating connection failed due to db exception", e);
        }
        return waterConnectionRequest.getConnection();
    }

	public Connection update(final WaterConnectionReq waterConnectionRequest) {
		logger.info("Service API entry for update Connection");
		if (waterConnectionRequest.getConnection().getIsLegacy()) {
			try {
				updateWaterConnection(waterConnectionRequest);
			} catch (final Exception e) {
				logger.error("update Connection failed due to db exception", e);
			}
		} else {
			try {
				Connection connection = waterConnectionRequest.getConnection();
				if (connection.getStatus() != null
						&& connection.getStatus().equalsIgnoreCase(NewConnectionStatus.CREATED.name())
						&& (waterConnectionRequest.getConnection().getEstimationCharge() != null
								&& !waterConnectionRequest.getConnection().getEstimationCharge().isEmpty())) {
					createDemand(waterConnectionRequest);
				}
				if (connection.getStatus() != null
						&& connection.getStatus().equalsIgnoreCase(NewConnectionStatus.CREATED.name()))
					connection.setStatus(NewConnectionStatus.VERIFIED.name());
				if (connection.getWorkflowDetails() != null && connection.getWorkflowDetails().getAction() != null)
					if (connection.getWorkflowDetails().getAction().equals("Approve") && connection.getStatus() != null
							&& connection.getStatus().equalsIgnoreCase(NewConnectionStatus.VERIFIED.name()))
						connection.setStatus(NewConnectionStatus.APPROVED.name());
				if (connection.getStatus() != null
						&& connection.getStatus().equalsIgnoreCase(NewConnectionStatus.APPROVED.name()))
					connection.setStatus(NewConnectionStatus.SANCTIONED.name());

				waterConnectionRequest.setConnection(connection);

				updateWorkFlow(waterConnectionRequest);
				updateWaterConnection(waterConnectionRequest);
			} catch (final Exception e) {
				logger.error("update Connection failed due to db exception", e);
			}
		}
		return waterConnectionRequest.getConnection();
	}
    
    private void updateWaterConnection(WaterConnectionReq waterConnectionRequest) { 
    	waterConnectionRepository.updateWaterConnection(waterConnectionRequest);
    }

    public Connection findByApplicationNmber(final String applicationNmber) {
        return waterConnectionRepository.findByApplicationNmber(applicationNmber);
    }
    public Connection getWaterConnectionByConsumerNumber(final String consumerCode) {
        return waterConnectionRepository.getWaterConnectionByConsumerNumber(consumerCode);
    }
    
    public void updateConnectionOnChangeOfDemand(final String demandId ,Connection waterConn, RequestInfo requestInfo)
    {
        waterConnectionRepository.updateConnectionOnChangeOfDemand(demandId,waterConn,  requestInfo);
    }

    private ProcessInstance initiateWorkFow(final WaterConnectionReq waterConnectionReq) {

        final ProcessInstance pros = transanctionWorkFlowService.startWorkFlow(waterConnectionReq);
        if (pros != null)
            waterConnectionReq.getConnection().setStateId(Long.valueOf(pros.getId()));
        return pros;
    }

    private DemandResponse createDemand(final WaterConnectionReq waterConnectionReq) {

        List<Demand> pros = demandConnectionService
                .prepareDemand(waterConnectionReq.getConnection().getDemand(), waterConnectionReq);
        DemandResponse demandRes = demandConnectionService.createDemand(pros, waterConnectionReq.getRequestInfo());
        if (demandRes != null && demandRes.getDemands() != null && !demandRes.getDemands().isEmpty())
            waterConnectionReq.getConnection().setDemandid(demandRes.getDemands().get(0).getId());
        return demandRes;
    }

    private Task updateWorkFlow(final WaterConnectionReq waterConnectionReq) {

        final Task task = transanctionWorkFlowService.updateWorkFlow(waterConnectionReq);
        if (task != null)
            waterConnectionReq.getConnection().setStateId(Long.valueOf(task.getId()));
        return task;
    }

	public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq) {
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(RequestInfo.builder().ts(111111111L).build()).build();
		List<Long> propertyIdentifierList = new ArrayList<>();
		String urlToInvoke = buildUrlToInvoke(waterConnectionGetReq);
		try {
			propertyIdentifierList = restConnectionService.getPropertyDetailsByParams(wrapper, urlToInvoke); 
		} catch (Exception e) {
			logger.error("Encountered an Exception while getting the property identifier from Property Module :" + e.getMessage());
		}
		waterConnectionGetReq.setPropertyIdentifierList(propertyIdentifierList);
		List<Connection> connectionList = waterConnectionRepository.getConnectionDetails(waterConnectionGetReq);
		if(connectionList.size() == 1) { 
			for(Connection conn : connectionList){ 
				List<DocumentOwner> documentList = getDocumentForConnection(conn);
				conn.setDocuments(documentList);
			}
		}
		return connectionList; 
	}

    public void propertyIdentifierListPreparator(WaterConnectionGetReq waterConnectionGetReq, List<Long> propertyIdentifierList) {
        if (null != waterConnectionGetReq.getPropertyIdentifier() && !waterConnectionGetReq.getPropertyIdentifier().isEmpty()) {
            Long propId = Long.parseLong(waterConnectionGetReq.getPropertyIdentifier());
            propertyIdentifierList.add(propId);
        }
    }
    
    @SuppressWarnings("static-access")
	public EstimationNotice getEstimationNotice(String topic, String key, WaterConnectionGetReq waterConnectionGetReq) {
		List<Connection> connectionList = waterConnectionRepository.getConnectionDetails(waterConnectionGetReq);
		EstimationNotice estimationNotice = null;
		Connection connection = null;
		for (int i = 0; i < connectionList.size(); i++) {
			connection = connectionList.get(i);
			List<String> chargeDescriptions = new ArrayList<>();
			chargeDescriptions.add(WcmsConnectionConstants.getChargeReasonToDisplay()
					.get(WcmsConnectionConstants.ESIMATIONCHARGEDEMANDREASON)
					.concat(" : " + Double.toString(connection.getDonationCharge())));
			estimationNotice = new EstimationNotice().builder()
					.applicantName(connection.getProperty().getNameOfApplicant())
					.applicationDate(connection.getCreatedDate())
					.applicationNumber(connection.getAcknowledgementNumber()).dateOfLetter(new Date().toString())
					.chargeDescription(chargeDescriptions).letterIntimationSubject("LetterIntimationSubject")
					.letterNumber("LetterNumber").letterTo(connection.getProperty().getNameOfApplicant())
					.serviceName("Water Department").slaDays(30L).ulbName(connection.getTenantId()).build();
		}
		Demand demand = restConnectionService.getDemandEstimation(connection);
		if (null != demand) {
			logger.info("Demand Details as received from Billing Service : " + demand.toString());
			if (null != demand.getDemandDetails()) {
				estimationNotice.getChargeDescription()
						.add(WcmsConnectionConstants.getChargeReasonToDisplay()
								.get(WcmsConnectionConstants.DONATIONCHARGEANDREASON).concat(
										" : " + demand.getDemandDetails().get(0).getCollectionAmount().toString()));
			}
		}
		PropertyResponse propertyResponse = restConnectionService
				.getPropertyDetailsByUpicNo(getWaterConnectionRequest(connection));
		if (null != propertyResponse) {
			logger.info("Property Response as received from Property Service : " + propertyResponse.toString());
			if (null != propertyResponse.getProperties() && propertyResponse.getProperties().size() > 0) {
				List<PropertyOwnerInfo> ownersList = propertyResponse.getProperties().get(0).getOwners();
				if (null != ownersList && ownersList.size() > 0) {
					estimationNotice.setApplicantName(ownersList.get(0).getName());
					estimationNotice.setLetterTo(ownersList.get(0).getName());
				}
			}
		}
		boolean insertStatus = waterConnectionRepository.persistEstimationNoticeLog(estimationNotice,
				connection.getId(), connection.getTenantId());
		if (insertStatus) {
			return estimationNotice;
		}
		return new EstimationNotice();
	}
    
    @SuppressWarnings("static-access")
	public WorkOrderFormat getWorkOrder(String topic, String key, WaterConnectionGetReq waterConnectionGetReq) {
    	// Fetch Connection Details using the Ack Number 
		List<Connection> connectionList = waterConnectionRepository.getConnectionDetails(waterConnectionGetReq);
		logger.info("Fetched the List of Connection Objects for the Ack Number : " + connectionList.size());
		WorkOrderFormat workOrder = null;
		Connection connection = null;
		for (int i = 0; i < connectionList.size(); i++) {
			connection = connectionList.get(i);
			workOrder = WorkOrderFormat.builder().ackNumber(connection.getAcknowledgementNumber())
					.ackNumberDate("AckNumberDate").connectionId(connection.getId())
					.workOrderNumber(connection.getWorkOrderNumber()).workOrderDate(new Date(new java.util.Date().getTime()).toString())
					.hscNumber(connection.getConsumerNumber()).hscNumberDate("HSCNumberDate").tenantId(connection.getTenantId()).build();
		}
		// Get Property Details to fetch the name of the owner 
		PropertyResponse propertyResponse = restConnectionService
				.getPropertyDetailsByUpicNo(getWaterConnectionRequest(connection));
		if (null != propertyResponse) {
			logger.info("Property Response as received from Property Service : " + propertyResponse.toString());
			if (null != propertyResponse.getProperties() && propertyResponse.getProperties().size() > 0) {
				List<PropertyOwnerInfo> ownersList = propertyResponse.getProperties().get(0).getOwners();
				if (null != ownersList && ownersList.size() > 0) {
					workOrder.setWaterTapOwnerName(ownersList.get(0).getName());
				}
			}
		}
		
		// Sending the message to Kafka Producer
		if (sendDocumentObjToProducer(topic, key, workOrder)) {
			return workOrder;
		}
		return new WorkOrderFormat();
	}
    
    private boolean sendDocumentObjToProducer(final String topic, final String key, final WorkOrderFormat workOrder) {
		try {
			kafkaTemplate.send(topic, key, workOrder);
		} catch (final Exception e) {
			logger.error("Producer failed to post request to kafka queue", e);
			return false;
		}
		return true;
	}
    
    public boolean persistWorkOrderLog(WorkOrderFormat workOrder) { 
    	return waterConnectionRepository.persistWorkOrderLog(workOrder);
    }
    
    private WaterConnectionReq getWaterConnectionRequest(Connection connection) { 
    	RequestInfo rInfo = new RequestInfo();
    	return new WaterConnectionReq(rInfo, connection);
    }
    
    private List<DocumentOwner> getDocumentForConnection(Connection connection) { 
    	return waterConnectionRepository.getDocumentForConnection(connection);
    }
    private String buildUrlToInvoke(WaterConnectionGetReq waterConnectionGetReq){ 
    	 StringBuilder url = new StringBuilder();
         url.append(configurationManager.getPropertyServiceHostNameTopic())
                 .append(configurationManager.getPropertyServiceSearchPathTopic())
                 .append("?tenantId=").append(waterConnectionGetReq.getTenantId());
         if(null != waterConnectionGetReq.getName() && !waterConnectionGetReq.getName().isEmpty()) {
        	 url.append("&ownerName=" + waterConnectionGetReq.getName());
         }
         if(null != waterConnectionGetReq.getMobileNumber() && !waterConnectionGetReq.getMobileNumber().isEmpty()) { 
        	 url.append("&mobileNumber=" + waterConnectionGetReq.getMobileNumber()); 
         }
         if(null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty()) { 
        	 url.append("&locality=" + waterConnectionGetReq.getLocality()); 
         }
         if(null != waterConnectionGetReq.getDoorNumber() && !waterConnectionGetReq.getDoorNumber().isEmpty()) { 
        	 url.append("&houseNoBldgApt=" + waterConnectionGetReq.getDoorNumber()); 
         }
         if(null != waterConnectionGetReq.getRevenueWard() && !waterConnectionGetReq.getRevenueWard().isEmpty()) { 
        	 url.append("&revenueWard=" + waterConnectionGetReq.getDoorNumber()); 
         }
         return url.toString();
    	
    }
    
 }
