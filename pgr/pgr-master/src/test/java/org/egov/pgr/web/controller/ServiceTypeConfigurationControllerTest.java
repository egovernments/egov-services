package org.egov.pgr.web.controller;

import org.egov.pgr.Resources;
import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.domain.service.ServiceTypeConfigurationService;
import org.egov.pgr.web.contract.ServiceTypeConfigurationResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceTypeConfigurationController.class)
public class ServiceTypeConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceTypeConfigurationService serviceTypeConfigurationService;

    @Test
    public void test_should_create_servicetype_configuration() throws Exception{

        mockMvc.perform(post("/servicetypeconfiguration/v1/_create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Resources().getFileContents("serviceTypeConfigurationRequest.json")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json(new Resources().getFileContents("serviceTypeConfigurationResponse.json")));
    }

}