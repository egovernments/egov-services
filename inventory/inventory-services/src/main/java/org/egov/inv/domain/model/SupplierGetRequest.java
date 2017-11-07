package org.egov.inv.domain.model;

import java.util.List;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SupplierGetRequest  {
	private List<String> id;

	private List<String> code;

	private String name;

	private String type;

	private String status;
	
	private Boolean active;

	private Long inActiveDate;

	private String contactNo;

	private String faxNo;

	private String website;

	private String email;

	private String panNo;

	private String tinNo;

	private String cstNo;

	private String vatNo;
	
	private String bankCode;
	
	private String bankBranch;
	
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
