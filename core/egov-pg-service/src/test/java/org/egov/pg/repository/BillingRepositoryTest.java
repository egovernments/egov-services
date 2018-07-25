package org.egov.pg.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pg.config.AppProperties;
import org.egov.pg.models.Bill;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@Ignore
public class BillingRepositoryTest {

    @Autowired
    org.springframework.core.env.Environment environment;
    private BillingRepository billingRepository;

    @Before
    public void setUp() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("billingServiceHost", "http://egov-micro-dev.egovernments.org/");
        env.setProperty("billingServicePath", "billing-service/bill/_search");
        AppProperties appProperties = new AppProperties(environment);

        billingRepository = new BillingRepository(new RestTemplate(), appProperties);
    }


    @Test
    public void testFetchBill() {

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("org.egov.pgr");
        requestInfo.setAuthToken("fc03f083-6152-4504-b96c-e0c12b0e8a5b");

        String billId = "1850";
        List<Bill> response = billingRepository.fetchBill(requestInfo, "default", billId);
        System.out.println(response);
    }


}
