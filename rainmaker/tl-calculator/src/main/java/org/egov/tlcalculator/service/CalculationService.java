package org.egov.tlcalculator.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.config.TLCalculatorConfigs;
import org.egov.tlcalculator.repository.BillingslabQueryBuilder;
import org.egov.tlcalculator.repository.BillingslabRepository;
import org.egov.tlcalculator.repository.ServiceRequestRepository;
import org.egov.tlcalculator.utils.CalculationUtils;
import org.egov.tlcalculator.web.models.*;
import org.egov.tlcalculator.web.models.tradelicense.TradeLicense;
import org.egov.tlcalculator.web.models.demand.Category;
import org.egov.tlcalculator.web.models.demand.TaxHeadEstimate;
import org.egov.tlcalculator.web.models.tradelicense.TradeUnit;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Slf4j
public class CalculationService {


    @Autowired
    private BillingslabRepository repository;

    @Autowired
    private BillingslabQueryBuilder queryBuilder;

    @Autowired
    private TLCalculatorConfigs config;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private CalculationUtils utils;



  public List<Calculation> calculate(RequestInfo requestInfo,List<CalulationCriteria> criterias){
      List<Calculation> calculations = new LinkedList<>();
      for(CalulationCriteria criteria : criterias) {
          TradeLicense license;
          if (criteria.getTradelicense()==null && criteria.getApplicationNumber() != null) {
              license = utils.getTradeLicense(requestInfo, criteria.getApplicationNumber(), criteria.getTenantId());
              criteria.setTradelicense(license);
          }
          Calculation calculation = new Calculation();
          calculation.setApplicationNumber(criteria.getApplicationNumber());
          calculation.setTenantId(criteria.getTenantId());
          calculation.setTaxHeadEstimates(getTaxHeadEstimates(criteria));
          calculations.add(calculation);
      }
      return calculations;
  }


  public List<Calculation> dummyCalculate(RequestInfo requestInfo,List<CalulationCriteria> criterias){
      List<Calculation> calculations = new LinkedList<>();

      for(CalulationCriteria criteria : criterias){
              calculations.add(Calculation.builder()
              .applicationNumber(criteria.getApplicationNumber())
              .tenantId(criteria.getTenantId())
              .taxHeadEstimates(Collections.singletonList(TaxHeadEstimate.builder()
                      .category(Category.TAX)
                      .estimateAmount(new BigDecimal(ThreadLocalRandom.current().nextInt(100, 1000 + 1)))
                      .taxHeadCode("TL_TAX")
                      .build())
              ).build());
      }

      return calculations;
  }




    private List<TaxHeadEstimate> getTaxHeadEstimates(CalulationCriteria calulationCriteria){
      List<TaxHeadEstimate> estimates = new LinkedList<>();

      estimates.add(getBaseTax(calulationCriteria));

      return estimates;
  }



  public TaxHeadEstimate getBaseTax(CalulationCriteria calulationCriteria){
      TradeLicense license = calulationCriteria.getTradelicense();

      BillingSlabSearchCriteria searchCriteria = new BillingSlabSearchCriteria();
      searchCriteria.setTenantId(license.getTenantId());
      searchCriteria.setStructureType(license.getTradeLicenseDetail().getStructureType());
      searchCriteria.setLicenseType(license.getLicenseType().toString());

      BigDecimal tradeUnitFee = getTradeUnitFee(license,searchCriteria);
      BigDecimal accessoryFee = new BigDecimal(0);

      if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getAccessories())){
           accessoryFee = getAccessoryFee(license,searchCriteria);
      }

      TaxHeadEstimate estimate = new TaxHeadEstimate();
      estimate.setEstimateAmount(tradeUnitFee.add(accessoryFee));
      estimate.setCategory(Category.TAX);
      estimate.setTaxHeadCode(config.getBaseTaxHead());
      return estimate;
  }




  private BigDecimal getTradeUnitFee(TradeLicense license,BillingSlabSearchCriteria searchCriteria){
      BigDecimal tradeUnitTotalFee = new BigDecimal(0);
      List<TradeUnit> tradeUnits = license.getTradeLicenseDetail().getTradeUnits();

       for(TradeUnit tradeUnit : tradeUnits)
       {
          List<Object> preparedStmtList = new ArrayList<>();
          searchCriteria.setTradeType(tradeUnit.getTradeType());
          searchCriteria.setType(config.getBillingSlabRateType());
          if(tradeUnit.getUomValue()!=null)
          {
              searchCriteria.setUomValue(Double.parseDouble(tradeUnit.getUomValue()));
              searchCriteria.setUom(tradeUnit.getUom());
          }
          // Call the Search
          String query = queryBuilder.getSearchQuery(searchCriteria, preparedStmtList);
          log.info("query "+query);
          log.info("preparedStmtList "+preparedStmtList.toString());
          List<BillingSlab> billingSlabs = repository.getDataFromDB(query, preparedStmtList);

          if(billingSlabs.size()>1)
              throw new CustomException("BILLINGSLAB ERROR","Found multiple BillingSlabs for the given calculation critera");
          if(CollectionUtils.isEmpty(billingSlabs))
              throw new CustomException("BILLINGSLAB ERROR","No BillingSlab Found");

           tradeUnitTotalFee = tradeUnitTotalFee.add(new BigDecimal(Double.toString(billingSlabs.get(0).getRate())));
      }

      return tradeUnitTotalFee;
  }


  private BigDecimal getAccessoryFee(TradeLicense license,BillingSlabSearchCriteria searchCriteria){
      BigDecimal accessoryTotalFee = new BigDecimal(0);
      List<Accessory> accessories = license.getTradeLicenseDetail().getAccessories();
       for(Accessory accessory : accessories)
       {
          List<Object> preparedStmtList = new ArrayList<>();
          searchCriteria.setAccessoryCategory(accessory.getAccessoryCategory());
          if(accessory.getUomValue()!=null)
          {
              searchCriteria.setUomValue(Double.parseDouble(accessory.getUomValue()));
              searchCriteria.setUom(accessory.getUom());
          }
          // Call the Search
          String query = queryBuilder.getSearchQuery(searchCriteria, preparedStmtList);
          List<BillingSlab> billingSlabs = repository.getDataFromDB(query, preparedStmtList);

          if(billingSlabs.size()>1)
              throw new CustomException("BILLINGSLAB ERROR","Found multiple BillingSlabs for the given calculation critera");
          if(CollectionUtils.isEmpty(billingSlabs))
              throw new CustomException("BILLINGSLAB ERROR","No BillingSlab Found");

           accessoryTotalFee = accessoryTotalFee.add(new BigDecimal(Double.toString(billingSlabs.get(0).getRate())));
      }
      return accessoryTotalFee;
  }









}
