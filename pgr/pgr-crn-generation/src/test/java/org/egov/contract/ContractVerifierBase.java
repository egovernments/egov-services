package org.egov.contract;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.egov.domain.service.CrnGeneratorService;
import org.egov.web.controller.CrnController;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ContractVerifierBase {

    @Mock
    CrnGeneratorService crnGeneratorService;

    @InjectMocks
    CrnController crnController;

    @Before
    public void setup() {
        when(crnGeneratorService.generate()).thenReturn("00056-2017-PD");
        RestAssuredMockMvc.standaloneSetup(crnController);
    }

}