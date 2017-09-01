package org.egov.propertytax.repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.egov.models.CommonTaxDetails;
import org.egov.models.Demand;
import org.egov.models.DemandDetail;
import org.egov.models.DemandRequest;
import org.egov.models.DemandResponse;
import org.egov.models.HeadWiseTax;
import org.egov.models.Owner;
import org.egov.models.Property;
import org.egov.models.RequestInfo;
import org.egov.models.TaxCalculation;
import org.egov.propertytax.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class BillingServiceRepository {

    private static final Logger logger = LoggerFactory.getLogger(BillingServiceRepository.class);

    @Autowired
	PropertiesManager propertiesManager;
    
    @Autowired
    private RestTemplate restTemplate;

    public List<Demand> prepareDemand(List<TaxCalculation> taxCalculationList, Property property) {
        List<Demand> demandList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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
            demand.setBusinessService(propertiesManager.getDemandBusinessService());
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
                logger.info(" Dates, fromDate = "+fromDate+", toDate = "+toDate+
                        " \n Epoch values, fromDate = " + fromDate.getTime() + " \n toDate = " + toDate.getTime());
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

        String url = propertiesManager.getBillingServiceHostName() +
        		propertiesManager.getBillingServiceCreatedDemand();
        logger.info("BillingServiceRepository createDemand(), URL - > " + url + " \n demandRequest --> " + demandRequest);

        return restTemplate.postForObject(url, demandRequest, DemandResponse.class);
    }

}
