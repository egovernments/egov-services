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
package org.egov.collection.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.repository.CollectionConfigurationRepository;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.web.contract.InstrumentType;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WorkflowConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(WorkflowConsumer.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;
		
	@Autowired
	private WorkflowService workflowService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CollectionConfigurationRepository collectionConfigurationRepository;

	@KafkaListener(topics = {"${kafka.topics.receipt.create.name}","${kafka.topics.receipt.update.name}"})
    public void listen(final Map<String, Object> receiptRequestMap, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("Record: "+receiptRequestMap);
        ReceiptRequest receiptRequest = objectMapper.convertValue(receiptRequestMap, ReceiptRequest.class);
        Receipt receipt = receiptRequest.getReceipt().get(0);
        InstrumentType instrumentType = receipt.getInstrument().getInstrumentType();
        Map<String,List<String>> workflowCongurations = collectionConfigurationRepository.searchWorkFlowConfigurationValues(receiptRequest.getRequestInfo(),receipt.getTenantId(), CollectionServiceConstants.RECEIPT_PREAPPROVED_OR_APPROVED_CONFIG_KEY);
        try {
            if(topic.equals(applicationProperties.getKafkaStartWorkflowTopic()) && !workflowCongurations.isEmpty() && workflowCongurations
                    .get(CollectionServiceConstants.RECEIPT_PREAPPROVED_OR_APPROVED_CONFIG_KEY)
                    .get(0)
                    .equals(CollectionServiceConstants.PREAPPROVED_CONFIG_VALUE) && !instrumentType.getName().equalsIgnoreCase(CollectionServiceConstants.INSTRUMENT_TYPE_ONLINE)) {
                logger.info("Consuming start workflow request");
                workflowService.startWorkflow(receiptRequest);
            }else if(topic.equals(applicationProperties.getKafkaUpdateworkflowTopic())){
                logger.info("Consuming update workflow request");
               // workflowService.updateWorkflow(receiptRequest.getReceipt().get(0).getWorkflowDetails());
            }

        } catch (final Exception e) {
            e.printStackTrace();
            logger.error("Error while listening to value: "+receiptRequestMap+" on topic: "+topic+": ", e.getMessage());
        }
    }

}