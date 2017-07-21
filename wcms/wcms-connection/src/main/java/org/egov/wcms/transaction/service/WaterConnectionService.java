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
import java.util.List;

import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.enums.NewConnectionStatus;
import org.egov.wcms.transaction.producers.WaterTransactionProducer;
import org.egov.wcms.transaction.repository.WaterConnectionRepository;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.ProcessInstance;
import org.egov.wcms.transaction.web.contract.Task;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.workflow.service.TransanctionWorkFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WaterConnectionService {

    public static final Logger logger = LoggerFactory.getLogger(WaterConnectionService.class);
    @Autowired
    private TransanctionWorkFlowService transanctionWorkFlowService;

    @Autowired
    private DemandConnectionService demandConnectionService;

    @Autowired
    private WaterTransactionProducer waterTransactionProducer;

    @Autowired
    private WaterConnectionRepository waterConnectionRepository;
    
    @Autowired
    private RestConnectionService restConnectionService; 

    public Connection createWaterConnection(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String waterConnectionValue = null;
        try {
            logger.info("WaterConnectionService request::" + waterConnectionRequest);
            waterConnectionValue = mapper.writeValueAsString(waterConnectionRequest);
            logger.info("waterConnectionValue::" + waterConnectionValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception while stringifying water coonection object", e);
        }
        return sendRequestObjToProducer(topic, key, waterConnectionRequest, waterConnectionValue);

    }

    public Connection updateWaterConnection(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String waterConnectionValue = null;
        try {
            logger.info("WaterConnectionService request::" + waterConnectionRequest);
            waterConnectionValue = mapper.writeValueAsString(waterConnectionRequest);
            logger.info("waterConnectionValue::" + waterConnectionValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception while stringifying water coonection object", e);
        }
        return sendRequestObjToProducer(topic, key, waterConnectionRequest, waterConnectionValue);

    }

    protected Connection sendRequestObjToProducer(final String topic, final String key,
            final WaterConnectionReq waterConnectionRequest,
            final String waterConnectionValue) {
        try {

            waterTransactionProducer.sendMessage(topic, key, waterConnectionValue);
        } catch (final Exception e) {
            logger.error("Producer failed to post request to kafka queue", e);
            return waterConnectionRequest.getConnection();
        }
        return waterConnectionRequest.getConnection();
    }

    public Connection create(final WaterConnectionReq waterConnectionRequest) {
        logger.info("Service API entry for create/update Connection");
        try {
            if(waterConnectionRequest.getConnection().getIsLegacy()!=null && waterConnectionRequest.getConnection().getIsLegacy().equals(Boolean.FALSE))
            initiateWorkFow(waterConnectionRequest);
            waterConnectionRepository.persistConnection(waterConnectionRequest);
        } catch (final Exception e) {
            logger.error("Persisting failed due to db exception", e);
        }
        return waterConnectionRequest.getConnection();
    }

    public Connection update(final WaterConnectionReq waterConnectionRequest) {
        logger.info("Service API entry for create/update Connection");
        try {
            Connection connection=waterConnectionRequest.getConnection();
            if(connection.getStatus()!=null && connection.getStatus().equalsIgnoreCase(NewConnectionStatus.CREATED.name()) && 
                    (waterConnectionRequest.getConnection().getEstimationCharge()!=null && 
                    !waterConnectionRequest.getConnection().getEstimationCharge().isEmpty())){
            createDemand(waterConnectionRequest);
            }
            if(connection.getStatus()!=null && connection.getStatus().equalsIgnoreCase(NewConnectionStatus.CREATED.name()))
                connection.setStatus(NewConnectionStatus.VERIFIED.name());
            if(connection.getWorkflowDetails()!=null && connection.getWorkflowDetails().getAction()!=null)
                if(connection.getWorkflowDetails().getAction().equals("Approve") && connection.getStatus()!=null && 
                    connection.getStatus().equalsIgnoreCase(NewConnectionStatus.VERIFIED.name()))
                connection.setStatus(NewConnectionStatus.APPROVED.name());
            if(connection.getStatus()!=null && connection.getStatus().equalsIgnoreCase(NewConnectionStatus.APPROVED.name()))
                connection.setStatus(NewConnectionStatus.SANCTIONED.name());
            
            waterConnectionRequest.setConnection(connection);
            
            createDemand(waterConnectionRequest);
            updateWorkFlow(waterConnectionRequest);
            waterConnectionRepository.updateWaterConnection(waterConnectionRequest);
        } catch (final Exception e) {
            logger.error("update Connection failed due to db exception", e);
        }
        return waterConnectionRequest.getConnection();
    }

    public Connection findByApplicationNmber(final String applicationNmber) {
        return waterConnectionRepository.findByApplicationNmber(applicationNmber);
    }

    private ProcessInstance initiateWorkFow(final WaterConnectionReq waterConnectionReq) {

        final ProcessInstance pros = transanctionWorkFlowService.startWorkFlow(waterConnectionReq);
        if(pros!=null)
        waterConnectionReq.getConnection().setStateId(Long.valueOf(pros.getId()));
        return pros;
    }

    private DemandResponse createDemand(final WaterConnectionReq waterConnectionReq) {

        List<Demand> pros = demandConnectionService
                .prepareDemand(waterConnectionReq.getConnection().getDemand(), waterConnectionReq);
        DemandResponse demandRes = demandConnectionService.createDemand(pros, waterConnectionReq.getRequestInfo());
        if(demandRes!=null && demandRes.getDemands()!=null && !demandRes.getDemands().isEmpty())
        waterConnectionReq.getConnection().setDemandid(demandRes.getDemands().get(0).getId());
        return demandRes;
    }

    private Task updateWorkFlow(final WaterConnectionReq waterConnectionReq) {

        final Task task = transanctionWorkFlowService.updateWorkFlow(waterConnectionReq);
        if(task!=null )
        waterConnectionReq.getConnection().setStateId(Long.valueOf(task.getId()));
        return task;
    }
    
    public List<Connection> getConnectionDetails(final WaterConnectionGetReq waterConnectionGetReq) {
    	List<Long> propertyIdentifierList = new ArrayList<>();
    	if(null != waterConnectionGetReq.getName() && !waterConnectionGetReq.getName().isEmpty()){
    		restConnectionService.getPropertyDetailsByName(waterConnectionGetReq);
    		propertyIdentifierListPreparator(waterConnectionGetReq, propertyIdentifierList);
    	}
    	if(null != waterConnectionGetReq.getMobileNumber() && !waterConnectionGetReq.getMobileNumber().isEmpty()) { 
    		restConnectionService.getPropertyDetailsByMobileNumber(waterConnectionGetReq);
    		propertyIdentifierListPreparator(waterConnectionGetReq, propertyIdentifierList);
    	}
    	if(null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty()) { 
    		restConnectionService.getPropertyDetailsByLocality(waterConnectionGetReq);
    		propertyIdentifierListPreparator(waterConnectionGetReq, propertyIdentifierList);
    	}
    	if(null != waterConnectionGetReq.getRevenueWard() && !waterConnectionGetReq.getRevenueWard().isEmpty()) { 
    		restConnectionService.getPropertyDetailsByRevenueWard(waterConnectionGetReq); 
    		propertyIdentifierListPreparator(waterConnectionGetReq, propertyIdentifierList);
    	}
    	if(null != waterConnectionGetReq.getDoorNumber() && !waterConnectionGetReq.getDoorNumber().isEmpty()) {
    		restConnectionService.getPropertyDetailsByDoorNumber(waterConnectionGetReq); 
    		propertyIdentifierListPreparator(waterConnectionGetReq, propertyIdentifierList);
    	}
    	waterConnectionGetReq.setPropertyIdentifierList(propertyIdentifierList);
    	return waterConnectionRepository.getConnectionDetails(waterConnectionGetReq);
    }
    
    public void propertyIdentifierListPreparator(WaterConnectionGetReq waterConnectionGetReq, List<Long> propertyIdentifierList) { 
    	if(null != waterConnectionGetReq.getPropertyIdentifier() && !waterConnectionGetReq.getPropertyIdentifier().isEmpty()) { 
			Long propId = Long.parseLong(waterConnectionGetReq.getPropertyIdentifier()); 
			propertyIdentifierList.add(propId);
		}
    }
}
