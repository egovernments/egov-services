package org.egov.lams.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.TestConfiguration;
import org.egov.lams.model.Notice;
import org.egov.lams.model.NoticeCriteria;
import org.egov.lams.service.NoticeService;
import org.egov.lams.util.FileUtils;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.NoticeResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
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
@WebMvcTest(NoticeController.class)
@Import(TestConfiguration.class)
public class NoticeControllerTest {

	@MockBean
	private NoticeService noticeService;
	
	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	public void test_Should_Create_Notices() throws Exception{
		NoticeResponse noticeResponse = new NoticeResponse();
		noticeResponse.setResponseInfo(new ResponseInfo());
		when(noticeService.generateNotice(any(NoticeRequest.class))).thenReturn(noticeResponse); 

		mockMvc.perform(post("/agreement/notice/_create")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("noticerequest.json")))
	                .andExpect(status().isCreated())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("noticeresponse.json")));
	}

	@Test
	public void test_Should_Search_Notices() throws Exception {
		List<Notice> notices = new ArrayList<>();
		ResponseInfo responseInfo = new ResponseInfo();
		when(noticeService.getNotices(any(NoticeCriteria.class))).thenReturn(notices);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(responseInfo);

		mockMvc.perform(post("/agreement/notice/_search")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("noticerequest.json")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("noticeresponse.json")));
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
}
