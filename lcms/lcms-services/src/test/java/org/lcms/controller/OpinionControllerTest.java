package org.lcms.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.EgovLcmsApplication;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Department;
import org.egov.lcms.models.Opinion;
import org.egov.lcms.models.OpinionRequest;
import org.egov.lcms.models.OpinionResponse;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.service.AdvocatePaymentService;
import org.egov.lcms.service.AdvocateService;
import org.egov.lcms.service.CaseService;
import org.egov.lcms.service.NoticeService;
import org.egov.lcms.service.OpinionService;
import org.egov.lcms.service.RegisterService;
import org.egov.lcms.service.SummonService;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.tracer.kafka.ErrorQueueProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = { EgovLcmsApplication.class })
public class OpinionControllerTest {

	@MockBean
	AdvocateService advocateService;

	@MockBean
	ResponseFactory responseInfoFactory;

	@MockBean
	PropertiesManager propertiesManager;

	@MockBean
	AdvocatePaymentService advocatePaymentService;

	@MockBean
	CaseService caseService;

	@MockBean
	NoticeService noticeService;

	@MockBean
	OpinionService opinionService;

	@MockBean
	RegisterService registerService;

	@MockBean
	SummonService summonService;

	@MockBean
	UniqueCodeGeneration uniqueCodeGeneration;

	@MockBean
	ErrorQueueProducer errorQueueProducer;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void opinionCreateTest() {

		List<Opinion> opinions = new ArrayList<>();
		Opinion opinion = new Opinion();
		opinion.setTenantId("default");
		opinion.setCode("default0001");
		opinion.setAdditionalAdvocate("veswanth");
		opinion.setInWardDate(12345l);
		opinion.setOpinionRequestDate(456123l);
		opinion.setOpinionOn("criminal case");
		opinion.setStateId("124");
		opinion.setOpinionDescription("good1");

		Department department = new Department();
		department.setActive(true);
		department.setCode("default0001");
		department.setName("veswanth");
		opinion.setDepartmentName(department);

		AuditDetails auditDetails = new AuditDetails();
		opinion.setAuditDetails(auditDetails);

		OpinionResponse opinionResponse = new OpinionResponse();
		opinions.add(opinion);
		opinionResponse.setOpinions(opinions);
		opinionResponse.setResponseInfo(new ResponseInfo());

		try {
			when(opinionService.createOpinion(any(OpinionRequest.class))).thenReturn(opinionResponse);

			mockMvc.perform(post("/legalcase/opinion/_create").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("createOpinionRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("createOpinionResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void opinionUpdateTest() {

		List<Opinion> opinions = new ArrayList<>();
		Opinion opinion = new Opinion();
		opinion.setCode("default0001");
		opinion.setTenantId("default");
		opinion.setAdditionalAdvocate("veswanthR");
		opinion.setInWardDate(123456l);
		opinion.setOpinionRequestDate(4561234l);
		opinion.setOpinionOn("revenue case");
		opinion.setOpinionDescription("progress");
		opinion.setStateId("124");

		Department department = new Department();
		department.setActive(true);
		department.setCode("default0001");
		department.setName("veswanthR");
		opinion.setDepartmentName(department);

		AuditDetails auditDetails = new AuditDetails();
		opinion.setAuditDetails(auditDetails);

		OpinionResponse opinionResponse = new OpinionResponse();
		opinions.add(opinion);
		opinionResponse.setResponseInfo(new ResponseInfo());
		opinionResponse.setOpinions(opinions);

		try {
			when(opinionService.updateOpinion(any(OpinionRequest.class))).thenReturn(opinionResponse);

			mockMvc.perform(post("/legalcase/opinion/_update").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("updateOpinionRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("updateOpinionResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	@Test
	public void opinionSearchTest() {
		List<Opinion> opinions = new ArrayList<>();
		Opinion opinion = new Opinion();
		opinion.setCode("default0001");
		opinion.setTenantId("default");
		opinion.setAdditionalAdvocate("veswanthR");
		opinion.setInWardDate(123456l);
		opinion.setOpinionRequestDate(4561234l);
		opinion.setOpinionOn("revenue case");
		opinion.setOpinionDescription("progress");
		opinion.setStateId("124");

		Department department = new Department();
		department.setActive(true);
		department.setCode("default0001");
		department.setName("veswanthR");
		opinion.setDepartmentName(department);

		AuditDetails auditDetails = new AuditDetails();
		opinion.setAuditDetails(auditDetails);

		OpinionResponse opinionResponse = new OpinionResponse();
		opinions.add(opinion);
		opinionResponse.setResponseInfo(new ResponseInfo());
		opinionResponse.setOpinions(opinions);

		try {
			when(opinionService.searchOpinion(any(RequestInfoWrapper.class), any(OpinionSearchCriteria.class)))
					.thenReturn(opinionResponse);

			mockMvc.perform(post("/legalcase/opinion/_search").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("searchOpinionRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("searchOpinionResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
		}
		assertTrue(Boolean.TRUE);
	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}
}
