package org.egov.mr.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.ReissueApplicantInfo;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.CertificateType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MarriageCertRowMapper implements ResultSetExtractor<List<ReissueCertAppl>> {

	@Override
	public List<ReissueCertAppl> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<String, ReissueCertAppl> reissuecertmap = new HashMap<>();
		while (rs.next()) {

			ReissueApplicantInfo reissueApplicantInfo = new ReissueApplicantInfo();
			ApprovalDetails approvalDetails = new ApprovalDetails();
			AuditDetails auditDetails = new AuditDetails();
			MarriageDocument marriageDocument = new MarriageDocument();
			MarriageCertificate marriageCertificate = new MarriageCertificate();

			List<Long> demands = new ArrayList<>();
			try {
				String reissueId = rs.getString("rc_id");
				log.info("MarriagecertRowMapper  reissueId;;" + reissueId);
				ReissueCertAppl reissueCertAppl = reissuecertmap.get(reissueId);

				if (reissueCertAppl == null) {

					reissueCertAppl = new ReissueCertAppl();
					reissueCertAppl.setId(rs.getString("rc_id"));
					reissueCertAppl.setRegnNo(rs.getString("rc_regnno"));
					reissueCertAppl.setApplicationNumber(rs.getString("rc_applicationnumber"));
					reissueCertAppl.setTenantId(rs.getString("rc_tenantid"));
					reissueCertAppl
							.setReissueApplStatus(ApplicationStatus.fromValue(rs.getString("rc_reissueapplstatus")));
					reissueCertAppl.setStateId(rs.getString("rc_stateid"));
					demands.add(rs.getLong("rc_demands"));
					reissueCertAppl.setDemands(demands);

					reissueCertAppl.setRejectionReason(rs.getString("rc_rejectionReason"));
					reissueCertAppl.setRemarks(rs.getString("rc_remarks"));
					reissueCertAppl.setIsActive(rs.getBoolean("rc_isactive"));

					reissueApplicantInfo.setName(rs.getString("rc_applicantname"));
					reissueApplicantInfo.setAddress(rs.getString("rc_applicantaddress"));
					reissueApplicantInfo.setMobileNo(rs.getString("rc_applicantmobileno"));
					reissueApplicantInfo.setFee(rs.getBigDecimal("rc_applicantfee"));
					reissueApplicantInfo.setAadhaar(rs.getString("rc_applicantaadhaar"));
					reissueCertAppl.setApplicantInfo(reissueApplicantInfo);

					approvalDetails.setAction(rs.getString("rc_approvalaction"));
					approvalDetails.setAssignee(rs.getLong("rc_approvalassignee"));
					approvalDetails.setComments(rs.getString("rc_approvalcomments"));
					approvalDetails.setDepartment(rs.getLong("rc_approvaldepartment"));
					approvalDetails.setDesignation(rs.getLong("rc_approvaldesignation"));
					approvalDetails.setStatus(rs.getString("rc_approvalstatus"));
					reissueCertAppl.setApprovalDetails(approvalDetails);

					auditDetails.setCreatedBy(rs.getString("rc_createdby"));
					auditDetails.setCreatedTime(rs.getLong("rc_createdtime"));
					auditDetails.setLastModifiedBy(rs.getString("rc_lastmodifiedby"));
					auditDetails.setLastModifiedTime(rs.getLong("rc_lastmodifiedtime"));
					reissueCertAppl.setAuditDetails(auditDetails);
					reissueCertAppl.setDocuments(new ArrayList<>());
					
					log.info("MarriagecertRowMapper  reissueCertAppl;;;" + reissueCertAppl);

					reissuecertmap.put(reissueId, reissueCertAppl);
					log.info("MarriagecertRowMapper  reissuecertmap;;;" + reissuecertmap);

				}

				marriageDocument.setId(rs.getString("ds_id"));
				marriageDocument.setDocumentType(rs.getString("ds_documenttypecode"));
				marriageDocument.setLocation(rs.getString("ds_location"));
				marriageDocument.setReissueCertificateId(rs.getString("ds_reissuecertificateid"));
				marriageDocument.setTenantId(rs.getString("ds_tenantid"));
				auditDetails.setCreatedBy(rs.getString("ds_createdby"));
				auditDetails.setCreatedTime(rs.getLong("ds_createdtime"));
				auditDetails.setLastModifiedBy(rs.getString("ds_lastmodifiedby"));
				auditDetails.setLastModifiedTime(rs.getLong("ds_lastmodifiedtime"));
				marriageDocument.setAuditDetails(auditDetails);

				marriageCertificate.setCertificateNo(rs.getString("mc_certificateno"));
				marriageCertificate.setCertificateDate(rs.getLong("mc_certificatedate"));
				marriageCertificate.setCertificateType(CertificateType.fromValue(rs.getString("mc_certificatetype")));
				marriageCertificate.setRegnNumber(rs.getString("mc_regnnumber"));
				marriageCertificate.setBridegroomPhoto(rs.getString("mc_bridegroomphoto"));
				marriageCertificate.setBridePhoto(rs.getString("mc_bridephoto"));
				marriageCertificate.setHusbandName(rs.getString("mc_husbandname"));
				marriageCertificate.setHusbandAddress(rs.getString("mc_husbandaddress"));
				marriageCertificate.setWifeName(rs.getString("mc_wifename"));
				marriageCertificate.setWifeAddress(rs.getString("mc_wifeaddress"));
				marriageCertificate.setMarriageDate(rs.getLong("mc_marriagedate"));
				marriageCertificate.setMarriageVenueAddress(rs.getString("mc_marriagevenueaddress"));
				marriageCertificate.setRegnDate(rs.getLong("mc_regndate"));
				marriageCertificate.setRegnSerialNo(rs.getString("mc_regnserialno"));
				marriageCertificate.setRegnVolumeNo(rs.getString("mc_regnvolumeno"));
				marriageCertificate.setCertificatePlace(rs.getString("mc_certificateplace"));
				marriageCertificate.setTemplateVersion(rs.getString("mc_templateversion"));
				marriageCertificate.setApplicationNumber(rs.getString("mc_applicationnumber"));
				marriageCertificate.setTenantId(rs.getString("mc_tenantid"));
				
				if (reissueCertAppl.getRegnNo().equals(marriageCertificate.getRegnNumber())) {
					reissueCertAppl.setCertificate(marriageCertificate);
				}
		
				if (reissueCertAppl.getId().equals(marriageDocument.getReissueCertificateId())) {
					reissueCertAppl.getDocuments().add(marriageDocument);
				}
				log.info("reissueCertAppl.getCertificates()  " + marriageCertificate.getRegnNumber());
				log.info("reissueCertAppl.getDocuments()  " + reissueCertAppl.getDocuments());

			} catch (Exception e) {
				log.info("exception in reissuecertificate : " + e);
				throw new RuntimeException("error while mapping object from result set : " + e);
			}

		}
		return new ArrayList<>(reissuecertmap.values());
	}
}