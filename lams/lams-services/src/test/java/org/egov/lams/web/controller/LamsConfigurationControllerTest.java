package org.egov.lams.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.lams.TestConfiguration;
import org.egov.lams.service.LamsConfigurationService;
import org.egov.lams.util.FileUtils;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.errorhandlers.ErrorHandler;
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
@WebMvcTest(LamsConfigurationController.class)
@Import(TestConfiguration.class)
public class LamsConfigurationControllerTest {

	@MockBean
	private LamsConfigurationService lamsConfigurationService;

	@MockBean
	private ErrorHandler errHandler;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	public void test_Should_Search_LamsConfigurations() throws Exception{
		
		ResponseInfo responeInfo = new ResponseInfo();
		Map<String, List<String>> resultMap = new HashMap<>();
		List<String> lamsList = new ArrayList<>();
		lamsList.add("lams");
		resultMap.put("lams", lamsList);
		
		when(lamsConfigurationService.getLamsConfigurations(any(LamsConfigurationGetRequest.class))).thenReturn(resultMap);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(responeInfo);
		
		mockMvc.perform(post("/lamsconfigurations/_search")
        		.param("tenantId","ap.kurnool")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("lamsconfigurationrequest.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("lamsconfigurationsresponse.json")));
	}
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
}
