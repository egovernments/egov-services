/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *
 *       Copyright (C) <2015>  eGovernments Foundation
 *
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.workflow.consumer;

import java.util.HashMap;
import java.util.Map;

import org.egov.wcms.exception.ConnectionWorkflowException;
import org.egov.wcms.workflow.config.ApplicationProperties;
import org.egov.wcms.workflow.model.contract.WaterConnectionReq;
import org.egov.wcms.workflow.service.ConnectionWorkflowService;
import org.egov.wcms.workflow.service.WaterConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConnectionWorkflowListener {
    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ObjectMapper objectMapper;

  
    @Autowired
    private ConnectionWorkflowService connectionWorkflowService;
    @Autowired
    private WaterConfigurationService waterConfigurationService;

    @KafkaListener(topics = { "${kafka.topics.wcms.newconnection-workflow.create}",
            "${kafka.topics.wcms.newconnection-workflow.update}",
            "${kafka.topics.connection.create.name}",
            "${kafka.topics.connection.update.name}",
            "${kafka.topics.updateconn.aftercollection}"})
    public void processMessage(final Map<String, Object> consumerRecord,
            @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) throws ConnectionWorkflowException {
        final HashMap<String, Object> workflowEnrichedMap = new HashMap<>();
        final WaterConnectionReq waterConnectionReq = objectMapper.convertValue(consumerRecord, WaterConnectionReq.class);
        if( waterConnectionReq.getConnection() !=null &&  waterConnectionReq.getConnection().getTenantId() !=null){
        final Boolean isWorkFlowEnabled = waterConfigurationService.getConfigurations(
                waterConnectionReq.getConnection().getTenantId(),
                waterConnectionReq.getRequestInfo());
        log.info("workflow enabled = "+ isWorkFlowEnabled);
        try {
            if (!waterConnectionReq.getConnection().getIsLegacy() && isWorkFlowEnabled){

                if (applicationProperties.getCreateConnection().equals(topic)) {
                    log.info("topic  for initiate WorkFlow "+ topic);
                    connectionWorkflowService.initiateWorkFlow(workflowEnrichedMap, waterConnectionReq);
                }

                else if (applicationProperties.getUpdateConnection().equals(topic)) {
                    connectionWorkflowService.updateWorkFlow(consumerRecord, workflowEnrichedMap, waterConnectionReq);
                    log.info("topic  for update WorkFlow "+ topic);

                }
                if ( topic.equalsIgnoreCase(applicationProperties.getUpdateconnectionAfterCollection())) {
                        connectionWorkflowService.prepareWorkflow(waterConnectionReq.getConnection());
                        connectionWorkflowService.updateWorkFlow(consumerRecord, workflowEnrichedMap, waterConnectionReq);
                        log.info("topic  for update WorkFlow "+ topic);
                    }
                        
            }

        }catch (final Exception exception) {
            throw new ConnectionWorkflowException("Error in Connection Workflow",
                    waterConnectionReq.getRequestInfo());
        }
        }

    }

        
}
