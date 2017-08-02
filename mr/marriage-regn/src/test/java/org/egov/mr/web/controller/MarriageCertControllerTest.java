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

import org.egov.mr.TestConfiguration;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.ReissueApplicantInfo;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.service.MarriageCertService;
import org.egov.mr.utils.FileUtils;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.ReissueCertResponse;
import org.egov.mr.web.contract.RequestInfo;
import org.egov.mr.web.contract.ResponseInfo;
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
	private ReissueCertResponse reissueCertResponse;

	@MockBean
	private ResponseInfo responseinfo;

	@MockBean
	private ErrorHandler errorHandler;

	@Test
	public void testForSearch() throws IOException, Exception {

		ReissueCertResponse expectedReissueCertApplList = ReissueCertResponse.builder()
				.reissueApplications(getReissueCertApplFromDB()).build();
		when(marriageCertService.getMarriageCerts(Matchers.any(MarriageCertCriteria.class),
				Matchers.any(RequestInfo.class))).thenReturn(expectedReissueCertApplList);

		try {
			mockMvc.perform(post("/certs/reissueAppl/_search").content(getFileContents("ReissueCertRequest.json)"))
					.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("ReissuecertResponse.json")));
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private String getFileContents(String filePath) throws IOException {
		return new FileUtils().getFileContents("org/egov/mr/web/controller/" + filePath);
	}

	public List<ReissueCertAppl> getReissueCertApplFromDB() {

		List<Long> demandList = new ArrayList<>();
		demandList.add(987654L);
		List<ReissueCertAppl> reissueCertApplList = new ArrayList<>();
		List<ReissueApplicantInfo> reissueApplicantInfoList = new ArrayList<>();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("kusuma").createdTime(8976L)
				.lastModifiedBy("kusuma").lastModifiedTime(6789L).build();
		MarriageCertificate marriageCertificate = MarriageCertificate.builder().applicationNumber("sdfghjkj")
				.bridePhoto("photo").bridegroomPhoto("photo").certificateDate(23456L).certificateNo("certno")
				.certificatePlace("sdfghj").certificateType(CertificateType.REISSUE).husbandAddress("qwewrtyui")
				.husbandName("qwertyui").marriageDate(123456L).marriageVenueAddress("asdfghjk").regnDate(2345678L)
				.regnNumber("qwertfcrt").regnSerialNo("zdxtrvghv").regnVolumeNo("ertyvvvvbv").templateVersion("v2")
				.tenantId("ap.kurnool").build();
		MarriageDocument marriageDocument = MarriageDocument.builder().location("dfghjkuyuw").id("1234")
				.reissueCertificateId("1234").tenantId("ap.kurnool").build();
		List<MarriageDocument> documentsList = new ArrayList<>();
		documentsList.add(marriageDocument);
		ReissueApplicantInfo reissueApplicantInfo = ReissueApplicantInfo.builder().aadhaar("234567987654")
				.address("sdfghjkoiuyt").email("fggsftdqjhdu").fee(new BigDecimal(20)).mobileNo("9876543210")
				.name("bnavdhja").build();
		reissueApplicantInfoList.add(reissueApplicantInfo);
		ApprovalDetails approvalDetails = ApprovalDetails.builder().action("action").assignee(1234L)
				.comments("comments").department(2345L).designation(1234L).status("Approved").build();
		ReissueCertAppl reissueCertAppl = ReissueCertAppl.builder().applicationNumber("retyui")
				.auditDetails(auditDetails).certificate(marriageCertificate).documents(documentsList).regnNo("wqertyui")
				.reissueApplStatus(ApplicationStatus.CREATED).rejectionReason("asdfghjertyu").remarks("jaweerrt")
				.stateId("234567").tenantId("ap.kurnool").demands(demandList).approvalDetails(approvalDetails)
				.applicantInfo(reissueApplicantInfo).build();
		reissueCertApplList.add(reissueCertAppl);

		return reissueCertApplList;

	}

}
