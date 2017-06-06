
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

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.service.CategoryTypeService;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.service.DocumentTypeService;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.service.MeterCostService;
import org.egov.wcms.service.PipeSizeTypeService;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.service.PropertyTypePipeSizeTypeService;
import org.egov.wcms.service.PropertyUsageTypeService;
import org.egov.wcms.service.UsageTypeService;
import org.egov.wcms.service.WaterSourceTypeService;
import org.egov.wcms.web.contract.CategoryTypeRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.egov.wcms.web.contract.DonationRequest;
import org.egov.wcms.web.contract.MeterCostRequest;
import org.egov.wcms.web.contract.PipeSizeTypeRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.PropertyTypePipeSizeTypeRequest;
import org.egov.wcms.web.contract.PropertyTypeUsageTypeReq;
import org.egov.wcms.web.contract.UsageTypeRequest;
import org.egov.wcms.web.contract.WaterSourceTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WaterMasterConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(WaterMasterConsumer.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private CategoryTypeService categoryService;

    @Autowired
    private PropertyUsageTypeService propUsageTypeService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private PipeSizeTypeService pipeSizeService;

    @Autowired
    private PropertyCategoryService propertyCategoryService;

    @Autowired
    private DocumentTypeService documentTypeService;

  
    @Autowired
    private DocumentTypeApplicationTypeService docTypeApplTypeService;
    
    @Autowired
    private MeterCostService meterCostService;
    
    @Autowired
    private PropertyTypePipeSizeTypeService propertyPipeSizeService;

    @Autowired
    private WaterSourceTypeService waterSourceTypeService;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = { "${kafka.topics.usagetype.create.name}","${kafka.topics.usagetype.update.name}",
            
            "${kafka.topics.category.create.name}", "${kafka.topics.category.update.name}",
            "${kafka.topics.pipesize.create.name}", "${kafka.topics.pipesize.update.name}",
            "${kafka.topics.propertyCategory.create.name}", "${kafka.topics.propertyCategory.update.name}",
            "${kafka.topics.donation.create.name}","${kafka.topics.donation.update.name}",
            "${kafka.topics.documenttype.create.name}","${kafka.topics.documenttype.update.name}",
            "${kafka.topics.documenttype.applicationtype.create.name}","${kafka.topics.documenttype.applicationtype.update.name}",
            "${kafka.topics.propertyusage.create.name}","${kafka.topics.propertyusage.update.name}",
            "${kafka.topics.propertypipesize.create.name}", "${kafka.topics.propertypipesize.update.name}",
            "${kafka.topics.watersourcetype.create.name}", "${kafka.topics.watersourcetype.update.name}" })

    public void listen(final ConsumerRecord<String, String> record) {
        LOGGER.info("key:" + record.key() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (record.topic().equals(applicationProperties.getCreateUsageTypeTopicName()))
                usageTypeService.create(objectMapper.readValue(record.value(), UsageTypeRequest.class));
            else if (record.topic().equals(applicationProperties.getUpdateUsageTypeTopicName()))
                usageTypeService.update(objectMapper.readValue(record.value(), UsageTypeRequest.class));
            else if (record.topic().equals(applicationProperties.getCreateCategoryTopicName()))
                categoryService.create(objectMapper.readValue(record.value(), CategoryTypeRequest.class));
            else if (record.topic().equals(applicationProperties.getUpdateCategoryTopicName()))
                categoryService.update(objectMapper.readValue(record.value(), CategoryTypeRequest.class));
            else if (record.topic().equals(applicationProperties.getCreatePipeSizetopicName()))
                pipeSizeService.create(objectMapper.readValue(record.value(), PipeSizeTypeRequest.class));
            else if (record.topic().equals(applicationProperties.getUpdatePipeSizeTopicName()))
                pipeSizeService.update(objectMapper.readValue(record.value(), PipeSizeTypeRequest.class));
            else if(record.topic().equals(applicationProperties.getCreatePropertyUsageTopicName()))
            	propUsageTypeService.create(objectMapper.readValue(record.value(), PropertyTypeUsageTypeReq.class));
            else if(record.topic().equals(applicationProperties.getCreateDonationTopicName()))
            	donationService.create(objectMapper.readValue(record.value(), DonationRequest.class));
            else if(record.topic().equals(applicationProperties.getUpdateDonationTopicName()))
            	donationService.update(objectMapper.readValue(record.value(), DonationRequest.class));
            else if(record.topic().equals(applicationProperties.getCreateDocumentTypeTopicName()))
            	documentTypeService.create(objectMapper.readValue(record.value(), DocumentTypeReq.class));
            else if (record.topic().equals(applicationProperties.getUpdateDocumentTypeTopicName()))
                documentTypeService.update(objectMapper.readValue(record.value(), DocumentTypeReq.class));
            else if (record.topic().equals(applicationProperties.getCreatePropertyCategoryTopicName()))
                propertyCategoryService.create(objectMapper.readValue(record.value(), PropertyTypeCategoryTypeReq.class));
            else if (record.topic().equals(applicationProperties.getUpdatePropertyCategoryTopicName()))
                propertyCategoryService.update(objectMapper.readValue(record.value(), PropertyTypeCategoryTypeReq.class));
            else if (record.topic().equals(applicationProperties.getUpdatePropertyUsageTopicName()))
                propUsageTypeService.update(objectMapper.readValue(record.value(), PropertyTypeUsageTypeReq.class));
            else if (record.topic().equals(applicationProperties.getCreatePropertyPipeSizeTopicName()))
                propertyPipeSizeService.create(objectMapper.readValue(record.value(), PropertyTypePipeSizeTypeRequest.class));
            else if (record.topic().equals(applicationProperties.getUpdatePropertyPipeSizeTopicName()))
                propertyPipeSizeService.update(objectMapper.readValue(record.value(), PropertyTypePipeSizeTypeRequest.class));
            else if(record.topic().equals(applicationProperties.getCreateDocumentTypeApplicationTypeTopicName()))
            	docTypeApplTypeService.create(objectMapper.readValue(record.value(), DocumentTypeApplicationTypeReq.class));
            else if (record.topic().equals(applicationProperties.getUpdateDocumentTypeApplicationTypeTopicName()))
            	docTypeApplTypeService.update(objectMapper.readValue(record.value(), DocumentTypeApplicationTypeReq.class));
            else if(record.topic().equals(applicationProperties.getCreateMeterCostTopicName()))
            	meterCostService.create(objectMapper.readValue(record.value(), MeterCostRequest.class));
            else if (record.topic().equals(applicationProperties.getCreateWaterSourceTypeTopicName()))
                waterSourceTypeService.create(objectMapper.readValue(record.value(), WaterSourceTypeRequest.class));
            else if (record.topic().equals(applicationProperties.getUpdateWaterSourceTypeTopicName()))
                waterSourceTypeService.update(objectMapper.readValue(record.value(), WaterSourceTypeRequest.class));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
