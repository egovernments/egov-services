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
package org.egov.wcms.notification.consumers;

import java.util.Map;

import org.egov.wcms.notification.config.PropertiesManager;
import org.egov.wcms.notification.exception.ConnectionNotificationException;
import org.egov.wcms.notification.service.NotificationService;
import org.egov.wcms.notification.web.contract.ConnectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConnectionNotificationConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PropertiesManager propertiesManager;
    
    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = { "${kafka.topics.notification.connection.create.name}",
            "${kafka.topics.notification.connection.update.name}" })
    public void processMessage(final Map<String, Object> consumerRecord,
            @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) throws ConnectionNotificationException {
        log.debug("key:" + topic + ":" + "value:" + consumerRecord);
        final ConnectionRequest connectionRequest = objectMapper.convertValue(consumerRecord, ConnectionRequest.class);

        try {
            if (propertiesManager.getCreateConnection().equals(topic)) {
                log.info("topic  for Create Connection "+ topic);
            notificationService
                    .waterNewCreationAcknowledgement(connectionRequest);
            } else if (propertiesManager.getUpdateConnection().equals(topic)){
                notificationService
                .waterUpdateConnection(connectionRequest);
                
            }
        }  catch (final Exception exception) {
            throw new ConnectionNotificationException("Error in Connection Notification",
                    connectionRequest.getRequestInfo());
        }

    }
}