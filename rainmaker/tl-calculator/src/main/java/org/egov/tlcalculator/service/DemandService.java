package org.egov.tlcalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.config.TLCalculatorConfigs;
import org.egov.tlcalculator.repository.DemandRepository;
import org.egov.tlcalculator.repository.ServiceRequestRepository;
import org.egov.tlcalculator.utils.CalculationUtils;
import org.egov.tlcalculator.web.models.*;
import org.egov.tlcalculator.web.models.tradelicense.OwnerInfo;
import org.egov.tlcalculator.web.models.tradelicense.TradeLicense;
import org.egov.tlcalculator.web.models.demand.*;
import org.egov.tlcalculator.web.models.tradelicense.OwnerInfo;
import org.egov.tlcalculator.web.models.tradelicense.TradeLicense;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;


@Service
public class DemandService {

   @Autowired
   private CalculationService calculationService;

    @Autowired
    private CalculationUtils utils;

    @Autowired
    private TLCalculatorConfigs config;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DemandRepository demandRepository;




    public void generateDemand(RequestInfo requestInfo,CalculationRes response){
        List<Calculation> calculations = response.getCalculation();

        //List that will contain Calculation for new demands
        List<Calculation> createCalculations = new LinkedList<>();

        //List that will contain Calculation for old demands
        List<Calculation> updateCalculations = new LinkedList<>();

        if(!CollectionUtils.isEmpty(calculations)){
            for(Calculation calculation : calculations)
            {
                    if(CollectionUtils.isEmpty(searchDemand(calculation.getTenantId(),
                            calculation.getTradeLicense().getApplicationNumber(),requestInfo)))
                        createCalculations.add(calculation);
                    else
                        updateCalculations.add(calculation);
            }
        }
        if(!CollectionUtils.isEmpty(createCalculations))
            createDemand(requestInfo,createCalculations);

        if(!CollectionUtils.isEmpty(updateCalculations))
            updateDemand(requestInfo,updateCalculations);
    }

    public BillResponse getBill(RequestInfo requestInfo,GenerateBillCriteria billCriteria){
         String consumerCode = billCriteria.getConsumerCode();
         String tenantId = billCriteria.getTenantId();

         List<Demand> demands = searchDemand(tenantId,consumerCode,requestInfo);

         if(CollectionUtils.isEmpty(demands))
             throw new CustomException("INVALID CONSUMERCODE","No demand exists for this consumer code");

         //Recalculating using ConsumerCode used as applicationNumber
         CalulationCriteria calulationCriteria = new CalulationCriteria();
         calulationCriteria.setApplicationNumber(consumerCode);
         calulationCriteria.setTenantId(tenantId);
         List<Calculation> calculations = calculationService.calculate(requestInfo,Collections.singletonList(calulationCriteria));

         //Demand Updated
         updateDemand(requestInfo,calculations);

         BillResponse response = generateBill(requestInfo,billCriteria);
         return response;
    }



    private List<Demand> createDemand(RequestInfo requestInfo,List<Calculation> calculations){
        List<Demand> demands = new LinkedList<>();
        for(Calculation calculation : calculations) {
            TradeLicense license = null;

            if(calculation.getTradeLicense()!=null)
                license = calculation.getTradeLicense();

            else if(calculation.getTradeLicense()==null && calculation.getApplicationNumber()!=null)
                license = utils.getTradeLicense(requestInfo, calculation.getApplicationNumber()
                        , calculation.getTenantId());


            if (license == null)
                throw new CustomException("INVALID APPLICATIONNUMBER", "Demand cannot be generated for applicationNumber " +
                        calculation.getApplicationNumber() + " TradeLicense with this number does not exist ");

            String tenantId = calculation.getTenantId();
            String consumerCode = calculation.getTradeLicense().getApplicationNumber();

            OwnerInfo owner = license.getTradeLicenseDetail().getOwners().get(0);

            List<DemandDetail> demandDetails = new LinkedList<>();

            calculation.getTaxHeadEstimates().forEach(taxHeadEstimate -> {
                demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
                        .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode())
                        .collectionAmount(BigDecimal.ZERO)
                        .tenantId(tenantId)
                        .build());
            });
            demands.add(Demand.builder()
                    .consumerCode(consumerCode)
                    .demandDetails(demandDetails)
                    .owner(owner)
                    .minimumAmountPayable(config.getMinimumPayableAmount())
                    .tenantId(tenantId)
                    .taxPeriodFrom(license.getValidFrom())
                    .taxPeriodTo(license.getValidTo())
                    .consumerType("tradelicense")
                    .businessService(config.getBusinessService())
                    .build());
        }
        return demandRepository.saveDemand(requestInfo,demands);
    }


    private List<Demand> updateDemand(RequestInfo requestInfo,List<Calculation> calculations){
        List<Demand> demands = new LinkedList<>();
        for(Calculation calculation : calculations) {

            List<Demand> searchResult = searchDemand(calculation.getTenantId(), calculation.getTradeLicense().getApplicationNumber()
                    , requestInfo);

            Demand demand = searchResult.get(0);
            List<DemandDetail> demandDetails = demand.getDemandDetails();

            Map<String, DemandDetail> taxHeadToDemandDetail = new HashMap<>();

            demandDetails.forEach(demandDetail -> {
                taxHeadToDemandDetail.put(demandDetail.getTaxHeadMasterCode(), demandDetail);
            });

            calculation.getTaxHeadEstimates().forEach(taxHeadEstimate -> {
                if (taxHeadToDemandDetail.containsKey(taxHeadEstimate.getTaxHeadCode()))
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

            demands.add(demand);
        }
         return demandRepository.updateDemand(requestInfo,demands);
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



    private BillResponse generateBill(RequestInfo requestInfo,GenerateBillCriteria billCriteria){

        String consumerCode = billCriteria.getConsumerCode();
        String tenantId = billCriteria.getTenantId();

        List<Demand> demands = searchDemand(tenantId,consumerCode,requestInfo);

        if(CollectionUtils.isEmpty(demands))
            throw new CustomException("INVALID CONSUMERCODE","Bill cannot be generated.No demand exists for the given consumerCode");

        String uri = utils.getBillGenerateURI();
        uri = uri.replace("{1}",billCriteria.getTenantId());
        uri = uri.replace("{2}",billCriteria.getConsumerCode());
        uri = uri.replace("{3}",billCriteria.getBusinessService());

        Object result = serviceRequestRepository.fetchResult(new StringBuilder(uri),RequestInfoWrapper.builder()
                                                             .requestInfo(requestInfo).build());
        BillResponse response = null;
         try{
              response = mapper.convertValue(result,BillResponse.class);
         }
         catch (IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Unable to parse response of generate bill");
         }
         return response;
    }











}
