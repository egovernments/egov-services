package org.egov.mr.repository.rowmapper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.ReissueApplicantInfo;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;

@RunWith(MockitoJUnitRunner.class)
public class MarriageCertRowMapperTest {

	@Mock
	ResultSet rs;

	@InjectMocks
	private MarriageCertRowMapper marriageCertRowMapper;

	@Test
	public void test() throws Exception, DataAccessException {
		when(rs.next()).thenReturn(true).thenReturn(false);

		when(rs.getString("rc_id")).thenReturn("6");

		when(rs.getString("rc_regnno")).thenReturn("egov6");
		when(rs.getString("rc_applicationnumber")).thenReturn("appl6");
		when(rs.getString("rc_tenantid")).thenReturn("ap.kurnool");

		when(rs.getString("rc_reissueapplstatus")).thenReturn(ApplicationStatus.APPROVED.toString());
		when(rs.getString("rc_stateid")).thenReturn("state6");
		when(rs.getLong("rc_demands")).thenReturn(Long.valueOf("7894561230"));
		when(rs.getString("rc_rejectionReason")).thenReturn("notrejected");
		when(rs.getString("rc_remarks")).thenReturn("goodremark");
		when(rs.getString("rc_isactive")).thenReturn("ACTIVE");

		when(rs.getString("rc_applicantname")).thenReturn("user6");
		when(rs.getString("rc_applicantaddress")).thenReturn("No.10 Dno.13 AKM, Outdoors, Bangalore");
		when(rs.getString("rc_applicantmobileno")).thenReturn("9874563210");
		when(rs.getBigDecimal("rc_applicantfee")).thenReturn(BigDecimal.valueOf(10.1));
		when(rs.getString("rc_applicantaadhaar")).thenReturn("BXQD12GH");

		when(rs.getString("rc_approvalaction")).thenReturn("APPROVED");
		when(rs.getLong("rc_approvalassignee")).thenReturn(Long.valueOf("00020"));
		when(rs.getString("rc_approvalcomments")).thenReturn("NoCorrections");
		when(rs.getLong("rc_approvaldepartment")).thenReturn(Long.valueOf("00020"));
		when(rs.getLong("rc_approvaldesignation")).thenReturn(Long.valueOf("20"));
		when(rs.getString("rc_approvalstatus")).thenReturn("ACTIVE");

		when(rs.getString("rc_createdby")).thenReturn("user6");
		when(rs.getLong("rc_createdtime")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("rc_lastmodifiedby")).thenReturn("user6");
		when(rs.getLong("rc_lastmodifiedtime")).thenReturn(Long.valueOf("9874563210"));

		when(rs.getString("ds_id")).thenReturn("6");
		when(rs.getString("ds_documenttypecode")).thenReturn("00015");
		when(rs.getString("ds_location")).thenReturn("Bangalore");
		when(rs.getString("ds_reissuecertificateid")).thenReturn("15");

		when(rs.getString("ds_tenantid")).thenReturn("ap.kurnool");

		when(rs.getString("ds_createdby")).thenReturn("user2");
		when(rs.getLong("ds_createdtime")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("ds_lastmodifiedby")).thenReturn("user6");
		when(rs.getLong("ds_lastmodifiedtime")).thenReturn(Long.valueOf("9874563210"));

		when(rs.getString("mc_certificateno")).thenReturn("00015");
		when(rs.getLong("mc_certificatedate")).thenReturn(Long.valueOf("9874563210"));
		when(rs.getString("mc_certificatetype")).thenReturn(CertificateType.REGISTRATION.toString());
		when(rs.getString("mc_regnnumber")).thenReturn("egov6");
		when(rs.getString("mc_bridegroomphoto")).thenReturn("NotAvailable");
		when(rs.getString("mc_bridephoto")).thenReturn("NotAvailable");
		when(rs.getString("mc_husbandname")).thenReturn("user2");
		when(rs.getString("mc_husbandaddress")).thenReturn("No.10 Dno13 AKM, Outdoors, Bangalore");
		when(rs.getString("mc_wifename")).thenReturn("user6");
		when(rs.getString("mc_wifeaddress")).thenReturn("No.10 Dno13 AKM, Outdoors, Bangalore");
		when(rs.getLong("mc_marriagedate")).thenReturn(Long.valueOf("9632587410"));
		when(rs.getString("mc_marriagevenueaddress")).thenReturn("FUNCTIONHALL");
		when(rs.getLong("mc_regndate")).thenReturn(Long.valueOf("1234567890"));
		when(rs.getString("mc_regnserialno")).thenReturn("6549873");
		when(rs.getString("mc_regnvolumeno")).thenReturn("20");
		when(rs.getString("mc_certificateplace")).thenReturn("Bangalore");
		when(rs.getString("mc_templateversion")).thenReturn("2");
		when(rs.getString("mc_applicationnumber")).thenReturn("egov6");
		when(rs.getString("mc_tenantid")).thenReturn("ap.kurnool");

		List<ReissueCertAppl> reissueCertAppls = marriageCertRowMapper.extractData(rs);
		assertEquals(getReissueCertAppl(), reissueCertAppls);
	}

