package org.egov.tlcalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.config.TLCalculatorConfigs;
import org.egov.tlcalculator.repository.CalculationRepository;
import org.egov.tlcalculator.repository.DemandRepository;
import org.egov.tlcalculator.repository.ServiceRequestRepository;
import org.egov.tlcalculator.repository.builder.CalculationQueryBuilder;
import org.egov.tlcalculator.utils.CalculationUtils;
import org.egov.tlcalculator.utils.TLCalculatorConstants;
import org.egov.tlcalculator.web.models.*;
import org.egov.tlcalculator.web.models.tradelicense.OwnerInfo;
import org.egov.tlcalculator.web.models.tradelicense.TradeLicense;
import org.egov.tlcalculator.web.models.demand.*;
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

    @Autowired
    private MDMSService mdmsService;

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private CalculationQueryBuilder calculationQueryBuilder;


    /**
     * Creates or updates Demand
     * @param requestInfo The RequestInfo of the calculation request
     * @param calculations The Calculation Objects for which demand has to be generated or updated
     */
    public void generateDemand(RequestInfo requestInfo,List<Calculation> calculations,Object mdmsData){

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
            createDemand(requestInfo,createCalculations,mdmsData);

        if(!CollectionUtils.isEmpty(updateCalculations))
            updateDemand(requestInfo,updateCalculations);
    }


    /**
     * Generates bill
     * @param requestInfo The RequestInfo of the calculation request
     * @param billCriteria The criteria for bill generation
     * @return The generate bill response along with ids of slab used for calculation
     */
    public BillAndCalculations getBill(RequestInfo requestInfo, GenerateBillCriteria billCriteria){
        BillResponse billResponse = generateBill(requestInfo,billCriteria);
        BillingSlabIds billingSlabIds = getBillingSlabIds(billCriteria);
        BillAndCalculations getBillResponse = new BillAndCalculations();
        getBillResponse.setBillingSlabIds(billingSlabIds);
        getBillResponse.setBillResponse(billResponse);
        return getBillResponse;
    }


    /**
     * Gets the billingSlabs from the db
     * @param billCriteria The criteria on which bill has to be generated
     * @return The billingSlabIds used for calculation
     */
    private BillingSlabIds getBillingSlabIds(GenerateBillCriteria billCriteria){
        List<Object> preparedStmtList = new ArrayList<>();
        CalculationSearchCriteria criteria = new CalculationSearchCriteria();
        criteria.setTenantId(billCriteria.getTenantId());
        criteria.setAplicationNumber(billCriteria.getConsumerCode());

        String query = calculationQueryBuilder.getSearchQuery(criteria,preparedStmtList);
        BillingSlabIds billingSlabIds = calculationRepository.getDataFromDB(query,preparedStmtList);
        return billingSlabIds;
    }


    /**
     * Creates demand for the given list of calculations
     * @param requestInfo The RequestInfo of the calculation request
     * @param calculations List of calculation object
     * @return Demands that are created
     */
    private List<Demand> createDemand(RequestInfo requestInfo,List<Calculation> calculations,Object mdmsData){
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

             Map<String,Long> taxPeriods = mdmsService.getTaxPeriods(requestInfo,license,mdmsData);

            demands.add(Demand.builder()
                    .consumerCode(consumerCode)
                    .demandDetails(demandDetails)
                    .owner(owner)
                    .minimumAmountPayable(config.getMinimumPayableAmount())
                    .tenantId(tenantId)
                    .taxPeriodFrom(taxPeriods.get(TLCalculatorConstants.MDMS_STARTDATE))
                    .taxPeriodTo(taxPeriods.get(TLCalculatorConstants.MDMS_ENDDATE))
                    .consumerType("tradelicense")
                    .businessService(config.getBusinessService())
                    .build());
        }
        return demandRepository.saveDemand(requestInfo,demands);
    }



    /**
     * Updates demand for the given list of calculations
     * @param requestInfo The RequestInfo of the calculation request
     * @param calculations List of calculation object
     * @return Demands that are updated
     */
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
                                    .collectionAmount(BigDecimal.ZERO)
                                    .build());
            });
            demand.setDemandDetails(new LinkedList<>(taxHeadToDemandDetail.values()));

            demands.add(demand);
        }
         return demandRepository.updateDemand(requestInfo,demands);
    }


    /**
     * Searches demand for the given consumerCode and tenantIDd
     * @param tenantId The tenantId of the tradeLicense
     * @param consumerCode The consumerCode of the demand
     * @param requestInfo The RequestInfo of the incoming request
     * @return Lis to demands for the given consumerCode
     */
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


    /**
     * Generates bill by calling BillingService
     * @param requestInfo The RequestInfo of the getBill request
     * @param billCriteria The criteria for bill generation
     * @return The response of the bill generate
     */
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
