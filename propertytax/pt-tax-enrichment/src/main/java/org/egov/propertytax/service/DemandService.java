package org.egov.propertytax.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.Demand;
import org.egov.models.DemandResponse;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.TaxCalculation;
import org.egov.models.TitleTransferRequest;
import org.egov.propertytax.repository.BillingServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        logger.info("pt-tax-enrichment DemandService demand list --> "+demands);
        DemandResponse demandResponse = billingServiceRepository.createDemand(demands, propertyRequest.getRequestInfo());
        logger.info("pt-tax-enrichment DemandService demandResponse --> "+demandResponse);
        property.setDemands(demandResponse.getDemands());
        return demandResponse;
    }
    
    /**
     * creating demands for titletransfer
     * @param titleTransferRequest
     * @return
     * @throws Exception
     */
    public DemandResponse createDemandForTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception {
		List<Demand> demands = billingServiceRepository.prepareDemandForTitleTransfer(titleTransferRequest);
		logger.info("pt-tax-enrichment DemandService demand list --> " + demands);
		DemandResponse demandResponse = billingServiceRepository.createDemand(demands,
				titleTransferRequest.getRequestInfo());
		logger.info("pt-tax-enrichment DemandService demandResponse --> " + demandResponse);

		return demandResponse;
	}
    
    public  void updateDemandForTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception{
    	billingServiceRepository.updateDemandForTittleTransfer(titleTransferRequest);
    }

	public DemandResponse updateDemand(PropertyRequest propertyRequest,Boolean isModify) throws Exception {

		List<TaxCalculation> calculationList = new ArrayList<>();
		Property property = propertyRequest.getProperties().get(0);
		TypeReference<List<TaxCalculation>> typeReference = new TypeReference<List<TaxCalculation>>() {
		};
		try {
			calculationList = objectMapper.readValue(property.getPropertyDetail().getTaxCalculations(), typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("pt-tax-enrichment DemandService calculationList --> " + calculationList);
		return billingServiceRepository.updateDemand(calculationList, property, propertyRequest.getRequestInfo(),isModify);

	}

}
