package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.common.persistence.entity.ServiceType;
import org.egov.pgrrest.common.persistence.entity.ServiceTypeCategory;
import org.egov.pgrrest.read.domain.model.ServiceTypeSearchCriteria;
import org.egov.pgrrest.read.domain.service.ServiceRequestTypeService;
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
@WebMvcTest(ServiceTypeController.class)
@Import(org.egov.pgrrest.TestConfiguration.class)
public class ServiceTypeControllerTest {

    public static final String AP_PUBLIC = "ap.public";
    public static final String FREQUENCY = "FREQUENCY";
    public static final String CATEGORY = "CATEGORY";
    public static final String ALL = "ALL";

    @MockBean
    private ServiceRequestTypeService serviceRequestTypeService;

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @Test
    public void getComplaintTypes() throws Exception {
        when(serviceRequestTypeService.getComplaintType("BPS", AP_PUBLIC)).thenReturn(getComplaintType());

        mockMvc.perform(post("/services/v1/BPS/_search?tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("complaintTypeResponse.json")));
    }

    @Test
    public void getComplaintTypesWhereTypeIsFrequency() throws Exception {
        when(serviceRequestTypeService.findByCriteria(getSearchCriteria(FREQUENCY))).thenReturn(getComplaintTypeList());

        mockMvc.perform(post("/services/v1/_search?type=FREQUENCY&count=2&tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("complaintTypeResponseForfrequency.json")));
    }

    @Test
    public void getAllComplaintTypes() throws Exception {
        when(serviceRequestTypeService.findByCriteria(getSearchCriteria(ALL))).thenReturn(getAllComplaintType());

        mockMvc.perform(post("/services/v1/_search?type=ALL&tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("allComplaintTypeResponse.json")));
    }

    @Test
    public void getComplaintTypesByCategory() throws Exception {
        when(serviceRequestTypeService.findByCriteria(getSearchCriteria(CATEGORY))).thenReturn(getComplaintTypeListForCategoryIdSearch());

        mockMvc.perform(post("/services/v1/_search?type=CATEGORY&categoryId=1&tenantId=ap.public")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("complaintTypeResponse.json")));
    }

    private ServiceType getComplaintType() {
        ServiceType complaintType = new ServiceType();
        complaintType.setTenantId(AP_PUBLIC);
        complaintType.setName("Building plan sanction");
        complaintType.setDescription("Building plan sanction");
        complaintType.setCode("BPS");
        complaintType.setCategory(getComplaintTypeCategory());
        return complaintType;
    }

    private ServiceTypeCategory getComplaintTypeCategory() {
        ServiceTypeCategory complaintTypeCategory = new ServiceTypeCategory();
        complaintTypeCategory.setDescription("Administration");
        complaintTypeCategory.setTenantId(AP_PUBLIC);

        return complaintTypeCategory;
    }

    private ServiceTypeSearchCriteria getSearchCriteria(String type) {
        if (type.equalsIgnoreCase(FREQUENCY))
            return ServiceTypeSearchCriteria.builder().complaintTypeSearch(FREQUENCY).count(2).tenantId(AP_PUBLIC).build();
        else if (type.equalsIgnoreCase(CATEGORY))
            return ServiceTypeSearchCriteria.builder().complaintTypeSearch(CATEGORY).categoryId(1L).tenantId(AP_PUBLIC).build();
        else
            return ServiceTypeSearchCriteria.builder().complaintTypeSearch(ALL).tenantId(AP_PUBLIC).build();
    }

    private List<ServiceType> getComplaintTypeList() {
        List<ServiceType> complaintTypeList = new ArrayList<ServiceType>();

        ServiceType complaintTypeBRKNB = new ServiceType();
        complaintTypeBRKNB.setName("Broken Bin");
        complaintTypeBRKNB.setCode("BRKNB");
        complaintTypeBRKNB.setTenantId(AP_PUBLIC);
        complaintTypeBRKNB.setCategory(getComplaintTypeCategory());

        ServiceType complaintTypeBPS = new ServiceType();
        complaintTypeBPS.setName("Building plan sanction");
        complaintTypeBPS.setCode("BPS");
        complaintTypeBPS.setTenantId(AP_PUBLIC);
        complaintTypeBPS.setCategory(getComplaintTypeCategory());

        complaintTypeList.add(complaintTypeBRKNB);
        complaintTypeList.add(complaintTypeBPS);

        return complaintTypeList;
    }

    private List<ServiceType> getAllComplaintType() {
        List<ServiceType> complaintTypeList = new ArrayList<ServiceType>();

        ServiceType complaintTypeBRKNB = new ServiceType();
        complaintTypeBRKNB.setName("Broken Bin");
        complaintTypeBRKNB.setCode("BRKNB");
        complaintTypeBRKNB.setTenantId(AP_PUBLIC);
        complaintTypeBRKNB.setCategory(getComplaintTypeCategory());

        ServiceType complaintTypeBPS = new ServiceType();
        complaintTypeBPS.setName("Building plan sanction");
        complaintTypeBPS.setCode("BPS");
        complaintTypeBPS.setTenantId(AP_PUBLIC);
        complaintTypeBPS.setCategory(getComplaintTypeCategory());

        ServiceType complaintTypeOOWF = new ServiceType();
        complaintTypeOOWF.setName("Obstruction of water flow");
        complaintTypeOOWF.setCode("OOWF");
        complaintTypeOOWF.setTenantId(AP_PUBLIC);
        complaintTypeOOWF.setCategory(getComplaintTypeCategory());

        complaintTypeList.add(complaintTypeBRKNB);
        complaintTypeList.add(complaintTypeBPS);
        complaintTypeList.add(complaintTypeOOWF);

        return complaintTypeList;
    }

    private List<ServiceType> getComplaintTypeListForCategoryIdSearch() {
        List<ServiceType> complaintTypeList = new ArrayList<ServiceType>();

        ServiceType complaintTypeBPS = new ServiceType();
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
