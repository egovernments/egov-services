package org.egov.mr.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.TestConfiguration;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.Page;
import org.egov.mr.model.ReissueApplicantInfo;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.service.MarriageCertService;
import org.egov.mr.utils.FileUtils;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.ReissueCertRequest;
import org.egov.mr.web.contract.ReissueCertResponse;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MarriageCertController.class)
@Import(TestConfiguration.class)
public class MarriageCertControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MarriageCertService marriageCertService;

	@InjectMocks
	private MarriageCertController marriageCertController;

	@MockBean
	private ErrorHandler errorHandler;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testForSearch() throws IOException, Exception {

		ResponseInfo responseinfo = ResponseInfo.builder().apiId("uief87324").resMsgId("string").status("200")
				.ver("string").ts(Long.valueOf("987456321")).build();
		Page page = Page.builder().totalResults(1).currentPage(null).pageSize(null).totalPages(null).offSet(null)
				.build();

		ReissueCertResponse expectedReissueCertApplList = ReissueCertResponse.builder()
				.reissueApplications(getReissueCertApplFromDB()).responseInfo(responseinfo).page(page).build();
		when(marriageCertService.getMarriageCerts(Matchers.any(MarriageCertCriteria.class),
				Matchers.any(RequestInfo.class))).thenReturn(expectedReissueCertApplList);

		try {
			mockMvc.perform(post("/certs/reissueAppl/_search").content(getFileContents("ReissueCertRequest.json"))
					.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("ReissuecertResponse.json")));
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	@Test
	public void testForCreate() throws IOException, Exception {

		ResponseInfo responseinfo = ResponseInfo.builder().apiId("uief87324").resMsgId("string").status("200")
				.ver("string").ts(Long.valueOf("987456321")).build();
		Page page = Page.builder().totalResults(1).currentPage(null).pageSize(null).totalPages(null).offSet(null)
				.build();

		ReissueCertResponse expectedReissueCertApplList = ReissueCertResponse.builder()
				.reissueApplications(getReissueCertApplFromDB()).responseInfo(responseinfo).page(page).build();

		when(marriageCertService.createAsync(Matchers.any(ReissueCertRequest.class)))
				.thenReturn(expectedReissueCertApplList);

		mockMvc.perform(post("/certs/reissueAppl/_create").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("ReissuecertificateRequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("ReissuecertResponse.json")));

	}

	@Test
	public void testForUpdate() throws IOException, Exception {

		ResponseInfo responseinfo = ResponseInfo.builder().apiId("uief87324").resMsgId("string").status("200")
				.ver("string").ts(Long.valueOf("987456321")).build();
		Page page = Page.builder().totalResults(1).currentPage(null).pageSize(null).totalPages(null).offSet(null)
				.build();

		ReissueCertResponse expectedReissueCertApplList = ReissueCertResponse.builder()
				.reissueApplications(getReissueCertApplFromDB()).responseInfo(responseinfo).page(page).build();

		when(marriageCertService.updateAsync(Matchers.any(ReissueCertRequest.class)))
				.thenReturn(expectedReissueCertApplList);

		mockMvc.perform(post("/certs/reissueAppl/_update").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("ReissuecertificateRequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("ReissuecertResponse.json")));
	}

	private String getFileContents(String filePath) throws IOException {
		return new FileUtils().getFileContents("org/egov/mr/web/controller/" + filePath);
	}

	public List<ReissueCertAppl> getReissueCertApplFromDB() {

		List<Long> demandList = new ArrayList<>();
		demandList.add(1496430744825L);
		List<ReissueCertAppl> reissueCertApplList = new ArrayList<>();
		List<ReissueApplicantInfo> reissueApplicantInfoList = new ArrayList<>();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("creator").createdTime(1496430744825L)
				.lastModifiedBy("modifier").lastModifiedTime(1496430744825L).build();
		MarriageCertificate marriageCertificate = MarriageCertificate.builder().applicationNumber("applnno")
				.bridePhoto("photo").bridegroomPhoto("photo").certificateDate(1496430744825L).certificateNo("certno")
				.certificatePlace("certplace").certificateType(CertificateType.REISSUE).husbandAddress("hus address")
				.husbandName("husname").wifeName("wife name").wifeAddress("wife address").marriageDate(1496430744825L)
				.marriageVenueAddress("venu address").regnDate(1496430744825L).regnNumber("regnnumber")
				.regnSerialNo("serialno").regnVolumeNo("volumeno").templateVersion("v2").tenantId("ap.kurnool").build();
		MarriageDocument marriageDocument = MarriageDocument.builder().location("location").id("1")
				.auditDetails(auditDetails).documentType("documenttype").reissueCertificateId("1234")
				.tenantId("ap.kurnool").build();
		List<MarriageDocument> documentsList = new ArrayList<>();
		documentsList.add(marriageDocument);
		ReissueApplicantInfo reissueApplicantInfo = ReissueApplicantInfo.builder().aadhaar("123456789")
				.address("address12345").email("emailid@gmail.com").fee(new BigDecimal(34)).mobileNo("9876543210")
				.name("name").build();
		reissueApplicantInfoList.add(reissueApplicantInfo);
		ApprovalDetails approvalDetails = ApprovalDetails.builder().action("action").assignee(1496430744825L)
				.comments("comments").department(1496430744825L).designation(1496430744825L).status("Approved").build();
		ReissueCertAppl reissueCertAppl = ReissueCertAppl.builder().id("1").applicationNumber("egov1234")
				.auditDetails(auditDetails).certificate(marriageCertificate).documents(documentsList).regnNo("regnno")
				.reissueApplStatus(ApplicationStatus.CREATED).rejectionReason("rejectionreaon").remarks("remarks")
				.stateId("12345").tenantId("ap.kurnool").demands(demandList).isActive(null)
				.approvalDetails(approvalDetails).applicantInfo(reissueApplicantInfo).build();

		reissueCertApplList.add(reissueCertAppl);
		/*
		 * 
		 * ReissueCertResponse reissueCertResponse =
		 * ReissueCertResponse.builder().reissueApplications(
		 * reissueCertApplList) .responseInfo(responseinfo).page(page).build();
		 * List<ReissueCertResponse> responselist = new ArrayList<>();
		 * responselist.add(reissueCertResponse);
		 */
		return reissueCertApplList;

	}

}
