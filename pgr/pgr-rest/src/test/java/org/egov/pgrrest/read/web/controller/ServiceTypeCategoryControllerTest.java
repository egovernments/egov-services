package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.common.persistence.entity.ServiceTypeCategory;
import org.egov.pgrrest.read.domain.service.ServiceTypeCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceTypeCategoryController.class)
@Import(org.egov.pgrrest.TestConfiguration.class)
public class ServiceTypeCategoryControllerTest {

    @MockBean
    private ServiceTypeCategoryService serviceTypeCategoryService;

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @Test
    public void test_should_return_service_type_categories() throws Exception {
        when(serviceTypeCategoryService.getAll("ap.public")).thenReturn(getComplaintTypeCategoryEntity());
        mockMvc.perform(post("/servicecategories/v1/_search")
            .param("tenantId", "ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("serviceTypeCategoryResponse.json")));
    }

    private List<ServiceTypeCategory> getComplaintTypeCategoryEntity() {
        List<ServiceTypeCategory> complaintTypeCategoryList = new ArrayList<ServiceTypeCategory>();
        ServiceTypeCategory complaintTypeCategory = new ServiceTypeCategory();
        complaintTypeCategory.setName("Administration");
        complaintTypeCategory.setDescription("Administration");
        complaintTypeCategory.setTenantId("ap.public");

        complaintTypeCategoryList.add(complaintTypeCategory);
        return complaintTypeCategoryList;

    }
}