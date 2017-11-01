package org.egov.lcms.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdvocateSearchCriteria {
	

	@NotNull
	@NotEmpty
	private String tenantId;
	
	private String[] code;
	
	private Boolean isIndividual;
	
	private String advocateName;
	
	private String organizationName;
	
	private Boolean isActive;
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String[] sort;
	
	private Integer offSet;
}
