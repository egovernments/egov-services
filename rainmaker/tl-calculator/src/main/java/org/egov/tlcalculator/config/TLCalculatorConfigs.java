package org.egov.tlcalculator.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@Data
public class TLCalculatorConfigs {



    @Value("${egov.billingservice.host}")
    private String billingHost;

    @Value("${egov.taxhead.search.endpoint}")
    private String taxHeadSearchEndpoint;

    @Value("${egov.taxperiod.search.endpoint}")
    private String taxPeriodSearchEndpoint;

    @Value("${egov.demand.create.endpoint}")
    private String demandCreateEndpoint;

    @Value("${egov.demand.update.endpoint}")
    private String demandUpdateEndpoint;

    @Value("${egov.demand.search.endpoint}")
    private String demandSearchEndpoint;

    @Value("${egov.bill.gen.endpoint}")
    private String billGenerateEndpoint;

    @Value("${egov.demand.minimum.payable.amount}")
    private BigDecimal minimumPayableAmount;

    @Value("${egov.demand.businessservice}")
    private String businessService;



    //TL Registry
    @Value("${egov.tradelicense.host}")
    private String tradeLicenseHost;

    @Value("${egov.tradelicense.context.path}")
    private String tradeLicenseContextPath;

    @Value("${egov.tradelicense.create.endpoint}")
    private String tradeLicenseCreateEndpoint;

    @Value("${egov.tradelicense.update.endpoint}")
    private String tradeLicenseUpdateEndpoint;

    @Value("${egov.tradelicense.search.endpoint}")
    private String tradeLicenseSearchEndpoint;


    //TaxHeads
    @Value("${egov.taxhead.basetax}")
    private String baseTaxHead;


}
