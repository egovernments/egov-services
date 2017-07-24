package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.ServiceDefinitionNotFoundException;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.domain.service.ServiceDefinitionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceDefinitionController.class)
@Import(TestConfiguration.class)
public class ServiceDefinitionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @MockBean
    private ServiceDefinitionService serviceDefinitionService;

    @Test
    public void test_should_return_service_definition_for_given_service_code_and_tenant_id() throws Exception {
        final ServiceDefinitionSearchCriteria searchCriteria =
            new ServiceDefinitionSearchCriteria("serviceCode1", "tenantId");
        when(serviceDefinitionService.find(searchCriteria))
            .thenReturn(getServiceDefinitionStubResponse());

        mockMvc.perform(post("/servicedefinition/v1/_search")
            .param("serviceCode", "serviceCode1")
            .param("tenantId", "tenantId")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("serviceDefinitionRetrievalRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("serviceDefinitionRetrievalResponse.json")));
    }

    @Test
    public void test_should_return_error_response_when_service_definition_not_found_for_given_service_code_and_tenant_id()
        throws Exception {
        final ServiceDefinitionSearchCriteria searchCriteria =
            new ServiceDefinitionSearchCriteria("serviceCode1", "tenantId");
        when(serviceDefinitionService.find(searchCriteria)).thenThrow(new ServiceDefinitionNotFoundException());

        mockMvc.perform(post("/servicedefinition/v1/_search")
            .param("serviceCode", "serviceCode1")
            .param("tenantId", "tenantId")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("serviceDefinitionRetrievalRequest.json")))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(resources.getFileContents("serviceDefinitionRetrievalErrorResponse.json")));
    }

    private ServiceDefinition getServiceDefinitionStubResponse() {
        return ServiceDefinition.builder()
            .tenantId("tenantId")
            .code("serviceCode1")
            .attributes(Collections.singletonList(getAttribute()))
            .build();
    }

    private AttributeDefinition getAttribute() {
        return AttributeDefinition.builder()
            .readOnly(false)
            .code("attribute1")
            .dataType(AttributeDataType.DATE)
            .roles(Collections.singletonList(new AttributeRolesDefinition("EMPLOYEE")))
            .actions(Collections.singletonList(new AttributeActionsDefinition(ServiceStatus.COMPLAINT_PROCESSING)))
            .required(true)
            .dataTypeDescription("data type description1")
            .values(getValues())
            .build();
    }

    private List<ValueDefinition> getValues() {
        return asList(new ValueDefinition("name1", "key1", true),
            new ValueDefinition("name2", "key2", false));
    }
}