package org.egov.tradelicense.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.LicenseFeeDetailContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.web.contract.Demand;
import org.egov.tradelicense.web.contract.DemandRequest;
import org.egov.tradelicense.web.contract.DemandResponse;
import org.egov.tradelicense.web.contract.FinancialYearContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class LicenseBillServiceTest {

    @Mock
    private PropertiesManager propertiesManager;

    @Mock
    private FinancialYearService financialYearService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LicenseBillService licenseBillService = new LicenseBillService();

    FinancialYearContract currentFYResponse;

    @Before
    public void before() {
        DemandResponse demandResponse = new DemandResponse();
        List<Demand> demands = new ArrayList<>();
        Demand demand = new Demand();
        demand.setId("1");
        demands.add(demand);
        demandResponse.setDemands(demands);
        currentFYResponse = new FinancialYearContract();
        currentFYResponse.setStartingDate(new Date());
        currentFYResponse.setEndingDate(new Date());
        when(propertiesManager.getBillBusinessService()).thenReturn("TradeLicense");
        when(propertiesManager.getTaxHeadMasterCode()).thenReturn("1401101");
        when(financialYearService
                .findFinancialYearIdByDate(Mockito.anyString(), Mockito.anyLong(), Mockito.any(RequestInfoWrapper.class)))
                        .thenReturn(currentFYResponse);
        when(propertiesManager.getBillingServiceHostName()).thenReturn("http://egov-micro-dev.egovernments.org");
        when(propertiesManager.getBillingServiceCreatedBill()).thenReturn("/billing-service/demand/_create");
        when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(DemandRequest.class), Mockito.any()))
                .thenReturn(demandResponse);
    }
    
    @Test
    public void test_create_bill() {
        TradeLicenseRequest tradeLicenseRequest = new TradeLicenseRequest();
        RequestInfo requestInfo = new RequestInfo();
        tradeLicenseRequest.setRequestInfo(requestInfo);
        TradeLicenseContract license = new TradeLicenseContract();
        license.setTenantId("mh.roha");
        license.setTradeType(BusinessNatureEnum.PERMANENT);
        LicenseFeeDetailContract feeDetail = new LicenseFeeDetailContract();
        feeDetail.setAmount(100D);
        license.setFeeDetails(Collections.singletonList(feeDetail));
        license.setValidityYears(1L);
        tradeLicenseRequest.setLicenses(Collections.singletonList(license));
        
        DemandResponse demandResponse = licenseBillService.createBill(tradeLicenseRequest);
        assertEquals("1", demandResponse.getDemands().get(0).getId());
    }

}
