package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Vendor;

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
public class VendorEntity {

	private String vendorNo = null;

	private String tenantId = null;

	private String name = null;

	private String registrationNo = null;

	private String email = null;

	private String tinNumber = null;

	private String gst = null;

	private String phoneNo = null;

	private String contactNo = null;

	private String faxNumber = null;

	private String address = null;

	private String details = null;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;

	public Vendor toDomain() {

		Vendor vendor = new Vendor();
		vendor.setVendorNo(vendorNo);
		vendor.setTenantId(tenantId);
		vendor.setName(name);
		vendor.setRegistrationNo(registrationNo);
		vendor.setEmail(email);
		vendor.setTinNumber(tinNumber);
		vendor.setGst(gst);
		vendor.setPhoneNo(phoneNo);
		vendor.setContactNo(contactNo);
		vendor.setFaxNumber(faxNumber);
		vendor.setAddress(address);
		vendor.setDetails(details);
		vendor.setAuditDetails(new AuditDetails());
		vendor.getAuditDetails().setCreatedBy(createdBy);
		vendor.getAuditDetails().setCreatedTime(createdTime);
		vendor.getAuditDetails().setLastModifiedBy(lastModifiedBy);
		vendor.getAuditDetails().setLastModifiedTime(lastModifiedTime);

		return vendor;

	}

}
