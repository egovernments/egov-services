package org.egov.pgr.read.web.controller;

import org.egov.pgr.Resources;
import org.egov.pgr.common.entity.ComplaintType;
import org.egov.pgr.common.entity.ComplaintTypeCategory;
import org.egov.pgr.read.domain.model.ComplaintTypeSearchCriteria;
import org.egov.pgr.read.domain.service.ComplaintTypeService;
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
@WebMvcTest(ComplaintTypeController.class)
@Import(org.egov.pgr.TestConfiguration.class)
public class ComplaintTypeControllerTest {

    public static final String AP_PUBLIC = "ap.public";
    public static final String FREQUENCY = "FREQUENCY";
    public static final String CATEGORY = "CATEGORY";
    public static final String ALL = "ALL";

    @MockBean
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @Test
    public void getComplaintTypes() throws Exception {
        when(complaintTypeService.getComplaintType("BPS", AP_PUBLIC)).thenReturn(getComplaintType());

        mockMvc.perform(post("/services/BPS?tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("complaintTypeResponse.json")));
    }

    @Test
    public void getComplaintTypesWhereTypeIsFrequency() throws Exception {
        when(complaintTypeService.findByCriteria(getSearchCriteria(FREQUENCY))).thenReturn(getComplaintTypeList());

        mockMvc.perform(post("/services?type=FREQUENCY&count=2&tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("complaintTypeResponseForfrequency.json")));
    }

    @Test
    public void getAllComplaintTypes() throws Exception {
        when(complaintTypeService.findByCriteria(getSearchCriteria(ALL))).thenReturn(getAllComplaintType());

        mockMvc.perform(post("/services?type=ALL&tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("allComplaintTypeResponse.json")));
    }

    @Test
    public void getComplaintTypesByCategory() throws Exception {
        when(complaintTypeService.findByCriteria(getSearchCriteria(CATEGORY))).thenReturn(getComplaintTypeListForCategoryIdSearch());

        mockMvc.perform(post("/services?type=CATEGORY&categoryId=1&tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("complaintTypeResponse.json")));
    }

    private ComplaintType getComplaintType() {
        ComplaintType complaintType = new ComplaintType();
        complaintType.setTenantId(AP_PUBLIC);
        complaintType.setName("Building plan sanction");
        complaintType.setDescription("Building plan sanction");
        complaintType.setCode("BPS");
        complaintType.setCategory(getComplaintTypeCategory());
        return complaintType;
    }

    private ComplaintTypeCategory getComplaintTypeCategory() {
        ComplaintTypeCategory complaintTypeCategory = new ComplaintTypeCategory();
        complaintTypeCategory.setDescription("Administration");
        complaintTypeCategory.setTenantId(AP_PUBLIC);

        return complaintTypeCategory;
    }

    private ComplaintTypeSearchCriteria getSearchCriteria(String type) {
        if (type.equalsIgnoreCase(FREQUENCY))
            return ComplaintTypeSearchCriteria.builder().complaintTypeSearch(FREQUENCY).count(2).tenantId(AP_PUBLIC).build();
        else if (type.equalsIgnoreCase(CATEGORY))
            return ComplaintTypeSearchCriteria.builder().complaintTypeSearch(CATEGORY).categoryId(1L).tenantId(AP_PUBLIC).build();
        else
            return ComplaintTypeSearchCriteria.builder().complaintTypeSearch(ALL).tenantId(AP_PUBLIC).build();
    }

    private List<ComplaintType> getComplaintTypeList() {
        List<ComplaintType> complaintTypeList = new ArrayList<ComplaintType>();

        ComplaintType complaintTypeBRKNB = new ComplaintType();
        complaintTypeBRKNB.setName("Broken Bin");
        complaintTypeBRKNB.setCode("BRKNB");
        complaintTypeBRKNB.setTenantId(AP_PUBLIC);
        complaintTypeBRKNB.setCategory(getComplaintTypeCategory());

        ComplaintType complaintTypeBPS = new ComplaintType();
        complaintTypeBPS.setName("Building plan sanction");
        complaintTypeBPS.setCode("BPS");
        complaintTypeBPS.setTenantId(AP_PUBLIC);
        complaintTypeBPS.setCategory(getComplaintTypeCategory());

        complaintTypeList.add(complaintTypeBRKNB);
        complaintTypeList.add(complaintTypeBPS);

        return complaintTypeList;
    }

    private List<ComplaintType> getAllComplaintType() {
        List<ComplaintType> complaintTypeList = new ArrayList<ComplaintType>();

        ComplaintType complaintTypeBRKNB = new ComplaintType();
        complaintTypeBRKNB.setName("Broken Bin");
        complaintTypeBRKNB.setCode("BRKNB");
        complaintTypeBRKNB.setTenantId(AP_PUBLIC);
        complaintTypeBRKNB.setCategory(getComplaintTypeCategory());

        ComplaintType complaintTypeBPS = new ComplaintType();
        complaintTypeBPS.setName("Building plan sanction");
        complaintTypeBPS.setCode("BPS");
        complaintTypeBPS.setTenantId(AP_PUBLIC);
        complaintTypeBPS.setCategory(getComplaintTypeCategory());

        ComplaintType complaintTypeOOWF = new ComplaintType();
        complaintTypeOOWF.setName("Obstruction of water flow");
        complaintTypeOOWF.setCode("OOWF");
        complaintTypeOOWF.setTenantId(AP_PUBLIC);
        complaintTypeOOWF.setCategory(getComplaintTypeCategory());

        complaintTypeList.add(complaintTypeBRKNB);
        complaintTypeList.add(complaintTypeBPS);
        complaintTypeList.add(complaintTypeOOWF);

        return complaintTypeList;
    }

    private List<ComplaintType> getComplaintTypeListForCategoryIdSearch() {
        List<ComplaintType> complaintTypeList = new ArrayList<ComplaintType>();

        ComplaintType complaintTypeBPS = new ComplaintType();
        complaintTypeBPS.setId(1L);
        complaintTypeBPS.setName("Building plan sanction");
        complaintTypeBPS.setCode("BPS");
        complaintTypeBPS.setTenantId(AP_PUBLIC);
        complaintTypeBPS.setDescription("Building plan sanction");
        complaintTypeBPS.setCategory(getComplaintTypeCategory());

        complaintTypeList.add(complaintTypeBPS);

        return complaintTypeList;
    }

}
