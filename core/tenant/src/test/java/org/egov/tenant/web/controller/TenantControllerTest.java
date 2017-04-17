package org.egov.tenant.web.controller;

import org.egov.tenant.Resources;
import org.egov.tenant.domain.model.Tenant;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.domain.model.TenantType;
import org.egov.tenant.domain.service.TenantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TenantController.class)
public class TenantControllerTest {

    @MockBean
    TenantService tenantService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void test_should_search_tenants() throws Exception {
        List<String> codes = asList("AP.KURNOOL", "AP.GUNTOOR");
        TenantSearchCriteria tenantSearchCriteria = new TenantSearchCriteria(codes);
        List<Tenant> tenants = getListOfTenants();
        when(tenantService.find(tenantSearchCriteria)).thenReturn(tenants);

        mockMvc.perform(post("/v1/tenant/_search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Resources().getFileContents("tenantSearchRequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new Resources().getFileContents("tenantSearchResponse.json")));
    }

    private List<Tenant> getListOfTenants() {
        return asList(
                Tenant.builder()
                        .id(1L)
                        .code("AP.KURNOOL")
                        .description("description")
                        .logoId("logoId")
                        .imageId("imageId")
                        .domainUrl("domainUrl")
                        .type(TenantType.CITY)
                        .build(),

                Tenant.builder()
                        .id(2L)
                        .code("AP.GUNTOOR")
                        .description("description")
                        .logoId("logoId")
                        .imageId("imageId")
                        .domainUrl("domainUrl")
                        .type(TenantType.CITY)
                        .build()
        );
    }
}