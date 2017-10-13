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
public class DepreciationSearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	private Integer[] ids;
	
	private Integer fromYear; 
	
	private Integer toYear;
	
	private String code;
	
	private String nameLocal;
	
	private Integer pageSize; 
	
	private Integer offset;
	
	private Integer year;
}
