package org.egov.tlcalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.config.TLCalculatorConfigs;
import org.egov.tlcalculator.repository.ServiceRequestRepository;
import org.egov.tlcalculator.utils.CalculationUtils;
import org.egov.tlcalculator.web.models.*;
import org.egov.tlcalculator.web.models.TL.OwnerInfo;
import org.egov.tlcalculator.web.models.TL.TradeLicense;
import org.egov.tlcalculator.web.models.demand.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
public class DemandService {



    @Autowired
    private CalculationUtils utils;

    @Autowired
    private TLCalculatorConfigs config;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ObjectMapper mapper;




    public List<Demand> generateDemand(RequestInfo requestInfo,CalculationRes response){
        List<Calculation> calculations = response.getCalculation();
        List<Demand> demands = new LinkedList<>();

        if(!CollectionUtils.isEmpty(calculations)){
            calculations.forEach(calculation -> {
                    demands.add(createDemand(requestInfo,calculation));
            });
         StringBuilder url = new StringBuilder(config.getBillingHost());
         url.append(config.getDemandCreateEndpoint());
         DemandRequest request = new DemandRequest(requestInfo,demands);
         serviceRequestRepository.fetchResult(url,request);
        }
        return demands;
    }


    private Demand createDemand(RequestInfo requestInfo,Calculation calculation){

        TradeLicense license = utils.getTradeLicense(requestInfo,calculation.getApplicationNumber()
                ,calculation.getTenantId());

        if(license==null)
            throw new CustomException("INVALID APPLICATIONNUMBER","Demand cannot be generated for applicationNumber " +
                    calculation.getApplicationNumber()+" TradeLicense with this number does not exist ");

        String tenantId = calculation.getTenantId();
        String consumerCode = calculation.getApplicationNumber();

        OwnerInfo owner = license.getTradeLicenseDetail().getOwners().get(0);

        List<DemandDetail> demandDetails = new LinkedList<>();

        calculation.getTaxHeadEstimates().forEach(taxHeadEstimate -> {
            demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
                    .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode())
                    .tenantId(tenantId)
                    .build());
        });

        return Demand.builder()
                .consumerCode(consumerCode)
                .owner(owner)
                .minimumAmountPayable(config.getMinimumPayableAmount())
                .tenantId(tenantId)
                .taxPeriodFrom(license.getValidFrom())
                .taxPeriodTo(license.getValidTo())
                .businessService(config.getBusinessService())
                .build();
    }


    private Demand updateDemand(RequestInfo requestInfo,Calculation calculation,String consumerCode){

        List<Demand> demands = searchDemand(calculation.getTenantId(),consumerCode,requestInfo);

        if(CollectionUtils.isEmpty(demands))
            throw new CustomException("INVALID CONSUMERCODE","No demand exists for the given consumerCode");

        Demand demand = demands.get(0);
        List<DemandDetail> demandDetails = demand.getDemandDetails();

        Map<String,DemandDetail> taxHeadToDemandDetail = new HashMap<>();

        demandDetails.forEach(demandDetail -> {
            taxHeadToDemandDetail.put(demandDetail.getTaxHeadMasterCode(),demandDetail);
        });

        calculation.getTaxHeadEstimates().forEach(taxHeadEstimate -> {
            if(taxHeadToDemandDetail.containsKey(taxHeadEstimate.getTaxHeadCode()))
                taxHeadToDemandDetail.get(taxHeadEstimate.getTaxHeadCode()).setTaxAmount(taxHeadEstimate.getEstimateAmount());
            else
                taxHeadToDemandDetail.put(taxHeadEstimate.getTaxHeadCode(),
                        DemandDetail.builder()
                        .taxAmount(taxHeadEstimate.getEstimateAmount())
                        .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode())
                        .tenantId(calculation.getTenantId())
                        .build());
        });

        demand.setDemandDetails(new LinkedList<>(taxHeadToDemandDetail.values()));

        StringBuilder url = new StringBuilder(config.getBillingHost());
        url.append(config.getDemandUpdateEndpoint());
        DemandRequest request = new DemandRequest(requestInfo,Collections.singletonList(demand));
        serviceRequestRepository.fetchResult(url,request);

        return demand;
    }


    private List<Demand> searchDemand(String tenantId,String consumerCode,RequestInfo requestInfo){
        String uri = utils.getDemandSearchURL();
        uri = uri.replace("{1}",tenantId);
        uri = uri.replace("{2}",config.getBusinessService());
        uri = uri.replace("{3}",consumerCode);

        Object result = serviceRequestRepository.fetchResult(new StringBuilder(uri),RequestInfoWrapper.builder()
                                                      .requestInfo(requestInfo).build());

        DemandResponse response = null;
        try {
             response = mapper.convertValue(result,DemandResponse.class);
        }
        catch (IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Failed to parse response from Demand Search");
        }

        if(CollectionUtils.isEmpty(response.getDemands()))
            return null;

        else return response.getDemands();

    }



/*    public BillResponse generateBill(RequestInfo requestInfo,GenerateBillCriteria billCriteria){

        String consumerCode = billCriteria.getConsumerCode();
        String tenantId = billCriteria.getTenantId();

        List<Demand> demands = searchDemand(tenantId,consumerCode,requestInfo);

        if(CollectionUtils.isEmpty(demands))
            throw new CustomException("INVALID CONSUMERCODE","Bill cannot be generated.No demand exists for the given consumerCode");




    }*/











}
