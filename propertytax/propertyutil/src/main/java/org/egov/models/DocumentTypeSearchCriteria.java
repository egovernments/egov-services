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
public class DocumentTypeSearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	private String name;
	
	private String code; 
	
	private String application;
	
	private Integer pageSize;
	
	private Integer offSet;
}
