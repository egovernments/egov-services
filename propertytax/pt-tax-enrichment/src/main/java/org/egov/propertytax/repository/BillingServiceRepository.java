package org.egov.propertytax.repository;

import org.egov.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class BillingServiceRepository {

    private static final Logger logger = LoggerFactory.getLogger(BillingServiceRepository.class);

    @Autowired
    Environment environment;
    @Autowired
    private RestTemplate restTemplate;

    public List<Demand> prepareDemand(List<TaxCalculation> taxCalculationList, Property property) {
        List<Demand> demandList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date fromDate;
        Date toDate;
        CommonTaxDetails taxDetails;
        Demand demand;
        DemandDetail demandDetail;
        String tenantId = property.getTenantId();
        String propertyType = property.getPropertyDetail().getPropertyType();
        List<DemandDetail> demandDetailsList;
        for (TaxCalculation taxCalculation : taxCalculationList) {
            taxDetails = taxCalculation.getPropertyTaxes();
            demand = new Demand();
            demand.setTenantId(tenantId);
            demand.setBusinessService("Property Tax");
            demand.setConsumerType(propertyType);
            demand.setConsumerCode(property.getPropertyDetail().getApplicationNo());
            demand.setMinimumAmountPayable(BigDecimal.ONE);
            demandDetailsList = new ArrayList<>();
            for (HeadWiseTax taxes : taxDetails.getHeadWiseTaxes()) {
                demandDetail = new DemandDetail();
                demandDetail.setTaxHeadMasterCode(taxes.getTaxName());
                demandDetail.setTaxAmount(BigDecimal.valueOf(taxes.getTaxValue()));
                demandDetail.setTenantId(tenantId);
                demandDetailsList.add(demandDetail);
            }
            demand.setDemandDetails(demandDetailsList);
            try {
                logger.info("TaxCalculation fromDate = " + taxCalculation.getFromDate() + " \n toDate = " + taxCalculation.getToDate());
                fromDate = sdf.parse(taxCalculation.getFromDate());
                toDate = sdf.parse(taxCalculation.getToDate());
                logger.info(" Epoch values, fromDate = " + fromDate.getTime() + " \n toDate = " + toDate.getTime());
                demand.setTaxPeriodFrom(fromDate.getTime());
                demand.setTaxPeriodTo(toDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Owner owner = new Owner();
            owner.setId(property.getOwners().get(0).getId());
            demand.setOwner(owner);
            demandList.add(demand);
        }
        return demandList;
    }

    public DemandResponse createDemand(List<Demand> demands, RequestInfo requestInfo) {
        DemandRequest demandRequest = new DemandRequest();
        demandRequest.setRequestInfo(requestInfo);
        demandRequest.setDemands(demands);

        logger.info("BillingServiceRepository createDemand(), demands --> " + demands);

        String url = environment.getProperty("egov.services.billing_service.hostname") +
                environment.getProperty("egov.services.billing_service.createdemand");
        logger.info("BillingServiceRepository createDemand(), URL - > " + url + " \n demandRequest --> " + demandRequest);

        return restTemplate.postForObject(url, demandRequest, DemandResponse.class);
    }

}
