
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

package org.egov.wcms.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.service.ConnectionCategoryService;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.service.PipeSizeService;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.service.PropertyUsageTypeService;
import org.egov.wcms.service.UsageTypeService;
import org.egov.wcms.web.contract.ConnectionCategoryRequest;
import org.egov.wcms.web.contract.DonationRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.PropertyTypeUsageTypeReq;
import org.egov.wcms.web.contract.UsageTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

public class WaterMasterConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterMasterConsumer.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private ConnectionCategoryService categoryService;
    
    @Autowired
    private PropertyUsageTypeService propUsageType;
    
    @Autowired
    private DonationService donationService;

    @Autowired
    private PipeSizeService pipeSizeService;
    
    @Autowired
    private PropertyCategoryService propertyCategoryService;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {"${kafka.topics.usagetype.create.name}","${kafka.topics.usagetype.update.name}",
            "${kafka.topics.category.create.name}","${kafka.topics.category.update.name}","${kafka.topics.pipesize.create.name}","${kafka.topics.pipesize.update.name}", 
            "${kafka.topics.propertyCategory.create.name}","${kafka.topics.propertyusage.create.name}", "${kafka.topics.donation.create.name}" })
    public void listen(final ConsumerRecord<String, String> record) {
        LOGGER.info("key:" + record.key() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (record.topic().equals(applicationProperties.getCreateUsageTypeTopicName()))
                usageTypeService.create(objectMapper.readValue(record.value(), UsageTypeRequest.class));
            else if(record.topic().equals(applicationProperties.getUpdateUsageTypeTopicName()))
                usageTypeService.update(objectMapper.readValue(record.value(), UsageTypeRequest.class));
            else if(record.topic().equals(applicationProperties.getCreateCategoryTopicName()))
                categoryService.create(objectMapper.readValue(record.value(), ConnectionCategoryRequest.class));
            else if(record.topic().equals(applicationProperties.getUpdateCategoryTopicName()))
                categoryService.update(objectMapper.readValue(record.value(), ConnectionCategoryRequest.class));
            else if(record.topic().equals(applicationProperties.getCreatePipeSizetopicName()))
                pipeSizeService.create(objectMapper.readValue(record.value(), PipeSizeRequest.class));
            else if(record.topic().equals(applicationProperties.getUpdatePipeSizeTopicName()))
                pipeSizeService.update(objectMapper.readValue(record.value(), PipeSizeRequest.class));
            else if(record.topic().equals(applicationProperties.getCreatePropertyCategoryTopicName()))
            	propertyCategoryService.create(objectMapper.readValue(record.value(), PropertyTypeCategoryTypeReq.class));
            else if(record.topic().equals(applicationProperties.getCreatePropertyUsageTopicName()))
            	propUsageType.create(objectMapper.readValue(record.value(), PropertyTypeUsageTypeReq.class));
            else if(record.topic().equals(applicationProperties.getCreateDonationTopicName()))
            	donationService.create(objectMapper.readValue(record.value(), DonationRequest.class));            	
        } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
