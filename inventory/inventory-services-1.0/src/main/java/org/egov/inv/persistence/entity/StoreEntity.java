package org.egov.inv.persistence.entity;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Department;
import org.egov.inv.model.Employee;
import org.egov.inv.model.Location;
import org.egov.inv.model.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class StoreEntity   {
	public static final String TABLE_NAME = "store";
	private String id;
	private String tenantId;
	private String code;
	private String name;
	private String description;
	private String department;
	private String officeLocation;
	private String billingAddress;
	private String deliveryAddress;
	private String contactNo1;
	private String contactNo2;
	private String email;
	private String storeInCharge;
	private Boolean isCentralStore;
	private Boolean active;
	private String createdBy;
	private String lastModifiedBy;
	private Long createdTime;
	private Long lastModifiedTime;


	public Store toDomain() {
		Store store = new Store();
		store.setId(this.id);
		store.setTenantId(this.tenantId);
		store.setCode(this.code);
		store.setName(this.name);
		store.setDescription(this.description);
		store.setDepartment(new Department().id(department));
		store.setOfficeLocation(new Location().id(officeLocation));
		store.setBillingAddress(this.billingAddress);
		store.setDeliveryAddress(this.deliveryAddress);
		store.setContactNo1(this.contactNo1);
		store.setContactNo2(this.contactNo2);
		store.setEmail(this.email);
		store.setStoreInCharge(new Employee().id(storeInCharge));
		store.setIsCentralStore(this.isCentralStore);
		store.setActive(this.active);
		AuditDetails auditDetail = new AuditDetails()
				.createdBy(createdBy)
				.lastModifiedBy(lastModifiedBy)
				.createdTime(createdTime)
				.lastModifiedTime(lastModifiedTime);
		store.setAuditDetails(auditDetail);
		return store;
	}

	public StoreEntity toEntity(Store store) {
		this.id = store.getId();
		this.tenantId = store.getTenantId();
		this.code = store.getCode();
		this.name = store.getName();
		this.description = store.getDescription();
		this.department = store.getDepartment() != null ? store.getDepartment().getId() : null;
		this.officeLocation = store.getOfficeLocation() != null ? store.getOfficeLocation().getId() : null;
		this.billingAddress = store.getBillingAddress();
		this.deliveryAddress = store.getDeliveryAddress();
		this.contactNo1 = store.getContactNo1();
		this.contactNo2 = store.getContactNo2();
		this.email = store.getEmail();
		this.storeInCharge = store.getStoreInCharge() != null ? store.getStoreInCharge().getId() : null;
		this.isCentralStore = store.getIsCentralStore();
		this.active = store.getActive();
		this.createdBy=store.getAuditDetails().getCreatedBy();
		this.lastModifiedBy=store.getAuditDetails().getLastModifiedBy();
		this.createdTime=store.getAuditDetails().getCreatedTime();
		this.lastModifiedTime=store.getAuditDetails().getLastModifiedTime();
		return this;
	}

}
