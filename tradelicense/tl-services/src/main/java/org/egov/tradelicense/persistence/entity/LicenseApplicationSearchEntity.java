package org.egov.tradelicense.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseApplicationSearch;
import org.egov.tradelicense.domain.model.LicenseFeeDetailSearch;
import org.egov.tradelicense.domain.model.SupportDocumentSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseApplicationSearchEntity {

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

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	private Double licenseFee;

	private String fieldInspectionReport;

	private String statusName;

	private static List<LicenseFeeDetailSearch> feeDetails;

	private static List<SupportDocumentSearch> supportDocuments;

	private List<LicenseFeeDetailSearchEntity> feeDetailEntitys;

	private List<SupportDocumentSearchEntity> supportDocumentEntitys;

	public LicenseApplicationSearch toDomain() {

		LicenseApplicationSearch licenseAppSearch = new LicenseApplicationSearch();

		AuditDetails auditDetails = new AuditDetails();

		licenseAppSearch.setId(this.id);
		licenseAppSearch.setTenantId(this.tenantId);
		licenseAppSearch.setLicenseId(this.licenseId);
		licenseAppSearch.setApplicationDate(this.applicationDate);
		licenseAppSearch.setApplicationNumber(this.applicationNumber);
		licenseAppSearch.setApplicationType(this.applicationType);
		licenseAppSearch.setState_id(this.state_id);
		licenseAppSearch.setStatus(this.status);
		auditDetails.setCreatedBy(this.createdBy);
		auditDetails.setCreatedTime(this.createdTime);
		auditDetails.setLastModifiedBy(this.lastModifiedBy);
		auditDetails.setLastModifiedTime(this.lastModifiedTime);
		licenseAppSearch.setAuditDetails(auditDetails);

		licenseAppSearch.setFieldInspectionReport(this.fieldInspectionReport);
		licenseAppSearch.setLicenseFee(this.licenseFee);
		licenseAppSearch.setStatusName(this.statusName);

		feeDetails = new ArrayList<LicenseFeeDetailSearch>();

		if (this.feeDetailEntitys != null) {

			for (LicenseFeeDetailSearchEntity feeDetailEntity : this.feeDetailEntitys) {

				feeDetails.add(feeDetailEntity.toDomain());
			}
		}

		licenseAppSearch.setFeeDetails(feeDetails);

		supportDocuments = new ArrayList<SupportDocumentSearch>();

		if (this.supportDocumentEntitys != null) {

			for (SupportDocumentSearchEntity supportDocumentEntity : this.supportDocumentEntitys) {

				supportDocuments.add(supportDocumentEntity.toDomain());
			}
		}

		licenseAppSearch.setSupportDocuments(supportDocuments);

		return licenseAppSearch;

	}

	public LicenseApplicationSearchEntity toEntity(LicenseApplicationSearch licenseApplicationSearch) {

		this.setId(licenseApplicationSearch.getId());

		this.tenantId = licenseApplicationSearch.getTenantId();

		AuditDetails auditDetails = licenseApplicationSearch.getAuditDetails();

		this.setApplicationNumber(licenseApplicationSearch.getApplicationNumber());

		if (licenseApplicationSearch.getApplicationType() != null) {

			this.setApplicationType(licenseApplicationSearch.getApplicationType().toString());
		}

		if (licenseApplicationSearch.getStatus() != null) {

			this.setStatus(licenseApplicationSearch.getStatus().toString());
		}

		this.setStatusName(licenseApplicationSearch.getStatusName());

		this.applicationDate = licenseApplicationSearch.getApplicationDate();

		this.setFieldInspectionReport(licenseApplicationSearch.getFieldInspectionReport());

		this.setLicenseId(licenseApplicationSearch.getLicenseId());

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		feeDetails = licenseApplicationSearch.getFeeDetails();

		supportDocuments = licenseApplicationSearch.getSupportDocuments();

		return this;
	}
}
