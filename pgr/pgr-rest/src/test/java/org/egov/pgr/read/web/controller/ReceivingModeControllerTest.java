package org.egov.pgr.read.web.controller;

import org.egov.pgr.TestConfiguration;

import org.egov.pgr.read.domain.model.ReceivingMode;
import org.egov.pgr.read.domain.service.ReceivingModeService;
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
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReceivingModeController.class)
@Import({TestConfiguration.class})
public class ReceivingModeControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReceivingModeService mockReceivingModeService;

	@Test
	public void testGetReceivingModes() throws Exception {
        String tenantId = "ap.public";
		List<ReceivingMode> receivingModes =getReceivingModes();
		when(mockReceivingModeService.getAllReceivingModes(tenantId)).thenReturn(receivingModes);
		  String expectedContent = readResource("getServiceRequests.json");
		mockMvc.perform(
				post("/receivingmode/_search?tenantId=ap.public"))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().json(expectedContent));

	}

	private String readResource(String string) throws Exception{
	      File file = ResourceUtils.getFile(this.getClass().getResource("/getReceivingModes.json"));
	        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

	}

	private List<ReceivingMode> getReceivingModes() {

		ReceivingMode receivingMode1=ReceivingMode.builder().id(1L).name("Website").code("WEBSITE").tenantId("ap.public").build();
		ReceivingMode receivingMode2=ReceivingMode.builder().id(2L).name("SMS").tenantId("ap.public").code("SMS").build();
		return Arrays.asList(receivingMode1,receivingMode2);
	}

}

