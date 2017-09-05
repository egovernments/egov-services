package org.egov.tradelicense.persistence.entity;
import java.sql.Timestamp;

import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseApplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseApplicationEntity {

	public static final String TABLE_NAME = "egtl_license_application";
	public static final String SEQUENCE_NAME = "seq_egtl_license_application";
	
	private Long id;
	
	private String applicationNumber;
	
	private String tenantId;
	
	private String applicationType;
	
	private String status;
	
	private String state_id;
	
	private Timestamp applicationDate;
	
	private long licenseId;
	
	private Double licenseFee;
	
	private String fieldInspectionReport;
	
	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;
	
//	private static List<LicenseFeeDetail> feeDetails;
//
//	private static List<SupportDocument> supportDocuments;
//
//	private List<LicenseFeeDetailEntity> feeDetailEntitys;
//
//	private List<SupportDocumentEntity> supportDocumentEntitys;
	
	public LicenseApplication toDomain( ){
		
		LicenseApplication licenseApplication = new LicenseApplication();
		AuditDetails auditDetails = new AuditDetails();
		
		licenseApplication.setTenantId(this.tenantId );
		
		licenseApplication.setApplicationNumber(this.applicationNumber);
		
		licenseApplication.setStatus(this.status);
		
		licenseApplication.setState_id(this.state_id);
		
		licenseApplication.setApplicationType(this.applicationType);
		
		if (this.applicationDate != null) {

			licenseApplication.setApplicationDate((this.applicationDate.getTime()));
		}
		
		licenseApplication.setFieldInspectionReport(this.fieldInspectionReport);
		
		licenseApplication.setLicenseFee( this.licenseFee);
		
		
//		feeDetails = new ArrayList<LicenseFeeDetail>();
//
//		if (this.feeDetailEntitys != null) {
//
//			for (LicenseFeeDetailEntity feeDetailEntity : this.feeDetailEntitys) {
//
//				feeDetails.add(feeDetailEntity.toDomain());
//			}
//		}
//
//		licenseApplication.setFeeDetails(feeDetails);
//
//		supportDocuments = new ArrayList<SupportDocument>();
//
//		if (this.supportDocumentEntitys != null) {
//
//			for (SupportDocumentEntity supportDocumentEntity : this.supportDocumentEntitys) {
//
//				supportDocuments.add(supportDocumentEntity.toDomain());
//			}
//		}
//		licenseApplication.setSupportDocuments(supportDocuments);
		
		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);
		licenseApplication.setAuditDetails(auditDetails);
		
		
		return licenseApplication;
	}
	
	public LicenseApplicationEntity toEntity(LicenseApplication licenseApplication){
		
        this.tenantId = licenseApplication.getTenantId();
		
        this.setId(licenseApplication.getId());
        
		this.setApplicationNumber( licenseApplication.getApplicationNumber());
		
		if( licenseApplication.getApplicationType() != null ){
			
			this.setApplicationType( licenseApplication.getApplicationType().toString());
		}
		
		this.setStatus( licenseApplication.getStatus() == null ? null : licenseApplication.getStatus().toString());
		
		this.setState_id( licenseApplication.getState_id() == null ? null : licenseApplication.getState_id().toString());
		
		if (licenseApplication.getApplicationDate() != null) {

			this.setApplicationDate(new Timestamp(licenseApplication.getApplicationDate()));
		}
		
		this.setLicenseFee(licenseApplication.getLicenseFee());
		
		this.setFieldInspectionReport(licenseApplication.getFieldInspectionReport());
		
		this.setLicenseId( licenseApplication.getLicenseId());
		
		this.setCreatedBy( licenseApplication.getAuditDetails() == null ? null : licenseApplication.getAuditDetails().getCreatedBy() );
		
		this.setCreatedTime(  licenseApplication.getAuditDetails() == null ? System.currentTimeMillis() : licenseApplication.getAuditDetails().getCreatedTime());
		
		this.setLastModifiedBy(licenseApplication.getAuditDetails() == null ? null : licenseApplication.getAuditDetails().getLastModifiedBy());
		
		this.setLastModifiedTime(licenseApplication.getAuditDetails() == null ? System.currentTimeMillis() : licenseApplication.getAuditDetails().getLastModifiedTime());
		
		return this;
	}
}
