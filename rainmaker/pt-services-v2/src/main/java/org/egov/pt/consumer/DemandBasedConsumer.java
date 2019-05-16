package org.egov.pt.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.producer.Producer;
import org.egov.pt.service.PropertyService;
import org.egov.pt.web.models.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DemandBasedConsumer {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private Producer producer;

    @Autowired
    private PropertyConfiguration config;


    /**
     * Listens on the bulk update topic and pushes failed batches on dead letter topic
     *
     * @param record The input bulk update requests
     */
    @KafkaListener(topics = {"${persister.demand.based.topic}"}, containerFactory = "kafkaListenerContainerFactoryBatch")
    public void listen(final HashMap<String, Object> record) {

        DemandBasedAssessmentRequest demandBasedAssessmentRequest = null;
        try {
            demandBasedAssessmentRequest = mapper.convertValue(record, DemandBasedAssessmentRequest.class);
        } catch (final Exception e) {
            log.error("Error while listening to value: " + record);
        }
        log.info("Number of batch records: " + demandBasedAssessmentRequest.getDemandBasedAssessments().size());
        log.info("Batch request: " + demandBasedAssessmentRequest);
        RequestInfo requestInfo = demandBasedAssessmentRequest.getRequestInfo();

        Map<String, List<DemandBasedAssessment>> tenantIdToDemandBasedAssessmentMap = groupByTenantId(demandBasedAssessmentRequest);

        for (Map.Entry<String, List<DemandBasedAssessment>> entry : tenantIdToDemandBasedAssessmentMap.entrySet()) {
            createAssessment(requestInfo, entry.getValue(), config.getDeadLetterTopicBatch());
        }
    }

    /**
     * Listens on the dead letter topic of the bulk request and processes
     * every record individually and pushes failed records on error topic
     *
     * @param record Single update request
     */
 //   @KafkaListener(topics = {"${persister.demand.based.dead.letter.topic.batch}"}, containerFactory = "kafkaListenerContainerFactory")
    public void listenDeadLetterTopic(final HashMap<String, Object> record) {

        DemandBasedAssessmentRequest demandBasedAssessmentRequest = null;
        try {
            demandBasedAssessmentRequest = mapper.convertValue(record, DemandBasedAssessmentRequest.class);
        } catch (final Exception e) {
            log.error("Error while listening to value: " + record);
        }
        log.info("Number of records: " + demandBasedAssessmentRequest.getDemandBasedAssessments().size());
        log.info("Assessment to updated: " + demandBasedAssessmentRequest.getDemandBasedAssessments().get(0).getAssessmentNumber());
        RequestInfo requestInfo = demandBasedAssessmentRequest.getRequestInfo();

        Map<String, List<DemandBasedAssessment>> tenantIdToDemandBasedAssessmentMap = groupByTenantId(demandBasedAssessmentRequest);

        for (Map.Entry<String, List<DemandBasedAssessment>> entry : tenantIdToDemandBasedAssessmentMap.entrySet()) {
            createAssessment(requestInfo, entry.getValue(), config.getDeadLetterTopicSingle());
        }
    }


    /**
     * Searches the property, sets the financialYear and calls update on it
     *
     * @param requestInfo            The RequestInfo object of the request
     * @param demandBasedAssessments The list of DemandBasedAssessment objects containing the assessmentNumber
     *                               to be updated
     * @param errorTopic             The topic on whcih failed request are pushed
     */
    private void createAssessment(RequestInfo requestInfo, List<DemandBasedAssessment> demandBasedAssessments, String errorTopic) {
        try {
            String financialYear = demandBasedAssessments.get(0).getFinancialYear();
            PropertyCriteria criteria = getSearchCriteria(demandBasedAssessments);
            List<Property> properties = propertyService.getPropertiesWithOwnerInfo(criteria, requestInfo);
            setFields(properties, financialYear);
            propertyService.updateProperty(new PropertyRequest(requestInfo, properties));
            if(errorTopic.equalsIgnoreCase(config.getDeadLetterTopicBatch()))
                log.info("Batch Processed Successfully: {}",demandBasedAssessments);

        } catch (Exception e) {
            DemandBasedAssessmentRequest request = DemandBasedAssessmentRequest.builder()
                    .requestInfo(requestInfo).build();

            for (DemandBasedAssessment demandBasedAssessment : demandBasedAssessments) {
                request.setDemandBasedAssessments(Collections.singletonList(demandBasedAssessment));
                log.error("UPDATE ERROR: ", e);
                log.info("error topic: {}", errorTopic);
                log.info("error request: {}", request);
                producer.push(errorTopic, request);
            }
        }

    }


    /**
     * Creates property search criteria based on DemandBasedAssessment
     *
     * @param demandBasedAssessments The list of demandBasedAssessment
     * @return PropertySearchCriteria
     */
    private PropertyCriteria getSearchCriteria(List<DemandBasedAssessment> demandBasedAssessments) {

        Set<String> assessmentNumbers = demandBasedAssessments.stream()
                .map(DemandBasedAssessment::getAssessmentNumber).collect(Collectors.toSet());

        PropertyCriteria criteria = new PropertyCriteria();
        criteria.setTenantId(demandBasedAssessments.get(0).getTenantId());
        criteria.setPropertyDetailids(assessmentNumbers);

        return criteria;
    }


    /**
     * Sets financialYear and source
     *
     * @param properties
     * @param financialYear
     */
    private void setFields(List<Property> properties, String financialYear) {
        properties.forEach(property -> {
            property.getPropertyDetails().get(0).setFinancialYear(financialYear);
            property.getPropertyDetails().get(0).setSource(PropertyDetail.SourceEnum.SYSTEM);
            property.getPropertyDetails().get(0).setAdhocExemption(null);
            property.getPropertyDetails().get(0).setAdhocPenalty(null);
        });
    }


    /**
     * Creates a map of tenantId to DemandBasedAssessment
     *
     * @param request The update request
     * @return Map of tenantId to DemandBasedAssessment
     */
    private Map<String, List<DemandBasedAssessment>> groupByTenantId(DemandBasedAssessmentRequest request) {
        Map<String, List<DemandBasedAssessment>> tenantIdToDemandBasedAssessmentMap = new HashMap<>();

        request.getDemandBasedAssessments().forEach(demandBasedAssessment -> {
            if (tenantIdToDemandBasedAssessmentMap.containsKey(demandBasedAssessment.getTenantId()))
                tenantIdToDemandBasedAssessmentMap.get(demandBasedAssessment.getTenantId()).add(demandBasedAssessment);
            else {
                LinkedList<DemandBasedAssessment> demandBasedAssessments = new LinkedList<>();
                demandBasedAssessments.add(demandBasedAssessment);
                tenantIdToDemandBasedAssessmentMap.put(demandBasedAssessment.getTenantId(), demandBasedAssessments);
            }
        });

        return tenantIdToDemandBasedAssessmentMap;
    }


}
