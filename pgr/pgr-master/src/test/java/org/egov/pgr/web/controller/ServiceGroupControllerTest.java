package org.egov.pgr.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.model.ServiceGroup;
import org.egov.pgr.service.ServiceGroupService;
import org.egov.pgr.web.contract.ServiceGroupGetRequest;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
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
    
    @InjectMocks
    private ServiceGroupController serviceGroupController;
    
	@Test(expected = Exception.class)
	public void test_should_fetch_service_group_list() throws Exception {
		List<ServiceGroup> serviceGroupList = prepareServiceGroupList();
		ServiceGroupGetRequest serviceGroupGetReq = ServiceGroupGetRequest.builder()
				.tenantId("default")
				.build();
		when(serviceGroupService.getAllServiceGroup(serviceGroupGetReq)).thenReturn(serviceGroupList);
		mockMvc.perform(post("/serviceGroup/_search")
				.param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("servicegroupfetchrequest.json")))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	            .andExpect(content().json(getFileContents("servicegroupfetchresponse.json")));
	}
	
	private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	private List<ServiceGroup> prepareServiceGroupList(){
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

}
