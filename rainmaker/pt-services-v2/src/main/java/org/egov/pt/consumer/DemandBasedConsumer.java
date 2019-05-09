package org.egov.pt.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.service.PropertyService;
import org.egov.pt.web.models.*;
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

    @KafkaListener(topics = {"${persister.demand.based.topic}"},containerFactory = "kafkaListenerContainerFactory")
    public void listen(final HashMap<String, Object> record) {

        DemandBasedAssessmentRequest demandBasedAssessmentRequest = null;
        try {
            demandBasedAssessmentRequest = mapper.convertValue(record, DemandBasedAssessmentRequest.class);
        } catch (final Exception e) {
            log.error("Error while listening to value: " + record);
        }

        RequestInfo requestInfo = demandBasedAssessmentRequest.getRequestInfo();

        Map<String,List<DemandBasedAssessment>> tenantIdToDemandBasedAssessmentMap = groupByTenantId(demandBasedAssessmentRequest);

        for(Map.Entry<String,List<DemandBasedAssessment>> entry : tenantIdToDemandBasedAssessmentMap.entrySet()){
            createAssessment(requestInfo,entry.getValue());
        }
    }


    private void createAssessment(RequestInfo requestInfo,List<DemandBasedAssessment> demandBasedAssessments){
        String financialYear = demandBasedAssessments.get(0).getFinancialYear();
        PropertyCriteria criteria = getSearchCriteria(demandBasedAssessments);
        List<Property> properties = propertyService.getPropertiesWithOwnerInfo(criteria,requestInfo);
        setFinancialYear(properties,financialYear);
        propertyService.updateProperty(new PropertyRequest(requestInfo,properties));
    }


    private PropertyCriteria getSearchCriteria(List<DemandBasedAssessment> demandBasedAssessments){

        Set<String> assessmentNumbers = demandBasedAssessments.stream()
                .map(DemandBasedAssessment::getAssessmentNumber).collect(Collectors.toSet());

        PropertyCriteria criteria = new PropertyCriteria();
        criteria.setTenantId(demandBasedAssessments.get(0).getTenantId());
        criteria.setPropertyDetailids(assessmentNumbers);

        return criteria;
    }



    private void setFinancialYear(List<Property> properties,String financialYear){
        properties.forEach(property -> {
            property.getPropertyDetails().get(0).setFinancialYear(financialYear);
        });
    }


    private Map<String,List<DemandBasedAssessment>> groupByTenantId(DemandBasedAssessmentRequest request){
        Map<String,List<DemandBasedAssessment>> tenantIdToDemandBasedAssessmentMap = new HashMap<>();

        request.getDemandBasedAssessments().forEach(demandBasedAssessment -> {
            if(tenantIdToDemandBasedAssessmentMap.containsKey(demandBasedAssessment.getTenantId()))
                tenantIdToDemandBasedAssessmentMap.get(demandBasedAssessment.getTenantId()).add(demandBasedAssessment);
            else {
                LinkedList<DemandBasedAssessment> demandBasedAssessments = new LinkedList<>();
                demandBasedAssessments.add(demandBasedAssessment);
                tenantIdToDemandBasedAssessmentMap.put(demandBasedAssessment.getTenantId(),demandBasedAssessments);
            }
        });

        return tenantIdToDemandBasedAssessmentMap;
    }




}
