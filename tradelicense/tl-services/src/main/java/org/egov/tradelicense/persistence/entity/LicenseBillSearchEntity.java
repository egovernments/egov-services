package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseApplicationBill;
import org.egov.tradelicense.domain.model.LicenseBillSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseBillSearchEntity {

	public static final String TABLE_NAME = "egtl_tradelicense_bill";
	public static final String SEQUENCE_NAME = "seq_egtl_tradelicense_bill";

	private Long id;

	private Long applicationId;

	private String billId;

	private String applicationBillId;

	private String tenantId;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public LicenseApplicationBill toDomain() {

		LicenseApplicationBill licenseBill = new LicenseApplicationBill();
		AuditDetails auditDetails = new AuditDetails();

		licenseBill.setId(this.id);

		licenseBill.setTenantId(this.tenantId);

		licenseBill.setApplicationId(this.applicationId);

		licenseBill.setBillId(this.billId);

		licenseBill.setApplicationId(this.applicationId);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		licenseBill.setAuditDetails(auditDetails);

		return licenseBill;
	}

	public LicenseBillSearchEntity toEntity(LicenseBillSearch licenseBill) {

		AuditDetails auditDetails = licenseBill.getAuditDetails();

		this.id = licenseBill.getId();

		this.applicationId = licenseBill.getApplicationId();

		this.billId = licenseBill.getBillId();

		this.applicationBillId = licenseBill.getApplicationBillId();

		this.tenantId = licenseBill.getTenantId();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? System.currentTimeMillis() : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? System.currentTimeMillis()
				: auditDetails.getLastModifiedTime();

		return this;
	}
}