package org.egov.pgr.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.domain.model.ServiceGroup;
import org.egov.pgr.service.ServiceGroupService;
import org.egov.pgr.util.CommonValidation;
import org.egov.pgr.web.contract.ServiceGroupGetRequest;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceGroupController.class)
@Import(TestConfiguration.class)
public class ServiceGroupControllerTest {

    public static final String TENANT_ID = "default";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceGroupService serviceGroupService;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private RequestInfo requestInfo;

    @InjectMocks
    private ServiceGroupController serviceGroupController;

    @MockBean
    private CommonValidation commonValidation;

    @Test
    public void test_should_fetch_service_group_list() throws Exception {
        List<ServiceGroup> serviceGroupList = prepareServiceGroupList();
        ServiceGroupGetRequest serviceGroupGetReq = ServiceGroupGetRequest.builder()
                .tenantId("default")
                .build();
        when(serviceGroupService.getAllServiceGroup(serviceGroupGetReq)).thenReturn(serviceGroupList);
        ResponseInfo resInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(resInfo);
        mockMvc.perform(post("/serviceGroup/v1/_search")
                .param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("servicegroupfetchrequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("servicegroupfetchresponse.json")));

        verify(serviceGroupService, times(1)).getAllServiceGroup(any(ServiceGroupGetRequest.class));
    }

    @Test
    public void test_should_create_service_group() throws Exception {
        when(serviceGroupService.createCategory(any(String.class), any(String.class), any(ServiceGroupRequest.class))).thenReturn(prepareServiceGroup());
        ResponseInfo resInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(resInfo);
        mockMvc.perform(post("/serviceGroup/v1/_create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("servicegroupcreaterequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("servicegroupcreateresponse.json")));
    }

    @Test
    public void test_should_update_service_group() throws Exception {
        when(serviceGroupService.updateCategory(any(String.class), any(String.class), any(ServiceGroupRequest.class))).thenReturn(prepareServiceGroup());
        ResponseInfo resInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(resInfo);
        mockMvc.perform(post("/serviceGroup/v1/_update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("servicegroupcreaterequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("servicegroupcreateresponse.json")));
    }

    @Test
    @Ignore
    public void test_should_not_create_service_group_when_mandatory_fields_are_missing() throws Exception {
        when(serviceGroupService.createCategory(any(String.class), any(String.class), any(ServiceGroupRequest.class))).thenReturn(prepareServiceGroup());
        ResponseInfo resInfo = new ResponseInfo();
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(resInfo);
        mockMvc.perform(post("/serviceGroup/v1/_create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("servicegroupcreatebadrequest.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("servicegroupcreateresponseforbadrequest.json")));
    }


    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ServiceGroup> prepareServiceGroupList() {
        ServiceGroup serviceGroup = new ServiceGroup();
        serviceGroup.setCode("SG01");
        serviceGroup.setId(1L);
        serviceGroup.setDescription("ServiceGroup01");
        serviceGroup.setName("ServiceGroup01");
        serviceGroup.setTenantId("default");
        List<ServiceGroup> serviceGroupList = new ArrayList<>();
        serviceGroupList.add(serviceGroup);
        return serviceGroupList;
    }

    private ServiceGroup prepareServiceGroup() {
        ServiceGroup sGroup = new ServiceGroup();
        sGroup.setCode("SG01");
        sGroup.setName("ServiceGroup01");
        sGroup.setDescription("ServiceGroupOne");
        sGroup.setTenantId("default");
        return sGroup;
    }

}