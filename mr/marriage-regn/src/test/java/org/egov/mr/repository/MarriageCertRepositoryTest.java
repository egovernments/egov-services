package org.egov.mr.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Demand;
import org.egov.mr.model.Fee;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.MarryingPerson;
import org.egov.mr.model.ReissueApplicantInfo;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.Action;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.MaritalStatus;
import org.egov.mr.model.enums.Venue;
import org.egov.mr.repository.querybuilder.MarriageCertQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageCertRowMapper;
import org.egov.mr.service.CertificateNumberService;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.ReissueCertRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MarriageCertRepositoryTest {

	@Mock
	private CertificateNumberService certificateNumberService;

	@Mock
	private MarriageCertQueryBuilder marriageCertQueryBuilder;

	@Mock
	private MarriageCertRowMapper marriageCertRowMapper;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private MarriageCertRepository marriageCertRepository;

	@Test
	public void testInsert() {

		when(certificateNumberService.generateCertificateNumber()).thenReturn("certificateNumber");
		when(jdbcTemplate.update(any(String.class), Matchers.<Object[]>any())).thenReturn(1);
		marriageCertRepository.insert(getMarriageRegn());
	}

	@Test
	public void testFindForCriteria() {
		when(marriageCertQueryBuilder.getQuery(any(MarriageCertCriteria.class), any(List.class)))
				.thenReturn(getQueryForFFC());
		when(jdbcTemplate.query(any(String.class), Matchers.<Object[]>any(), any(MarriageCertRowMapper.class)))
				.thenReturn(getReissueCertApplication());

		List<ReissueCertAppl> reissueCertAppls = marriageCertRepository.findForCriteria(getMarriageCertCriteria());
		assertEquals(getReissueCertApplication(), reissueCertAppls);
	}

	@Test
	public void testCreateDoc() {
		int[] value = new int[] { 2, 6 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		marriageCertRepository.createDoc(getReissueCertRequest());
	}

	@Test
	public void testUpdateDoc() {
		int[] value = new int[] { 2, 6 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		marriageCertRepository.updateDoc(getReissueCertRequest());
	}

	@Test
	public void testCreateReissue() {
		int[] value = new int[] { 2, 6 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		marriageCertRepository.createReissue(getReissueCertRequest());
	}

	@Test
	public void testUpdateReissue() {
		int[] value = new int[] { 2, 6 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		marriageCertRepository.updateReissue(getReissueCertRequest());
	}

	@Test
	public void testCreateCert() {
		int[] value = new int[] { 2, 6 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		marriageCertRepository.createCert(getReissueCertAppl());
	}

	/**
	 * @HelperMethods
	 * 
	 * @return
	 */
	private String getQueryForFFC() {
		return "INSERT INTO table VALUE()";
	}

	private ReissueCertRequest getReissueCertRequest() {
		ReissueCertRequest reissueCertRequest = new ReissueCertRequest();
		reissueCertRequest.setRequestInfo(new RequestInfo());
		reissueCertRequest.setReissueApplication(getReissueCertAppl());
		return reissueCertRequest;
	}

	private MarriageCertCriteria getMarriageCertCriteria() {
		MarriageCertCriteria marriageCertCriteria = new MarriageCertCriteria();
		marriageCertCriteria.setApplicationNumber("9516234870");
		marriageCertCriteria.setPageNo(Short.valueOf("06"));
		marriageCertCriteria.setPageSize(Short.valueOf("06"));
		marriageCertCriteria.setRegnNo("852456");
		marriageCertCriteria.setTenantId("ap.kurnool");
		return marriageCertCriteria;

	}

	private List<ReissueCertAppl> getReissueCertApplication() {

		ReissueApplicantInfo reissueApplicantInfo = new ReissueApplicantInfo();
		reissueApplicantInfo.setAadhaar("XASD655ZA");
		reissueApplicantInfo.setAddress("No.10 D.No13 AKM, Outdoors, Bangalore");
		reissueApplicantInfo.setEmail("abc@gmail.com");
		reissueApplicantInfo.setFee(BigDecimal.valueOf(10.5));
		reissueApplicantInfo.setMobileNo("9874563210");
		reissueApplicantInfo.setName("reissue");

		ApprovalDetails approvalDetails = new ApprovalDetails();
		approvalDetails.setAction("APPROVED");
		approvalDetails.setAssignee(Long.valueOf("9874563210"));
		approvalDetails.setComments("GetSigned");
		approvalDetails.setDepartment(Long.valueOf("9856321470"));
		approvalDetails.setDesignation(Long.valueOf("123654"));
		approvalDetails.setStatus("ACTIVE");

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedTime(Long.valueOf("1593234786"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedTime(Long.valueOf("1593234786"));

		List<Long> demands = new ArrayList<>();
		demands.add(Long.valueOf(6));
		demands.add(Long.valueOf(2));

		ReissueCertAppl reissueCertAppl = new ReissueCertAppl();
		reissueCertAppl.setApplicantInfo(reissueApplicantInfo);
		reissueCertAppl.setApplicationNumber("00015");
		reissueCertAppl.setApprovalDetails(approvalDetails);
		reissueCertAppl.setAuditDetails(auditDetails);
		reissueCertAppl.setCertificate(null);
		reissueCertAppl.setDemands(demands);
		reissueCertAppl.setDocuments(null);
		reissueCertAppl.setId("6");
		reissueCertAppl.setIsActive(true);
		reissueCertAppl.setRegnNo("00015");
		reissueCertAppl.setReissueApplStatus(ApplicationStatus.APPROVED);
		reissueCertAppl.setRejectionReason("NotActive");
		reissueCertAppl.setRemarks("perfect");
		reissueCertAppl.setStateId("00015");
		reissueCertAppl.setTenantId("ap.kurnool");

		List<ReissueCertAppl> list = new ArrayList<>();
		list.add(reissueCertAppl);
		return list;
	}

	private ReissueCertAppl getReissueCertAppl() {
		ReissueApplicantInfo reissueApplicantInfo = new ReissueApplicantInfo();
		reissueApplicantInfo.setAadhaar("XASD655ZA");
		reissueApplicantInfo.setAddress("No.10 D.No13 AKM, Outdoors, Bangalore");
		reissueApplicantInfo.setEmail("abc@gmail.com");
		reissueApplicantInfo.setFee(BigDecimal.valueOf(10.5));
		reissueApplicantInfo.setMobileNo("9874563210");
		reissueApplicantInfo.setName("reissue");

		ApprovalDetails approvalDetails = new ApprovalDetails();
		approvalDetails.setAction("APPROVED");
		approvalDetails.setAssignee(Long.valueOf("9874563210"));
		approvalDetails.setComments("GetSigned");
		approvalDetails.setDepartment(Long.valueOf("9856321470"));
		approvalDetails.setDesignation(Long.valueOf("123654"));
		approvalDetails.setStatus("ACTIVE");

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedTime(Long.valueOf("1593234786"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedTime(Long.valueOf("1593234786"));

		List<Long> demands = new ArrayList<>();
		demands.add(Long.valueOf(6));
		demands.add(Long.valueOf(2));

		ReissueCertAppl reissueCertAppl = new ReissueCertAppl();
		reissueCertAppl.setApplicantInfo(reissueApplicantInfo);
		reissueCertAppl.setApplicationNumber("00015");
		reissueCertAppl.setApprovalDetails(approvalDetails);
		reissueCertAppl.setAuditDetails(auditDetails);
		reissueCertAppl.setCertificate(null);
		reissueCertAppl.setDemands(demands);
		reissueCertAppl.setDocuments(null);
		reissueCertAppl.setId("6");
		reissueCertAppl.setIsActive(true);
		reissueCertAppl.setRegnNo("00015");
		reissueCertAppl.setReissueApplStatus(ApplicationStatus.APPROVED);
		reissueCertAppl.setRejectionReason("NotActive");
		reissueCertAppl.setRemarks("perfect");
		reissueCertAppl.setStateId("00015");
		reissueCertAppl.setTenantId("ap.kurnool");

		return reissueCertAppl;
	}

	private MarriageRegn getMarriageRegn() {
		List<String> actions = new ArrayList<>();
		actions.add("APPROVED");
		actions.add("REJECTED");
		actions.add("ONHOLD");

		ReissueApplicantInfo reissueApplicantInfo = new ReissueApplicantInfo();
		reissueApplicantInfo.setAadhaar("XASD655ZA");
		reissueApplicantInfo.setAddress("No.10 D.No13 AKM, Outdoors, Bangalore");
		reissueApplicantInfo.setEmail("abc@gmail.com");
		reissueApplicantInfo.setFee(BigDecimal.valueOf(10.5));
		reissueApplicantInfo.setMobileNo("9874563210");
		reissueApplicantInfo.setName("reissue");

		ApprovalDetails approvalDetails = new ApprovalDetails();
		approvalDetails.setAction("APPROVED");
		approvalDetails.setAssignee(Long.valueOf("9874563210"));
		approvalDetails.setComments("GetSigned");
		approvalDetails.setDepartment(Long.valueOf("9856321470"));
		approvalDetails.setDesignation(Long.valueOf("123654"));
		approvalDetails.setStatus("ACTIVE");

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("5");
		auditDetails.setCreatedTime(Long.valueOf("1593234786"));
		auditDetails.setLastModifiedBy("5");
		auditDetails.setLastModifiedTime(Long.valueOf("1593234786"));

		List<Demand> demands = new ArrayList<>();
		Demand demand = new Demand();
		demand.setAuditDetail(auditDetails);
		demand.setId("6");
		demand.setTenantId("ap.kurnool");
		demands.add(demand);

		MarryingPerson marryingPerson = new MarryingPerson();
		marryingPerson.setAadhaar("ZXASD12WW12");
		marryingPerson.setCity("Bangalore");
		marryingPerson.setEmail("xyz@gmail.com");
		marryingPerson.setId(Long.valueOf("00024"));
		marryingPerson.setStatus(MaritalStatus.UNMARRIED);

		List<MarriageCertificate> mc = new ArrayList<>();
		Fee fee = new Fee();
		fee.setId("6");
		fee.setFee(BigDecimal.valueOf(10.1));

		MarriageRegn marriageRegn = new MarriageRegn();
		marriageRegn.setActions(Action.CREATE);
		marriageRegn.setApplicationNumber("00015");
		marriageRegn.setApprovalDetails(approvalDetails);
		marriageRegn.setAuditDetails(auditDetails);
		marriageRegn.setBride(marryingPerson);
		marriageRegn.setBridegroom(marryingPerson);
		marriageRegn.setCertificates(mc);
		marriageRegn.setCity("Bangalore");
		marriageRegn.setDemands(demands);
		marriageRegn.setDocuments(null);
		marriageRegn.setFee(fee);
		marriageRegn.setIsActive(true);
		marriageRegn.setLocality("Belandur");
		marriageRegn.setMarriageDate(Long.valueOf("161947"));
		marriageRegn.setMarriagePhoto("yes");
		marriageRegn.setPlaceOfMarriage("UDUPI");
		marriageRegn.setPriest(null);
		marriageRegn.setRegnDate(Long.valueOf("123456789"));
		marriageRegn.setRegnNumber("00015");
		marriageRegn.setRegnUnit(null);
		marriageRegn.setRejectionReason("Nothing");
		marriageRegn.setRemarks("allGood");
		marriageRegn.setSerialNo("00015");
		marriageRegn.setSource(null);
		marriageRegn.setStateId("00015");
		marriageRegn.setStatus(ApplicationStatus.CREATED);
		marriageRegn.setStreet("Agara");
		marriageRegn.setTenantId("ap.kurnool");
		marriageRegn.setVenue(Venue.FUNCTION_HALL);
		marriageRegn.setVolumeNo("00015");
		marriageRegn.setWitnesses(null);

		return marriageRegn;
	}

}
