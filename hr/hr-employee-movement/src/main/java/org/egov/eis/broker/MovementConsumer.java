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

package org.egov.eis.broker;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.eis.service.MovementService;
import org.egov.eis.web.contract.MovementRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MovementConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(MovementConsumer.class);

    @Value("${kafka.topics.movement.create.name}")
    private String movementCreateTopic;

    @Value("${kafka.topics.movement.update.name}")
    private String movementUpdateTopic;

    @Autowired
    private MovementService movementService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {
            "${kafka.topics.movement.create.name}",
            "${kafka.topics.movement.update.name}" })

    public void listen(final ConsumerRecord<String, String> record) {
        LOGGER.info("key:" + record.key() + ":" + "value:" + record.value());
        try {
            if (record.topic().equalsIgnoreCase(movementCreateTopic))
                movementService.create(objectMapper.readValue(record.value(), MovementRequest.class));
            else if (record.topic().equalsIgnoreCase(movementUpdateTopic))
                movementService.update(objectMapper.readValue(record.value(), MovementRequest.class));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
