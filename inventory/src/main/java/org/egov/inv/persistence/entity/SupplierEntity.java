package org.egov.inv.persistence.entity;

import io.swagger.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierEntity {

	public static final String TABLE_NAME = "supplier";
	public static final String SEQUENCE_NAME = "seq_supplier";
	public static final String ALIAS = "supplier";

	private String id = null;

	private String supplierType = null;

	private String code = null;

	private String name = null;

	private String address = null;

	private String status = null;
	
	private Long inactivedate = null;

	private String supplierContactNo = null;

	private String faxNo = null;

	private String website = null;

	private String email = null;

	private String narration = null;

	private String panNo = null;

	private String tinNo = null;

	private String cstNo = null;

	private String vatNo = null;

	private String contactPerson = null;

	private String contactPersonNo = null;

	private String bankName = null;

	private String bankAcctNo = null;

	private String bankIfsc = null;

	private String bankMicr = null;

	private String createdBy = null;

	private Long createdTime = null;

	private String lastmodifiedBy = null;

	private Long lastmodifiedTime = null;
	
	private String tenantId;
	
	public Object toEntity(Supplier supplier) {
		this.id = supplier.getId();
		this.name = supplier.getName();
		this.code = supplier.getCode();
		this.supplierType = supplier.getSupplierType().getName();
		this.address = supplier.getAddress();
		this.status = supplier.getStatus().getName();
		this.inactivedate = supplier.getInActiveDate();
		this.supplierContactNo = supplier.getSupplierContactNo();
		this.faxNo = supplier.getFaxNo();
		this.website = supplier.getWebsite();
		this.email = supplier.getEmail();
		this.narration = supplier.getNarration();
		this.panNo = supplier.getPanNo();
		this.tinNo = supplier.getTinNo();
		this.cstNo = supplier.getCstNo();
		this.vatNo = supplier.getVatNo();
		this.contactPerson = supplier.getContactPerson();
		this.contactPersonNo = supplier.getContactPersonNo();
        this.bankName = supplier.getBankInfo().getName();
        this.bankAcctNo = supplier.getBankInfo().getAcctNo();
        this.bankIfsc = supplier.getBankInfo().getIfsc();
        this.bankMicr = supplier.getBankInfo().getMicr();
		this.createdBy = supplier.getAuditDetails().getCreatedBy();
		this.createdTime = supplier.getAuditDetails().getCreatedTime();
		this.lastmodifiedBy = supplier.getAuditDetails().getLastModifiedBy();
		this.lastmodifiedTime = supplier.getAuditDetails().getLastModifiedTime();
		this.tenantId = supplier.getAuditDetails().getTenantId();
		
		return this;
	}

}
