package org.egov.tradelicense.persistence.entity;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.LicenseBill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseBillEntity {

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

	public LicenseBill toDomain() {

		LicenseBill licenseBill = new LicenseBill();
		AuditDetails auditDetails = new AuditDetails();

		licenseBill.setId(this.id);

		licenseBill.setTenantId(this.tenantId);

		licenseBill.setApplicationId(this.applicationId);

		licenseBill.setBillId(this.billId);

		licenseBill.setApplicationBillId(this.applicationBillId);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		licenseBill.setAuditDetails(auditDetails);

		return licenseBill;
	}

	public LicenseBillEntity toEntity(LicenseBill licenseBill) {

		AuditDetails auditDetails = licenseBill.getAuditDetails();

		this.id = licenseBill.getId();

		this.applicationId = licenseBill.getApplicationId();

		this.billId = licenseBill.getBillId();

		this.applicationBillId = licenseBill.getApplicationBillId();

		this.tenantId = licenseBill.getTenantId();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails != null && auditDetails.getCreatedTime() != null)
				? auditDetails.getCreatedTime() : System.currentTimeMillis();

		this.lastModifiedTime = (auditDetails != null && auditDetails.getLastModifiedTime() != null)
				? auditDetails.getLastModifiedTime() : System.currentTimeMillis();

		return this;
	}
}