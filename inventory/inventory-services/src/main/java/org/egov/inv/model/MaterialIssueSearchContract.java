package org.egov.inv.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MaterialIssueSearchContract {
	
	private String tenantId;
	
	private List<String> id;
	
	private String fromStore;
	
	private String toStore;
	
	private String issueNoteNumber;
	
	private String issuePurpose;
	
	private Long issueDate;

	private String supplier;

	private String materialIssueStatus;
	
	private String description;
	
	private BigDecimal totalIssueValue;
	
	private Boolean scrapCreated;

	private Integer pageNumber;
		
	private String sortBy;
	
	private Integer pageSize;
	

}
