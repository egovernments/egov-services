package org.egov.pgr.read.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.TestConfiguration;
import org.egov.pgr.read.domain.service.ReceivingCenterService;
import org.egov.pgr.read.persistence.entity.ReceivingCenter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
		List<ReceivingCenter> recievingCenters = new ArrayList<>(Collections.singletonList(new ReceivingCenter()));
		when(mockReceivingCenterService.getAllReceivingCenters(tenantId)).thenReturn(recievingCenters);

		mockMvc.perform(get("/receivingcenter?tenantId=" + tenantId)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk());
	}

	@Test
	public void testGetReceivingCenterById() throws Exception {
		ReceivingCenter receivingCenter = ReceivingCenter.builder().id(1L).name("Complaint Cell").isCrnRequired(true)
				.orderNo(8L).build();
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