/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.collection.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.repository.CollectionConfigurationRepository;
import org.egov.collection.repository.CommonWorkFlowRepository;
import org.egov.collection.repository.EmployeeRepository;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
public class WorkflowService {

	public static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);

	
	@Autowired
	private CommonWorkFlowRepository commonWorkFlowRepository;
	
	@Autowired
	private ApplicationProperties applicationProperties;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CollectionConfigurationRepository collectionConfigurationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
	
	public void startWorkflow(final ReceiptRequest receiptReq) {
        logger.info("Persisting workflow details");
        List<Receipt> receipts = receiptReq.getReceipt();
        Receipt receipt = receipts.get(0);
        List<Bill> bills = receipt.getBill();
        Bill bill = bills.get(0);
        List<Employee> employees = employeeRepository.getPositionsForEmployee(receiptReq.getRequestInfo(),receiptReq.getRequestInfo().getUserInfo().getId(),receipt.getTenantId());
        List<Assignment> assignments = !employees.isEmpty() ? employees.get(0).getAssignments() : Collections.EMPTY_LIST ;

        for(BillDetail billDetail:bill.getBillDetails()) {
            Map<String, List<String>> workFlowConfigurationValues = collectionConfigurationRepository.searchWorkFlowConfigurationValues(receiptReq.getRequestInfo(), receipt.getTenantId(), CollectionServiceConstants.MANUAL_OT_AUTO_WORKFLOW_CONFIG_VALUE);
            if (!workFlowConfigurationValues.isEmpty() && workFlowConfigurationValues.get(CollectionServiceConstants.MANUAL_OT_AUTO_WORKFLOW_CONFIG_VALUE).get(0).equalsIgnoreCase(CollectionServiceConstants.MANUAL_WORKFLOW_CONFIG_VALUE)) {
                WorkflowDetailsRequest workflowDetails = receipt.getWorkflowDetails();
                workflowDetails.setInitiatorPosition(!assignments.isEmpty() && assignments != null ? assignments.get(0).getId() : null);
                workflowDetails.setRequestInfo(receiptReq.getRequestInfo());
                workflowDetails.setReceiptNumber(billDetail.getReceiptNumber());
                ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(workflowDetails,
                        applicationProperties.getBusinessType(), applicationProperties.getType(), applicationProperties.getComments());
                processInstanceRequest.setRequestInfo(receiptReq.getRequestInfo());
                ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
                try {
                    processInstanceResponse = commonWorkFlowRepository.startWorkFlow(processInstanceRequest);
                } catch (Exception e) {
                    logger.error("ProcessInstance id couldn't be fetched from workflow svc", e.getCause());
                }
                if (processInstanceResponse != null) {
                    logger.info("Proccess Instance Id received is: " + processInstanceResponse.getProcessInstance().getId());
                    billDetail.setStateId(Long.valueOf(processInstanceResponse.getProcessInstance().getId()));
                    billDetail.setStatus(processInstanceResponse.getProcessInstance().getStatus());
                    logger.info("WorkflowConsumer  listen() receipt workflowdetails after workflow ---->>  " + workflowDetails);

                } else {
                    logger.error("Repository returned null processInstanceResponse");
                }
            }
        }
            kafkaTemplate.send(applicationProperties.getKafkaUpdateWorkFlowDetails(), receiptReq);
    }


    /**
     *
     * @param workflowDetails
     * @param businessKey
     * @param type
     * @param comments
     * @return ProcessInstance to start workflow
     */
    public ProcessInstanceRequest getProcessInstanceRequest(final WorkflowDetailsRequest workflowDetails,String businessKey,
                                                            String type,String comments) {

        final RequestInfo requestInfo = workflowDetails.getRequestInfo();
        final ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        final ProcessInstance processInstance = new ProcessInstance();
        final Position assignee = new Position();
        assignee.setId(workflowDetails.getAssignee());

        logger.info("Workflowdetails received: " + workflowDetails);

        processInstance.setBusinessKey(businessKey);
        processInstance.setType(type);
        processInstance.setComments(StringUtils.isNotBlank(workflowDetails.getComments()) ? workflowDetails.getComments() : comments);
        processInstance.setInitiatorPosition(workflowDetails.getInitiatorPosition());
        processInstance.setAssignee(assignee);
        processInstance.setTenantId(workflowDetails.getTenantId());
        processInstance.setDetails("Receipt Create : " + workflowDetails.getReceiptNumber());
        processInstanceRequest.setProcessInstance(processInstance);
        processInstanceRequest.setRequestInfo(requestInfo);

        return processInstanceRequest;
    }
	
	
}

