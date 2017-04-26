package org.egov.pgr.read.web.controller;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.TestConfiguration;
import org.egov.pgr.read.domain.service.ReceivingCenterService;
import org.egov.pgr.common.entity.ReceivingCenter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(ReceivingCenterController.class)
@Import(TestConfiguration.class)
public class ReceivingCenterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceivingCenterService mockReceivingCenterService;

    @Test
    public void testGetReceivingCenters() throws Exception {
        String tenantId = "ap.public";
        List<ReceivingCenter> recievingCenters =getReceivingCenters();
        String expectedContent = readResource("getServiceRequests.json");
        when(mockReceivingCenterService.getAllReceivingCenters(tenantId)).thenReturn(recievingCenters);
        mockMvc.perform(post("/receivingcenter/_getallreceivingcenters?tenantId=" + tenantId))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    		.andExpect(content().json(expectedContent));
    }

    private String readResource(String string) throws Exception {
    	 File file = ResourceUtils.getFile(this.getClass().getResource("/getReceivingCenters.json"));
	        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		
	}

	private List<ReceivingCenter> getReceivingCenters() {
		ReceivingCenter receivingCenter1=ReceivingCenter.builder().id(2L).name("Mayor/Chairperson Office").orderNo(2L).crnRequired(true).tenantId("ap.public").build();
		ReceivingCenter receivingCenter2=ReceivingCenter.builder().id(3L).name("Zonal Office").orderNo(7L).crnRequired(true).tenantId("ap.public").build();
			
		return Arrays.asList(receivingCenter1,receivingCenter2);
	}

	@Test
    public void testGetReceivingCenterById() throws Exception {
        ReceivingCenter receivingCenter = ReceivingCenter.builder()
            .id(1L)
            .name("Complaint Cell")
            .crnRequired(true)
            .orderNo(8L).tenantId("ap.public").build();
        when(mockReceivingCenterService.getReceivingCenterById("ap.public", 1L)).thenReturn(receivingCenter);

        mockMvc.perform(post("/receivingcenter/_getreceivingcenterbyid?tenantId=ap.public&id=" + 1L))
            .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(getFileContents("receivingCenter.json")));
    }

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}