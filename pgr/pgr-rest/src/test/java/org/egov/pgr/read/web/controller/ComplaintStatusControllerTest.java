package org.egov.pgr.read.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.TestConfiguration;
import org.egov.pgr.read.domain.service.ComplaintStatusService;
import org.egov.pgr.read.persistence.entity.ComplaintStatus;
import org.egov.pgr.read.persistence.repository.ComplaintStatusRepository;
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
@WebMvcTest(ComplaintStatusController.class)
@Import(TestConfiguration.class)
public class ComplaintStatusControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ComplaintStatusRepository complaintStatusRepository;
	@MockBean
	private ComplaintStatusService complaintStatusService;

	@Test
	public void findAllStatusTest() throws Exception {

		String tenantId = "ap.public";
		List<ComplaintStatus> complaintStatuses = new ArrayList<>(
				Collections.singletonList(ComplaintStatus.builder().name("REGISTERED").build()));
		when(complaintStatusService.getAllComplaintStatus()).thenReturn(complaintStatuses);
		mockMvc.perform(post("/_statuses?tenantId=" + tenantId)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("complaintStatusResponse.json")));

	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
