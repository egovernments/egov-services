
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

package org.egov.pgr.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.annotation.InterfaceStability;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.domain.model.*;
import org.egov.pgr.domain.model.OTPConfig;
import org.egov.pgr.domain.service.ServiceDefinitionService;
import org.egov.pgr.domain.service.ServiceTypeService;
import org.egov.pgr.persistence.repository.ServiceTypeConfigurationRepository;
import org.egov.pgr.service.EscalationHierarchyService;
import org.egov.pgr.service.EscalationTimeTypeService;
/*import org.egov.pgr.web.contract.RouterReq;*/
import org.egov.pgr.service.GrievanceTypeService;
import org.egov.pgr.service.OTPConfigService;
import org.egov.pgr.service.ReceivingCenterTypeService;
import org.egov.pgr.service.ReceivingModeTypeService;
import org.egov.pgr.service.RouterService;
/*import org.egov.pgr.service.RouterService;*/
import org.egov.pgr.service.ServiceGroupService;
import org.egov.pgr.web.contract.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class PGRConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(PGRConsumer.class);


    @Autowired
    private ServiceGroupService serviceGroupService;
    
    @Autowired
    private RouterService routerService;
    
    @Autowired
	private ReceivingCenterTypeService receivingCenterTypeService;

	@Autowired
	private ReceivingModeTypeService receivingModeTypeService;
	
	@Autowired
	private GrievanceTypeService grievanceTypeService;
	
	@Autowired
	private EscalationTimeTypeService escalationTimeTypeService;

	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired 
	private OTPConfigService otpConfigService;
	
	@Autowired 
	private EscalationHierarchyService escalationHierarchyService; 

	@Autowired
	private ServiceTypeConfigurationRepository serviceTypeConfigurationRepository;

	@Autowired
	private ServiceTypeService serviceTypeService;

	@Autowired
	private ServiceDefinitionService serviceDefinitionService;
	
   	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {
			"${kafka.topics.servicegroup.create.name}", "${kafka.topics.receivingcenter.create.name}",
			"${kafka.topics.receivingmode.create.name}", "${kafka.topics.receivingcenter.update.name}",
			"${kafka.topics.receivingmode.update.name}", "${kafka.topics.servicetype.create.name}", 
			"${kafka.topics.servicegroup.update.name}", "${kafka.topics.servicetype.update.name}","${kafka.topics.router.create.name}",
			"${kafka.topics.servicegroup.update.name}", "${kafka.topics.escalationtimetype.create.name}", 
			"${kafka.topics.escalationtimetype.update.name}", "${kafka.topics.otpconfig.update.name}", "${kafka.topics.otpconfig.create.name}",
			"${kafka.topics.escalationhierarchy.update.name}", "${kafka.topics.escalationhierarchy.create.name}", "${kafka.topics.servicetypeconfiguration.create.name}",
			"${kafka.topics.servicetypeconfiguration.update.name}","${kafka.topics.servicetypes.create.name}",
			"${kafka.topics.servicetypes.create.key}", "${kafka.topics.servicedefinition.create.name}",
			"${kafka.topics.servicedefinition.create.key}", "${kafka.topics.servicetypes.update.name}",
			"${kafka.topics.servicetypes.update.key}", "${kafka.topics.servicedefinition.update.name}",
			"${kafka.topics.servicedefinition.update.key}"})

	public void listen(final ConsumerRecord<String, String> record) {
		LOGGER.info("RECORD: " + record.toString());
		LOGGER.info("key:" + record.key() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
		final ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			if (record.topic().equals(applicationProperties.getCreateServiceGroupTopicName())) {
				LOGGER.info("Consuming create ServiceGroup request");
				serviceGroupService.create(objectMapper.readValue(record.value(), ServiceGroupRequest.class));
			} else if (record.topic().equals(applicationProperties.getCreateReceivingCenterTopicName())) {
				LOGGER.info("Consuming create ReceivingCenterType request");
				receivingCenterTypeService.create(objectMapper.readValue(record.value(), ReceivingCenterTypeReq.class));
			} else if (record.topic().equals(applicationProperties.getUpdateReceivingCenterTopicName())) {
				LOGGER.info("Consuming update ReceivingCenterType request");
				receivingCenterTypeService.update(objectMapper.readValue(record.value(), ReceivingCenterTypeReq.class));
			} else if (record.topic().equals(applicationProperties.getCreateReceivingModeTopicName())) {
				LOGGER.info("Consuming create ReceivingModeType request");
				receivingModeTypeService.create(objectMapper.readValue(record.value(), ReceivingModeTypeReq.class));
			} else if (record.topic().equals(applicationProperties.getUpdateReceivingModeTopicName())) {
				LOGGER.info("Consuming update ReceivingModeType request");
				receivingModeTypeService.update(objectMapper.readValue(record.value(), ReceivingModeTypeReq.class));
			} else if (record.topic().equals(applicationProperties.getCreateGrievanceTypeTopicName())) {
				LOGGER.info("Consuming create GrievanceType request");
				grievanceTypeService.create(objectMapper.readValue(record.value(), ServiceRequest.class));
			} else if (record.topic().equals(applicationProperties.getUpdateServiceGroupTopicName())) {
				LOGGER.info("Consuming update ServiceGroup request");
				serviceGroupService.update(objectMapper.readValue(record.value(), ServiceGroupRequest.class));
			} else if (record.topic().equals(applicationProperties.getUpdateGrievanceTypeTopicName())) {
				LOGGER.info("Consuming update GrievanceType request");
				grievanceTypeService.update(objectMapper.readValue(record.value(), ServiceRequest.class));
			} else if (record.topic().equals(applicationProperties.getCreateServiceGroupTopicName())) {
				LOGGER.info("Consuming create Category request");
				serviceGroupService.create(objectMapper.readValue(record.value(), ServiceGroupRequest.class));
			} else if (record.topic().equals(applicationProperties.getCreateRouterTopicName())) {
				LOGGER.info("Consuming create Router request");
				routerService.create(objectMapper.readValue(record.value(), RouterTypeReq.class));
			} else if (record.topic().equals(applicationProperties.getCreateEscalationTimeTypeName())) {
				LOGGER.info("Consuming create Escalation time type request");
				escalationTimeTypeService.create(objectMapper.readValue(record.value(), EscalationTimeTypeReq.class));
			} else if (record.topic().equals(applicationProperties.getUpdateEscalationTimeTypeName())) {
				LOGGER.info("Consuming update Escalation time type request");
				escalationTimeTypeService.update(objectMapper.readValue(record.value(), EscalationTimeTypeReq.class));
			} else if (record.topic().equals(applicationProperties.getCreateOtpConfigTopicName())) {
				LOGGER.info("Consuming create OTP Config request");
				otpConfigService.create(objectMapper.readValue(record.value(),  OTPConfig.class));
			} else if (record.topic().equals(applicationProperties.getUpdateOtpConfigTopicName())) {
				LOGGER.info("Consuming update OTP Config request");
				otpConfigService.update(objectMapper.readValue(record.value(),  OTPConfig.class)); 
			} else if (record.topic().equals(applicationProperties.getCreateEscalationHierarchyTopicName())) {
				LOGGER.info("Consuming Create Escalation Hierarchy Request");
				escalationHierarchyService.create(objectMapper.readValue(record.value(), EscalationHierarchyReq.class));
			} else if (record.topic().equals(applicationProperties.getUpdateEscalationHierarchyTopicName())) {
				LOGGER.info("Consuming Update Escalation Hierarchy Request");
				escalationHierarchyService.update(objectMapper.readValue(record.value(), EscalationHierarchyReq.class));
			}else if (record.topic().equals(applicationProperties.servicetypeconfigurationCreateTopic())) {
				serviceTypeConfigurationRepository.save(objectMapper.readValue(record.value(), org.egov.pgr.persistence.dto.ServiceTypeConfiguration.class));
			}
			else if (record.topic().equals(applicationProperties.servicetypeconfigurationUpdateTopic())) {
				serviceTypeConfigurationRepository.update(objectMapper.readValue(record.value(), org.egov.pgr.persistence.dto.ServiceTypeConfiguration.class));
			}
			else if(record.topic().equals(applicationProperties.getCreateServiceTypeTopicName())) {
				serviceTypeService.persistServiceType(objectMapper.readValue(record.value(), ServiceTypeRequest.class).toDomain());
			}
			else if(record.topic().equals(applicationProperties.getUpdateServiceTypeTopicName())){
				serviceTypeService.persistForUpdate(objectMapper.readValue(record.value(), ServiceTypeRequest.class).toDomain());
			}
			else if(record.topic().equals(applicationProperties.getCreateServiceDefinitionName())){
				serviceDefinitionService.persist(objectMapper.readValue(record.value(), ServiceDefinitionRequest.class).toDomain());
			}
			else if(record.topic().equals(applicationProperties.getUpdateServiceDefinitionName())){
				serviceDefinitionService.persistForUpdate(objectMapper.readValue(record.value(), ServiceDefinitionRequest.class).toDomain());
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
   	}

