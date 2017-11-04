package org.egov.inv.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SupplierGetRequest {
	private List<String> ids;

	private String code;

	private String name;

	private String supplyType;

	private String address;

	private String status;

	private Long inActiveDate;

	private String supplierContactNo;

	private String faxNo;

	private String website;

	private String email;

	private String panNo;

	private String tinNo;

	private String cstNo;

	private String vatNo;
	
	private String bankCode;
	
	private String bankBranchCode;
	
	private String gstNo;

	private String contactPerson;

	private String contactPersonNo;

	private String bankAccNo;

	private String bankIfsc;

	private Integer pageSize;

	private Integer offset;

	private Integer pageNumber;

	private String sortBy;

	private String tenantId;

}
