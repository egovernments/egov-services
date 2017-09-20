
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

import java.util.Map;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.service.DocumentTypeService;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.service.GapcodeService;
import org.egov.wcms.service.MeterCostService;
import org.egov.wcms.service.MeterStatusService;
import org.egov.wcms.service.MeterWaterRatesService;
import org.egov.wcms.service.NonMeterWaterRatesService;
import org.egov.wcms.service.PipeSizeService;
import org.egov.wcms.service.ServiceChargeService;
import org.egov.wcms.service.SourceTypeService;
import org.egov.wcms.service.StorageReservoirService;
import org.egov.wcms.service.SupplyTypeService;
import org.egov.wcms.service.TreatmentPlantService;
import org.egov.wcms.service.UsageTypeService;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.egov.wcms.web.contract.DonationRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.egov.wcms.web.contract.MeterCostReq;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.egov.wcms.web.contract.MeterWaterRatesRequest;
import org.egov.wcms.web.contract.NonMeterWaterRatesReq;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.egov.wcms.web.contract.StorageReservoirRequest;
import org.egov.wcms.web.contract.SupplyTypeRequest;
import org.egov.wcms.web.contract.TreatmentPlantRequest;
import org.egov.wcms.web.contract.UsageTypeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WaterMasterConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private SupplyTypeService supplyTypeService;

    @Autowired
    private ServiceChargeService serviceChargeService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private DocumentTypeApplicationTypeService docTypeApplTypeService;

    @Autowired
    private MeterCostService meterCostService;

    @Autowired
    private SourceTypeService waterSourceTypeService;

    @Autowired
    private StorageReservoirService storageReservoirService;

    @Autowired
    private TreatmentPlantService treatmentPlantService;

    @Autowired
    private MeterWaterRatesService meterWaterRatesService;

    @Autowired
    private MeterStatusService meterStatusService;

    @Autowired
    private NonMeterWaterRatesService nonMeterWaterRatesService;

    @Autowired
    private GapcodeService gapcodeService;

    @Autowired
    private UsageTypeService usageTypeService;

    @KafkaListener(topics = { "${kafka.topics.usagetype.create.name}", "${kafka.topics.usagetype.update.name}",
            "${kafka.topics.pipesize.create.name}", "${kafka.topics.pipesize.update.name}",
            "${kafka.topics.donation.create.name}", "${kafka.topics.donation.update.name}",
            "${kafka.topics.documenttype.create.name}", "${kafka.topics.documenttype.update.name}",
            "${kafka.topics.documenttype.applicationtype.create.name}",
            "${kafka.topics.documenttype.applicationtype.update.name}",
            "${kafka.topics.sourcetype.create.name}",
            "${kafka.topics.sourcetype.update.name}", "${kafka.topics.supplytype.create.name}",
            "${kafka.topics.supplytype.update.name}", "${kafka.topics.storagereservoir.create.name}",
            "${kafka.topics.storagereservoir.update.name}", "${kafka.topics.treatmentplant.create.name}",
            "${kafka.topics.treatmentplant.update.name}", "${kafka.topics.meterwaterrates.create.name}",
            "${kafka.topics.meterwaterrates.update.name}", "${kafka.topics.metercost.create.name}",
            "${kafka.topics.metercost.update.name}", "${kafka.topics.meterstatus.create.name}",
            "${kafka.topics.meterstatus.update.name}", "${kafka.topics.nonmeterwaterrates.create.name}",
            "${kafka.topics.nonmeterwaterrates.update.name}", "${kafka.topics.servicecharge.create.name}",
            "${kafka.topics.servicecharge.update.name}", "${kafka.topics.gapcode.create.name}",
            "${kafka.topics.gapcode.update.name}" })

    public void processMessage(final Map<String, Object> consumerRecord,
            @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) {
        log.debug("key:" + topic + ":" + "value:" + consumerRecord);

        try {
            if (applicationProperties.getCreatePipeSizetopicName().equals(topic))
                pipeSizeService.create(objectMapper.convertValue(consumerRecord, PipeSizeRequest.class));
            else if (applicationProperties.getUpdatePipeSizeTopicName().equals(topic))
                pipeSizeService.update(objectMapper.convertValue(consumerRecord, PipeSizeRequest.class));

            else if (applicationProperties.getCreateDonationTopicName().equals(topic))
                donationService.create(objectMapper.convertValue(consumerRecord, DonationRequest.class));
            else if (applicationProperties.getUpdateDonationTopicName().equals(topic))
                donationService.update(objectMapper.convertValue(consumerRecord, DonationRequest.class));
            else if (applicationProperties.getCreateDocumentTypeTopicName().equals(topic))
                documentTypeService.create(objectMapper.convertValue(consumerRecord, DocumentTypeReq.class));
            else if (applicationProperties.getUpdateDocumentTypeTopicName().equals(topic))
                documentTypeService.update(objectMapper.convertValue(consumerRecord, DocumentTypeReq.class));

            else if (applicationProperties.getCreateDocumentTypeApplicationTypeTopicName().equals(topic))
                docTypeApplTypeService
                        .create(objectMapper.convertValue(consumerRecord, DocumentTypeApplicationTypeReq.class));
            else if (applicationProperties.getUpdateDocumentTypeApplicationTypeTopicName().equals(topic))
                docTypeApplTypeService
                        .update(objectMapper.convertValue(consumerRecord, DocumentTypeApplicationTypeReq.class));
            else if (applicationProperties.getCreateMeterCostTopicName().equals(topic)) {
                log.info("Consuming MeterCostCreate Request");
                meterCostService.create(objectMapper.convertValue(consumerRecord, MeterCostReq.class));
            } else if (applicationProperties.getUpdateMeterCostTopicName().equals(topic)) {
                log.info("Consuming MeterCostUpdate Request");
                meterCostService.update(objectMapper.convertValue(consumerRecord, MeterCostReq.class));
            } else if (applicationProperties.getCreateMeterStatusTopicName().equals(topic)) {
                log.info("Consuming MeterStatusCreate Request");
                meterStatusService.create(objectMapper.convertValue(consumerRecord, MeterStatusReq.class));
            } else if (applicationProperties.getUpdateMeterStatusTopicName().equals(topic)) {
                log.info("Consuming MeterStatusUpdate Request");
                meterStatusService.update(objectMapper.convertValue(consumerRecord, MeterStatusReq.class));
            } else if (applicationProperties.getCreateServiceChargeTopicName().equals(topic)) {
                log.info("Consuming createServiceChargeRequest");
                serviceChargeService
                        .create(objectMapper.convertValue(consumerRecord, ServiceChargeReq.class));
            } else if (applicationProperties.getUpdateServiceChargeTopicName().equals(topic)) {
                log.info("Consuming updateServiceChargeRequest");
                serviceChargeService
                        .update(objectMapper.convertValue(consumerRecord, ServiceChargeReq.class));
            } else if (applicationProperties.getCreateUsageTypeTopicName().equals(topic)) {
                log.info("Consuming createUsageTypeRequest");
                usageTypeService
                        .create(objectMapper.convertValue(consumerRecord, UsageTypeReq.class));
            } else if (applicationProperties.getUpdateUsageTypeTopicName().equals(topic)) {
                log.info("Consuming updateUsageTypeRequest");
                usageTypeService
                        .update(objectMapper.convertValue(consumerRecord, UsageTypeReq.class));
            } else if (applicationProperties.getCreateSourceTypeTopicName().equals(topic))
                waterSourceTypeService.create(objectMapper.convertValue(consumerRecord, SourceTypeRequest.class));
            else if (applicationProperties.getUpdateSourceTypeTopicName().equals(topic))
                waterSourceTypeService.update(objectMapper.convertValue(consumerRecord, SourceTypeRequest.class));
            else if (applicationProperties.getCreateSupplyTypeTopicName().equals(topic))
                supplyTypeService.create(objectMapper.convertValue(consumerRecord, SupplyTypeRequest.class));
            else if (applicationProperties.getUpdateSupplyTypeTopicName().equals(topic))
                supplyTypeService.update(objectMapper.convertValue(consumerRecord, SupplyTypeRequest.class));
            else if (applicationProperties.getCreateStorageReservoirTopicName().equals(topic))
                storageReservoirService
                        .create(objectMapper.convertValue(consumerRecord, StorageReservoirRequest.class));
            else if (applicationProperties.getUpdateStorageReservoirTopicName().equals(topic))
                storageReservoirService
                        .update(objectMapper.convertValue(consumerRecord, StorageReservoirRequest.class));
            else if (applicationProperties.getCreateTreatmentPlantTopicName().equals(topic))
                treatmentPlantService.create(objectMapper.convertValue(consumerRecord, TreatmentPlantRequest.class));
            else if (applicationProperties.getUpdateTreatmentPlantTopicName().equals(topic))
                treatmentPlantService.update(objectMapper.convertValue(consumerRecord, TreatmentPlantRequest.class));
            else if (applicationProperties.getCreateMeterWaterRatesTopicName().equals(topic))
                meterWaterRatesService.create(objectMapper.convertValue(consumerRecord, MeterWaterRatesRequest.class));
            else if (applicationProperties.getUpdateMeterWaterRatesTopicName().equals(topic))
                meterWaterRatesService.update(objectMapper.convertValue(consumerRecord, MeterWaterRatesRequest.class));
            else if (applicationProperties.getCreateNonMeterWaterRatesTopicName().equals(topic))
                nonMeterWaterRatesService
                        .create(objectMapper.convertValue(consumerRecord, NonMeterWaterRatesReq.class));
            else if (applicationProperties.getUpdateNonMeterWaterRatesTopicName().equals(topic))
                nonMeterWaterRatesService
                        .update(objectMapper.convertValue(consumerRecord, NonMeterWaterRatesReq.class));
            else if (applicationProperties.getCreateGapcodeTopicName().equals(topic))
                System.out.println(gapcodeService.create(objectMapper.convertValue(consumerRecord, GapcodeRequest.class)));
            else if (applicationProperties.getUpdateGapcodeTopicName().equals(topic))
                System.out.println(gapcodeService.update(objectMapper.convertValue(consumerRecord, GapcodeRequest.class)));
        } catch (final Exception exception) {
            log.debug("processMessage:" + exception);
            throw exception;
        }
    }
}