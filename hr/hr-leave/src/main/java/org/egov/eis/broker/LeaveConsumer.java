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
import org.egov.eis.model.LeaveType;
import org.egov.eis.repository.ElasticSearchRepository;
import org.egov.eis.service.LeaveAllotmentService;
import org.egov.eis.service.LeaveApplicationService;
import org.egov.eis.service.LeaveOpeningBalanceService;
import org.egov.eis.service.LeaveTypeService;
import org.egov.eis.web.contract.LeaveAllotmentRequest;
import org.egov.eis.web.contract.LeaveApplicationRequest;
import org.egov.eis.web.contract.LeaveApplicationSingleRequest;
import org.egov.eis.web.contract.LeaveOpeningBalanceRequest;
import org.egov.eis.web.contract.LeaveTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LeaveConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(LeaveConsumer.class);

    private static final String OBJECT_TYPE_LEAVETYPE = "leavetype";

    @Value("${kafka.topics.leaveopeningbalance.create.name}")
    private String leaveOpeningBalanceCreateTopic;

    @Value("${kafka.topics.leaveopeningbalance.update.name}")
    private String leaveOpeningBalanceUpdateTopic;

    @Value("${kafka.topics.leavetype.name}")
    private String leaveTypeTopic;

    @Value("${kafka.topics.leaveallotment.create.name}")
    private String leaveAllotmentCreateTopic;

    @Value("${kafka.topics.leaveallotment.update.name}")
    private String leaveAllotmentUpdateTopic;

    @Value("${kafka.topics.leaveapplication.create.name}")
    private String leaveApplicationCreateTopic;

    @Value("${kafka.topics.leaveapplication.update.name}")
    private String leaveApplicationUpdateTopic;

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private ElasticSearchRepository elasticSearchRepository;

    @Autowired
    private LeaveOpeningBalanceService leaveOpeningBalanceService;

    @Autowired
    private LeaveAllotmentService leaveAllotmentService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {
            "${kafka.topics.leaveopeningbalance.create.name}", "${kafka.topics.leaveopeningbalance.update.name}",
            "${kafka.topics.leavetype.name}", "${kafka.topics.leaveallotment.create.name}",
            "${kafka.topics.leaveallotment.update.name}", "${kafka.topics.leaveapplication.create.name}",
            "${kafka.topics.leaveapplication.update.name}" })

    public void listen(final ConsumerRecord<String, String> record) {
        LOGGER.info("key:" + record.key() + ":" + "value:" + record.value());
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (record.topic().equalsIgnoreCase(leaveOpeningBalanceCreateTopic))
                leaveOpeningBalanceService
                        .create(objectMapper.readValue(record.value(), LeaveOpeningBalanceRequest.class));
            else if (record.topic().equalsIgnoreCase(leaveOpeningBalanceUpdateTopic))
                leaveOpeningBalanceService
                        .update(objectMapper.readValue(record.value(), LeaveOpeningBalanceRequest.class));
            else if (record.topic().equalsIgnoreCase(leaveTypeTopic)) {
                LOGGER.info("SaveLeaveTypeConsumer egov-hr-leavetype leaveTypeService:" + leaveTypeService);
                final LeaveTypeRequest leaveTypeRequest = leaveTypeService
                        .create(objectMapper.readValue(record.value(), LeaveTypeRequest.class));
                for (final LeaveType leaveType : leaveTypeRequest.getLeaveType())
                    // TODO : leavetype index id should be changed
                    elasticSearchRepository.index(OBJECT_TYPE_LEAVETYPE,
                            leaveType.getTenantId() + "" + leaveType.getName(), leaveType);
            } else if (record.topic().equalsIgnoreCase(leaveAllotmentCreateTopic))
                leaveAllotmentService.create(objectMapper.readValue(record.value(), LeaveAllotmentRequest.class));
            else if (record.topic().equalsIgnoreCase(leaveAllotmentUpdateTopic))
                leaveAllotmentService.update(objectMapper.readValue(record.value(), LeaveAllotmentRequest.class));
            else if (record.topic().equalsIgnoreCase(leaveApplicationCreateTopic))
                leaveApplicationService.create(objectMapper.readValue(record.value(), LeaveApplicationRequest.class));
            else if (record.topic().equalsIgnoreCase(leaveApplicationUpdateTopic))
                leaveApplicationService.update(objectMapper.readValue(record.value(), LeaveApplicationSingleRequest.class));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
