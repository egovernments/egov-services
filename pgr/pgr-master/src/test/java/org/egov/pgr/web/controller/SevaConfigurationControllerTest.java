package org.egov.pgr.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.service.SevaConfigurationService;
import org.egov.pgr.web.contract.SevaConfigurationGetRequest;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(SevaConfigurationController.class)
@Import(TestConfiguration.class)
public class SevaConfigurationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SevaConfigurationService sevaConfigurationService;

	@MockBean
	private ErrorHandler ErrorHandler;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;

	@Test
	public void test_to_search_seva_config_key() throws Exception {
		SevaConfigurationGetRequest criteria = SevaConfigurationGetRequest.builder().id(Collections.singletonList(1L))
				.tenantId("default").build();

		when(sevaConfigurationService.getSevaConfigurations(criteria)).thenReturn(getSearchResult());

		mockMvc.perform(post("/sevaconfigurations/_search?tenantId=default&id=1")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(getFileContents("sevaConfigRequest.json")))
				.andExpect(status().isOk()).andExpect(content().json(getFileContents("sevaConfigResponse.json")));
	}

	private Map<String, List<String>> getSearchResult() {
		Map<String, List<String>> objectObjectHashMap = new HashMap<String, List<String>>();
		objectObjectHashMap.put("id", Collections.singletonList("1L"));
		return objectObjectHashMap;

	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
