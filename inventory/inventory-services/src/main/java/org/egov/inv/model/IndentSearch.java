package org.egov.inv.model;

import java.math.BigDecimal;
import java.util.List;

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
public class IndentSearch   {
	
	 private String tenantId;
	 private List<String> ids;
	 private String issueStore;
	 private Long indentDate;
	 private String indentNumber;
	 private String indentPurpose;
	 private String description;
	 private String indentStatus;
	 private BigDecimal totalIndentValue;
	 private Integer pageSize;
	 private Integer pageNumber;
	 private String sortBy;
	 //new -----
	 private String indentType;
	 private String inventoryType;
	 private String departmentId;
	 private String indentRaisedBy;
	 private String stateId;
	 private String searchPurpose;
}
