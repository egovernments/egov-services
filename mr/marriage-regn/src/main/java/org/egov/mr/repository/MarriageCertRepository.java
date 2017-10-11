package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.mr.model.MarriageCertificate;
import org.egov.mr.model.MarriageDocument;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.model.enums.CertificateType;
import org.egov.mr.repository.querybuilder.MarriageCertQueryBuilder;
import org.egov.mr.repository.rowmapper.MarriageCertRowMapper;
import org.egov.mr.service.CertificateNumberService;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.ReissueCertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MarriageCertRepository {

	public static final String CERTIFICATE_INSERT_QUERY = "INSERT INTO egmr_marriage_certificate("
			+ "id, certificateno, certificatedate, certificatetype, regnnumber, bridegroomphoto,"
			+ " bridephoto, husbandname, husbandaddress, wifename, wifeaddress, marriagedate, marriagevenueaddress, regndate,"
			+ " regnserialno, regnvolumeno,"
			// + " certificateplace, templateversion,"
			+ "createdby, createdtime, lastmodifiedby, lastmodifiedtime,"
			+ " applicationnumber, tenantid)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

	private final String Reissue_Cert_Insert_Query="INSERT INTO egmr_reissuecertificate(id, regnno, applicantname, applicantaddress,"
			+ " applicantmobileno, applicantfee, applicantaadhaar, applicationnumber, reissueapplstatus, stateid,"
			+ " approvaldepartment, approvaldesignation, approvalassignee, approvalaction, approvalstatus, approvalcomments,"
			+ " demands, rejectionreason, remarks, isactive, createdby, createdtime, lastmodifiedby,"
			+ " lastmodifiedtime, tenantid)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private final String Reissue_Update_Query="UPDATE egmr_reissuecertificate SET regnno=?, applicantname=?,"
			+ " applicantaddress=?, applicantmobileno=?, applicantfee=?, applicantaadhaar=?, applicationnumber=?,"
			+ " reissueapplstatus=?, stateid=?, approvaldepartment=?, approvaldesignation=?, approvalassignee=?,"
			+ " approvalaction=?, approvalstatus=?, approvalcomments=?, demands=?, rejectionreason=?, remarks=?,"
			+ " isactive=?,lastmodifiedby=?, lastmodifiedtime=? WHERE id=? AND tenantid=?;"; 
	
	private final String Docs_insert_Query="INSERT INTO egmr_documents(id, reissuecertificateid,"
			+ " documenttypecode, location, createdby, createdtime, lastmodifiedby, lastmodifiedtime,"
			+ " tenantid)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"; 
	
	private final String Docs_Update_Query="UPDATE egmr_documents SET documenttypecode=?,"
			+ " location=?, lastmodifiedby=?, lastmodifiedtime=?"
			+ " WHERE tenantid=? and reissuecertificateid=?;";
	
	public final String GET_APPLICATION_NO_QUERY = "SELECT applicationnumber FROM egmr_marriage_certificate"
			+ " WHERE tenantid = ?";
	public final String create_Certificate_Query="INSERT INTO egmr_marriage_certificate(certificateno, certificatedate,"
			+ " certificatetype, regnnumber, bridegroomphoto, bridephoto, husbandname, husbandaddress, wifename,"
			+ " wifeaddress, marriagedate, marriagevenueaddress, regndate, regnserialno, regnvolumeno, certificateplace,"
			+ " templateversion, applicationnumber, tenantid)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	@Autowired
	private CertificateNumberService certificateNumberService;

	@Autowired
	private MarriageCertQueryBuilder marriageCertQueryBuilder;

	@Autowired
	private MarriageCertRowMapper marriageCertRowMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insert(MarriageRegn marriageRegn) {
		String certificateNo = certificateNumberService.generateCertificateNumber();
		Object[] obj = new Object[] {marriageRegn.getId(), certificateNo, new Date().getTime(), CertificateType.REGISTRATION.toString(),
				marriageRegn.getRegnNumber(), marriageRegn.getBridegroom().getPhoto(),
				marriageRegn.getBride().getPhoto(), marriageRegn.getBridegroom().getName(),
				marriageRegn.getBridegroom().getResidenceAddress(), marriageRegn.getBride().getName(),
				marriageRegn.getBride().getResidenceAddress(), marriageRegn.getMarriageDate(),
				marriageRegn.getPlaceOfMarriage(), marriageRegn.getRegnDate(), marriageRegn.getSerialNo(),
				marriageRegn.getVolumeNo(), 1, new Date().getTime(), 1, new Date().getTime(),
				// marriageRegn.getCertificatePlace(),
				// marriageRegn.getTemplateVersion(),
				marriageRegn.getApplicationNumber(), marriageRegn.getTenantId() };
		jdbcTemplate.update(CERTIFICATE_INSERT_QUERY, obj);
	}

	public List<ReissueCertAppl> findForCriteria(MarriageCertCriteria marriageCertCriteria) {
		log.info("MarriageCertRepository  marriageCertCriteria:::" + marriageCertCriteria.getTenantId());
		List<Object> preparedStatementValues = new ArrayList<Object>();
		log.info("MarriageCertRepository  preparedStatementValues:::" + preparedStatementValues);
		String queryStr = marriageCertQueryBuilder.getQuery(marriageCertCriteria, preparedStatementValues);
		log.info("queryStr;;"+queryStr);
		List<ReissueCertAppl> marriageCert = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				marriageCertRowMapper);
		return marriageCert;

	}

	public void createDoc(ReissueCertRequest reissueAppRequest){
		List<MarriageDocument> documents=reissueAppRequest.getReissueApplication().getDocuments();
		
		jdbcTemplate.batchUpdate(Docs_insert_Query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				MarriageDocument mrDoc=documents.get(index);
				
				ps.setString(1,mrDoc.getId());
				ps.setString(2, reissueAppRequest.getReissueApplication().getId());
				ps.setString(3, mrDoc.getDocumentType());
				ps.setString(4, mrDoc.getLocation());
				ps.setString(5, reissueAppRequest.getRequestInfo().getDid());
				ps.setLong	(6, new Date().getTime());
				ps.setString(7, reissueAppRequest.getRequestInfo().getDid());
				ps.setLong	(8, new Date().getTime());
				ps.setString(9, mrDoc.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return documents.size();
			}
		});
	}
	
	public void updateDoc(ReissueCertRequest reissueAppRequest){
		List<MarriageDocument> documents=reissueAppRequest.getReissueApplication().getDocuments();
		
		jdbcTemplate.batchUpdate(Docs_Update_Query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				MarriageDocument mrDoc=documents.get(index);
				
				ps.setString(1, mrDoc.getDocumentType());
				ps.setString(2, mrDoc.getLocation());
				ps.setString(3, reissueAppRequest.getRequestInfo().getDid());
				ps.setLong	(4, new Date().getTime());
				ps.setString(5, mrDoc.getTenantId());
				ps.setString(6, reissueAppRequest.getReissueApplication().getId());
			}

			@Override
			public int getBatchSize() {
				return documents.size();
			}
		});
	}
	
	public void createReissue(ReissueCertRequest reissueAppRequest) {

		ReissueCertAppl reissueApp=reissueAppRequest.getReissueApplication();
			
		try {
			jdbcTemplate.update(Reissue_Cert_Insert_Query,new PreparedStatementSetter(){

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1,reissueApp.getId());
					ps.setString(2,reissueApp.getRegnNo());
					ps.setString(3,reissueApp.getApplicantInfo().getName());
					ps.setString(4,reissueApp.getApplicantInfo().getAddress());
					ps.setString(5,reissueApp.getApplicantInfo().getMobileNo());
					ps.setBigDecimal(6,reissueApp.getApplicantInfo().getFee());
					ps.setString(7,reissueApp.getApplicantInfo().getAadhaar());
					ps.setString(8,reissueApp.getApplicationNumber());
					ps.setString(9,reissueApp.getReissueApplStatus().toString());
					ps.setString(10,reissueApp.getStateId());
					ps.setLong	(11,reissueApp.getApprovalDetails().getDepartment());
					ps.setLong	(12,reissueApp.getApprovalDetails().getDesignation());
					ps.setLong	(13,reissueApp.getApprovalDetails().getAssignee());
					ps.setString(14,reissueApp.getApprovalDetails().getAction());
					ps.setString(15,reissueApp.getApprovalDetails().getStatus());
					ps.setString(16,reissueApp.getApprovalDetails().getComments());
					ps.setLong	(17,reissueApp.getDemands().get(0));
					ps.setString(18,reissueApp.getRejectionReason());
					ps.setString(19,reissueApp.getRemarks());
					ps.setBoolean(20,reissueApp.getIsActive());
					ps.setString(21,reissueAppRequest.getRequestInfo().getDid());
					ps.setLong	(22,new Date().getTime());
					ps.setString(23,reissueAppRequest.getRequestInfo().getDid());
					ps.setLong	(24,new Date().getTime());
					ps.setString(25,reissueApp.getTenantId());
					
				}
				
			});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void updateReissue(ReissueCertRequest reissueAppRequest) {
		ReissueCertAppl reissueApp=reissueAppRequest.getReissueApplication();
			
		try {
			jdbcTemplate.update(Reissue_Update_Query, new PreparedStatementSetter(){

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1,reissueApp.getRegnNo());
					ps.setString(2,reissueApp.getApplicantInfo().getName());
					ps.setString(3,reissueApp.getApplicantInfo().getAddress());
					ps.setString(4,reissueApp.getApplicantInfo().getMobileNo());
					ps.setBigDecimal(5,reissueApp.getApplicantInfo().getFee());
					ps.setString(6,reissueApp.getApplicantInfo().getAadhaar());
					ps.setString(7,reissueApp.getApplicationNumber());
					ps.setString(8,reissueApp.getReissueApplStatus().toString());
					ps.setString(9,reissueApp.getStateId());
					ps.setLong	(10,reissueApp.getApprovalDetails().getDepartment());
					ps.setLong	(11,reissueApp.getApprovalDetails().getDesignation());
					ps.setLong	(12,reissueApp.getApprovalDetails().getAssignee());
					ps.setString(13,reissueApp.getApprovalDetails().getAction());
					ps.setString(14,reissueApp.getApprovalDetails().getStatus());
					ps.setString(15,reissueApp.getApprovalDetails().getComments());
					ps.setLong	(16,reissueApp.getDemands().get(0));
					ps.setString(17,reissueApp.getRejectionReason());
					ps.setString(18,reissueApp.getRemarks());
					ps.setBoolean(19,reissueApp.getIsActive());
					ps.setString(20,reissueAppRequest.getRequestInfo().getDid());
					ps.setLong	(21,new Date().getTime());
					ps.setString(22,reissueApp.getId());
					ps.setString(23,reissueApp.getTenantId());
				}
				
			});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void createCert(ReissueCertAppl reissueApp){
		
		try {
			jdbcTemplate.update(create_Certificate_Query, new PreparedStatementSetter(){

				MarriageCertificate marriageCertificate=reissueApp.getCertificate();
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1,marriageCertificate.getCertificateNo());
					ps.setLong(2,marriageCertificate.getCertificateDate());
					ps.setString(3,marriageCertificate.getCertificateType().toString());
					ps.setString(4,marriageCertificate.getRegnNumber());
					ps.setString(5,marriageCertificate.getBridegroomPhoto());
					ps.setString(6,marriageCertificate.getBridePhoto());
					ps.setString(7,marriageCertificate.getHusbandName());
					ps.setString(8,marriageCertificate.getHusbandAddress());
					ps.setString(9,marriageCertificate.getWifeName());
					ps.setString(10,marriageCertificate.getWifeAddress());
					ps.setLong	(11,marriageCertificate.getMarriageDate());
					ps.setString(12,marriageCertificate.getMarriageVenueAddress());
					ps.setLong	(13,marriageCertificate.getRegnDate());
					ps.setString(14,marriageCertificate.getRegnSerialNo());
					ps.setString(15,marriageCertificate.getRegnVolumeNo());
					ps.setString(16,marriageCertificate.getCertificatePlace());
					ps.setString(17,marriageCertificate.getTemplateVersion());
					ps.setString(18,marriageCertificate.getApplicationNumber());
					ps.setString(19,marriageCertificate.getTenantId());
					
				}
				
			});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
	}
}
