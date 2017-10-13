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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.ConnectionOwner;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.User;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.repository.WaterConnectionRepository;
import org.egov.wcms.transaction.repository.WaterConnectionSearchRepository;
import org.egov.wcms.transaction.utils.WcmsConnectionConstants;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.PropertyInfo;
import org.egov.wcms.transaction.web.contract.PropertyOwnerInfo;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConnectionNoticeService extends WaterConnectionService {

    @Autowired
    private WaterConnectionRepository waterConnectionRepository;

    @Autowired
    private WaterConnectionSearchRepository waterConnectionSearchRepository;

    @Autowired
    private RestConnectionService restConnectionService;

    public EstimationNotice getEstimationNotice(final String topic, final String key,
            final WaterConnectionGetReq waterConnectionGetReq,
            final RequestInfo requestInfo) {
    	log.info("Enter to get estimation notice ::");
        final List<PropertyInfo> propertyInfoList = new ArrayList<>();
        final List<User> userList = new ArrayList<>();
        final List<Connection> connectionList = waterConnectionSearchRepository.getConnectionDetails(waterConnectionGetReq, requestInfo,
                propertyInfoList, userList);
    
        EstimationNotice estimationNotice = null;
        Connection connection = null;
        for (int i = 0; i < connectionList.size(); i++) {
            connection = connectionList.get(i);
            String applicantName = null;
            if(connection.getWithProperty()){
          for(PropertyOwnerInfo propertyOwner : connection.getProperty().getPropertyOwner()){
        	  if(propertyOwner.getIsPrimaryOwner()){
        		  applicantName = propertyOwner.getName();
        		  break;
        	  }
        		}
            }
            else{
          for(ConnectionOwner owner : connection.getConnectionOwners())
          {
        	  if(owner.getPrimaryOwner()){
        		  applicantName = owner.getName();
        		  break;
        	  }
          }
            }
            final List<String> chargeDescriptions = new ArrayList<>();
            chargeDescriptions.add(WcmsConnectionConstants.getChargeReasonToDisplay()
                    .get(WcmsConnectionConstants.ESIMATIONCHARGEDEMANDREASON)
                    .concat(" : " + Double.toString(connection.getDonationCharge())));
            new EstimationNotice();
            estimationNotice = EstimationNotice.builder()
                    .applicantName(applicantName)
                    .applicationDate(connection.getCreatedDate())
                    .applicationNumber(connection.getAcknowledgementNumber()).dateOfLetter(new Date().toString())
                    .chargeDescription(chargeDescriptions).letterIntimationSubject("LetterIntimationSubject")
                    .letterNumber("LetterNumber").letterTo(applicantName)
                    .serviceName("Water Department").slaDays(30L).ulbName(connection.getTenantId()).build();
        
        final Demand demand = restConnectionService.getDemandEstimation(connection);
        if (null != demand) {
            log.info("Demand Details as received from Billing Service : " + demand.toString());
            if (null != demand.getDemandDetails())
                estimationNotice.getChargeDescription()
                        .add(WcmsConnectionConstants.getChargeReasonToDisplay()
                                .get(WcmsConnectionConstants.DONATIONCHARGEANDREASON).concat(
                                        " : " + demand.getDemandDetails().get(0).getCollectionAmount().toString()));
        }
        final boolean insertStatus = waterConnectionRepository.persistEstimationNoticeLog(estimationNotice,
                connection.getId(), connection.getTenantId(),
                getObjectForInsertEstimationNotice(estimationNotice, connection.getId(), connection.getTenantId()));
        
           if (insertStatus)
            return estimationNotice;
        }
        return new EstimationNotice();
    }

    public WorkOrderFormat getWorkOrder(final String topic, final String key, final WaterConnectionGetReq waterConnectionGetReq,
            final RequestInfo requestInfo) {
        // Fetch Connection Details using the Ack Number
        final List<PropertyInfo> propertyInfoList = new ArrayList<>();
        final List<User> userList = new ArrayList<>();
        final List<Connection> connectionList = waterConnectionSearchRepository.getConnectionDetails(waterConnectionGetReq, requestInfo,
                propertyInfoList, userList);
        log.info("Fetched the List of Connection Objects for the Ack Number : " + connectionList.size());
        WorkOrderFormat workOrder = null;
        Connection connection = null;
        for (int i = 0; i < connectionList.size(); i++) {
            connection = connectionList.get(i);
            String applicantName = null;
            if(connection.getWithProperty()){
            for(PropertyOwnerInfo propertyOwner : connection.getProperty().getPropertyOwner()){
          	  if(propertyOwner.getIsPrimaryOwner()){
          		  applicantName = propertyOwner.getName();
          		  break;
          	  }
          		}
            }
            else{
            for(ConnectionOwner owner : connection.getConnectionOwners())
            {
          	  if(owner.getPrimaryOwner()){
          		  applicantName = owner.getName();
          		  break;
          	  }
            }
            }
            workOrder = WorkOrderFormat.builder().ackNumber(connection.getAcknowledgementNumber())
                    .ackNumberDate("AckNumberDate").connectionId(connection.getId())
                    .workOrderNumber(connection.getWorkOrderNumber())
                    .workOrderDate(new java.util.Date().getTime())
                    .hscNumber(connection.getConsumerNumber()).waterTapOwnerName(applicantName)
                    .hscNumberDate("HSCNumberDate").tenantId(connection.getTenantId())
                    .build();
        }
        // Sending the message to Kafka Producer
        if (sendDocumentObjToProducer(topic, key, workOrder))
            return workOrder;
        return new WorkOrderFormat();
    }

    private WaterConnectionReq getWaterConnectionRequest(final Connection connection) {
        final RequestInfo rInfo = new RequestInfo();
        return new WaterConnectionReq(rInfo, connection);
    }

   

    public boolean persistWorkOrderLog(final WorkOrderFormat workOrder) {
        return waterConnectionRepository.persistWorkOrderLog(workOrder, getObjectForInsertWorkOrder(workOrder));
    }

    @SuppressWarnings("deprecation")
    public Map<String, Object> getObjectForInsertEstimationNotice(final EstimationNotice estimationNotice,
            final long connectionId,
            final String tenantId) {
        final Long createdBy = 1L;
        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
    
    
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("waterConnectionId", connectionId);
        parameters.put("tenantId", tenantId);
        Long dateOfLetter=null;
        Long applicationDate=null;
        try {
            Date d = f.parse(estimationNotice.getDateOfLetter());
            dateOfLetter = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date d = f.parse(estimationNotice.getApplicationDate());
             applicationDate = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parameters.put("dateOfLetter", dateOfLetter);
        parameters.put("letterNumber", estimationNotice.getLetterNumber());
        parameters.put("letterTo", estimationNotice.getLetterTo());
        parameters.put("letterIntimationSubject", estimationNotice.getLetterIntimationSubject());
        parameters.put("applicationNumber", estimationNotice.getApplicationNumber());
        parameters.put("applicationDate", applicationDate);
        parameters.put("applicantName", estimationNotice.getApplicantName());
        parameters.put("serviceName", "New water tap connection");
        parameters.put("waterNumber", estimationNotice.getApplicationNumber());
        parameters.put("slaDays", estimationNotice.getSlaDays());
        parameters.put("chargeDescription1", estimationNotice.getChargeDescription().get(0));
        parameters.put("chargeDescription2", estimationNotice.getChargeDescription().get(1));
        parameters.put("createdBy", createdBy);
        parameters.put("createdDate", new java.util.Date().getTime());
        return parameters;
    }

    public Map<String, Object> getObjectForInsertWorkOrder(final WorkOrderFormat workOrder) {
        final Long createdBy = 1L;
        final Map<String, Object> parameters = new HashMap<String, Object>();
    
        parameters.put("waterConnectionId", workOrder.getConnectionId());
        parameters.put("tenantId", workOrder.getTenantId());
        parameters.put("workOrderNumber", workOrder.getWorkOrderNumber());
        parameters.put("workOrderDate",workOrder.getWorkOrderDate());
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

}