	private List<ReissueCertAppl> getReissueCertAppl() {

		ReissueApplicantInfo reissueApplicationInfo = new ReissueApplicantInfo();
		reissueApplicationInfo.setAadhaar("BXQD12GH");
		reissueApplicationInfo.setAddress("No.10 Dno.13 AKM, Outdoors, Bangalore");
		reissueApplicationInfo.setFee(BigDecimal.valueOf(10.1));
		reissueApplicationInfo.setMobileNo("9874563210");
		reissueApplicationInfo.setName("user6");

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("user2");
		auditDetails.setCreatedTime(Long.valueOf("9874563210"));
		auditDetails.setLastModifiedBy("user6");
		auditDetails.setLastModifiedTime(Long.valueOf("9874563210"));

		ApprovalDetails approvalDetails = new ApprovalDetails();
		approvalDetails.setAction("APPROVED");
		approvalDetails.setAssignee(Long.valueOf("00020"));
		approvalDetails.setComments("NoCorrections");
		approvalDetails.setDepartment(Long.valueOf("00020"));
		approvalDetails.setDesignation(Long.valueOf("20"));
		approvalDetails.setStatus("ACTIVE");

		List<Long> demands = new ArrayList<>();
		demands.add(Long.valueOf("7894561230"));

		MarriageCertificate marriageCertificate = new MarriageCertificate();
		marriageCertificate.setApplicationNumber("egov6");
		marriageCertificate.setBridegroomPhoto("NotAvailable");
		marriageCertificate.setBridePhoto("NotAvailable");
		marriageCertificate.setCertificateDate(Long.valueOf("9874563210"));
		marriageCertificate.setCertificateNo("00015");
		marriageCertificate.setCertificatePlace("Bangalore");
		marriageCertificate.setCertificateType(CertificateType.REGISTRATION);
		marriageCertificate.setHusbandAddress("No.10 Dno13 AKM, Outdoors, Bangalore");
		marriageCertificate.setHusbandName("user2");
		marriageCertificate.setMarriageDate(Long.valueOf("9632587410"));
		marriageCertificate.setMarriageVenueAddress("FUNCTIONHALL");
		marriageCertificate.setRegnDate(Long.valueOf("1234567890"));
		marriageCertificate.setRegnNumber("egov6");
		marriageCertificate.setRegnSerialNo("6549873");
		marriageCertificate.setRegnVolumeNo("20");
		marriageCertificate.setTemplateVersion("2");
		marriageCertificate.setTenantId("ap.kurnool");
		marriageCertificate.setWifeAddress("No.10 Dno13 AKM, Outdoors, Bangalore");
		marriageCertificate.setWifeName("user6");

		ReissueCertAppl reissueCertAppl = new ReissueCertAppl();
		reissueCertAppl.setApplicantInfo(reissueApplicationInfo);
		reissueCertAppl.setApplicationNumber("appl6");
		reissueCertAppl.setApprovalDetails(approvalDetails);
		reissueCertAppl.setAuditDetails(auditDetails);
		reissueCertAppl.setCertificate(marriageCertificate);
		reissueCertAppl.setDemands(demands);
		reissueCertAppl.setDocuments(new ArrayList<>());
		reissueCertAppl.setId("6");
		reissueCertAppl.setIsActive(false);
		reissueCertAppl.setRegnNo("egov6");
		reissueCertAppl.setReissueApplStatus(ApplicationStatus.APPROVED);

		reissueCertAppl.setRejectionReason("notrejected");
		reissueCertAppl.setRemarks("goodremark");
		reissueCertAppl.setStateId("state6");
		reissueCertAppl.setTenantId("ap.kurnool");

		List<ReissueCertAppl> reissueCertAppls = new ArrayList<>();
		reissueCertAppls.add(reissueCertAppl);

		return reissueCertAppls;
	}

}
