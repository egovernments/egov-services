package org.egov.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VacancyRemissionSearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String upicNumber;
	
	private String applicationNo;
	
	private String approvalDate;
	
	private Boolean isApproved;
}
