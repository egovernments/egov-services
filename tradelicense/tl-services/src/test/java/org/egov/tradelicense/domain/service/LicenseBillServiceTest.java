package org.egov.tradelicense.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.model.LicenseApplication;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.TradeLicense;
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
    public void test_create_bill() throws ParseException {
        RequestInfo requestInfo = new RequestInfo();
        TradeLicense license = new TradeLicense();
        license.setTenantId("mh.roha");
        license.setTradeType(BusinessNature.PERMANENT);
        LicenseApplication application = new LicenseApplication();
        application.setApplicationNumber("1234");
        application.setLicenseFee(100D);
        LicenseFeeDetail feeDetail = new LicenseFeeDetail();
        feeDetail.setAmount(100D);
        application.setFeeDetails(Collections.singletonList(feeDetail));
        license.setApplication(application);
        license.setValidityYears(1L);
        
        DemandResponse demandResponse = licenseBillService.createBill(license, requestInfo);
        assertEquals("1", demandResponse.getDemands().get(0).getId());
    }

}
