package org.egov.pgr.read.web.controller;

import org.egov.pgr.TestConfiguration;
import org.egov.pgr.read.domain.service.ReceivingModeService;
import org.egov.pgr.common.entity.ReceivingMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
	public void testGetReceivingCenters() throws Exception {
        String tenantId = "ap.public";
		List<ReceivingMode> recievingModes = new ArrayList<>(Collections.singletonList(new ReceivingMode()));
		when(mockReceivingModeService.getAllReceivingModes(tenantId)).thenReturn(recievingModes);

		mockMvc.perform(get("/receivingmode")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("tenantId", "ap.public")
        ).andExpect(status().isOk());
	}

}

