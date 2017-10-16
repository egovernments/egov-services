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

package org.egov.wcms.transaction.consumer;

import java.util.Map;

import org.egov.wcms.transaction.config.ApplicationProperties;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.service.ConnectionNoticeService;
import org.egov.wcms.transaction.service.WaterConnectionService;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionPersistConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(TransactionPersistConsumer.class);

    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WaterConnectionService waterConnectionService;
    
    @Autowired
    private ConnectionNoticeService connectionNoticeService;

    @KafkaListener(topics =
        { "${kafka.topics.newconnection.create.name}","${kafka.topics.newconnection.update.name}",
            "${kafka.topics.legacyconnection.create.name}" , "${kafka.topics.workorder.persist.name}" ,
            "${kafka.topics.demandBill.update.name}",
            "${kafka.topics.wcms.newconnection-workflow.create}",
            "${kafka.topics.wcms.newconnection-workflow.update}", 
            "${kafka.topics.legacyconnection.update.name}"})

    public void processMessage(final Map<String, Object> consumerRecord,
			@Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) {
		LOGGER.debug("key:" + topic + ":" + "value:" + consumerRecord);
		try {
			if (topic.equals(applicationProperties.getInitiatedWorkFlow()))
				waterConnectionService.create(objectMapper.convertValue(consumerRecord.get(applicationProperties.getInitiatedWorkFlow()), WaterConnectionReq.class));
			if (topic.equals(applicationProperties.getUpdateNewConnectionTopicName()))
				waterConnectionService.update(objectMapper.convertValue(consumerRecord, WaterConnectionReq.class));
			if(topic.equals(applicationProperties.getWorkOrderTopicName()))
			    connectionNoticeService.persistWorkOrderLog(objectMapper.convertValue(consumerRecord, WorkOrderFormat.class));
			if(topic.equals(applicationProperties.getUpdateDemandBillTopicName()))
			    waterConnectionService.updateWaterConnectionAfterCollection(objectMapper.convertValue(consumerRecord, DemandResponse.class));
			if(topic.equals(applicationProperties.getLegacyConnectionUpdateTopicName())) 
				waterConnectionService.updateLegacyConnection(objectMapper.convertValue(consumerRecord, WaterConnectionReq.class));
		} catch (final Exception e) {
			LOGGER.error("Exception Encountered while processing the received message : " + e.getMessage());
		}
	}

}
