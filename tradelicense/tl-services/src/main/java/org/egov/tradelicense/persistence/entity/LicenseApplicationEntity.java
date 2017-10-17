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

	private Long licenseId;

	private Double licenseFee;

	private String fieldInspectionReport;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public LicenseApplication toDomain() {

		LicenseApplication licenseApplication = new LicenseApplication();

		AuditDetails auditDetails = new AuditDetails();

		licenseApplication.setId(this.id);

		licenseApplication.setTenantId(this.tenantId);

		licenseApplication.setApplicationNumber(this.applicationNumber);

		licenseApplication.setStatus(this.status);

		licenseApplication.setState_id(this.state_id);

		licenseApplication.setApplicationType(this.applicationType);

		if (this.applicationDate != null) {

			licenseApplication.setApplicationDate(this.applicationDate.getTime());
		}

		licenseApplication.setFieldInspectionReport(this.fieldInspectionReport);

		licenseApplication.setLicenseId(this.licenseId);

		licenseApplication.setLicenseFee(this.licenseFee);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		licenseApplication.setAuditDetails(auditDetails);

		return licenseApplication;
	}

	public LicenseApplicationEntity toEntity(LicenseApplication licenseApplication) {

		AuditDetails auditDetails = licenseApplication.getAuditDetails();

		this.tenantId = licenseApplication.getTenantId();

		this.setId(licenseApplication.getId());

		this.setApplicationNumber(licenseApplication.getApplicationNumber());

		if (licenseApplication.getApplicationType() != null) {

			this.setApplicationType(licenseApplication.getApplicationType().toString());
		}

		this.setStatus(licenseApplication.getStatus() == null ? null : licenseApplication.getStatus().toString());

		this.setState_id(licenseApplication.getState_id() == null ? null : licenseApplication.getState_id().toString());

		if (licenseApplication.getApplicationDate() != null) {

			this.setApplicationDate(new Timestamp(licenseApplication.getApplicationDate()));
		}

		this.setLicenseFee(licenseApplication.getLicenseFee());

		this.setFieldInspectionReport(licenseApplication.getFieldInspectionReport());

		this.setLicenseId(licenseApplication.getLicenseId());

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null || auditDetails.getCreatedTime() == null) ? System.currentTimeMillis()
				: auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null || auditDetails.getLastModifiedTime() == null)
				? System.currentTimeMillis() : auditDetails.getLastModifiedTime();

		return this;
	}
}
