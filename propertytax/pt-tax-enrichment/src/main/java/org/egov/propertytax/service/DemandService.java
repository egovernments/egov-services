package org.egov.propertytax.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.models.*;
import org.egov.propertytax.repository.BillingServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DemandService {

    private static final Logger logger = LoggerFactory.getLogger(DemandService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BillingServiceRepository billingServiceRepository;

    public DemandResponse createDemand(PropertyRequest propertyRequest) {
        List<TaxCalculation> calculationList = new ArrayList<>();
        Property property = propertyRequest.getProperties().get(0);
        TypeReference<List<TaxCalculation>> typeReference = new TypeReference<List<TaxCalculation>>() {
        };
        try {
            calculationList = objectMapper.readValue(property.getPropertyDetail().getTaxCalculations(), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("pt-tax-enrichment DemandService calculationList --> "+calculationList);
        List<Demand> demands = billingServiceRepository.prepareDemand(calculationList, property);
        DemandResponse demandResponse = billingServiceRepository.createDemand(demands, propertyRequest.getRequestInfo());

        property.setDemands(demandResponse.getDemands());
        return demandResponse;
    }
}
