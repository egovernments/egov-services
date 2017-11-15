package org.egov.inv.persistence.entity;

import io.swagger.model.AuditDetails;
import io.swagger.model.Bank;
import io.swagger.model.CommonEnum;
import io.swagger.model.Supplier;
import io.swagger.model.Supplier.StatusEnum;
import io.swagger.model.Supplier.TypeEnum;
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

	private String type = null;

	private String code = null;

	private String name = null;

	private String address = null;

	private String status = null;
	
	private Boolean active = null;

	private Long inactivedate = null;

	private String contactno = null;

	private String faxNo = null;

	private String website = null;

	private String email = null;

	private String description = null;

	private String panNo = null;

	private String tinNo = null;

	private String cstNo = null;

	private String vatNo = null;
	
	private String gstNo = null;

	private String contactPerson = null;

	private String contactPersonNo = null;

	private String bankCode = null;
	
	private String bankBranch = null;

	private String bankAcctNo = null;

	private String bankIfsc = null;

	private String bankMicr = null;

	private String createdBy = null;

	private Long createdTime = null;

	private String lastmodifiedBy = null;

	private Long lastmodifiedTime = null;

	private String tenantId;

	public Object toEntity(Supplier supplier) {
		if(supplier.getId() !=null)
		this.id = supplier.getId();
		this.name = supplier.getName();
		this.code = supplier.getCode();
		if(supplier.getType() != null)
		this.type = supplier.getType().name();
		this.address = supplier.getAddress();
		if(supplier.getStatus() != null)
		this.status = supplier.getStatus().name();
		if(supplier.getInActiveDate() != null)
		this.inactivedate = supplier.getInActiveDate();
		this.contactno = supplier.getContactNo();
		if(supplier.getFaxNo() != null)
		this.faxNo = supplier.getFaxNo();
		if(supplier.getWebsite() != null)
		this.website = supplier.getWebsite();
		if(supplier.getActive() !=null)
		this.active = supplier.getActive();
		if(supplier.getEmail() !=null)
		this.email = supplier.getEmail();
		if(supplier.getDescription() != null)
		this.description = supplier.getDescription();
		if(supplier.getPanNo() != null)
		this.panNo = supplier.getPanNo();
		if(supplier.getTinNo() != null)
		this.tinNo = supplier.getTinNo();
		if(supplier.getCstNo() != null)
		this.cstNo = supplier.getCstNo();
		if(supplier.getVatNo() != null)
		this.vatNo = supplier.getVatNo();
		if(supplier.getGstNo() != null)
		this.gstNo = supplier.getGstNo();
		if(supplier.getContactPerson() != null)
		this.contactPerson = supplier.getContactPerson();
		if(supplier.getContactPersonNo() != null)
		this.contactPersonNo = supplier.getContactPersonNo();
		this.bankCode = supplier.getBankCode();
		if(supplier.getBankBranch() != null)
		this.bankBranch = supplier.getBankBranch();
		this.bankAcctNo = supplier.getAcctNo();
		this.bankIfsc = supplier.getIfsc();
		if(supplier.getMicr() != null)
		this.bankMicr = supplier.getMicr();
		this.createdBy = supplier.getAuditDetails().getCreatedBy();
		this.createdTime = supplier.getAuditDetails().getCreatedTime();
		this.lastmodifiedBy = supplier.getAuditDetails().getLastModifiedBy();
		this.lastmodifiedTime = supplier.getAuditDetails().getLastModifiedTime();
		this.tenantId = supplier.getAuditDetails().getTenantId();

		return this;
	}

	public Supplier toDomain() {
		Supplier supplier = new Supplier();
		supplier.setId(id);
		supplier.setCode(code);
		supplier.setName(name);
		if(type != null)
		supplier.setType(TypeEnum.valueOf(type));
		supplier.setAddress(address);
		supplier.setTenantId(tenantId);
		supplier.setActive(active);
		if(status != null)
		supplier.setStatus(StatusEnum.valueOf(status));
		supplier.setInActiveDate(inactivedate);
	    supplier.setContactNo(contactno);
    	supplier.setFaxNo(faxNo);
		supplier.setWebsite(website);
		supplier.setEmail(email);
	    supplier.setDescription(description);
		supplier.setPanNo(panNo);
		supplier.setTinNo(tinNo);
		supplier.setVatNo(vatNo);
		supplier.setGstNo(gstNo);
		supplier.setCstNo(cstNo);
		supplier.setContactPerson(contactPerson);
		supplier.setContactPersonNo(contactPersonNo);
	    supplier.setBankCode(bankCode);
	    supplier.setBankBranch(bankBranch);
	    supplier.setAcctNo(bankAcctNo);
	    supplier.setIfsc(bankIfsc);
	    supplier.setMicr(bankMicr);
    	AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(createdBy);
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedBy(lastmodifiedBy);
		auditDetails.setLastModifiedTime(lastmodifiedTime);
		auditDetails.setTenantId(tenantId);
		supplier.setAuditDetails(auditDetails);
		return supplier;

	}

}
