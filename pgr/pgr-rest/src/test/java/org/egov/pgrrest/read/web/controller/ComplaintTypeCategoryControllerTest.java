package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.read.domain.service.ComplaintTypeCategoryService;
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
@WebMvcTest(ComplaintTypeCategoryController.class)
@Import(org.egov.pgrrest.TestConfiguration.class)
public class ComplaintTypeCategoryControllerTest {

    @MockBean
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @Test
    public void getComplaintTypeCategory() throws Exception {
        when(complaintTypeCategoryService.getAll("ap.public")).thenReturn(getComplaintTypeCategoryEntity());
        mockMvc.perform(post("/complaintTypeCategories?tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("complaintTypeCategoryResponse.json")));
    }

    private List<org.egov.pgrrest.common.entity.ComplaintTypeCategory> getComplaintTypeCategoryEntity() {
        List<org.egov.pgrrest.common.entity.ComplaintTypeCategory> complaintTypeCategoryList = new ArrayList<org.egov.pgrrest.common.entity.ComplaintTypeCategory>();
        org.egov.pgrrest.common.entity.ComplaintTypeCategory complaintTypeCategory = new org.egov.pgrrest.common.entity.ComplaintTypeCategory();
        complaintTypeCategory.setName("Administration");
        complaintTypeCategory.setDescription("Administration");
        complaintTypeCategory.setTenantId("ap.public");

        complaintTypeCategoryList.add(complaintTypeCategory);
        return complaintTypeCategoryList;

    }
}